package com.firealarm.admin.appconfig;

import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.EnumUtils;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import lombok.Data;

/**
 * Spring Profiles 헬퍼
 *
 * @author ByeongDon
 */
@Data
public class AppProfiles implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(AppProfiles.class);

	private static AppProfiles instance;
	private static void setInstance(AppProfiles instance) { AppProfiles.instance = instance; }
	public static AppProfiles getInstance() {
		return instance;
	}

	@Autowired
	private Environment environment;

	public AppProfiles(Environment environment) {
		super();
		this.environment = environment;
	}

	/** spring.profiles.active 획득   */
    public String[] getActiveProfiles() {
    	return environment.getActiveProfiles();
    }

    /** spring.profiles.default 획득   */
    public String[] getDefaultProfiles() {
    	return environment.getDefaultProfiles();
    }

    /**
     * 주어진 프로파일의 조건을 검사
     * 예: acceptsProfiles("p1", "!p2") => p1프로파일이고, pr프로파일이 아니면 true
     */
    public boolean acceptsProfiles(String... profiles) {
		return environment.acceptsProfiles(profiles);
	}

    /** 서버 런타임 프로파일을 획득 */
    public RUNTIME getActiveRuntimeProfile() {
    	List<RUNTIME> runtimeTypeList = EnumUtils.getEnumList(RUNTIME.class);
    	for (RUNTIME runtime : runtimeTypeList) {
    		if(acceptsProfiles(runtime.name().toLowerCase())) {
        		return runtime;
        	}
		}
    	return null;
    }
    /** 서버 런타임 프로파일을 소문자 문자열로 획득 */
    public String getActiveRuntimeProfileNameLowered() {
    	RUNTIME runtime = getActiveRuntimeProfile();
    	return (runtime != null)? runtime.name().toLowerCase() : null;
    }

    /** 서버런타임 프로파일이 production인가? */
    public boolean isRuntimeProduction() {
    	RUNTIME runtimeProfile = getActiveRuntimeProfile();
    	return RUNTIME.PRODUCTION.equals(runtimeProfile);
    }
    /** 서버런타임 프로파일이 development인가? */
    public boolean isRuntimeDevelopment() {
    	RUNTIME runtimeProfile = getActiveRuntimeProfile();
    	return RUNTIME.DEVELOPMENT.equals(runtimeProfile);
    }
    /** 서버런타임 프로파일이 local인가? */
    public boolean isRuntimeLocal() {
    	RUNTIME runtimeProfile = getActiveRuntimeProfile();
    	return RUNTIME.LOCAL.equals(runtimeProfile);
    }

	@Override
	public void afterPropertiesSet() {
		//Holder설정 (static에서 접근시 사용)
		AppProfiles.setInstance(this);

		//로그출력
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n===============================================");
		sb.append("\r\n- spring.profiles.active: " + ((getActiveRuntimeProfile() != null)? getActiveRuntimeProfileNameLowered() : null));
		sb.append("\r\n- java TimeZone.getDefault() => " + TimeZone.getDefault().getID());
        sb.append("\r\n- joda DateTimeZone.getDefault() => " + DateTimeZone.getDefault());
		sb.append("\r\n===============================================");
		logger.info(sb.toString());

		//런타임 프로파일 설정 필수
		Assert.notNull(getActiveRuntimeProfile());
	}

}
