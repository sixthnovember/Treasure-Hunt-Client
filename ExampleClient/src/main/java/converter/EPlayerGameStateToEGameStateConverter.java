package converter;

import gamemodel.EGameState;
import messagesbase.messagesfromserver.EPlayerGameState;

public class EPlayerGameStateToEGameStateConverter implements IConverter<EGameState> {

	private EPlayerGameState gameState;

	public EPlayerGameStateToEGameStateConverter(EPlayerGameState ePlayerGameState) {
		super();
		this.gameState = ePlayerGameState;
	}

	@Override
	public EGameState convert() {
		if (gameState == EPlayerGameState.MustWait) {
			return EGameState.WAIT;
		} else if (gameState == EPlayerGameState.MustAct) {
			return EGameState.MYTURN;
		} else if (gameState == EPlayerGameState.Won) {
			return EGameState.WON;
		} else {
			return EGameState.LOST;
		}
	}

}
