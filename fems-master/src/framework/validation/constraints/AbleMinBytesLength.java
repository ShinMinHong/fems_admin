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

import framework.validation.constraints.impl.AbleMinBytesLengthValidatorForCharSequence;

/**
 * 어노테이션된 문자열의 Bytes를 제한 ({@code null} or empty인 경우 검사안함)
 * @author deepfree@ablecoms.com
 */
@Documented
@Constraint(validatedBy = { AbleMinBytesLengthValidatorForCharSequence.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface AbleMinBytesLength {
	
	/** 최소 문자열의 Bytes 길이 */
	int value(); //required property
	
	/** 바이트길이 측정에 사용할 문자셋 */
	String charset() default "MS949";
	
	String message() default "{framework.validation.constraint.ableminbyteslength.message}";
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };
	
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
	@Retention(RUNTIME)
	@Documented
	@interface List {
		AbleMinBytesLength[] value();
	}	
}
