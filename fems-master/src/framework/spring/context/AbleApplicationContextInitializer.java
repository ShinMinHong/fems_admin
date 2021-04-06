package framework.spring.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

/**
 * Spring Application Context Initializer
 *
 * 서버기동시 재기동 기록 WARN 로깅
 * 서버기동시 Spring.Profile등 환경변수 INFO 로깅
 * spring.profiles.active를 Properties로 설정한 경우와 Environment로 설정한 경우에 대해 모두 지원하기 위한 Sync 작업
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static final Logger logger = LoggerFactory.getLogger(AbleApplicationContextInitializer.class);

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextInitializer#initialize(org.springframework.context.ConfigurableApplicationContext)
	 */
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		logger.warn("\r\n##########################################\r\n"
				+ "Init Application Context.....\r\n"
				+ "Application Name: {}\r\n"
				+ "##########################################",
				applicationContext.getApplicationName());
		syncSpringProfileEnvWithProperty();
		logSpringActiveProfile(applicationContext);
	}

	/**
	 * Spring active profiles를 출력
	 * @param applicationContext
	 */
	private void logSpringActiveProfile(ConfigurableApplicationContext applicationContext) {
		String defaultProfiles = System.getProperty("spring.profiles.default");
		String activeProfiles = System.getProperty("spring.profiles.active");
		defaultProfiles = (StringUtils.isEmpty(defaultProfiles))?"":defaultProfiles;
		activeProfiles = (StringUtils.isEmpty(activeProfiles))?"":activeProfiles;

		StringBuilder sb = new StringBuilder();
		sb.append("\r\n");
		sb.append("############################################################\r\n");
		sb.append("## Init Application Context.....\r\n");
		sb.append("## Application Name: "+applicationContext.getApplicationName()+"\r\n");
		sb.append("## \r\n");
		sb.append("## DefaultProfiles: "+defaultProfiles+"\r\n");
		sb.append("## ActiveProfiles: "+activeProfiles+"\r\n");
		sb.append("############################################################");
		sb.append("");
		logger.info(sb.toString());
	}


	/**
	 * spring active profile의 env 설정을 property 설정에 동기화
	 * (원래 env로 짜야 하는데, 인수인계 받아보니 property로 다 짜져있어서...)
	 */
	private void syncSpringProfileEnvWithProperty() {
		String profileProperty = System.getProperty("spring.profiles.active");
		String profileEnv = System.getenv("spring.profiles.active");
		if(StringUtils.isEmpty(profileProperty)) {
			//Spring Profile이 Property에 설정되지 않은 경우
			if(!StringUtils.isEmpty(profileEnv)) {
				//env의 설정값으로 Property값 설정 (기존코드 호환성을 위해)
				System.setProperty("spring.profiles.active", profileEnv);
				logger.info("spring.profiles.active env value '{}' copy to property", profileEnv);
			} else {
				//Env, Property 모두 설정되지 않은 경우
				//[경고] spring.profiles.active Property에 설정된 값과 Env의 값이 다름
				logger.warn("spring.profiles.active property value or env value required !!!!!!!!!!!!!!!!!");
				logger.warn("please define spring.profiles.active property value or env value !!!!!!!!!!!!!!!!!");
			}
		} else {
			//Spring Profile이 Property에 설정된경우 않은 경우
			if(!StringUtils.isEmpty(profileEnv)) {
				//env와 property에 모두 설정된 경우 - 동일성 확인
				if(!profileProperty.equals(profileEnv)) {
					//[경고] spring.profiles.active Property에 설정된 값과 Env의 값이 다름
					logger.warn("spring.profiles.active property value '{}' is different with env '{}'", profileProperty, profileEnv);
				}
			}
		}
	}

}
