package client.main;

import java.util.AbstractMap;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import converter.EMoveToEMovementConverter;
import converter.EPlayerGameStateToEGameStateConverter;
import converter.FullMapToGameMapConverter;
import converter.HalfMapToPlayerHalfMapConverter;
import exception.GameStateException;
import exception.InvalidMapException;
import exception.InvalidMoveException;
import exception.PlayerRegistrationException;
import gamemodel.EGameState;
import gamemodel.GameMap;
import map.HalfMap;
import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ERequestState;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromserver.PlayerState;
import reactor.core.publisher.Mono;

public class Network {

	private String gameId;
	private String playerId;
	private String serveraddress;
	private final static Logger logger = LoggerFactory.getLogger(ClientManager.class);

	private WebClient baseWebClient;

	public Network(String serverBaseUrl, String gameId) {
		this.serveraddress = serverBaseUrl;
		this.gameId = gameId;
		this.baseWebClient = WebClient.builder().baseUrl(serveraddress + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();
	}

	public String registerClient(String gameid, String studentFirstName, String studentLastName, String studentUAccount)
			throws PlayerRegistrationException {

		PlayerRegistration playerReg = new PlayerRegistration(studentFirstName, studentLastName, studentUAccount);

		@SuppressWarnings("rawtypes")
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + this.gameId + "/players")
				.body(BodyInserters.fromValue(playerReg)).retrieve().bodyToMono(ResponseEnvelope.class);

		@SuppressWarnings("unchecked")
		ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();

		if (resultReg.getState() == ERequestState.Error) {
			throw new PlayerRegistrationException(
					"Player registration did not work!" + resultReg.getExceptionMessage());
		} else {
			this.playerId = resultReg.getData().get().getUniquePlayerID();
		}

		return this.playerId;
	}

	public void sendHalfMapToServer(HalfMap halfmap) throws InvalidMapException {

		HalfMapToPlayerHalfMapConverter converter = new HalfMapToPlayerHalfMapConverter(halfmap, this.playerId);
		PlayerHalfMap playerHalfmap = converter.convert();

		EPlayerGameState gameState;
		try {
			gameState = checkIfActNext();
			if (gameState == EPlayerGameState.Won || gameState == EPlayerGameState.Lost) {
				return;
			}
		} catch (GameStateException e) {
			logger.error("GameState could not be received successfully: " + e.getMessage());
		}

		@SuppressWarnings("rawtypes")
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + this.gameId + "/halfmaps")
				.body(BodyInserters.fromValue(playerHalfmap)).retrieve().bodyToMono(ResponseEnvelope.class);

		@SuppressWarnings("unchecked")
		ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();

		if (resultReg.getState() == ERequestState.Error) {
			throw new InvalidMapException("Client error, errormessage: " + resultReg.getExceptionMessage());
		} else {
			logger.info("Player has sent a valid halfmap!");
		}

	}

	private EPlayerGameState checkIfActNext() throws GameStateException {

		while (true) {

			@SuppressWarnings("rawtypes")
			Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
					.uri("/" + this.gameId + "/states/" + this.playerId).retrieve().bodyToMono(ResponseEnvelope.class);

			@SuppressWarnings("unchecked")
			ResponseEnvelope<GameState> requestResult = webAccess.block();

			if (requestResult.getState() == ERequestState.Error) {
				throw new GameStateException("Client error, errormessage: " + requestResult.getExceptionMessage());
			} else {
				Set<PlayerState> playerOfGame = requestResult.getData().get().getPlayers();
				for (PlayerState playerState : playerOfGame) {
					if (playerState.getUniquePlayerID().equals(this.playerId)) {
						if (playerState.getState() != EPlayerGameState.MustWait) {
							return playerState.getState();
						}
					}
				}
			}

		}

	}

	public Entry<GameMap, EGameState> getCurrentGameState() throws GameStateException {

		@SuppressWarnings("rawtypes")
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
				.uri("/" + this.gameId + "/states/" + this.playerId).retrieve().bodyToMono(ResponseEnvelope.class);

		@SuppressWarnings("unchecked")
		ResponseEnvelope<GameState> requestResult = webAccess.block();

		if (requestResult.getState() == ERequestState.Error) {
			throw new GameStateException("Client error, errormessage: " + requestResult.getExceptionMessage());
		} else {

			Set<PlayerState> playerOfGame = requestResult.getData().get().getPlayers();
			EPlayerGameState gamestate = null;

			for (PlayerState playerState : playerOfGame) {
				if (playerState.getUniquePlayerID().equals(this.playerId)) {
					gamestate = playerState.getState();
				}
			}

			FullMap map = requestResult.getData().get().getMap();

			FullMapToGameMapConverter converter = new FullMapToGameMapConverter(map);
			GameMap gamemap = converter.convert();
			EPlayerGameStateToEGameStateConverter stateConverter = new EPlayerGameStateToEGameStateConverter(gamestate);
			EGameState egamestate = stateConverter.convert();

			AbstractMap.Entry<GameMap, EGameState> pair = new AbstractMap.SimpleEntry<>(gamemap, egamestate);

			return pair;

		}

	}

	public void sendMoveToServer(EMovement movement) throws InvalidMoveException {

		EMoveToEMovementConverter converter = new EMoveToEMovementConverter(movement);
		EMove move = converter.convert();

		EPlayerGameState gameState;
		try {
			gameState = checkIfActNext();
			if (gameState == EPlayerGameState.Won || gameState == EPlayerGameState.Lost) {
				return;
			}
		} catch (GameStateException e) {
			logger.error("GameState could not be received successfully: " + e.getMessage());
		}

		PlayerMove playerMove = PlayerMove.of(this.playerId, move);

		@SuppressWarnings("rawtypes")
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + this.gameId + "/moves")
				.body(BodyInserters.fromValue(playerMove)).retrieve().bodyToMono(ResponseEnvelope.class);

		@SuppressWarnings("unchecked")
		ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();

		if (resultReg.getState() == ERequestState.Error) {
			throw new InvalidMoveException("Client error, errormessage: " + resultReg.getExceptionMessage());
		} else {
			logger.info("Player has sent a valid move!");
		}

	}

	public String getGameId() {
		return gameId;
	}

	public String getPlayerId() {
		return playerId;
	}

}
