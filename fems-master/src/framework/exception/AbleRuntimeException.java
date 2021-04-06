package framework.exception;

/**
 * Framework Runtime Exception
 * 
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -1811214606343796458L;
	
	public AbleRuntimeException() {
		super();
	}

	public AbleRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public AbleRuntimeException(String message) {
		super(message);
	}

	public AbleRuntimeException(Throwable cause) {
		super(cause);
	}

}
