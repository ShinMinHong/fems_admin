package framework.exception;

import org.springframework.validation.Errors;

/**
 * ablecoms framework validation exception
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleValidationException extends AbleRuntimeException {

	private static final long serialVersionUID = 3367655639150032733L;

	protected Errors errors;

	public Errors getErrors() {
		return errors;
	}

	public AbleValidationException(Errors errors) {
		super();
		this.errors = errors;
	}

	public AbleValidationException(String message, Errors errors) {
		super(message);
		this.errors = errors;
	}

	@Override
	public String toString() {
		return "AbleValidationException [errors=" + errors + "]";
	}
}
