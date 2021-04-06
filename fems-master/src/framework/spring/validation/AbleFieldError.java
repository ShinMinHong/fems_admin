package framework.spring.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * <code>org.springframework.validation.FieldError</code>의 응답용 Wrapper DT
 *
 * 내부에서 messageSource를 이용해서 에러메시지 Resolve
 * 이때 FieldName로 Resolve
 *
 * @author ByeongDon
 */
@JsonPropertyOrder({"objectName", "field", "rejectedValue", "code", "message"})
public class AbleFieldError extends AbleObjectError {

	/** 에러메시지 찾을때 접두어 */
	private static final String MESSAGE_PREFIX_FOR_ERROR = "server.validate.";
	private static final String DOMAIN_PREFIX_FOR_ERROR = "domain.";

	private static final String ABLE_VALIDATION_CONSTRAINT_PREFIX = "{framework.validation.constraint.";

	protected String field;
	protected String fieldName;
	protected Object rejectedValue;

	public String getField() { return field; }
	public void setField(String field) { this.field = field; }
	public String getFieldName() { return fieldName; }
	public void setFieldName(String fieldName) { this.fieldName = fieldName; }
	public Object getRejectedValue() {
		//MultipartFile은 JSON으로 RejectedValue를 내려줄수 없어서 파일명을 전송
		if(rejectedValue instanceof MultipartFile) {
			return ((MultipartFile)rejectedValue).getOriginalFilename();
		}
		return rejectedValue;
	}
	public void setRejectedValue(Object rejectedValue) {
		this.rejectedValue = rejectedValue;
	}

	public AbleFieldError(FieldError error) {
		super(error);
		this.field = error.getField();
		this.rejectedValue = error.getRejectedValue();
	}
	public AbleFieldError(FieldError error, MessageSource messageSource) {
		super(error);
		this.field = error.getField();
		this.rejectedValue = error.getRejectedValue();
		resolveFieldName(messageSource);
		resolveMessage(error, messageSource, null);
	}
	public AbleFieldError(FieldError error, MessageSource messageSource, String customFieldName) {
		super(error);
		this.field = error.getField();
		this.fieldName = customFieldName;
		this.rejectedValue = error.getRejectedValue();
		resolveFieldName(messageSource);
		resolveMessage(error, messageSource, customFieldName);
	}

	private void resolveFieldName(MessageSource messageSource) {
		try {
			if(StringUtils.isEmpty(this.fieldName)) {
				return;
			}
			Locale locale = LocaleContextHolder.getLocale();
			List<String> msgCode = new ArrayList<String>();
			msgCode.add((DOMAIN_PREFIX_FOR_ERROR + this.objectName + "." + this.field).toLowerCase());
			msgCode.add((DOMAIN_PREFIX_FOR_ERROR + this.field).toLowerCase());
			DefaultMessageSourceResolvable resolvable = new DefaultMessageSourceResolvable(msgCode.toArray(new String[msgCode.size()]), this.field);
			this.fieldName = messageSource.getMessage(resolvable, locale);
		} catch (NoSuchMessageException e) {
			logger.trace("{} - {}", e.getClass().getName(), e.getMessage(), e);
			this.fieldName = this.field;
		}
	}

	private void resolveMessage(FieldError fieldError, MessageSource messageSource, String customFieldName) {
		try {
			logger.debug("############# fieldError.getDefaultMessage : {}", fieldError.getDefaultMessage() );
			Locale locale = LocaleContextHolder.getLocale();
			if( null != fieldError.getDefaultMessage() && !fieldError.getDefaultMessage().isEmpty()) {
				if (StringUtils.startsWithIgnoreCase(StringUtils.trimAllWhitespace(fieldError.getDefaultMessage()), ABLE_VALIDATION_CONSTRAINT_PREFIX)){
					DefaultMessageSourceResolvable resolvable = getPrefixAddedMessageResolvable(fieldError, true/*toLowercase*/, customFieldName);
					logger.debug("resolvable ====> {}", resolvable);
					this.message = messageSource.getMessage(resolvable, locale);
				} else {
					DefaultMessageSourceResolvable resolvable = getMessageResolvable(fieldError, true, customFieldName);
					this.message = messageSource.getMessage(resolvable, locale);
				}
			} else {
				DefaultMessageSourceResolvable resolvable = getPrefixAddedMessageResolvable(fieldError, true/*toLowercase*/, customFieldName);
				logger.debug("resolvable ====> {}", resolvable);
				this.message = messageSource.getMessage(resolvable, locale);
			}
		} catch (NoSuchMessageException e) {
			logger.trace("{} - {}", e.getClass().getName(), e.getMessage(), e);
			this.message = fieldError.getDefaultMessage();
		}
	}

	/**
	 * Annotation Validation 에러메시지코드와 내부의 Argument의 필드명 변환
	 */
	private DefaultMessageSourceResolvable getMessageResolvable(DefaultMessageSourceResolvable fieldError, boolean toLowercase, String customFieldName) {
		String[] codes = addPrefixToStringArray(fieldError.getCodes(), "", toLowercase);
		Object[] arguments = addPrefixToMessageSourceResolvableArguments(fieldError.getArguments(), DOMAIN_PREFIX_FOR_ERROR, toLowercase, customFieldName);
		String defaultMessage = fieldError.getDefaultMessage();
		return new DefaultMessageSourceResolvable(codes, arguments, defaultMessage);
	}

	/**
	 * FieldError(DefaultMessageSourceResolvable)의 에러메시지코드와 내부의 Argument의 필드명 변환을 위한 메시지코드에 접두어 처리
	 *   에러메시지 접두어: MESSAGE_PREFIX_FOR_ERROR "server.validate."
	 *   필드명메시지 접두어: DOMAIN_PREFIX_FOR_ERROR "domain."
	 */
	private DefaultMessageSourceResolvable getPrefixAddedMessageResolvable(DefaultMessageSourceResolvable fieldError, boolean toLowercase, String customFieldName) {
		String[] codes = addPrefixToStringArray(fieldError.getCodes(), MESSAGE_PREFIX_FOR_ERROR, toLowercase);
		Object[] arguments = addPrefixToMessageSourceResolvableArguments(fieldError.getArguments(), DOMAIN_PREFIX_FOR_ERROR, toLowercase, customFieldName);
		String defaultMessage = fieldError.getDefaultMessage();
		return new DefaultMessageSourceResolvable(codes, arguments, defaultMessage);
	}

	/**
	 * 객체 배열에서 MessageSourceResolvable객체이면 메시지 코드 문자열 목록에 접두어 처리와 소문자 처리
	 * 주어진 배열의 타입이 <code>DefaultMessageSourceResolvable</code>이면 안의 메시지코드에 접두어 처리
	 */
	private Object[] addPrefixToMessageSourceResolvableArguments(Object[] arguments, String prefix, boolean toLowercase, String customFieldName) {
		String prefixToApply = prefix;
		if(prefix == null) {
			prefixToApply = "";
		}
		if(!toLowercase && StringUtils.isEmpty(prefixToApply)) {
			return arguments;
		}
		ArrayList<Object> result = new ArrayList<Object>();
		for (Object argument : arguments) {
			if(argument instanceof DefaultMessageSourceResolvable) {
				DefaultMessageSourceResolvable messageResolvable = (DefaultMessageSourceResolvable)argument;
				if(!StringUtils.isEmpty(customFieldName) && messageResolvable.getDefaultMessage().equals(this.field)) {
					result.add(customFieldName);
					continue;
				}
				String[] innerCodes = addPrefixToStringArray(messageResolvable.getCodes(), prefixToApply, toLowercase);
				Object[] innerArguments = messageResolvable.getArguments();
				String innerDefaultMessage = messageResolvable.getDefaultMessage();
				result.add(new DefaultMessageSourceResolvable(innerCodes, innerArguments, innerDefaultMessage));
			} else {
				result.add(argument);
			}
		}
		return result.toArray();
	}

	/** 문자열 목록에 접두어 처리와 소문자 처리 */
	private String[] addPrefixToStringArray(String[] codes, String prefix, boolean toLowercase) {
		String prefixToApply = prefix;
		if(prefix == null) {
			prefixToApply = "";
		}
		if(!toLowercase && StringUtils.isEmpty(prefixToApply)) {
			return codes;
		}
		ArrayList<String> result = new ArrayList<String>();
		for(String code: codes) {
			if(toLowercase) {
				code = code.toLowerCase();
			}
			result.add(prefixToApply + code);
		}
		return result.toArray(new String[result.size()]);
	}

	@Override
	public String toString() {
		return "AbleFieldError [objectName=" + objectName + ", field=" + field + ", rejectedValue=" + rejectedValue + ", code=" + code + ", message=" + message
				+ "]";
	}
}
