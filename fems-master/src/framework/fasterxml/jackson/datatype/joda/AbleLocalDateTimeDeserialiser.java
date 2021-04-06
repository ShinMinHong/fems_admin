package framework.fasterxml.jackson.datatype.joda;

import java.io.IOException;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;

import framework.util.AbleDateUtil;

/**
 * Ablecoms Joda LocalDateTime Deserializer
 *
 * http://wiki.fasterxml.com/JacksonFAQDateHandling
 * http://www.baeldung.com/jackson-serialize-dates
 * https://raymondhlee.wordpress.com/2015/01/24/custom-json-serializer-and-deserializer-for-joda-datetime-objects/
 * https://dzone.com/articles/how-serialize-javautildate
 * http://stackoverflow.com/questions/3269459/how-to-serialize-joda-datetime-with-jackson-json-processer
 *
 * @author ByeongDon
 */
public class AbleLocalDateTimeDeserialiser extends LocalDateTimeDeserializer {
	private static final long serialVersionUID = -565820334558847688L;
	private static final Logger logger = LoggerFactory.getLogger(AbleLocalDateTimeDeserialiser.class);

	@Override
	public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		try {
			if(JsonTokenId.ID_STRING == p.getCurrentTokenId()) {
				String str = p.getText().trim();
				if (str.length() == 0) {
					return null;
				}
				LocalDateTime result = AbleDateUtil.parseToLocalDateTime(str, false);
				if (result != null) {
					return result;
				}
			}
		} catch (IOException e) {
			if (logger.isTraceEnabled()) {
				logger.trace("{} - {}", e.getClass().getName(), e.getMessage(), e);
			}
		}
		return super.deserialize(p, ctxt);
	}
}
