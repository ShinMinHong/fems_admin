package framework.exception;

/**
 * 변환 관련 오류
 * 
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleConvertException extends AbleRuntimeException {

	private static final long serialVersionUID = 5476430130572956410L;

	public AbleConvertException() {
		super();
	}

	public AbleConvertException(String message, Throwable cause) {
		super(message, cause);
	}

	public AbleConvertException(String message) {
		super(message);
	}

	public AbleConvertException(Throwable cause) {
		super(cause);
	}	
}