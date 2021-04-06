package framework.validation.constraints.impl;

import javax.validation.ConstraintValidatorContext;

import framework.validation.constraints.AbleMinLength;

/**
 * AbleMinLength Validator
 * 
 * @author ByeongDon
 */
public class AbleMinLengthValidatorForCharSequence extends AbleConstraintValidator<AbleMinLength, CharSequence> {

	private int minLength;
	
	public void initialize(AbleMinLength constraintAnnotation) {
		minLength = constraintAnnotation.value();
	}

	public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		return (value.length() >= minLength); 
	}

}