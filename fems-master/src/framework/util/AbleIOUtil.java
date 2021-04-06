package framework.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.springframework.util.StringUtils;

/**
 * IO 관련 유틸
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleIOUtil {

	private AbleIOUtil() { /**/ }

	/**
	 * InputStream에서 문자열을 끝까지 읽는다. (UTF-8 인코딩 적용)
	 */
	public static String readStringToEnd(InputStream is) throws IOException {
		return readStringToEnd(is, "UTF-8");
	}

	/**
	 * InputStream에서 문자열을 끝까지 읽는다. (주어진 인코딩 적용)
	 */
	public static String readStringToEnd(InputStream is, String charsetName) throws IOException {
		String body = new String(readBytesToEnd(is), Charset.forName(charsetName));
		return StringUtils.trimTrailingWhitespace(body);
	}

	/**
	 * InputStream에서 Byte[]을 끝까지 읽는다.
	 */
	public static byte[] readBytesToEnd(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		while((length = is.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}
		return baos.toByteArray();
	}

}
