package framework.spring.core.convert;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import framework.exception.AbleConvertException;
import framework.util.AbleDateUtil;

/**
 * 문자열과 Joda DateTime형의 변환
 *
 * 여러 DateTime형의 문자열을 DateTime으로 변환
 *
 * @author Min ByeongDon <deepfree@gmail.com> *
 */
public class AbleStringToJodaDateTimeConverter implements Converter<String, DateTime> {

	private final Logger logger = LoggerFactory.getLogger(AbleStringToJodaDateTimeConverter.class);

	//DateTimeFormatter formatter = ISODateTimeFormat.dateTime(); //"yyyy-MM-dd'T'HH:mm:ss.SSSZ"

	/* (non-Javadoc)
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	public DateTime convert(String dateString) {
		try {
			logger.trace("convert({})", dateString);
			return AbleDateUtil.parseToDateTime(dateString);
		} catch (Exception e) {
			logger.trace("convert({}) - Exception: {}", dateString, e.toString());
			throw new AbleConvertException("Cannot convert dateString to Joda DateTime.", e) ;
		}
	}
}

