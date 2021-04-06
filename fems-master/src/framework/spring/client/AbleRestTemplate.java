package framework.spring.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * HTTP API 연동을 위한 RestTemplate 확장
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleRestTemplate extends RestTemplate {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	public AbleRestTemplate() {
		super();
	}
	public AbleRestTemplate(ClientHttpRequestFactory requestFactory) {
		super(requestFactory);
	}
	public AbleRestTemplate(List<HttpMessageConverter<?>> messageConverters) {
		super(messageConverters);
	}
}

