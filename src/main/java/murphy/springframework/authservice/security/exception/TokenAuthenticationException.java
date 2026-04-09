package murphy.springframework.authservice.security.exception;

public class TokenAuthenticationException extends RuntimeException {
	public TokenAuthenticationException(String message) {
		super(message);
	}
}
