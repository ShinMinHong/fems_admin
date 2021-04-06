package framework.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 호스팅 관련 Util
 *
 * @author ByeongDon
 */
public class HostUtil {
	protected static final Logger logger = LoggerFactory.getLogger(HostUtil.class);

	private HostUtil() { /**/ }

    /**
     * OS가 Windows 인지 여부
     */
    public static boolean isWindowsOS() {
        return System.getProperty("os.name").contains("Windows");
    }

    /**
     * 서버의 IP를 획득 (InetAddress.getLocalHost()) - Safe함수, 실패시 null
     */
    public static String getServerIPSafe() {
    	try {
    		return getServerIPUnsafe();
		} catch (UnknownHostException ex) {
			logger.trace("UnknownHostException - {}", ex.getMessage(), ex);
			return null;
		}
    }

    /**
     * 서버의 IP를 획득 (InetAddress.getLocalHost()) - Safe함수
     */
    public static String getServerIPSafe(String defaultValue) {
    	try {
    		return getServerIPUnsafe();
		} catch (UnknownHostException ex) {
			logger.warn("UnknownHostException - {}", ex.getMessage(), ex);
			return defaultValue;
		}
    }

    /**
     * 서버의 IP를 획득 (InetAddress.getLocalHost())
     */
    public static String getServerIPUnsafe() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        String serverIP = inetAddress.getHostAddress();
        return serverIP;
    }

    public static List<String> getServerIpListInMultiNetworkCard() {
    	try {
    		return getServerIpListInMultiNetworkCardUnSafe();
    	} catch (Exception ex) {
    		logger.warn("서버 IP 목록 획득 실패. {}", ex.getMessage(), ex);
    		return Collections.emptyList();
    	}
    }
    
    
    /**
     * @return 네트워크 카드가 여러장인 경우 IP 추출 
     * @throws SocketException
     */
    public static List<String> getServerIpListInMultiNetworkCardUnSafe() throws SocketException {
    	List<String> ipList = new ArrayList<>();
    	Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
    	while(nis.hasMoreElements())
    	{
    	    NetworkInterface ni = nis.nextElement();
    	    Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
    	    while (inetAddresses.hasMoreElements())
    	    {
    	    	InetAddress i = inetAddresses.nextElement();
    	        ipList.add(i.getHostAddress());
    	    }
    	}
    	return ipList;
    }

}
