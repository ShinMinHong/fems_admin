package framework.validation.constraints.impl;

import javax.validation.ConstraintValidatorContext;

import framework.validation.constraints.AbleMaxLength;

/**
 * AbleMaxLength Validator
 * 
 * @author ByeongDon
 */
public class AbleMaxLengthValidatorForLong extends AbleConstraintValidator<AbleMaxLength, Long> {

	private int maxLength;
	
	@Override
	public void initialize(AbleMaxLength constraintAnnotation) {
		maxLength = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		boolean result = (Long.toString(Math.abs(value)).length() <= maxLength);
		if(logger.isTraceEnabled()) {
			logger.trace("isValid({}) maxLength:{} => {}", value, maxLength, result);	
		}
		return result; 
	}

}