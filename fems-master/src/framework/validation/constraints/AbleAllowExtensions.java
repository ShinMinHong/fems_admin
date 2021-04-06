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

import framework.validation.constraints.impl.AbleAllowExtensionsForListOfMultipartFile;
import framework.validation.constraints.impl.AbleAllowExtensionsForMultipartFile;

/**
 * 어노테이션된 멀티파트 파일의 확장자를 제한 ({@code null} or empty인 경우 검사안함)
 * @author deepfree@ablecoms.com
 */
@Documented
@Constraint(validatedBy = { AbleAllowExtensionsForMultipartFile.class, AbleAllowExtensionsForListOfMultipartFile.class})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface AbleAllowExtensions {

	/** 허용 확장자 목록 (comma-seperated) */
	String value(); //required property

	String message() default "{framework.validation.constraint.ableallowextensions.message}";

	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };

	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
	@Retention(RUNTIME)
	@Documented
	@interface List {
		AbleAllowExtensions[] value();
	}
}
