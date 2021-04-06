package framework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class AbleResourceNotFoundException extends AbleRuntimeException {

	private static final long serialVersionUID = 9098427096055189463L;

	public AbleResourceNotFoundException() {
		super();
	}

	public AbleResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public AbleResourceNotFoundException(String message) {
		super(message);
	}

	public AbleResourceNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
