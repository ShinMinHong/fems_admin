package framework.spring.web.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AbleControllerLogging 정책 제어 Annotation
 *
 * @author deepfree@ablecoms.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AbleControllerLoggingPolicy {

	/** 컨트롤러의 액션 시작, 종료를 숨길지 여부 */
	public boolean hideStartEndLog() default false;

	/** 컨트롤러의 액션 예외를 숨길지 여부 */
	public boolean hideExceptionLog() default false;

	/** 응답Body를 출력을 SKIP할지 여부 */
	public boolean hideResponseBody() default true;

}
