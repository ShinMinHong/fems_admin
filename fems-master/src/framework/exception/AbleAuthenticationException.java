package framework.exception;

import org.springframework.security.core.Authentication;

/**
 * 인증 실패 예외
 * 
 * @author Min ByeongDon <deepfree@gmail.com>
 */
@SuppressWarnings("serial")
public class AbleAuthenticationException extends AbleRuntimeException {

	protected Authentication tryAuthentication;

	public AbleAuthenticationException() {
		super();
	}

	public AbleAuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AbleAuthenticationException(String message) {
		super(message);
	}

	public AbleAuthenticationException(Throwable cause) {
		super(cause);
	}

	public AbleAuthenticationException(Authentication tryAuthentication) {
		super();
		this.tryAuthentication = tryAuthentication;
	}

	public AbleAuthenticationException(Authentication tryAuthentication, String message, Throwable cause) {
		super(message, cause);
		this.tryAuthentication = tryAuthentication;
	}

	public AbleAuthenticationException(Authentication tryAuthentication, String message) {
		super(message);
		this.tryAuthentication = tryAuthentication;
	}

	public AbleAuthenticationException(Authentication tryAuthentication, Throwable cause) {
		super(cause);
		this.tryAuthentication = tryAuthentication;
	}

}
