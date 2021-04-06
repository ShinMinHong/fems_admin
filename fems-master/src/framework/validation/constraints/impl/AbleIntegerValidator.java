package framework.validation.constraints.impl;

import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import framework.validation.constraints.AbleInteger;

/**
 * AbleIntegerValidator Validator
 * 숫자만 입력 가능 (음수,양수,0)
 *
 * @author Kim
 *
 */
public class AbleIntegerValidator extends AbleConstraintValidator<AbleInteger, CharSequence> {

	@Override
	public void initialize(AbleInteger constraintAnnotation) {
		/**/
	}

	@Override
	public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
		if(StringUtils.isEmpty(value)) {
			return true;
		}
		String regEx = "^(-?)\\d+";
		return value.toString().matches(regEx);
	}

}
