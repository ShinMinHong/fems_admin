package framework.io;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.input.TeeInputStream;
import org.springframework.util.StringUtils;

/**
 * {@link TeeInputStream}을 이용하여 InputStream의 읽은 내용을 Tee처리하는 Wrapper
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleTeeInputStreamWrapper {

	InputStream teeStream;
	ByteArrayOutputStream baos;

	public AbleTeeInputStreamWrapper(InputStream inputStream) {
		baos = new ByteArrayOutputStream();
		this.teeStream = new TeeInputStream(inputStream, baos);
	}

	public InputStream getInputStream() {
		return teeStream;
	}

	/**
	 * @return 입력내용 Bytes
	 */
	public byte[] getTeeInputBytes() {
		return this.baos.toByteArray();
	}

	/**
	 * @return 입력내용 String (UTF-8)
	 * @throws UnsupportedEncodingException
	 */
	public String getTeeInputString() throws UnsupportedEncodingException {
		return StringUtils.trimTrailingWhitespace(this.baos.toString("UTF-8"));
	}

}
