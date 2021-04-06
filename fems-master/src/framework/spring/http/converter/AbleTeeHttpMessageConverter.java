package framework.spring.http.converter;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import framework.spring.http.AbleTeeHttpInputMessage;
import framework.spring.http.AbleTeeHttpOutputMessage;

/**
 * {@link HttpMessageConverter}를 래핑하여 입출력 내용을 Tee하여 로깅하는 클래스
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleTeeHttpMessageConverter<T> implements HttpMessageConverter<T> {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	HttpMessageConverter<T> httpConverter = null;

	public AbleTeeHttpMessageConverter(HttpMessageConverter<T> httpConverter) {
		this.httpConverter = httpConverter;
	}

	/* (non-Javadoc)
	 * @see org.springframework.http.converter.HttpMessageConverter#canRead(java.lang.Class, org.springframework.http.MediaType)
	 */
	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return httpConverter.canRead(clazz, mediaType);
	}

	/* (non-Javadoc)
	 * @see org.springframework.http.converter.HttpMessageConverter#canWrite(java.lang.Class, org.springframework.http.MediaType)
	 */
	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return httpConverter.canWrite(clazz, mediaType);
	}

	/* (non-Javadoc)
	 * @see org.springframework.http.converter.HttpMessageConverter#getSupportedMediaTypes()
	 */
	@Override
	public List<MediaType> getSupportedMediaTypes() {
		return httpConverter.getSupportedMediaTypes();
	}

	@Override
	public T read(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		AbleTeeHttpInputMessage teeHttpInputMessage = new AbleTeeHttpInputMessage(inputMessage);
		T result = httpConverter.read(clazz, teeHttpInputMessage);
		logger.debug("\r\n  >>>> READ: {}", teeHttpInputMessage.getTeeInputString());
		return result;
	}

	@Override
	public void write(T t, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		AbleTeeHttpOutputMessage teeHttpOutputMessage = new AbleTeeHttpOutputMessage(outputMessage);
		httpConverter.write(t, contentType, teeHttpOutputMessage);
		logger.debug("\r\n  >>>> WRITE: {}", teeHttpOutputMessage.getTeeOutputString());
	}

}
