package framework.spring.client;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.util.MultiValueMap;

import framework.crypto.AES256Crypto;
import framework.spring.http.converter.AbleGenericTeeHttpMessageConverter;
import framework.spring.http.converter.AbleMapHttpMessageConverter;
import framework.spring.http.converter.AbleSecuredHttpMessageConverter;
import framework.spring.http.converter.AbleTeeHttpMessageConverter;
import framework.util.AbleUtil;

/**
 * AbleRestTemplate의 Builder
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleRestTemplateBuilder {
	static Logger logger = LoggerFactory.getLogger(AbleRestTemplateBuilder.class);

	private AbleRestTemplateBuilder() { /**/ }

	/**
	 * AbleRestTemplate를 생성 (FORM API방식, 암호화 미적용) - POST(application/x-www-form-urlencoded)
	 */
	public static AbleRestTemplate buildFormApiTemplate() {
		AbleRestTemplate restTemplate = buildRestTemplate(API_TYPE.REQUEST_FORM, null);
		return restTemplate;
	}

	/**
	 * AbleRestTemplate를 생성 (REST API방식, 암호화 미적용) - POST(application/json)
	 */
	public static AbleRestTemplate buildRestApiTemplate() {
		AbleRestTemplate restTemplate = buildRestTemplate(API_TYPE.REQUEST_BODY_JSON, null);
		return restTemplate;
	}

	/**
	 * AbleRestTemplate를 생성 (REST API방식, 암호화 적용, 파라미터에 따라 실서버용/개발용 암호화키 사용) - POST(application/json)
	 * @param secretKey 암호화시 사용할 키 (API_TYPE.REQUEST_BODY_SECURED_JSON에만 사용)
	 */
	public static AbleRestTemplate buildSecureRestApiTemplate(String secretKey) {
		AbleRestTemplate restTemplate = buildRestTemplate(API_TYPE.REQUEST_BODY_SECURED_JSON, secretKey);
		return restTemplate;
	}

	/**
	 * 내부용 - 실제로 AbleRestTemplate을 환경에 맞춰 생성
	 * @param apiType api의 통신 방식
	 * @param secretKey 암호화시 사용할 키 (API_TYPE.REQUEST_BODY_SECURED_JSON에만 사용)
	 */
	private static AbleRestTemplate buildRestTemplate(API_TYPE apiType, String secretKey) {
		AbleRestTemplate restTemplate = new AbleRestTemplate();

		//MessageConverter 설정
		List<HttpMessageConverter<?>> messageConverters = getHttpMessageConverters(apiType, secretKey);
		restTemplate.setMessageConverters(messageConverters);

		//Logging을 위한 Interceptor 설정
		List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
		interceptors.add(new AbleLoggingClientHttpRequestInterceptor());
		restTemplate.setInterceptors(interceptors);

		//ResponseBody를 여러번 읽을 수 있도록 ClientHttpRequestFactory 처리
		ClientHttpRequestFactory requestFactoryInner = new SimpleClientHttpRequestFactory(); //SimpleClientHttpRequestFactory: 기본 JDK기능을 이용
		//ClientHttpRequestFactory requestFactoryInner = new HttpComponentsClientHttpRequestFactory(); //HttpComponentsClientHttpRequestFactory: Apache HttpComponent 4.3이상을 이용

		Proxy proxy = null;

		//WindowsOS이고 real 운영환경이 아닌경우 socks proxy 지원
		boolean isWindowsOS = System.getProperty("os.name").contains("Windows");
		if(isWindowsOS && !AbleUtil.isProductionProfile()) {
			String socksProxyHost = System.getenv("socksProxyHost");
			String socksProxyPort = System.getenv("socksProxyPort");
			if(!StringUtils.isEmpty(socksProxyHost) && !StringUtils.isEmpty(socksProxyPort)) {
				logger.debug("#### APPLY SOCK PROXY. {}:{} ####", socksProxyHost, socksProxyPort);
				//apply local ssh tunnel sock proxy
				int socksProxyPortInt = Integer.parseInt(socksProxyPort);
				proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(socksProxyHost, socksProxyPortInt));
			}
		}

		if(proxy == null) {
			//Proxy 설정
			String proxySet = System.getProperty("proxySet");
			logger.debug("AbleRestTemplateBuilder - proxySet.{}", proxySet);
			if("true".equals(proxySet)) {
				String proxyHost = System.getProperty("proxyHost");
				String proxyPort = System.getProperty("proxyPort");
				logger.debug("AbleRestTemplateBuilder - proxyHost:{}, proxyPort:{}", proxyHost, proxyPort);
				if(!StringUtils.isEmpty(proxyHost) && !StringUtils.isEmpty(proxyPort)) {
					logger.debug("AbleRestTemplateBuilder - Apply Proxy. {}:{}", proxyHost, proxyPort);
					int proxyPortInt = Integer.parseInt(proxyPort);
					if(requestFactoryInner instanceof SimpleClientHttpRequestFactory) {
						//SimpleClientHttpRequestFactory requestFactoryInnerTyped = (SimpleClientHttpRequestFactory)requestFactoryInner;
						proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPortInt));
					} else if(requestFactoryInner instanceof HttpComponentsClientHttpRequestFactory) {
						HttpComponentsClientHttpRequestFactory requestFactoryInnerTyped = (HttpComponentsClientHttpRequestFactory)requestFactoryInner;
						HttpHost httpProxy = new HttpHost(proxyHost, proxyPortInt);
						HttpClient httpClient = HttpClients.custom().setProxy(httpProxy).build();
						requestFactoryInnerTyped.setHttpClient(httpClient);
					}
				}
			}
		}

		if(proxy != null) {
			if(requestFactoryInner instanceof SimpleClientHttpRequestFactory) {
				SimpleClientHttpRequestFactory requestFactoryInnerTyped = (SimpleClientHttpRequestFactory)requestFactoryInner;
				requestFactoryInnerTyped.setProxy(proxy);
			} else if(requestFactoryInner instanceof HttpComponentsClientHttpRequestFactory) {
				//HttpComponentsClientHttpRequestFactory requestFactoryInnerTyped = (HttpComponentsClientHttpRequestFactory)requestFactoryInner;
				//HttpComponentsClientHttpRequestFactory의 SOCKS Proxy 지원은 구현필요
				logger.warn("#### APPLY SOCK NOT SUPPORTED for HttpComponentsClientHttpRequestFactory ####");
			}
		}


		ClientHttpRequestFactory requestFactory = new BufferingClientHttpRequestFactory(requestFactoryInner);
		restTemplate.setRequestFactory(requestFactory);
		return restTemplate;
	}


	/**
	 * RestTemplate에 설정할 MessageConverter 목록을 생성하여 획득 (JSON용 Converter 반환)
	 * @param apiType api의 통신 방식
	 * @param secretKey 암호화시 사용할 키 (API_TYPE.REQUEST_BODY_SECURED_JSON에만 사용)
	 */
	private static List<HttpMessageConverter<?>> getHttpMessageConverters(API_TYPE apiType, String secretKey) {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		if(apiType.equals(API_TYPE.REQUEST_BODY_SECURED_JSON)) {
			HttpMessageConverter<?> converter = getSecuredJsonConverter(secretKey);
			messageConverters.add(converter);
		} else if(apiType.equals(API_TYPE.REQUEST_BODY_JSON)) {
			messageConverters.add(new AbleTeeHttpMessageConverter<Object>(getNonSecuredJsonConverter()));
		} else if(apiType.equals(API_TYPE.REQUEST_FORM)) {
			messageConverters.add(new AbleTeeHttpMessageConverter<MultiValueMap<String, ?>>(new AllEncompassingFormHttpMessageConverter()));
			messageConverters.add(new AbleTeeHttpMessageConverter<Map<String, ?>>(new AbleMapHttpMessageConverter()));
			messageConverters.add(new AbleGenericTeeHttpMessageConverter<Object>(getNonSecuredJsonConverter()));
			//messageConverters.add(new AbleTeeHttpMessageConverter<String>(new StringHttpMessageConverter()));
		}
		return messageConverters;
	}

	private static HttpMessageConverter<?> getSecuredJsonConverter(String secretKey) {
		MappingJackson2HttpMessageConverter httpConverter = new MappingJackson2HttpMessageConverter();
		List<MediaType> mediaTypes = new ArrayList<MediaType>();
		mediaTypes.add(new MediaType("application", "secured+json", Charset.forName("UTF-8")));
		httpConverter.setSupportedMediaTypes(mediaTypes);
		AES256Crypto crypto = new AES256Crypto(secretKey);
		HttpMessageConverter<?> converter = new AbleSecuredHttpMessageConverter<Object>(crypto, httpConverter);
		return converter;
	}

	private static MappingJackson2HttpMessageConverter getNonSecuredJsonConverter() {
		MappingJackson2HttpMessageConverter httpConverter = new MappingJackson2HttpMessageConverter();
		List<MediaType> mediaTypes = new ArrayList<MediaType>();
		mediaTypes.add(new MediaType("application", "json", Charset.forName("UTF-8")));
		mediaTypes.add(new MediaType("application", "*+json", Charset.forName("UTF-8")));
		mediaTypes.add(new MediaType("text", "html", Charset.forName("UTF-8")));
		mediaTypes.add(new MediaType("text", "*"));
		httpConverter.setSupportedMediaTypes(mediaTypes);
		return httpConverter;
	}
}
