package framework.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionUtil {
	static Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);

	private ExceptionUtil() { }

	/**
	 * Exception 객체에서 printStackTrace 내용을 획득
	 */
	public static String getPrintStackTrace(Throwable ex) {
		try {
			if(ex == null) {
				return null;
			}
	        StringWriter errors = new StringWriter();
	        ex.printStackTrace(new PrintWriter(errors));
	        return errors.toString();
		} catch (Exception innerEx) {
			logger.warn("getPrintStackTrace FAILED: {}", innerEx, innerEx);
			logger.warn("getPrintStackTrace Original Exception: {}", ex, ex);
			return ex.toString();
		}
    }
}
