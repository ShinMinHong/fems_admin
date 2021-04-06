package spring.appservlet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import framework.spring.web.log.AbleControllerLoggingAspect;

/**
 * Spring AppServlet - 컴포넌트 설정
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
@Configuration
public class AppServletConfig {

	/** Controller Logging AOP */
	@Bean
	public AbleControllerLoggingAspect ableControllerLoggingAspect() {
		return new AbleControllerLoggingAspect();
	}

}
