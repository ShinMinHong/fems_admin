package framework.exception;

/**
 * 설정관련 오류
 * 
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleConfigurationException extends AbleRuntimeException {

	private static final long serialVersionUID = 7112528976573188127L;

	public AbleConfigurationException() {
		super();
	}

	public AbleConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AbleConfigurationException(String message) {
		super(message);
	}

	public AbleConfigurationException(Throwable cause) {
		super(cause);
	}
	
}
