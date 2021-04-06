package framework.exception;

/**
 * IO 처리 관련 예외
 * 
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleIOException extends AbleRuntimeException {

	private static final long serialVersionUID = 5632244228482978109L;

	public AbleIOException() {
		super();
	}

	public AbleIOException(String message, Throwable cause) {
		super(message, cause);
	}

	public AbleIOException(String message) {
		super(message);
	}

	public AbleIOException(Throwable cause) {
		super(cause);
	}	
}
