package framework.spring.web.rest;


import java.util.List;

import framework.spring.validation.AbleFieldError;
import framework.spring.web.rest.AbleResponseEntity.REST_CALLBACK_CMD;
import framework.spring.web.rest.AbleResponseEntity.REST_RESPONSE_STATUS;

/**
 * Ablecoms Framework REST 공통 메시지 Builder
 *
 * @author ByeongDon
 */
public class AbleResponseEntityBuilder {

	protected REST_RESPONSE_STATUS status = REST_RESPONSE_STATUS.SUCCESS;
	protected String msg = null;
	protected String verboseMsg = null;
	protected REST_CALLBACK_CMD cbCmd = null;
	protected Object cbParam = null;
	protected Object body = null;
	protected List<AbleFieldError> fieldError = null;

	public AbleResponseEntityBuilder() {
		super();
	}

	public AbleResponseEntityBuilder(REST_RESPONSE_STATUS status) {
		super();
		this.status = status;
	}

	public AbleResponseEntityBuilder status(REST_RESPONSE_STATUS status) {
		this.status = status;
		return this;
	}

	public AbleResponseEntityBuilder msg(String msg) {
		this.msg = msg;
		return this;
	}

	public AbleResponseEntityBuilder verboseMsg(String verboseMsg) {
		this.verboseMsg = verboseMsg;
		return this;
	}

	public AbleResponseEntityBuilder callback(REST_CALLBACK_CMD cbCmd, String cbParam) {
		this.cbCmd = cbCmd;
		this.cbParam = cbParam;
		return this;
	}

	public AbleResponseEntityBuilder fieldError(List<AbleFieldError> fieldError) {
		this.fieldError = fieldError;
		return this;
	}

	public AbleResponseEntity<Object> build() {
		AbleResponseEntity<Object> entity = new AbleResponseEntity<Object>(this.status, this.msg, this.cbCmd, this.cbParam, this.body, this.fieldError);
		entity.setVerboseMsg(verboseMsg);
		return entity;
	}

	public <T> AbleResponseEntity<T> body(T body) {
		this.body = body;
		AbleResponseEntity<T> entity = new AbleResponseEntity<T>(this.status, this.msg, this.cbCmd, this.cbParam, body, this.fieldError);
		entity.setVerboseMsg(verboseMsg);
		return entity;
	}

	/** error로 응답하는 AbleResponseEntity 빌더 획득 */
	public static AbleResponseEntityBuilder error() {
		return new AbleResponseEntityBuilder(REST_RESPONSE_STATUS.ERROR);
	}

	/** fail로 응답하는 AbleResponseEntity 빌더 획득 */
	public static AbleResponseEntityBuilder fail() {
		return new AbleResponseEntityBuilder(REST_RESPONSE_STATUS.FAIL);
	}

	/** success로 응답하는 AbleResponseEntity 빌더 획득 */
	public static AbleResponseEntityBuilder success() {
		return new AbleResponseEntityBuilder(REST_RESPONSE_STATUS.SUCCESS);
	}

	/** 주어진 객체를 success로 응답하는 AbleResponseEntity 획득 */
	public static <T> AbleResponseEntity<T> success(T body) {
		return success().body(body);
	}
}
