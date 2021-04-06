package framework.validation.constraints.impl;

import javax.validation.ConstraintValidatorContext;

import framework.validation.constraints.AbleMaxLength;

/**
 * AbleMaxLength Validator
 * 
 * @author ByeongDon
 */
public class AbleMaxLengthValidatorForCharSequence extends AbleConstraintValidator<AbleMaxLength, CharSequence> {

	private int maxLength;
	
	public void initialize(AbleMaxLength constraintAnnotation) {
		maxLength = constraintAnnotation.value();
	}

	public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		return (value.length() <= maxLength);
	}
}