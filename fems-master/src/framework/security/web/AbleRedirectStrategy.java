package framework.security.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.UrlUtils;

import com.firealarm.admin.appconfig.AppConst;

import framework.spring.web.rest.AbleResponseEntity;
import framework.spring.web.rest.AbleResponseEntity.REST_CALLBACK_CMD;
import framework.spring.web.rest.AbleResponseEntityBuilder;
import framework.util.AbleUtil;


/**
 * DefaultRedirectStrategy와 동일
 *
 * 요청이 Ajax인경우에는 REST 응답으로 Redirect 처리
 *
 * @see org.springframework.security.web.DefaultRedirectStrategy
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleRedirectStrategy implements RedirectStrategy {

    protected final Log logger = LogFactory.getLog(getClass());

    private boolean contextRelative;

    /**
     * Redirects the response to the supplied URL.
     * <p>
     * If <tt>contextRelative</tt> is set, the redirect value will be the value after the request context path. Note
     * that this will result in the loss of protocol information (HTTP or HTTPS), so will cause problems if a
     * redirect is being performed to change to HTTPS, for example.
     */
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
    	String requestUrl = request.getRequestURI();
        String redirectUrl = calculateRedirectUrl(request.getContextPath(), url);
        redirectUrl = response.encodeRedirectURL(redirectUrl);
        if(isAjaxRequest(request)) {
        	AbleResponseEntity<Object> entity = AbleResponseEntityBuilder
        			.success()
        			.msg("")
        			.callback(REST_CALLBACK_CMD.REDIRECT, redirectUrl)
        			.build();
        	if (logger.isDebugEnabled()) {
                logger.debug("Redirecting Ajax '" + requestUrl + "' to '" + redirectUrl + "'");
            }
        	response.setContentType(AppConst.API_PRODUCES);
        	response.getWriter().write(AbleUtil.toJson(entity));
        } else {
        	if (logger.isDebugEnabled()) {
                logger.debug("Redirecting '" + requestUrl + "' to '" + redirectUrl + "'");
            }
        	response.sendRedirect(redirectUrl);
        }
    }

    /**
     * 주어진 요청이 Ajax요청인지 여부
     * @param request 판단할 요청 객체
     * @return Ajax 요청 여부
     */
    protected boolean isAjaxRequest(HttpServletRequest request) {
        if(request.getHeader("X-Requested-With") != null) {
        	String xRequestWith = request.getHeader("X-Requested-With").toLowerCase();
        	if( xRequestWith.indexOf("xmlhttprequest") > -1 || xRequestWith.indexOf("ajax") > -1 || xRequestWith.indexOf("iframe") > -1 ) {
        		return true;
        	}
        }
        return false;
    }

    private String calculateRedirectUrl(String contextPath, String url) {

        if (!UrlUtils.isAbsoluteUrl(url)) {
            if (contextRelative) {
                return url;
            } else {
                return contextPath + url;
            }
        }

        // Full URL, including http(s)://
        if (!contextRelative) {
            return url;
        }

        // Calculate the relative URL from the fully qualified URL, minus the last
        // occurrence of the scheme and base context.
        url = url.substring(url.lastIndexOf("://") + 3); // strip off scheme
        url = url.substring(url.indexOf(contextPath) + contextPath.length());
        if (url.length() > 1 && url.charAt(0) == '/') {
            url = url.substring(1);
        }

        return url;
    }

    /**
     * If <tt>true</tt>, causes any redirection URLs to be calculated minus the protocol
     * and context path (defaults to <tt>false</tt>).
     */
    public void setContextRelative(boolean useRelativeContext) {
        this.contextRelative = useRelativeContext;
    }

}

