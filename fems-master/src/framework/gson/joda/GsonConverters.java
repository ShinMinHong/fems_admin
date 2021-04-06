package framework.gson.joda;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Ablecoms Gson Converter Registration Util
 *
 * gson-jodatime-serializers 오픈소스 참고
 *
 */
public class GsonConverters {

	private GsonConverters() { /**/ }

	/**
	 * 전체 Gson Converter 등록
	 */
	public static GsonBuilder registerAllConverters(GsonBuilder builder) {
		if(builder == null) {
			throw new NullPointerException("builder cannot be null.");
		}

		registerJodaLocalDateTimeConverter(builder);

		return builder;
	}

	/**
	 * Joda LocalDateTime Gson Converter 등록
	 */
	public static GsonBuilder registerJodaLocalDateTimeConverter(GsonBuilder builder) {
		if(builder == null) {
			throw new NullPointerException("builder cannot be null.");
		}
		builder.registerTypeAdapter(new TypeToken<LocalDateTime>(){}.getType(), new LocalDateTimeGsonConverter());
		builder.registerTypeAdapter(new TypeToken<LocalDate>(){}.getType(), new LocalDateGsonConverter());
		builder.registerTypeAdapter(new TypeToken<DateTime>(){}.getType(), new DateTimeGsonConverter());
		return builder;
	}

}
