package framework.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.firealarm.admin.common.vo.SelectOption;

import framework.exception.AbleRuntimeException;
import framework.fasterxml.jackson.AbleObjectMapper;
import framework.spring.validation.AbleExcelUploadObjectError;
import framework.spring.validation.AbleFieldError;
import framework.spring.validation.AbleObjectError;

/**
 * Common Helper Class
 *
 * 주요 오픈소스의 Helper에서 제공되지 않는 일반적인 공통 함수 모음
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleUtil {
	protected static Logger logger = LoggerFactory.getLogger(AbleUtil.class);

	/**
	 * 스프링의 활성화 프로파일 획득 (spring.profiles.active, spring.profiles.default from env, properties)
	 */
	public static String getActiveProfile() {
		String profile = System.getenv("spring.profiles.active");
		if(StringUtils.isEmpty(profile)) {
			profile = System.getProperty("spring.profiles.active");
		}
		if(StringUtils.isEmpty(profile)) {
			profile = System.getenv("spring.profiles.default");
		}
		if(StringUtils.isEmpty(profile)) {
			profile = System.getProperty("spring.profiles.default");
		}

		if(StringUtils.isEmpty(profile)) {
			logger.warn("getActiveProfile() => return null. check spring profile config!!!!");
		}
		return profile;
	}

	/** spring profile이 production 또는 stage인지 여부 */
	public static boolean isProductionOrStageProfile() {
		String profile = getActiveProfile();
		return "production".equals(profile) || "stage".equals(profile);
	}

	/** spring profile이 production인지 여부 */
	public static boolean isProductionProfile() {
		String profile = getActiveProfile();
		return "production".equals(profile);
	}

	/** spring profile이 real인지 여부 */
	public static boolean isStageProfile() {
		String profile = getActiveProfile();
		return "stage".equals(profile);
	}

	/** spring profile이 real인지 여부 */
	public static boolean isLocalProfile() {
		String profile = getActiveProfile();
		return "local".equals(profile);
	}

	//MethodName///////////////////////////////////////////////////////////////////////
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
		//	String methodName = steList[i].getMethodName();
		//	logger.debug("{} - {}", i, methodName);
		//}
		return steList[startIndex + upperDepth].getMethodName();
	}
	private static final List<String> NOT_USER_METHODS = Arrays.asList("getStackTrace", "getCurrentMethodName", "getStackTraceMethodName");

	//JSON 처리////////////////////////////////////////////////////////////////////////
	/**
	 * 주어진 객체를 JSON문자열로 Serialize (Jackson2 ObjectMapper 사용)
	 * @param object 변환할 객체
	 * @return JSON 문자열
	 *
	 * @see http://wiki.fasterxml.com/JacksonJsonViews
	 * @see http://www.jroller.com/RickHigh/entry/working_with_jackson_json_views
	 */
	public static String toJson(Object object) {
		return toJson(object, null);
	}

	/**
	 * 주어진 객체를 JSON문자열로 Serialize (Jackson2 ObjectMapper 사용)
	 * @param object 변환할 객체
	 * @param indentOutput pretty print 여부
	 * @return JSON 문자열
	 *
	 * @see http://wiki.fasterxml.com/JacksonJsonViews
	 * @see http://www.jroller.com/RickHigh/entry/working_with_jackson_json_views
	 */
	public static String toJson(Object object, boolean indentOutput) {
		return toJson(object, null, indentOutput);
	}

	/**
	 * 주어진 객체를 JSON문자열로 Serialize (Jackson2 ObjectMapper 사용)
	 * @param object 변환할 객체
	 * @param serializationView Serialization에 사용할 View Class (<code>@JsonView</code> 참고)
	 * @return JSON 문자열
	 *
	 * @see http://wiki.fasterxml.com/JacksonJsonViews
	 * @see http://www.jroller.com/RickHigh/entry/working_with_jackson_json_views
	 */
	public static String toJson(Object object, Class<?> serializationView) {
		return toJson(object, serializationView, false);
	}

	/**
	 * 주어진 객체를 JSON문자열로 Serialize (Jackson2 ObjectMapper 사용)
	 * @param object 변환할 객체
	 * @param serializationView Serialization에 사용할 View Class (<code>@JsonView</code> 참고)
	 * @param indentOutput pretty print 여부
	 * @return JSON 문자열
	 *
	 * @see http://wiki.fasterxml.com/JacksonJsonViews
	 * @see http://www.jroller.com/RickHigh/entry/working_with_jackson_json_views
	 */
	public static String toJson(Object object, Class<?> serializationView, boolean indentOutput) {
		String json;
		ObjectMapper mapper = new AbleObjectMapper();
		try {
			if(indentOutput) {
				mapper.enable(SerializationFeature.INDENT_OUTPUT);
			} else {
				mapper.disable(SerializationFeature.INDENT_OUTPUT);
			}
			if(serializationView != null) {
				json = mapper.writerWithView(serializationView).writeValueAsString(object);
			} else {
				json = (object == null) ? null : mapper.writeValueAsString(object);
			}
		} catch (JsonProcessingException e) {
			throw new AbleRuntimeException("Failed to write as json", e);
		}
		return json.replace("/", "\\/"); //StringEscapeUtils.escapeJson
	}


	/**
	 * 주어진 Json문자열을 객체 변환
	 * @param json JSON문자열
	 * @param valueType 획득할 객체타입
	 * @return 변환된 객체
	 */
	public static <T> T fromJson(String json, Class<T> valueType) {
		ObjectMapper mapper = new AbleObjectMapper();
		try {
			T value = mapper.readValue(json, valueType);
			return value;
		} catch (Exception e) {
			throw new AbleRuntimeException("Failed to read from json", e);
		}
	}

	/**
	 * 예외 메시지를 획득 (옵션에 따라 Throwable의 Cause의 메시지도 포함)
	 * @param ex 메시지를 가져올 Throwable 예외
	 * @param includeExceptionClassSimpleName 예외 클래스의 SimpleName을 포함?
	 * @param includeCauseMessage Cuase의 메시지도 포함할 것인가?
	 * @return ExceptionSimpleName: 메시지 (- CauseExceptionSimpleName: 메시지 - ...)
	 */
	public static String getMessageOfException(Throwable ex, boolean includeExceptionClassSimpleName, boolean includeCauseMessage) {
		String message = ((includeExceptionClassSimpleName)? ex.getClass().getSimpleName() + ": " : "") + ex.getMessage();
		if(includeCauseMessage) {
			Throwable innerEx = ex.getCause();
			if(innerEx != null) {
				return message + " - " + getMessageOfException(innerEx, includeExceptionClassSimpleName, includeCauseMessage);
			}
		}
		return message;
	}

	/**
	 * 예외 또는 예외 내부 Cause를 뒤져서 원하는 Type의 예외를 찾는다.
	 * @param ex 예외
	 * @param requiredType 예외 또는 예외내부Cause에서 찾아볼 예외 타입
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Exception> T findInnerException(Throwable ex, Class<? extends Throwable> requiredType) {
		if(ex.getClass() == requiredType) {
			return (T) ex;
		}
		Throwable cause =  ex.getCause();
		if(cause == null) {
			return null;
		}
		return findInnerException(cause, requiredType);
	}

	/**
	 * 예외 또는 예외 내부 Cause를 뒤져서 원하는 Type이 있는지를 확인한다.
	 * @param ex 예외
	 * @param requiredType 예외 또는 예외내부Cause에서 찾아볼 예외 타입
	 * @return
	 */
	public static Boolean hasInnerException(Throwable ex, Class<? extends Throwable> requiredType) {
		return findInnerException(ex, requiredType) != null;
	}

	/**
	 * 에러 메시지 획득
	 * @param errors 에러정보 객체 (BindingResult등...)
	 * @param messageSource 메시지소스. 에러메시지 변환에 사용
	 * @param locale 로케일. 에러메시지 국제화시 현재 로케일 (null이면 LocaleContextHolder에서 취득)
	 * @param seperator 에러메시지간 구분자
	 * @return 에러메시지
	 */
	public static String buildErrorMessage(Errors errors, MessageSource messageSource, Locale locale, String seperator) {
		StringBuilder errorMessages = new StringBuilder();
		Locale localeToApply = locale;
		if(localeToApply == null) {
			localeToApply = LocaleContextHolder.getLocale();
		}

		List<AbleObjectError> ableObjectErrors = new ArrayList<AbleObjectError>();
		List<ObjectError> globalErrors = errors.getGlobalErrors();
		for (ObjectError objectError : globalErrors) {
			AbleObjectError ableObjectError = new AbleObjectError(objectError);
			ableObjectErrors.add(ableObjectError);
			errorMessages.append(ableObjectError.getMessage() + seperator);
		}

		List<AbleFieldError> ableFieldErrors = new ArrayList<AbleFieldError>();
        List<FieldError> fieldErrors = errors.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
        	AbleFieldError ableFieldError = new AbleFieldError(fieldError, messageSource);
        	ableFieldErrors.add(ableFieldError);
        	errorMessages.append(ableFieldError.getMessage() + seperator);
        }

        return errorMessages.toString();
	}

	/**
	 * 엑셀 annotation validation
	 * @param instance 데이터가 담긴 객체
	 * @param validator SmartValidator 구현체 - OptionalValidatorFactoryBean
	 * @param messageSource messageSource 메시지소스. 에러메시지 변환에 사용
	 * @param validationHints - validation hints, such as validation groups against a JSR-303 provider
	 * @return AbleExcelUploadObjectError 엑셀업로드 에러 객체
	 */
	public static AbleExcelUploadObjectError annotationValidationForExcel(Object instance, SmartValidator validator, MessageSource messageSource, Object... validationHints){
		AbleFieldError ableFieldError = getFieldErrorByValidator(instance, validator, messageSource, validationHints);
		if ( ableFieldError != null ){
			return new AbleExcelUploadObjectError(ableFieldError);
		}
		return null;
	}

	/**
	 * annotation validation
	 * @param instance 데이터가 담긴 객체
	 * @param validator SmartValidator 구현체 - OptionalValidatorFactoryBean
	 * @param messageSource messageSource 메시지소스. 에러메시지 변환에 사용
	 * @param validationHints - validation hints, such as validation groups against a JSR-303 provider
	 * @return 에러 메시지
	 */
	public static String annotationValidation(Object instance, SmartValidator validator, MessageSource messageSource, Object... validationHints){
		AbleFieldError ableFieldError = getFieldErrorByValidator(instance, validator, messageSource, validationHints);
		if ( ableFieldError != null ){
			return ableFieldError.getMessage();
		}
		return null;
	}

	/**
	 * annotation validation
	 * @param instance 데이터가 담긴 객체
	 * @param validator SmartValidator 구현체 - OptionalValidatorFactoryBean
	 * @param messageSource messageSource 메시지소스. 에러메시지 변환에 사용
	 * @param validationHints - validation hints, such as validation groups against a JSR-303 provider
	 * @return AbleFieldError 필드에러 객체
	 */
	protected static AbleFieldError getFieldErrorByValidator(Object instance, SmartValidator validator, MessageSource messageSource, Object... validationHints){
		DataBinder dataBinder = new DataBinder(instance);
		BindingResult bindingResultForRow = new BeanPropertyBindingResult(
				instance, instance.getClass().getName(),
				dataBinder.isAutoGrowNestedPaths(), dataBinder.getAutoGrowCollectionLimit());

		if(validationHints.length > 0) {
			//group validation - hibernate
			validator.validate(instance, bindingResultForRow, validationHints);
		} else {
			//validation - springframework
			validator.validate(instance, bindingResultForRow);
		}

		if ( bindingResultForRow.hasErrors() ){
        	FieldError fieldError = bindingResultForRow.getFieldError();
        	String field = fieldError.getField();
        	String customFieldName = field;
    		customFieldName = AbleVOUtil.getDisplayName(instance.getClass(), field);
	        return new AbleFieldError(fieldError, messageSource, customFieldName);
		}
		return null;
	}

    /**
     * 주어진 요청이 jquery.filedownload의 download나 jquery.iframe-transport의 upload할 경우의 iframe요청인지 여부
     *   js에서 iFrame 전송시 <input type='hidden' name='X-Requested-With' value='IFrame' /> 추가
     *   (jquery iframe transport plugin 기법. jquery.filedownload.mod.js에도 수정 구현함)
     * @param request 판단할 요청 객체
     * @return Ajax 요청 여부
     */
    public static boolean isIFrameTransportRequest(HttpServletRequest request) {
		String xRequestWith = getRequestHeaderWithFallback(request, "X-Requested-With");
		if (xRequestWith != null && xRequestWith.indexOf("iframe") > -1) {
			return true;
		}
        return false;
    }

	/**
	 * Request의 Header 또는 Parameter에서 X-Requested-With 값 획득
	 * @param request
	 * @param headerName TODO
	 * @return
	 */
    public static String getRequestHeaderWithFallback(HttpServletRequest request, String headerName) {
		logger.trace("{} ==> X-Requested-With at Header: {}", request.getClass().getSimpleName(), request.getHeader(headerName));
		logger.trace("{} ==> X-Requested-With at Parameter: {}", request.getClass().getSimpleName(), request.getParameter(headerName));
		logger.trace("{} ==> X-Requested-With at Attribute: {}", request.getClass().getSimpleName(), request.getAttribute(headerName));

		String xRequestWith = null;
        if(request.getHeader(headerName) != null) {
        	xRequestWith = request.getHeader(headerName).toLowerCase();
        } else if(request.getParameter(headerName) != null) {
        	xRequestWith = request.getParameter(headerName).toLowerCase();
        } else if(request.getAttribute(headerName) != null) {
        	xRequestWith = request.getAttribute(headerName).toString().toLowerCase();
        }
		return xRequestWith;
	}

	/** selectOptions Map Converter */
	public static Map<String, String> getCodepMapFromSelectOptions(List<SelectOption> selectOptions){
		Map<String, String> codeMap = new HashMap<String, String>();
		for ( SelectOption selectOption : selectOptions ){
			codeMap.put(selectOption.getValue(), selectOption.getText());
		}
		return codeMap;
	}
}
