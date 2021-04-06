package framework.servlet.filter.wrap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Ablecoms TeeServletOutputStream. Logback Access의 TeeServletOutputStream 참고.
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 *
 */
public class AbleTeeServletOutputStream extends ServletOutputStream {

	static final Logger logger = LoggerFactory.getLogger(AbleTeeServletOutputStream.class);

	final ServletOutputStream underlyingStream;
	final ByteArrayOutputStream baosCopy;

	AbleTeeServletOutputStream(ServletResponse httpServletResponse) throws IOException {
		logger.trace("TeeServletOutputStream.constructor() called");
		this.underlyingStream = httpServletResponse.getOutputStream();
		baosCopy = new ByteArrayOutputStream();
	}

	byte[] getOutputStreamAsByteArray() {
		return baosCopy.toByteArray();
	}

	@Override
	public void write(int val) throws IOException {
		if (underlyingStream != null) {
			underlyingStream.write(val);
			baosCopy.write(val);
		}
	}

	@Override
	public void write(byte[] byteArray) throws IOException {
		if (underlyingStream == null) {
			return;
		}
		logger.trace("WRITE TeeServletOutputStream.write(byte[]) called. byteArray: {}", StringUtils.trimTrailingWhitespace(new String(byteArray)));
		write(byteArray, 0, byteArray.length);
	}

	@Override
	public void write(byte byteArray[], int offset, int length) throws IOException {
		if (underlyingStream == null) {
			return;
		}
		logger.trace("WRITE TeeServletOutputStream.write(byte[], int, int) called. offset:{}, length:{}, byteArray: {}", offset, length, StringUtils.trimTrailingWhitespace(new String(byteArray, offset, length)));
		underlyingStream.write(byteArray, offset, length);
		baosCopy.write(byteArray, offset, length);
	}

	@Override
	public void close() throws IOException {
		logger.trace("CLOSE TeeServletOutputStream.close() called");

		// If the servlet accessing the stream is using a writer instead of
		// an OutputStream, it will probably call os.close() before calling
		// writer.close. Thus, the underlying output stream will be called
		// before the data sent to the writer could be flushed.
	}

	@Override
	public void flush() throws IOException {
		if (underlyingStream == null) {
			return;
		}
		logger.trace("FLUSH TeeServletOutputStream.flush() called");
		underlyingStream.flush();
		baosCopy.flush();
	}

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public void setWriteListener(WriteListener arg0) {
		/**/
	}
}
