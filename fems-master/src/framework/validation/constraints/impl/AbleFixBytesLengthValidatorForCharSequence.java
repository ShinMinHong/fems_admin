package framework.validation.constraints.impl;

import java.io.UnsupportedEncodingException;

import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import framework.validation.constraints.AbleFixBytesLength;

/**
 * AbleFixBytesLength Validator
 *
 * @author ByeongDon
 */
public class AbleFixBytesLengthValidatorForCharSequence extends AbleConstraintValidator<AbleFixBytesLength, CharSequence> {
	private static final Logger logger = LoggerFactory.getLogger(AbleFixBytesLengthValidatorForCharSequence.class);

	private int fixBytesLength;
	private String charset;

	public void initialize(AbleFixBytesLength constraintAnnotation) {
		fixBytesLength = constraintAnnotation.value();
		charset = constraintAnnotation.charset();
	}

	public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		boolean result = false;
		try {
			result = (String.valueOf(value).getBytes(charset).length == fixBytesLength);
		} catch (UnsupportedEncodingException e) {
			logger.trace("{} - {}", e.getClass().getName(), e.getMessage(), e);
		}
		return result;
	}

}