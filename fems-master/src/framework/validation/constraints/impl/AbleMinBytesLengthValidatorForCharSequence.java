package framework.validation.constraints.impl;

import java.io.UnsupportedEncodingException;

import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import framework.validation.constraints.AbleMinBytesLength;

/**
 * AbleMinBytesLength Validator
 *
 * @author ByeongDon
 */
public class AbleMinBytesLengthValidatorForCharSequence extends AbleConstraintValidator<AbleMinBytesLength, CharSequence> {
	private static final Logger logger = LoggerFactory.getLogger(AbleMinBytesLengthValidatorForCharSequence.class);

	private int minBytesLength;
	private String charset;

	public void initialize(AbleMinBytesLength constraintAnnotation) {
		minBytesLength = constraintAnnotation.value();
		charset = constraintAnnotation.charset();
	}

	public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		boolean result = false;
		try {
			result = (String.valueOf(value).getBytes(charset).length >= minBytesLength);
		} catch (UnsupportedEncodingException e) {
			logger.trace("{} - {}", e.getClass().getName(), e.getMessage(), e);
		}
		return result;
	}

}
