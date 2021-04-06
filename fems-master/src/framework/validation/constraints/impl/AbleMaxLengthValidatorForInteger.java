package framework.validation.constraints.impl;

import javax.validation.ConstraintValidatorContext;

import framework.validation.constraints.AbleMaxLength;

/**
 * AbleMaxLength Validator
 * 
 * @author ByeongDon
 */
public class AbleMaxLengthValidatorForInteger extends AbleConstraintValidator<AbleMaxLength, Integer> {

	private int maxLength;
	
	@Override
	public void initialize(AbleMaxLength constraintAnnotation) {
		maxLength = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		boolean result = (Integer.toString(Math.abs(value)).length() <= maxLength);
		if(logger.isTraceEnabled()) {
			logger.trace("isValid({}) maxLength:{} => {}", value, maxLength, result);	
		}
		return result; 
	}

}