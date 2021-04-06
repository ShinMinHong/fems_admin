package framework.validation.constraints.impl;

import javax.validation.ConstraintValidatorContext;

import framework.validation.constraints.AbleMinLength;

/**
 * AbleMinLength Validator
 * 
 * @author ByeongDon
 */
public class AbleMinLengthValidatorForLong extends AbleConstraintValidator<AbleMinLength, Long> {

	private int minLength;
	
	@Override
	public void initialize(AbleMinLength constraintAnnotation) {
		minLength = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		boolean result = (Long.toString(Math.abs(value)).length() >= minLength);
		return result; 
	}

}