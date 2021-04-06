package framework.http.converter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
/**
 * {@link HttpMessageConvert}를 래핑하여 입출력 내용을 Tee하여 로깅하는 클래스
 *
 */
public class CommonTeeHttpMessageConvert<T> implements GenericHttpMessageConverter<T> {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    HttpMessageConverter<T> httpConverter = null;

    public CommonTeeHttpMessageConvert(HttpMessageConverter<T> httpConverter) {
        this.httpConverter = httpConverter;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.http.converter.HttpMessageConverter#canRead(java.lang.Class, org.springframework.http.MediaType)
     */
    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return httpConverter.canRead(clazz, mediaType);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.http.converter.HttpMessageConverter#canWrite(java.lang.Class, org.springframework.http.MediaType)
     */
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return httpConverter.canWrite(clazz, mediaType);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.http.converter.HttpMessageConverter#getSupportedMediaTypes()
     */
    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return httpConverter.getSupportedMediaTypes();
    }

    @Override
    public T read(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        CommonTeeHttpInputMessage teeHttpInputMessage = new CommonTeeHttpInputMessage(inputMessage);
        T result = httpConverter.read(clazz, teeHttpInputMessage);
        logger.debug("\r\n  >>>> READ: {}", teeHttpInputMessage.getTeeInputString());
        return result;
    }

    @Override
    public void write(T t, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        CommonTeeHttpOutputMessage teeHttpOutputMessage = new CommonTeeHttpOutputMessage(outputMessage);
        httpConverter.write(t, contentType, teeHttpOutputMessage);
        logger.debug("\r\n  >>>> WRITE: {}", teeHttpOutputMessage.getTeeOutputString());
    }
    
	/** GenericHttpMessageConverter#canRead 구현 */
	@Override
	public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
		if(httpConverter instanceof GenericHttpMessageConverter) {
			GenericHttpMessageConverter<?> genericHttpConverter = (GenericHttpMessageConverter<?>) httpConverter;
			return genericHttpConverter.canRead(type, contextClass, mediaType);
		}
		return false;
	}

	/** GenericHttpMessageConverter#read 구현 */
	@Override
	public T read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		if(httpConverter instanceof GenericHttpMessageConverter) {
			GenericHttpMessageConverter<T> genericHttpConverter = (GenericHttpMessageConverter<T>) httpConverter;
			return genericHttpConverter.read(type, contextClass, inputMessage);
		}
		return null;
	}

	/** GenericHttpMessageConverter#canWrite 구현 */
	@Override
	public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
		if(httpConverter instanceof GenericHttpMessageConverter) {
			GenericHttpMessageConverter<?> genericHttpConverter = (GenericHttpMessageConverter<?>) httpConverter;
			return genericHttpConverter.canWrite(type, clazz, mediaType);
		}
		return false;
	}

	/** GenericHttpMessageConverter#write 구현 */
	@Override
	public void write(T t, Type type, MediaType contentType, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		if(httpConverter instanceof GenericHttpMessageConverter) {
			GenericHttpMessageConverter<T> genericHttpConverter = (GenericHttpMessageConverter<T>) httpConverter;
			genericHttpConverter.write(t, type, contentType, outputMessage);
		}
	}

}
