package framework.validation.constraints.impl;

import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import framework.validation.constraints.AbleFixLength;

/**
 * AbleFixLength Validator
 *
 * @author ByeongDon
 */
public class AbleFixLengthValidatorForCharSequence extends AbleConstraintValidator<AbleFixLength, CharSequence> {

	private int fixLength;

	public void initialize(AbleFixLength constraintAnnotation) {
		fixLength = constraintAnnotation.value();
	}

	public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
		if (value == null || value == ""){
			return true;
		}
		if(StringUtils.isEmpty(value.toString())) {
			return true;
		}
		return (value.length() == fixLength);
	}
}