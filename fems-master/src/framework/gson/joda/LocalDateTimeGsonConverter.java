package framework.gson.joda;

import java.lang.reflect.Type;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.StringUtils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Gson Joda LocalDateTime Serializer, Deserializer
 *
 *  [사용법]
 *
 */
public class LocalDateTimeGsonConverter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

	/** Formatter specifier */
	private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

	@Override
	public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
		final DateTimeFormatter formatter = DateTimeFormat.forPattern(PATTERN);
		return new JsonPrimitive(formatter.print(src));
	}

	@Override
	public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		String jsonString = json.getAsString();
		if(StringUtils.isEmpty(jsonString)) {
			return null;
		}
		final DateTimeFormatter formatter = DateTimeFormat.forPattern(PATTERN);
		return formatter.parseLocalDateTime(jsonString);
	}


}
