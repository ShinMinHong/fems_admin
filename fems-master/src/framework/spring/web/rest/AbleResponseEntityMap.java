package framework.spring.web.rest;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import framework.spring.validation.AbleFieldError;

/**
 * Ablecoms Framework REST 공통 메시지 (body를 Map<String, Object>으로 동적처리)
 *
 * @author ByeongDon
 */
public class AbleResponseEntityMap extends AbleResponseEntity<Map<String, Object>> {

	/**	성공 응답 생성 */
	public AbleResponseEntityMap() {
		super();
		this.body = new HashMap<String, Object>();
	}

	/** 생성자 **/
	public AbleResponseEntityMap(
			REST_RESPONSE_STATUS status, String msg,
			REST_CALLBACK_CMD cbCmd, Object cbParam,
			List<AbleFieldError> fieldError) {
		super(status, msg, cbCmd, cbParam, fieldError);
		this.body = new HashMap<String, Object>();
	}

	/** 생성자 **/
	public AbleResponseEntityMap(
			REST_RESPONSE_STATUS status, String msg,
			REST_CALLBACK_CMD cbCmd, Object cbParam) {
		super(status, msg, cbCmd, cbParam);
		this.body = new HashMap<String, Object>();
	}

	/**
	 * body map에 값을 추가. put과 같지만 함수형 지원
	 */
	public AbleResponseEntityMap add(String key, Object value) {
		this.body.put(key, value);
		return this;
	}

	//Map Interface Delegate
	public int size() { return this.body.size(); }
	@JsonIgnore public boolean isEmpty() { return this.body.isEmpty(); }
	public boolean containsKey(Object key) { return this.body.containsKey(key); }
	public boolean containsValue(Object value) { return this.body.containsValue(value); }
	public Object get(Object key) { return this.body.get(key); }
	public Object put(String key, Object value) { return this.body.put(key, value); }
	public Object remove(Object key) { return this.body.remove(key); }
	public void putAll(Map<? extends String, ? extends Object> m) { this.body.putAll(m); }
	public void clear() { this.body.clear(); }
	public Set<String> keySet() { return this.body.keySet(); }
	public Collection<Object> values() { return this.body.values(); }
	public Set<java.util.Map.Entry<String, Object>> entrySet() { return this.body.entrySet(); }


}
