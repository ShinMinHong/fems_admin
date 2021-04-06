package framework.validation.constraints.impl;

import java.io.UnsupportedEncodingException;

import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import framework.validation.constraints.AbleMaxBytesLength;

/**
 * AbleMaxBytesLength Validator
 *
 * @author ByeongDon
 */
public class AbleMaxBytesLengthValidatorForCharSequence extends AbleConstraintValidator<AbleMaxBytesLength, CharSequence> {
	private static final Logger logger = LoggerFactory.getLogger(AbleMaxBytesLengthValidatorForCharSequence.class);

	private int maxBytesLength;
	private String charset;

	public void initialize(AbleMaxBytesLength constraintAnnotation) {
		maxBytesLength = constraintAnnotation.value();
		charset = constraintAnnotation.charset();
	}

	public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		boolean result = false;
		try {
			result = (String.valueOf(value).getBytes(charset).length <= maxBytesLength);
		} catch (UnsupportedEncodingException e) {
			logger.trace("{} - {}", e.getClass().getName(), e.getMessage(), e);
		}
		return result;
	}
}