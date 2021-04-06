package framework.validation.constraints.impl;

import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import framework.validation.constraints.AbleRangeNullable;

/**
 * AbleRangeNullable Validator
 *
 * @author Kim GeunMok
 */
public class AbleRangeNullableValidator extends AbleConstraintValidator<AbleRangeNullable, CharSequence> {
	private static final Logger logger = LoggerFactory.getLogger(AbleRangeNullableValidator.class);

	private int min;
	private int max;

	public void initialize(AbleRangeNullable constraintAnnotation) {
		min = constraintAnnotation.min();
		max = constraintAnnotation.max();
	}

	public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
		if(value == null || value == "") {
			//공백인 경우
			return true;
		}
		boolean result = false;
		try {
			//int형으로 parse가 가능하고 범위 체크
			if (Integer.parseInt(String.valueOf(value)) >= min && Integer.parseInt(String.valueOf(value)) <= max ){
				result = true;
			}
		} catch (Exception e) {
			logger.trace("{} - {}", e.getClass().getName(), e.getMessage(), e);
		}
		return result;
	}
}
