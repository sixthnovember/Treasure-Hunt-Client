package exception;

public class PlayerRegistrationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PlayerRegistrationException(String message) {
		super(message);
	}

}