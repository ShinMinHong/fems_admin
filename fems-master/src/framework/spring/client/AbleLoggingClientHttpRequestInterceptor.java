package framework.spring.client;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import framework.util.AbleConvertUtil;
import framework.util.AbleIOUtil;

public class AbleLoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		ClientHttpResponse response = null;
		try {
			response = execution.execute(request, body);
			if(logger.isDebugEnabled()) {
				String responseBody = AbleIOUtil.readStringToEnd(response.getBody());
				logger.info(""
						+ "\r\n\t\t>>>> REQUEST: {} {}"
						+ "\r\n\t\t>>>>  - header: {}"
						+ "\r\n\t\t>>>>  - body: {} "
						+ "\r\n\t\t<<<< RESPONE: status {}, "
						+ "\r\n\t\t<<<<  - header{}, "
						+ "\r\n\t\t<<<<  - body:{}",
						request.getMethod(), request.getURI(),
						request.getHeaders(),
						AbleConvertUtil.convertToString(body),
						response.getStatusCode(),
						response.getHeaders(),
						responseBody);
			}
		} catch (Exception ex) {
			if(logger.isDebugEnabled()) {
				HttpStatus responseStatus = null;
				HttpHeaders responseHeaders = null;
				String responseBody = null;
				if(response != null) {
					responseStatus = response.getStatusCode();
					responseHeaders = response.getHeaders();
					try {
						responseBody = AbleIOUtil.readStringToEnd(response.getBody());
					} catch (IOException e) {
						logger.trace("{} - {}", e.getClass().getName(), e.getMessage(), e);
						responseBody = "<FAILED TO READ>";
					}
				}
				logger.warn("HTTP EXCEPTION: {}"
						+ "\r\n\t\t>>>> REQUEST: {} {}"
						+ "\r\n\t\t>>>>  - header: {}"
						+ "\r\n\t\t>>>>  - body: {} "
						+ "\r\n\t\t<<<< RESPONE: status: {}, "
						+ "\r\n\t\t<<<<  - header: {}, "
						+ "\r\n\t\t<<<<  - body:{}",
						ex.getMessage(),
						request.getMethod(), request.getURI(),
						request.getHeaders(),
						AbleConvertUtil.convertToString(body),
						responseStatus,
						responseHeaders,
						responseBody);
			}
			throw new IOException(ex);
		}
		return response;
	}
}
