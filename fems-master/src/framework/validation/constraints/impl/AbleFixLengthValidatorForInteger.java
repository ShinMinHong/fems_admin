package framework.validation.constraints.impl;

import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import framework.validation.constraints.AbleFixLength;

/**
 * AbleFixLength Validator
 *
 * @author ByeongDon
 */
public class AbleFixLengthValidatorForInteger extends AbleConstraintValidator<AbleFixLength, Integer> {

	private int fixLength;

	@Override
	public void initialize(AbleFixLength constraintAnnotation) {
		fixLength = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if(StringUtils.isEmpty(value.toString())) {
			return true;
		}
		boolean result = (Integer.toString(Math.abs(value)).length() == fixLength);
		return result;
	}

}