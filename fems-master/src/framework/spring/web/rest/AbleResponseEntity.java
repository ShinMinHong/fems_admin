package framework.spring.web.rest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import framework.base.model.AbleView.CommonBaseView;
import framework.spring.validation.AbleFieldError;

/**
 * Ablecoms Framework REST 공통 메시지
 *
 * @author ByeongDon
 */
public class AbleResponseEntity<T> {

	/** 응답코드 */
	public enum REST_RESPONSE_STATUS {
		SUCCESS, ERROR, FAIL
	}

	/** 응답 콜백 커맨드 */
	public enum REST_CALLBACK_CMD {
		LOGINREQUIRED,
		REDIRECT,
		ALERT
	}

	@JsonView(CommonBaseView.class)
	protected String status = REST_RESPONSE_STATUS.SUCCESS.toString();
	@JsonView(CommonBaseView.class)
	protected String msg = null;
	@JsonView(CommonBaseView.class)
	protected String verboseMsg = null;
	@JsonView(CommonBaseView.class)
	protected REST_CALLBACK_CMD cbCmd = null;
	@JsonView(CommonBaseView.class)
	protected Object cbParam = null;
	@JsonView(CommonBaseView.class)
	protected T body;
	@JsonView(CommonBaseView.class)
	protected List<AbleFieldError> fieldError = null;

	public String getStatus() {
		return status;
	}
	public AbleResponseEntity<T> setStatus(REST_RESPONSE_STATUS status) {
		this.status = status.toString();
		return this;
	}
	public String getMsg() {
		return msg;
	}
	public AbleResponseEntity<T> setMsg(String msg) {
		this.msg = msg;
		return this;
	}
	public String getVerboseMsg() {
		return verboseMsg;
	}
	public AbleResponseEntity<T> setVerboseMsg(String verboseMsg) {
		this.verboseMsg = verboseMsg;
		return this;
	}
	public REST_CALLBACK_CMD getCbCmd() {
		return cbCmd;
	}
	public AbleResponseEntity<T> setCbCmd(REST_CALLBACK_CMD cbCmd) {
		this.cbCmd = cbCmd;
		return this;
	}
	public Object getCbParam() {
		return cbParam;
	}
	public AbleResponseEntity<T> setCbParam(Object cbParam) {
		this.cbParam = cbParam;
		return this;
	}
	public T getBody() {
		return body;
	}
	public AbleResponseEntity<T> setBody(T body) {
		this.body = body;
		return this;
	}
	public List<AbleFieldError> getFieldError() {
		return fieldError;
	}
	public AbleResponseEntity<T> setFieldError(List<AbleFieldError> fieldError) {
		this.fieldError = fieldError;
		return this;
	}


	public AbleResponseEntity<T> status(REST_RESPONSE_STATUS status) {
		this.status = status.toString();
		return this;
	}
	public AbleResponseEntity<T> msg(String msg) {
		this.msg = msg;
		return this;
	}
	public AbleResponseEntity<T> verboseMsg(String verboseMsg) {
		this.verboseMsg = verboseMsg;
		return this;
	}
	public AbleResponseEntity<T> callback(REST_CALLBACK_CMD cbCmd, String cbParam) {
		this.cbCmd = cbCmd;
		this.cbParam = cbParam;
		return this;
	}
	public AbleResponseEntity<T> body(T body) {
		this.body = body;
		return this;
	}


	/**	성공 응답 생성 */
	public AbleResponseEntity() {
		super();
	}

	/**	성공 응답 생성 with Body */
	public AbleResponseEntity(T body) {
		super();
		this.body = body;
	}

	/**	응답 생성 with Status, Msg, cbCmd, cbParam */
	public AbleResponseEntity(REST_RESPONSE_STATUS status, String msg, REST_CALLBACK_CMD cbCmd, Object cbParam) {
		super();
		this.status = status.toString();
		this.msg = msg;
		this.cbCmd = cbCmd;
		this.cbParam = cbParam;
	}

	/** 생성자 **/
	public AbleResponseEntity(REST_RESPONSE_STATUS status, String msg, REST_CALLBACK_CMD cbCmd, Object cbParam, List<AbleFieldError> fieldError) {
		super();
		this.status = status.toString();
		this.msg = msg;
		this.cbCmd = cbCmd;
		this.cbParam = cbParam;
		this.fieldError = fieldError;
	}

	/** 생성자 **/
	public AbleResponseEntity(REST_RESPONSE_STATUS status, String msg, REST_CALLBACK_CMD cbCmd, Object cbParam, T body, List<AbleFieldError> fieldError) {
		super();
		this.status = status.toString();
		this.msg = msg;
		this.cbCmd = cbCmd;
		this.cbParam = cbParam;
		this.body = body;
		this.fieldError = fieldError;
	}

	/** error로 응답하는 AbleResponseEntity 빌더 획득 */
	public static AbleResponseEntityBuilder error() {
		return AbleResponseEntityBuilder.error();
	}

	/** fail로 응답하는 AbleResponseEntity 빌더 획득 */
	public static AbleResponseEntityBuilder fail() {
		return AbleResponseEntityBuilder.fail();
	}

	/** success로 응답하는 AbleResponseEntity 빌더 획득 */
	public static AbleResponseEntityBuilder success() {
		return AbleResponseEntityBuilder.success();
	}

	/** 주어진 객체를 success로 응답하는 AbleResponseEntity 획득 */
	public static <T> AbleResponseEntity<T> success(T body) {
		return AbleResponseEntityBuilder.success(body);
	}
}
