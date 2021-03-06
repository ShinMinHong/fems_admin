package framework.spring.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import framework.spring.web.servlet.mvc.method.annotation.AbleSecuredRequestResponseBodyMethodProcessor;

/**
 * {@link ResponseBody}와 같지만 Response Body를 송신전 복호화 처리를 해야하는 경우의 SecuredResponseBody Annotation
 * 
 * {@link RequestResponseBodyMethodProcessor}를 수정한 {@link AbleSecuredRequestResponseBodyMethodProcessor}에서 처리
 * 
 * @author Min ByeongDon <deepfree@gmail.com>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SecuredResponseBody {

}

