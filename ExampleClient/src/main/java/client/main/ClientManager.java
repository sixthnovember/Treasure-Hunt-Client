package client.main;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exception.GameStateException;
import exception.InvalidMapException;
import exception.InvalidMoveException;
import exception.PlayerRegistrationException;
import gamemodel.EGameState;
import gamemodel.GameInfo;
import gamemodel.GameMap;
import map.HalfMap;
import map.HalfMapGenerator;
import map.Point;
import map.Terrain;
import view.CommandLineInterface;

public class ClientManager {

	private GameInfo model;
	@SuppressWarnings("unused")
	private CommandLineInterface view;
	private Network network;
	private String playerid;
	private final static Logger logger = LoggerFactory.getLogger(ClientManager.class);
	private PositionFinding positionFinding;
	private WayFinding wayFinding;

	public ClientManager(GameInfo model, CommandLineInterface view, Network network) {
		super();
		this.model = model;
		this.view = view;
		this.network = network;
		this.positionFinding = new PositionFinding();
		this.wayFinding = new WayFinding();
	}

	public void registerClient(String gameid, String studentFirstName, String studentLastName, String studentUAccount) {
		try {
			this.playerid = this.network.registerClient(gameid, studentFirstName, studentLastName, studentUAccount);
			logger.info("Client registered: " + this.playerid);
		} catch (PlayerRegistrationException e) {
			logger.error("Client could not be registered: " + e.getMessage());
			System.exit(0);
		}
	}

	public void playGame() {
		HalfMapGenerator generator = new HalfMapGenerator();
		HalfMap halfmap = generator.generate();
		GameMap gamemap = new GameMap(halfmap.getHalfmap());
		updateGame(gamemap, EGameState.WAIT);
		logger.info("Printing halfmap done!");
		sendHalfMap(halfmap);
		logger.info("Sending HalfMap done!");
		while (this.model.getGamestate() != EGameState.WON || this.model.getGamestate() != EGameState.LOST) {
			Entry<GameMap, EGameState> pair = getCurrentGameState();
			GameMap gamemap_new = pair.getKey();
			EGameState gamestate_new = pair.getValue();
			updateGame(gamemap_new, gamestate_new);
			if (!this.model.isTreasureFound()) {
				findTreasure();
				logger.info("Treasure found!");
			} else {
				findEnemyCastle();
				logger.info("Enemy castle found!");
			}
		}
	}

	private void findTreasure() {
		while (!this.model.isTreasureFound()) {
			if (this.positionFinding.getTreasurePosition() != null) {
				Point myPosition = this.positionFinding.getPlayerPosition();
				logger.info(String.format("My position: (%d, %d)", myPosition.getX(), myPosition.getY()));
				Point treasurePosition = this.positionFinding.getTreasurePosition();
				logger.info(
						String.format("Treasure position: (%d, %d)", treasurePosition.getX(), treasurePosition.getY()));
				ArrayList<EMovement> way = this.wayFinding.getShortestWayTo(myPosition, treasurePosition);
				goToPoint(way);
				updateTreasureFound();
			} else {
				findClosestMountain(false);
			}
		}
	}

	private void goToPoint(ArrayList<EMovement> moves) {
		for (int i = 0; i < moves.size(); ++i) {
			sendMove(moves.get(i));
			Entry<GameMap, EGameState> pair = getCurrentGameState();
			GameMap gamemap = pair.getKey();
			EGameState gamestate_new = pair.getValue();
			updateGame(gamemap, gamestate_new);

			Point playerPosition = this.positionFinding.getPlayerPosition();
			if (this.model.getGamemap().getGamemap().get(playerPosition).getTerrain() == Terrain.MOUNTAIN) {
				this.positionFinding.addVisitedMountain(playerPosition);
			}
		}
	}

	private void findEnemyCastle() {
		while (this.model.getGamestate() != EGameState.LOST || this.model.getGamestate() != EGameState.WON) {
			if (this.positionFinding.getEnemyCastlePosition() != null) {
				Point myPosition = this.positionFinding.getPlayerPosition();
				logger.info(String.format("My position: (%d, %d)", myPosition.getX(), myPosition.getY()));
				Point enemyCastlePosition = this.positionFinding.getEnemyCastlePosition();
				logger.info(String.format("Enemy castle position: (%d, %d)", enemyCastlePosition.getX(),
						enemyCastlePosition.getY()));
				ArrayList<EMovement> way = this.wayFinding.getShortestWayTo(myPosition, enemyCastlePosition);
				goToPoint(way);
			} else {
				findClosestMountain(true);
			}
		}
	}

	private void findClosestMountain(boolean treasureFound) {
		Point myPosition = this.positionFinding.getPlayerPosition();
		logger.info(String.format("My position: (%d, %d)", myPosition.getX(), myPosition.getY()));
		Point closestMountain = this.positionFinding.getClosestMountain(treasureFound);
		logger.info(
				String.format("Closest mountain position: (%d, %d)", closestMountain.getX(), closestMountain.getY()));
		ArrayList<EMovement> way = this.wayFinding.getShortestWayTo(myPosition, closestMountain);
		goToPoint(way);
	}

	private void updateGame(GameMap gamemap, EGameState gamestate) {
		this.model.setGamemap(gamemap);
		this.model.setGamestate(gamestate);
		this.wayFinding.setGameMap(gamemap);
		this.positionFinding.setGameMap(gamemap);

		if (gamestate == EGameState.WON || gamestate == EGameState.LOST) {
			if (gamestate == EGameState.WON) {
				updateEnemyCastleFound();
			}
			logger.info("Game is over!");
			System.exit(0);
		}
	}

	private void updateTreasureFound() {
		this.model.setTreasureFound(true);
	}

	private void updateEnemyCastleFound() {
		this.model.setEnemyCastleFound(true);
	}

	private void sendHalfMap(HalfMap halfmap) {
		try {
			network.sendHalfMapToServer(halfmap);
		} catch (InvalidMapException e) {
			logger.error("HalfMap could not be sent successfully: " + e.getMessage());
			System.exit(0);
		}

	}

	private Entry<GameMap, EGameState> getCurrentGameState() {
		try {
			Entry<GameMap, EGameState> pair = this.network.getCurrentGameState();
			GameMap gamemap_new = pair.getKey();
			EGameState gamestate_new = pair.getValue();
			return new AbstractMap.SimpleEntry<>(gamemap_new, gamestate_new);
		} catch (GameStateException e) {
			logger.error("GameState could not be received successfully: " + e.getMessage());
			System.exit(0);
		}
		return null;
	}

	private void sendMove(EMovement movement) {
		try {
			this.network.sendMoveToServer(movement);
		} catch (InvalidMoveException e) {
			logger.error("Move could not be sent successfully: " + e.getMessage());
			System.exit(0);
		}
	}

}