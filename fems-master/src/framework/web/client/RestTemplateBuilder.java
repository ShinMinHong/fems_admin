package framework.web.client;


import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import framework.exception.AbleRuntimeException;
import framework.http.converter.CommonMapHttpMessageConverter;
import framework.http.converter.CommonTeeHttpMessageConvert;

import framework.fasterxml.jackson.AbleObjectMapper;

public class RestTemplateBuilder {
    private static final Logger logger = LoggerFactory.getLogger(RestTemplateBuilder.class);

    protected RestTemplateBuilder() { /**/ }

    /**
     * HttpsURLConnection의 기본 속성을 설정
     */
    public static void configHttpsURLConnectionDefault(SSLContext sslContext, HostnameVerifier hostnameVerifier) {
    	//전역설정
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
    }

    /**
     * RestTemplate을 빌드 (모든 SSL인증서를 신뢰, Hostname검증안함, Converter설정)
     */
    public static RestTemplate buildRestTemplate() {
    	NoopHostnameVerifier hostnameVerifier = new NoopHostnameVerifier();
    	SSLContext sslContext = buildSSLContextDefault();
    	//전역설정
		configHttpsURLConnectionDefault(sslContext, hostnameVerifier);
		//개별설정
    	return buildRestTemplate(sslContext, hostnameVerifier);
    }

    /**
     * 주어진 SSLContext와 hostnameVerifier로 TestTemplat 빌드
     * @param sslContext
     * @param hostnameVerifier
     * @return
     */
    public static RestTemplate buildRestTemplate(SSLContext sslContext, HostnameVerifier hostnameVerifier) {
    	try {
	        RestTemplate restTemplate = new RestTemplate();

	        //HttpRequestFactory 설정
	        HttpComponentsClientHttpRequestFactory factory = null;

	        //HttpRequestFactory - SSL 설정
            HttpClient httpClient = HttpClients.custom()
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier(hostnameVerifier)
                    .build();
            factory = new HttpComponentsClientHttpRequestFactory(httpClient);

            //HttpRequestFactory - TimeOut 설정
	        factory.setConnectTimeout(5000);            // Set the connection timeout of the underlying HttpClient
	        factory.setReadTimeout(15000);               // Set the socket read timeout of the underlying HttpClient
	        factory.setConnectionRequestTimeout(10000);  // Set the timeout in milliseconds used when requesting a connection from the connection manager the underlying HttpClient
	        restTemplate.setRequestFactory(factory);

	        //Converter 설정
	        configHttpMessageConverter(restTemplate);

	        return restTemplate;
        } catch (Exception ex) {
            logger.warn("buildRestTemplate Error: {}", ex.toString(), logger.isTraceEnabled() ? ex : null);
            throw new AbleRuntimeException(ex);
        }
    }

    /**
     * 모든 인증서를 신뢰하는 SSLContext 생성 (모든 Host를 허용하고, TrustCheck를 하지 않는 TrustManager 사용)
     */
    protected static SSLContext buildSSLContextDefault() {
    	try {
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                        @Override public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { /**/ }
                        @Override public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { /**/ }
                    }
            };
    		SSLContext sslContextDefault = SSLContext.getInstance("TLS");
    		sslContextDefault.init(null, trustAllCerts, new SecureRandom());
    		return sslContextDefault;
		} catch (Exception ex) {
			throw new AbleRuntimeException(ex);
		}
    }

    /**
     * Client인증서를 이용한 양방향 SSL이 가능한 SSLContext 생성
     * @param clientCertClassPath 클라이언트 인증서 경로
     * @param keyPassphrase 인증서 비밀번호
     */
	protected static SSLContext buildSSLContextForClientCert(String clientCertClassPath, String keyPassphrase) {
        try {
    		KeyStore keyStore = KeyStore.getInstance("PKCS12");
            ClassPathResource resource = new ClassPathResource(clientCertClassPath);
			keyStore.load(resource.getInputStream(), keyPassphrase.toCharArray());
	        SSLContext sslContext = SSLContexts.custom()
	                .loadKeyMaterial(keyStore, keyPassphrase.toCharArray())
	                .loadTrustMaterial(keyStore, new TrustSelfSignedStrategy())
	                .build();
			return sslContext;
		} catch (Exception ex) {
			throw new AbleRuntimeException(ex);
		}
	}

    /** Converter 설정 */
	protected static void configHttpMessageConverter(RestTemplate restTemplate) {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new CommonTeeHttpMessageConvert<MultiValueMap<String, ?>>(new AllEncompassingFormHttpMessageConverter()));
        messageConverters.add(new CommonTeeHttpMessageConvert<Map<String, ?>>(new CommonMapHttpMessageConverter()));
        messageConverters.add(new CommonTeeHttpMessageConvert<String>(new StringHttpMessageConverter()));
        messageConverters.add(new CommonTeeHttpMessageConvert<Object>(getJsonHttpMessageConverter()));
        restTemplate.setMessageConverters(messageConverters);
    }
	protected static MappingJackson2HttpMessageConverter getJsonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter httpConverter = new MappingJackson2HttpMessageConverter();
        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(new MediaType("application", "json", Charset.forName("UTF-8")));
        mediaTypes.add(new MediaType("application", "*+json", Charset.forName("UTF-8")));
        mediaTypes.add(new MediaType("text", "html", Charset.forName("UTF-8")));
        httpConverter.setSupportedMediaTypes(mediaTypes);

        //ObjectMapper의 TimeZone을 JVM TimeZone으로 설정
        ObjectMapper mapper = new AbleObjectMapper();
        httpConverter.setObjectMapper(mapper);

        return httpConverter;
    }

}



