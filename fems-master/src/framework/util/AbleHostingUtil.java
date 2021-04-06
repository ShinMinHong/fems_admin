package framework.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * WAS Hosting 환경관련 Util
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleHostingUtil {

	private AbleHostingUtil() { /**/ }

	/**
	 * 실행중인 WAS의 Container명을 획득
	 * @return WAS Container Name
	 */
	public static String getWasContainerName() {
		//Weblogic WAS Container Name
		String name = System.getProperty("weblogic.Name");
		if(StringUtils.isNotEmpty(name)) {
			return name;
		}
		//NOTE: 다른 WAS 컨테이너에 대한 대응 필요시 여기에 코딩
		return "";
	}

	/** WEB Proxy를 통한 접근인가? */
	public static boolean isProxyRequest(HttpServletRequest request) {
		String clientIP = request.getHeader("client-ip"); //L7을 통한경우 L7-WEB-WAS
		String proxyClientIP = request.getHeader("Proxy-Client-IP");
		String xForwardedFor = request.getHeader("X-Forwarded-For");
		if(StringUtils.isNotEmpty(clientIP) || StringUtils.isNotEmpty(proxyClientIP) || StringUtils.isNotEmpty(xForwardedFor)) {
			return true;
		}
		return false;
	}

	/** LocalHost, LocalIP로 접근 */
	public static boolean isLocalhostRequest(HttpServletRequest request) {
		String remoteAddr = request.getRemoteAddr();
		String localAddr = request.getLocalAddr();
		if(remoteAddr.equals(localAddr)) {
			return true; //Local IP를 통한 접근
		}
		if("0:0:0:0:0:0:0:1".equals(remoteAddr)) {
			return true; //localhost로 접근
		}
		return false;
	}

	/** Client의 IP를 출력 */
	public static String getClientIP(HttpServletRequest request) {
		String xForwardedFor = request.getHeader("X-FORWARDED-FOR"); //WEB을 거쳐서 WAS에 온경우 Origin IP
		if(StringUtils.isNotEmpty(xForwardedFor)) {
			return xForwardedFor;
		}

		String clientIP = request.getHeader("client-ip"); //L7을 통한경우 L7-WEB-WAS
		if(StringUtils.isNotEmpty(clientIP)) {
			return clientIP;
		}

		String proxyClientIP = request.getHeader("Proxy-Client-IP");
		if(StringUtils.isNotEmpty(proxyClientIP)) {
			return proxyClientIP;
		}

		return request.getRemoteAddr();
	}
}

