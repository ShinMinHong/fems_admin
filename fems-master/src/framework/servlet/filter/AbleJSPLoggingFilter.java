package framework.servlet.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.WebUtils;

/**
 * Logging Filter for every invokation Smartro JSP
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleJSPLoggingFilter implements Filter {

	/** Logger available to subclasses */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected FilterConfig config;

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}

	@Override
	public void destroy() { /**/ }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try{
			ServletResponse responseWrapper = response;

			if(logger.isDebugEnabled()) {
				logger.debug("\r\n\t##################################################"
						+ "\r\n\t#### START JSP - " + getServletPathName(request)
		                + "\r\n\t\trequest parameter: " + getLoggableRequestMap(request));
			}

			chain.doFilter(request, response);

			if(logger.isDebugEnabled()) {
				logger.debug("\r\n\t#### END JSP - " + getServletPathName(request)
						+ "\r\n\t##################################################"
		        		+ getLoggableResponseBody(responseWrapper));
			}

		} catch(Exception ex) {
			if(logger.isDebugEnabled()) {
	    		logger.error("{} FAILED.\r\nex message: {}", getServletPathName(request), ex.toString(), ex);
	    	}
		}
	}

	private String getServletPathName(ServletRequest request) {
		if(request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest)request;
			return httpRequest.getServletPath();
		}
		return request.toString();
	}

	private String getLoggableResponseBody(ServletResponse response) {
		HashMap<String, String> map = new HashMap<String, String>();

		String contentType = response.getContentType();
		map.put("contentType", contentType);

		HttpServletResponse httpRequest = WebUtils.getNativeResponse(response, HttpServletResponse.class);
		if(httpRequest != null) {
			int status = httpRequest.getStatus();
			map.put("StatusCode", String.valueOf(status));
		}

		StringBuilder sb = new StringBuilder();
		for(Entry<String, String> item : map.entrySet()) {
			sb.append("\r\n\t\t- " + item.getKey() + ": " + item.getValue());
		}
		return sb.toString();
	}

	/**
	 * ??????????????? ??????????????? ????????? ??????????????? Map<String, String> ????????? ??????
	 *
	 * request.getParameterMap()??? Map<String, String[]>?????? Map<String, String>?????? ????????? ???????????? ???????????? ??????
	 *
	 * @param request ?????? ??????
	 * @return Map<String, String> ????????? ?????? ???????????????
	 */
	public static Map<String, String> getLoggableRequestMap(ServletRequest request) {
		Map<String, String> result = new HashMap<String, String>();
		Map<String, String[]> parameterMap = request.getParameterMap();
		String key;
		String[] value;
		for(Entry<String, String[]> item : parameterMap.entrySet()) {
			key = item.getKey();
			value = item.getValue();
			result.put(key, Arrays.asList(value).toString());
		}
		return result;
	}
}

