package framework.validation.constraints.impl;

import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import framework.validation.constraints.AbleFixLength;

/**
 * AbleFixLength Validator
 *
 * @author ByeongDon
 */
public class AbleFixLengthValidatorForLong extends AbleConstraintValidator<AbleFixLength, Long> {

	private int fixLength;

	@Override
	public void initialize(AbleFixLength constraintAnnotation) {
		fixLength = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		if(StringUtils.isEmpty(value.toString())) {
			return true;
		}
		boolean result = (Long.toString(Math.abs(value)).length() == fixLength);
		return result;
	}

}