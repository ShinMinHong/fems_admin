package framework.unirest;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mashape.unirest.http.ObjectMapper;

import framework.exception.AbleRuntimeException;
import framework.fasterxml.jackson.AbleObjectMapper;

/**
 * Unirest용 ObjectMapper 설정. 내부에서 Jackson의 ObjectMapper에 위임
 *
 * @author ByeongDon
 *
 */
public class UnirestObjectMapper implements ObjectMapper {

	private com.fasterxml.jackson.databind.ObjectMapper jsonMapper = new AbleObjectMapper();

	public com.fasterxml.jackson.databind.ObjectMapper getJsonMapper() {
		return jsonMapper;
	}

	public UnirestObjectMapper() {
		//SerializationFeature
		jsonMapper.setSerializationInclusion(Include.NON_NULL);
		jsonMapper.disable(SerializationFeature.INDENT_OUTPUT);
		jsonMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		jsonMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
		//DeserializationFeature
		jsonMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	@Override
	public <T> T readValue(String value, Class<T> valueType) {
		try {
			return jsonMapper.readValue(value, valueType);
		} catch (IOException e) {
			throw new AbleRuntimeException(e);
		}
	}

	public <T> T readValue(String value, TypeReference<?> valueTypeRef) {
		try {
			return jsonMapper.readValue(value, valueTypeRef);
		} catch (IOException e) {
			throw new AbleRuntimeException(e);
		}
	}

	@Override
	public String writeValue(Object value) {
		try {
			return jsonMapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new AbleRuntimeException(e);
		}
	}
}