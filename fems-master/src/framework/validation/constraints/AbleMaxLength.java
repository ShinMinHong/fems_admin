package framework.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

import framework.validation.constraints.impl.AbleMaxLengthValidatorForCharSequence;
import framework.validation.constraints.impl.AbleMaxLengthValidatorForInteger;
import framework.validation.constraints.impl.AbleMaxLengthValidatorForLong;

/**
 * 어노테이션된 문자열의 길이를 제한 ({@code null} or empty인 경우 검사안함) 
 * @author deepfree@ablecoms.com
 */
@Documented
@Constraint(validatedBy = { AbleMaxLengthValidatorForCharSequence.class, AbleMaxLengthValidatorForInteger.class, AbleMaxLengthValidatorForLong.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface AbleMaxLength {
	
	/** 최대 허용 문자열의 길이 */
	int value(); //required property
	
	String message() default "{framework.validation.constraint.ablemaxlength.message}";
	//String message() default "값은 {value}자 이하로 입력해야 합니다.";
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };
	
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
	@Retention(RUNTIME)
	@Documented
	@interface List {
		AbleMaxLength[] value();
	}	
}
