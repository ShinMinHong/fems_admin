package framework.validation.constraints.impl;

import javax.validation.ConstraintValidatorContext;

import framework.validation.constraints.AbleMinLength;

/**
 * AbleMinLength Validator
 * 
 * @author ByeongDon
 */
public class AbleMinLengthValidatorForInteger extends AbleConstraintValidator<AbleMinLength, Integer> {

	private int minLength;
	
	@Override
	public void initialize(AbleMinLength constraintAnnotation) {
		minLength = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		boolean result = (Integer.toString(Math.abs(value)).length() >= minLength);
		return result; 
	}

}