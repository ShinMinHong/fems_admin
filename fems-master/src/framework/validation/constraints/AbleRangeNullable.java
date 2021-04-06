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

import framework.validation.constraints.impl.AbleRangeNullableValidator;

/**
 * 어노테이션된 문자열의 Range 제한 ({@code null} or empty인 경우 검사안함)
 * @author kelick82@ablecoms.com
 */
@Documented
@Constraint(validatedBy = { AbleRangeNullableValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface AbleRangeNullable {
	
	/** 최소값 */
	int min(); //required property
	/** 최대값 */
	int max(); //required property
	
	String message() default "{framework.validation.constraint.AbleRangeNullable.message}";
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };
	
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
	@Retention(RUNTIME)
	@Documented
	@interface List {
		AbleRangeNullable[] value();
	}	
}
