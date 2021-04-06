package framework.security.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.UrlUtils;

/**
 * 항상 HTTP쪽으로 리다이렉트
 */
public class AbleForceHttpRedirectStrategy implements RedirectStrategy {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		String scheme = request.getScheme()+"://";
		
		StringBuffer fullUrl = request.getRequestURL();
		String uri = request.getRequestURI();
		int idx = (((uri != null) && (uri.length() > 0)) ? fullUrl.indexOf(uri) : fullUrl.length());
		String contextPath = fullUrl.substring(0, idx);


    	String redirectUrl = calculateRedirectUrl(scheme, contextPath, url);
    	redirectUrl = response.encodeRedirectURL(redirectUrl);

		if (logger.isDebugEnabled()) {
			logger.debug("Redirecting to '{}'", redirectUrl);
		}
		response.sendRedirect(redirectUrl);
	}

	private String calculateRedirectUrl(String scheme, String contextPath, String url) {
		logger.debug("calculateRedirectUrl contextPath:{}, url:{}", contextPath, url);
		//상대경로라면 Context Path 붙이는 작업 진행
		if (!UrlUtils.isAbsoluteUrl(url)) {
			url = contextPath + url;
			logger.debug("calculateRedirectUrl Relative -> Absolute Full Url : {}", url);
			return url;
		}

		//절대 경로라면 앞의 Schema를 제거하고, 전달받은 https://를 붙임
		logger.debug("calculateRedirectUrl UrlUtils.isAbsoluteUrl : true. url.lastIndexOf :// :{}", url.lastIndexOf("://"));
		url = url.substring(url.lastIndexOf("://") + 3); // strip off scheme
		url = scheme + url;
		logger.debug("calculateRedirectUrl Absolute -> Absolute Full Url : {}", url);

		return url;
	}
	
//	/**
//	 * Redirects the response to the supplied URL.
//	 * <p>
//	 * If <tt>contextRelative</tt> is set, the redirect value will be the value after the
//	 * request context path. Note that this will result in the loss of protocol
//	 * information (HTTP or HTTPS), so will cause problems if a redirect is being
//	 * performed to change to HTTPS, for example.
//	 */
//	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
//
////    	String requestUrl = request.getRequestURI();
//        String redirectUrl = calculateRedirectUrl(request.getContextPath(), url);
//        redirectUrl = response.encodeRedirectURL(redirectUrl);
//        String redirectParamUri = request.getHeader("Referer");
//        String removeHost = request.getScheme() + "://" + request.getHeader("Host");
//        String redirectParamURL = "";
//
//		if (!StringUtils.isEmpty(redirectParamUri)) {
//			redirectParamURL = redirectParamUri.replace(removeHost, "");
//		}
//
//        redirectUrl = ( redirectUrl.indexOf("?") > -1 ? redirectUrl + "&" : redirectUrl + "?" ) + "targeturl=" + request.getContextPath() + redirectParamURL;
//
//		if (logger.isDebugEnabled()) {
//			logger.debug("Redirecting to '" + redirectUrl + "'");
//		}
//
//		response.sendRedirect(redirectUrl);
//	}
//
//    private String calculateRedirectUrl(String contextPath, String url) {
//        if (!UrlUtils.isAbsoluteUrl(url)) {
//            if (contextRelative) {
//                return url;
//            } else {
//                return contextPath + url;
//            }
//        }
//
//        // Full URL, including http(s)://
//        if (!contextRelative) {
//            return url;
//        }
//
//        // Calculate the relative URL from the fully qualified URL, minus the last
//        // occurrence of the scheme and base context.
//        url = url.substring(url.lastIndexOf("://") + 3); // strip off scheme
//        url = url.substring(url.indexOf(contextPath) + contextPath.length());
//
//        if (url.length() > 1 && url.charAt(0) == '/') {
//            url = url.substring(1);
//        }
//
//        return url;
//    }
}
