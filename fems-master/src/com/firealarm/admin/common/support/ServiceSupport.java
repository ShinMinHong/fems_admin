package com.firealarm.admin.common.support;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import framework.spring.client.AbleRestTemplate;
import framework.spring.client.FireAlarmRestTemplateBuilder;
import com.firealarm.admin.appconfig.AppConfig;

/**
 * Service Support
 *
 * 서비스의 기본 로직을 포함
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class ServiceSupport extends ComponentSupport {

	/** 서버 Properties 설정 접근자 */
	@Autowired protected AppConfig appConfig;

	/** MessageSource 접근자 */
	@Autowired protected MessageSource messageSource;

	AbleRestTemplate formApi = null;
	AbleRestTemplate restApi = null;
	AbleRestTemplate secureRestApi = null;

	/**
	 * FORM API 호출을 위한 AbleRestTemplate획득 (
	 */
	protected AbleRestTemplate formApi() {
		if(formApi == null) {
			formApi = FireAlarmRestTemplateBuilder.buildFormApiTemplate();
		}
		return formApi;
	}

	/**
	 * 비암호화 REST API호출을 위한 AbleRestTemplate를 획득
	 */
	protected AbleRestTemplate restApi() {
		if(restApi == null) {
			restApi = FireAlarmRestTemplateBuilder.buildRestApiTemplate();
		}
		return restApi;
	}

}
