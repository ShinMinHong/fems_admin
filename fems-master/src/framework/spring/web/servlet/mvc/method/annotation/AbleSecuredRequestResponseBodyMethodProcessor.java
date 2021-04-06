package framework.spring.web.servlet.mvc.method.annotation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import framework.crypto.AES256Crypto;
import framework.io.AbleTeeInputStreamWrapper;
import framework.io.AbleTeeOutputStreamWrapper;
import framework.spring.http.AbleTeeHttpInputMessage;
import framework.spring.http.AbleTeeHttpOutputMessage;
import framework.spring.web.bind.annotation.SecuredRequestBody;
import framework.spring.web.bind.annotation.SecuredResponseBody;

/**
 * {@link RequestBody}, {@link ResponseBody}를 처리하는 {@link RequestResponseBodyMethodProcessor}와 유사하지만,
 * {@link SecuredRequestBody}, {@link SecuredResponseBody}를 암복호화하여 처리
 *
 * 상속등으로 사용하고, secretKey를 서비스에 맞춰서 설정하면 된다.
 *
 * Resolves method arguments annotated with {@code @SecuredRequestBody} and
 * handles return values from methods annotated with
 * {@code @SecuredResponseBody} by reading and writing to the body of the
 * request or response with an {@link HttpMessageConverter}.
 *
 * <p>
 * An {@code @SecuredRequestBody} method argument is also validated if it is
 * annotated with {@code @javax.validation.Valid}. In case of validation
 * failure, {@link MethodArgumentNotValidException} is raised and results in a
 * 400 response status code if {@link DefaultHandlerExceptionResolver} is
 * configured.
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 * @author Arjen Poutsma
 * @author Rossen Stoyanchev
 * @since 3.1
 */
public abstract class AbleSecuredRequestResponseBodyMethodProcessor extends AbstractMessageConverterMethodProcessor implements InitializingBean {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	protected String secretKey = "";
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	@Override
	public void afterPropertiesSet() {
		if(StringUtils.isEmpty(secretKey)) {
			logger.info("!!!!!!!!! Secret Key is empty !!!!!!!!! => check set secretKey property.");
		}
	}

	public AbleSecuredRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> messageConverters) {
		super(messageConverters);
	}

	public AbleSecuredRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> messageConverters, ContentNegotiationManager contentNegotiationManager) {
		super(messageConverters, contentNegotiationManager);
	}

	public AbleSecuredRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> messageConverters, ContentNegotiationManager contentNegotiationManager, List<Object> responseBodyAdvice) {
		super(messageConverters, contentNegotiationManager, responseBodyAdvice);
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// deepfree - SecuredRequestBody로 변경
		return parameter.hasParameterAnnotation(SecuredRequestBody.class);
	}

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		// deepfree - SecuredResponseBody로 변경
		return (AnnotationUtils.findAnnotation(returnType.getContainingClass(), SecuredResponseBody.class) != null || returnType.getMethodAnnotation(SecuredResponseBody.class) != null);
	}

	/**
	 * Throws MethodArgumentNotValidException if validation fails.
	 * if {@link RequestBody#required()}
	 * is {@code true} and there is no body content or if there is no suitable
	 * converter to read the content with.
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		Object arg = readWithMessageConverters(webRequest, parameter, parameter.getGenericParameterType());
		String name = Conventions.getVariableNameForParameter(parameter);
		WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
		if (arg != null) {
			validateIfApplicable(binder, parameter);
			if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
				throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
			}
		}
		mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
		return arg;
	}

	@Override
	protected <T> Object readWithMessageConverters(NativeWebRequest webRequest, MethodParameter methodParam, Type paramType) throws IOException, HttpMediaTypeNotSupportedException {

		final HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpInputMessage inputMessage = new ServletServerHttpRequest(servletRequest);

		InputStream inputStream = inputMessage.getBody();
		if (inputStream == null) {
			return handleEmptyBody(methodParam);
		} else if (inputStream.markSupported()) {
			inputStream.mark(1);
			if (inputStream.read() == -1) {
				return handleEmptyBody(methodParam);
			}
			inputStream.reset();
		} else {
			final PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
			int b = pushbackInputStream.read();
			if (b == -1) {
				return handleEmptyBody(methodParam);
			} else {
				pushbackInputStream.unread(b);
			}

			//deepfree remark
			//inputMessage = new ServletServerHttpRequest(servletRequest) {
			//	@Override
			//	public InputStream getBody() throws IOException {
			//		// Form POST should not get here
			//		return pushbackInputStream;
			//	}
			//};

			//deepfree add start
			AES256Crypto crypto = new AES256Crypto(secretKey);
			AbleTeeInputStreamWrapper teeInputStream = new AbleTeeInputStreamWrapper(pushbackInputStream); //원본 요청메시지를 Tee처리
			final InputStream cipherInputStream = crypto.getDecryptChiperInputStream(teeInputStream.getInputStream());
			inputMessage = new ServletServerHttpRequest(servletRequest) {
				@Override
				public InputStream getBody() throws IOException {
					// Form POST should not get here
					return cipherInputStream;
				}
			};
			AbleTeeHttpInputMessage teeDecryptedHttpInputMessage = new AbleTeeHttpInputMessage(inputMessage); //복호화된 요청메시지를 Tee처리

			try {
				Object result = super.readWithMessageConverters(teeDecryptedHttpInputMessage, methodParam, paramType);
				cipherInputStream.close();

				String requestBody = teeInputStream.getTeeInputString();
				String decryptedRequestBody = teeDecryptedHttpInputMessage.getTeeInputString(); //!!!!!!!!!!!!
				logger.debug("\r\n  >>>> READ {}"
						+ "\r\n  >>>>      => {}"
						+ "\r\n  >>>>      => {}",
						requestBody, decryptedRequestBody, result);
				return result;
			} catch (Exception ex) {
				String requestBody = teeInputStream.getTeeInputString();
				logger.debug("\r\n  >>>> READ {}"
						+ "\r\n  >>>>      => FAILED TO READ CRYPTO REQUEST BODY!! exception: {}", requestBody, ex.getMessage(), ex);
				throw new HttpMessageNotReadableException("FAILED TO READ CRYPTO REQUEST BODY!! requestBody: " + requestBody, ex);
			}
			//deepfree add end
		}

		return super.readWithMessageConverters(inputMessage, methodParam, paramType);
	}

	private Object handleEmptyBody(MethodParameter param) {
		//deepfree - SecuredRequestBody로 변경
		if (param.getParameterAnnotation(SecuredRequestBody.class).required()) {
			throw new HttpMessageNotReadableException("Required request body content is missing: " + param);
		}
		return null;
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
			throws IOException, HttpMediaTypeNotAcceptableException {

		mavContainer.setRequestHandled(true);

		// Try even with null return value. ResponseBodyAdvice could get involved.
		writeWithMessageConverters(returnValue, returnType, webRequest);
	}

	@Override
	protected ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest) {
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		return new ServletServerHttpResponse(response);

//		//deepfree add start
//		AbleTeeHttpServletResponse teeResponse = new AbleTeeHttpServletResponse(response); //원본 응답을 가로채기위한 Tee처리
//		return new ServletServerHttpResponse(teeResponse);
//		//deepfree add end
	}

	@Override
	protected <T> void writeWithMessageConverters(T returnValue, MethodParameter returnType, NativeWebRequest webRequest)
			throws IOException, HttpMediaTypeNotAcceptableException {

		ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
		ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
		//writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);

		//deepfree add start
		AbleTeeHttpOutputMessage teeHttpOutputMessage = new AbleTeeHttpOutputMessage(outputMessage); //최종 응답메시지를 Tee처리
		AES256Crypto crypto = new AES256Crypto(secretKey);
		OutputStream cipherOutputStream = crypto.getEncryptChiperOutputStream(teeHttpOutputMessage.getBody());
		final AbleTeeOutputStreamWrapper teeOutputStream = new AbleTeeOutputStreamWrapper(cipherOutputStream); //Plain 응답메시지를 Tee처리
		HttpServletResponse response = outputMessage.getServletResponse();
		ServletServerHttpResponse outputMessageWrap = new ServletServerHttpResponse(response) {
			@Override
			public OutputStream getBody() throws IOException {
				super.getBody(); //writeHeaders() 처리위임
				return teeOutputStream.getOutputStream();
			}
		};

		try {
			writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessageWrap);
			cipherOutputStream.close();
			String responseBodyPlain = teeOutputStream.getTeeOutputString();
			String responseBody = teeHttpOutputMessage.getTeeOutputString();
			logger.debug("\r\n  >>>> WRITE {}"
					+ "\r\n  >>>>      => {}",
					responseBodyPlain, responseBody);
		} catch (Exception ex) {
			String responseBody = teeHttpOutputMessage.getTeeOutputString();
			logger.debug("\r\n  >>>> WRITE {}"
					+ "\r\n  >>>>      => FAILED TO WRITE CRYPTO RESPONSE BODY!! exception: {}", responseBody, ex.getMessage(), ex);
			throw new HttpMessageNotWritableException("FAILED TO WRITE CRYPTO RESPONSE BODY!!", ex);
		}
		//deepfree add end
	}

}