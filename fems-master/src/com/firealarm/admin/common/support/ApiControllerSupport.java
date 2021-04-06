package com.firealarm.admin.common.support;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.dao.NonTransientDataAccessResourceException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.firealarm.admin.security.exception.MngAreaAccessDeniedException;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.exception.AbleRuntimeException;
import framework.exception.AbleValidationException;
import framework.spring.validation.AbleFieldError;
import framework.spring.web.rest.AbleResponseEntity;
import framework.spring.web.rest.AbleResponseEntity.REST_CALLBACK_CMD;
import framework.spring.web.rest.AbleResponseEntityBuilder;
import framework.util.AbleUtil;
import framework.util.AbleVOUtil;

/**
 * Controller Support
 *
 * 컨트롤러의 기본 로직을 포함
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class ApiControllerSupport extends ControllerSupport {

	/**
	 * {@link AbleValidationException} handler
	 */
	@ExceptionHandler({ AbleValidationException.class })
	@ResponseBody
    protected AbleResponseEntity<Object> handleAbleValidationException(AbleValidationException e, WebRequest request, Locale locale) {
		try {
			logger.debug("ERROR OCCURED => handleAbleValidationException: {}", e.toString());
			AbleResponseEntityBuilder responseBuilder = AbleResponseEntityBuilder.error().msg(e.getMessage());

	        Errors errors = e.getErrors();
	        if(errors != null) {
	        	Map<String, String> displayNameMap = new HashMap<>();
	        	if(errors instanceof BindingResult) {
	        		BindingResult bindingResult = (BindingResult)errors;
	        		Object targetObject = bindingResult.getTarget();
	        		displayNameMap = AbleVOUtil.getDisplayNameMap(targetObject.getClass());
	        	}
	        	List<FieldError> fieldErrors = errors.getFieldErrors();
		        List<AbleFieldError> ableFieldErrors = new ArrayList<AbleFieldError>();
		        for (FieldError fieldError : fieldErrors) {
		        	String customFieldName = displayNameMap.get(fieldError.getField());
					AbleFieldError ableFieldError = new AbleFieldError(fieldError, messageSource, customFieldName);
		        	ableFieldErrors.add(ableFieldError);
		        }
		        responseBuilder.fieldError(ableFieldErrors);
	        }

			return responseBuilder.build();
		} catch (Exception innerEx) {
			logger.error("handleAbleValidationException FAILED. innerEx:{}, originalException: {}", innerEx, e, e);
			return handleThrowable(innerEx, request, locale);
		}
    }

	/**
	 * {@link AuthenticationException} handler
	 */
	@ExceptionHandler({ AuthenticationException.class })
	@ResponseBody
    protected AbleResponseEntity<Object> handleAuthenticationException(RuntimeException e, WebRequest request, Locale locale) {
		try {
			logger.info("ERROR OCCURED => handleAuthenticationException: {}", e.toString(), e);
			AbleResponseEntityBuilder responseBuilder = AbleResponseEntityBuilder.error().msg("로그인이 필요합니다.")
					.callback(REST_CALLBACK_CMD.LOGINREQUIRED, null);
			return responseBuilder.build();
		} catch (Exception innerEx) {
			logger.error("handleAuthenticationException FAILED. innerEx:{}, originalException: {}", innerEx, e, e);
			return handleThrowable(innerEx, request, locale);
		}
    }

	/**
	 * {@link AccessDeniedException} handler
	 */
	@ExceptionHandler({ AccessDeniedException.class, MngAreaAccessDeniedException.class })
	@ResponseBody
    protected AbleResponseEntity<Object> handleAccessDeniedException(RuntimeException e, WebRequest request, Locale locale) {
		try {
			logger.info("ERROR OCCURED => handleAccessDeniedException: {}", e.toString(), e);
			AbleResponseEntityBuilder responseBuilder = AbleResponseEntityBuilder.error().msg("접근이 거부되었습니다.");
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Object principal = authentication.getPrincipal();
			if(principal == null || !(principal instanceof AppUserDetails)) {
				//미로그인 상태이면 로그인도 유도
				responseBuilder.callback(REST_CALLBACK_CMD.LOGINREQUIRED, null);
			}
			return responseBuilder.build();
		} catch (Exception innerEx) {
			logger.error("handleAccessDeniedException FAILED. innerEx:{}, originalException: {}", innerEx, e, e);
			return handleThrowable(innerEx, request, locale);
		}
    }

	/**
	 * {@link DataAccessException} handler
	 */
	@ExceptionHandler({ DataAccessException.class })
	@ResponseBody
    protected AbleResponseEntity<Object> handleDataAccessException(DataAccessException e, WebRequest request, Locale locale) {
		try {
			logger.error("ERROR OCCURED => handleDataAccessException: " + AbleUtil.getMessageOfException(e, true, true));
			AbleResponseEntityBuilder responseBuilder = AbleResponseEntityBuilder.error()
					.msg("요청하신 내용의 데이터 처리에 실패하였습니다. 관리자에게 문의해주세요.")
					.verboseMsg(translateDataAccessExceptionMessage(e, locale));
			return responseBuilder.build();
		} catch (Exception innerEx) {
			logger.error("handleDataAccessException FAILED. innerEx:{}, originalException: {}", innerEx, e, e);
			return handleThrowable(innerEx, request, locale);
		}
    }
	protected String translateDataAccessExceptionMessage(DataAccessException e, Locale locale) {
		String message = "데이터 처리에 예외가 발생하였습니다.";
		if (e instanceof NonTransientDataAccessException) {
			// non-transient - where a retry of the same operation would fail
			if (e instanceof DataIntegrityViolationException) {
				// 유일키 제약(unique constraint) 위반과 같은 정합성 위반이 삽입(insert)나 갱신(update)의 결과로 발생한 경우
				message = "데이터 처리도중 정합성 위반 예외가 발생하였습니다.";
				if (e instanceof DuplicateKeyException) {
					// 중복키 발생
					message = "데이터 처리도중 중복키 예외가 발생하였습니다.";
				}
				SQLException sqlEx = (SQLException)AbleUtil.findInnerException(e, SQLException.class);
				if(sqlEx != null) {
					int sqlErrorCode = sqlEx.getErrorCode();
					if(sqlErrorCode == 2292) {
						//ORA-02292: 무결성 제약조건이 위배되었습니다- 자식 레코드가 발견되었습니다
						message = "데이터 처리도중 무결성 제약 예외가 발생하였습니다. 자식 레코드가 발견되었습니다.";
					}
				}
			} else if (e instanceof NonTransientDataAccessResourceException) {
				// Data access exception thrown when a resource fails completely and the failure is permanent.
				if (e instanceof DataAccessResourceFailureException) {
					// 데이터베이스로의 연결 실패 등 완전하게 자원 접근에 실패했을 경우
					message = "데이터의 연결 또는 자원 접근에 실패하였습니다.";
				}
			} else if (e instanceof PermissionDeniedDataAccessException) {
				// Exception thrown when the underlying resource denied a permission to access a specific element, such as a specific database table.
				message = "데이터 자원에 접근이 거부되었습니다.";
			}
		} else if (e instanceof TransientDataAccessException) {
			// transient - where a previously failed operation might be able to succeed
			// when the operation is retried without any intervention
			if (e instanceof ConcurrencyFailureException) {
				// messageCode = "server.msg.restprocessfail.concurrencyfailureexception";
			} else if (e instanceof QueryTimeoutException) {
				message = "데이터 실행 시간이 초과되었습니다.";
			}
		}
		return message;
	}

	/**
	 * {@link AbleRuntimeException} Hander
	 */
	@ExceptionHandler({ AbleRuntimeException.class })
	@ResponseBody
	public AbleResponseEntity<Object> handleAbleRuntimeException(AbleRuntimeException e, WebRequest request, Locale locale) {
		try {
			logger.error("ERROR OCCURED => handleAbleRuntimeException: " + e.getMessage());
			if(e.getCause() != null) {
				logger.debug("		cause: " + e.getCause().toString());
			}
			AbleResponseEntityBuilder responseBuilder = AbleResponseEntityBuilder.error().msg(e.getMessage());
	        return responseBuilder.build();
		} catch (Exception innerException) {
			logger.error("handleRESTInvalidRequestException Failed.", e);
			return handleThrowable(innerException, request, locale);
		}
    }

	/**
	 * {@link Throwable} Hander
	 */
	@ExceptionHandler({ Throwable.class })
	@ResponseBody
	public AbleResponseEntity<Object> handleThrowable(Throwable e, WebRequest request, Locale locale) {
		logger.error("ERROR OCCURED => handleThrowable: " + e.toString(), e);
		AbleResponseEntityBuilder responseBuilder = AbleResponseEntity.error().msg("API 요청 처리에 실패하였습니다.");
		if(!AbleUtil.isProductionProfile()) {
			//Debelopment Profile 일때만
			return responseBuilder.verboseMsg(e.getClass().getSimpleName() + " - " + e.getMessage()).build(); //내부 error
		}
		return responseBuilder.build();
    }


}
