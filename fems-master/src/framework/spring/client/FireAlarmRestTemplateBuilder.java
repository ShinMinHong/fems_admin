package framework.spring.client;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.util.MultiValueMap;

import com.firealarm.admin.appconfig.AppConst;

import framework.crypto.AES256Crypto;
import framework.spring.http.converter.AbleSecuredHttpMessageConverter;
import framework.spring.http.converter.AbleTeeHttpMessageConverter;

/**
 * AbleRestTemplate의 Builder
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class FireAlarmRestTemplateBuilder {

	private FireAlarmRestTemplateBuilder() { /**/ }

	/**
	 * AbleRestTemplate를 생성 (FORM API방식, 암호화 미적용) - POST(application/x-www-form-urlencoded)
	 */
	public static AbleRestTemplate buildFormApiTemplate() {
		AbleRestTemplate restTemplate = buildRestTemplate(API_TYPE.REQUEST_FORM, false, false);
		return restTemplate;
	}

	/**
	 * AbleRestTemplate를 생성 (REST API방식, 암호화 미적용) - POST(application/json)
	 */
	public static AbleRestTemplate buildRestApiTemplate() {
		AbleRestTemplate restTemplate = buildRestTemplate(API_TYPE.REQUEST_BODY_JSON, false, false);
		return restTemplate;
	}

	/**
	 * 내부용 - 실제로 AbleRestTemplate을 환경에 맞춰 생성
	 * @param apiType api의 통신 방식
	 * @param isSecure 암호화 컨버터를 적용할 것인가?
	 * @param isProductionEnv 암호화 적용시 실서버용키를 사용할 것인가?
	 */
	private static AbleRestTemplate buildRestTemplate(API_TYPE apiType, boolean isSecure, boolean isProductionEnv) {
		AbleRestTemplate restTemplate = new AbleRestTemplate();

		//MessageConverter 설정
		List<HttpMessageConverter<?>> messageConverters = getHttpMessageConverters(apiType, isSecure, isProductionEnv);
		restTemplate.setMessageConverters(messageConverters);

		//Logging을 위한 Interceptor 설정
		List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
		interceptors.add(new AbleLoggingClientHttpRequestInterceptor());
		restTemplate.setInterceptors(interceptors);

		//ResponseBody를 여러번 읽을 수 있도록 ClientHttpRequestFactory 처리
		ClientHttpRequestFactory requestFactoryInner = new SimpleClientHttpRequestFactory(); //SimpleClientHttpRequestFactory: 기본 JDK기능을 이용
		//ClientHttpRequestFactory requestFactoryInner = new HttpComponentsClientHttpRequestFactory(); //HttpComponentsClientHttpRequestFactory: Apache HttpComponent 4.3이상을 이용
		ClientHttpRequestFactory requestFactory = new BufferingClientHttpRequestFactory(requestFactoryInner);
		restTemplate.setRequestFactory(requestFactory);
		return restTemplate;
	}


	/**
	 * RestTemplate에 설정할 MessageConverter 목록을 생성하여 획득 (JSON용 Converter 반환)
	 * @param apiType api의 통신 방식
	 * @param isSecure 암호화 컨버터를 사용할 것인지 여부
	 * @param isProductionEnv 암호화시 실서버 환경용 키를 사용할지 여부
	 */
	private static List<HttpMessageConverter<?>> getHttpMessageConverters(API_TYPE apiType, boolean isSecure, boolean isProductionEnv) {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		if(apiType.equals(API_TYPE.REQUEST_BODY_SECURED_JSON)) {
			String secretKey = (isProductionEnv)? AppConst.SECRET_KEY_REAL : AppConst.SECRET_KEY_DEV;
			AES256Crypto crypto = new AES256Crypto(secretKey);
			MappingJackson2HttpMessageConverter httpConverter = new MappingJackson2HttpMessageConverter();
			List<MediaType> mediaTypes = new ArrayList<MediaType>();
			mediaTypes.add(new MediaType("application", "secured+json", Charset.forName("UTF-8")));
			httpConverter.setSupportedMediaTypes(mediaTypes);
			HttpMessageConverter<?> converter = new AbleSecuredHttpMessageConverter<Object>(crypto, httpConverter);
			messageConverters.add(converter);
		} else if(apiType.equals(API_TYPE.REQUEST_BODY_JSON)) {
			MappingJackson2HttpMessageConverter httpConverter = new MappingJackson2HttpMessageConverter();
			List<MediaType> mediaTypes = new ArrayList<MediaType>();
			mediaTypes.add(new MediaType("application", "json", Charset.forName("UTF-8")));
			mediaTypes.add(new MediaType("application", "*+json", Charset.forName("UTF-8")));
			httpConverter.setSupportedMediaTypes(mediaTypes);
			HttpMessageConverter<?> converter = new AbleTeeHttpMessageConverter<Object>(httpConverter);
			messageConverters.add(converter);
		} else if(apiType.equals(API_TYPE.REQUEST_FORM)) {
			messageConverters.add(new AbleTeeHttpMessageConverter<MultiValueMap<String, ?>>(new AllEncompassingFormHttpMessageConverter()));
			messageConverters.add(new AbleTeeHttpMessageConverter<String>(new StringHttpMessageConverter()));
		}
		return messageConverters;
	}
}

