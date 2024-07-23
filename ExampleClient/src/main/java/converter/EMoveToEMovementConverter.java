package converter;

import client.main.EMovement;
import messagesbase.messagesfromclient.EMove;

public class EMoveToEMovementConverter implements IConverter<EMove> {

	private EMovement movement;

	public EMoveToEMovementConverter(EMovement movement) {
		super();
		this.movement = movement;
	}

	@Override
	public EMove convert() {
		if (this.movement == EMovement.DOWN) {
			return EMove.Down;
		} else if (this.movement == EMovement.UP) {
			return EMove.Up;
		} else if (this.movement == EMovement.LEFT) {
			return EMove.Left;
		} else {
			return EMove.Right;
		}
	}

}
