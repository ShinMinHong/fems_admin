package framework.util;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reflection Util
 *
 * @author ByeongDon
 */
public class ReflectionUtil {
	protected static final Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

	private static final List<String> NOT_USER_METHODS = Arrays.asList("getStackTrace", "getCurrentMethodName", "getStackTraceMethodName");

	private ReflectionUtil() { /**/ }

	/**
     * 현재메소드명 획득
     */
    public static String getCurrentMethodName() {
        return getStackTraceMethodName(0);
    }
    /**
     * 현재메소드명 또는 호출자의 메소드명을 획득
     * @param upperDepth 0이면 현재메소드명, 1이상이면 상위 호출 depth 메소드명
     * @return 메소드명
     */
    public static String getStackTraceMethodName(final int upperDepth) {
        final StackTraceElement[] steList = Thread.currentThread().getStackTrace();
        int length = steList.length;
        int startIndex = 0;
        for (int i=0; i<length; i++) {
            String methodName = steList[i].getMethodName();
            if(NOT_USER_METHODS.contains(methodName)) {
                startIndex++;
            } else {
                break;
            }
        }
        //[DUMP TEST]
        //for (int i=0; i<length; i++) {
        //  String methodName = steList[i].getMethodName();
        //  logger.debug("{} - {}", i, methodName);
        //}
        return steList[startIndex + upperDepth].getMethodName();
    }

}
