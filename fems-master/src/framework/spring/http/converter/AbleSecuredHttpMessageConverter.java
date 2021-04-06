package framework.spring.http.converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import framework.crypto.AES256Crypto;
import framework.spring.http.AbleHttpInputMessage;
import framework.spring.http.AbleHttpOutputMessage;
import framework.spring.http.AbleTeeHttpInputMessage;
import framework.spring.http.AbleTeeHttpOutputMessage;

/**
 * {@link HttpMessageConverter}에 암호화 기능을 추가하는 Generic Converter
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 *
 */
public class AbleSecuredHttpMessageConverter<T> implements HttpMessageConverter<T> {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	AES256Crypto crypto;
	HttpMessageConverter<T> httpConverter = null;

	/**
	 * 생성자
	 * @param crypto 전송계층에 암호화를 처리하기 위한 암호화처리기
	 * @param httpConverter 실제 평문에 대한 메시지 변환을 위한 내부 HttpMessageConverter
	 */
	public AbleSecuredHttpMessageConverter(AES256Crypto crypto, HttpMessageConverter<T> httpConverter) {
		super();
		if(crypto == null || httpConverter == null) {
			throw new IllegalArgumentException("crypto, httpConverter parameters cannot be null.");
		}
		this.crypto = crypto;
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

	/* (non-Javadoc)
	 * @see org.springframework.http.converter.HttpMessageConverter#read(java.lang.Class, org.springframework.http.HttpInputMessage)
	 */
	@Override
	public T read(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		AbleTeeHttpInputMessage teeHttpInputMessage = new AbleTeeHttpInputMessage(inputMessage); //최초 입력 암호문 Tee처리
		InputStream cis = this.crypto.getDecryptChiperInputStream(teeHttpInputMessage.getBody());
		AbleHttpInputMessage cryptoInputMessage = new AbleHttpInputMessage(teeHttpInputMessage, cis);
		AbleTeeHttpInputMessage teeCryptoHttpInputMessage = new AbleTeeHttpInputMessage(cryptoInputMessage); //복호화된 평문 Tee처리
		T result = httpConverter.read(clazz, teeCryptoHttpInputMessage);
		logger.debug("\r\n  >>>> READ: {}"
				+ "\r\n  >>>>       => {}",
				teeHttpInputMessage.getTeeInputString(),
				teeCryptoHttpInputMessage.getTeeInputString());
		return result;
	}

	/* (non-Javadoc)
	 * @see org.springframework.http.converter.HttpMessageConverter#write(java.lang.Object, org.springframework.http.MediaType, org.springframework.http.HttpOutputMessage)
	 */
	@Override
	public void write(T t, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		AbleTeeHttpOutputMessage teeHttpOutputMessage = new AbleTeeHttpOutputMessage(outputMessage); //최종 암호문 출력 Tee처리
		OutputStream cos = this.crypto.getEncryptChiperOutputStream(teeHttpOutputMessage.getBody());
		AbleHttpOutputMessage cryptoOutputMessage = new AbleHttpOutputMessage(teeHttpOutputMessage, cos);
		AbleTeeHttpOutputMessage teeCryptoHttpOutputMessage = new AbleTeeHttpOutputMessage(cryptoOutputMessage); //평문 Tee처리
		httpConverter.write(t, contentType, teeCryptoHttpOutputMessage);
		cos.close();
		logger.debug("\r\n  >>>> WRITE: {}"
				+ "\r\n  >>>>        => {}",
				teeCryptoHttpOutputMessage.getTeeOutputString(),
				teeHttpOutputMessage.getTeeOutputString());
	}
}

