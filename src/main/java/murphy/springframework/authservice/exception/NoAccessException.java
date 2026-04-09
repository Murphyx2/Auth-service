package murphy.springframework.authservice.exception;

public class NoAccessException extends RuntimeException {

	public NoAccessException() {
		super("No access");
	}
}
