package framework.exception;

/**
 * 입력형식의 Format이 올바르지 않은 경우의 예외
 * 
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleInvalidFormatException extends AbleRuntimeException {
	
	private static final long serialVersionUID = -432647972322788447L;

	public AbleInvalidFormatException() {
		super();
	}

	public AbleInvalidFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public AbleInvalidFormatException(String message) {
		super(message);
	}

	public AbleInvalidFormatException(Throwable cause) {
		super(cause);
	}	
	
}
