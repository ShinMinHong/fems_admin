package framework.spring.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import framework.spring.web.servlet.mvc.method.annotation.AbleSecuredRequestResponseBodyMethodProcessor;

/**
 * {@link RequestBody}와 같지만 Request Body를 수신전 복호화 처리를 해야하는 경우의 SecuredRequestBody Annotation
 * 
 * {@link RequestResponseBodyMethodProcessor}를 수정한 {@link AbleSecuredRequestResponseBodyMethodProcessor}에서 처리
 * 
 * @author Min ByeongDon <deepfree@gmail.com>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SecuredRequestBody {

	/**
	 * Whether body content is required.
	 * <p>Default is {@code true}, leading to an exception thrown in case
	 * there is no body content. Switch this to {@code false} if you prefer
	 * {@code null} to be passed when the body content is {@code null}.
	 */
	boolean required() default true;

}
