package framework.fasterxml.jackson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.util.ClassUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import framework.fasterxml.jackson.datatype.joda.AbleLocalDateTimeDeserialiser;

/**
 * Jackson2의 ObjectMapper
 *
 * 스프링에서는 ObjectMapper를 Jackson2ObjectMapperBuilder등을 통해 설정해서 받을 수 있지만,
 * 그냥 Pojo에서 사용시에도 같은 설정을 받기 위해 ObjectMapper 내부에 설정을 적용
 *
 * @see Jackson2ObjectMapperBuilder - ObjectMapper Builder
 * @see Jackson2ObjectMapperFactoryBean - ObjectMapper Factory Bean
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleXmlMapper extends XmlMapper {

	private static final long serialVersionUID = -3261058393672166051L;

	protected static final Logger logger = LoggerFactory.getLogger(AbleXmlMapper.class);

	private ClassLoader moduleClassLoader = getClass().getClassLoader();

	private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";

	public AbleXmlMapper() {
		this(null, DEFAULT_DATETIME_FORMAT);
	}

	public AbleXmlMapper(ApplicationContext applicationContext) {
		this(applicationContext, DEFAULT_DATETIME_FORMAT);
	}

	public AbleXmlMapper(String simpleDateTimeFormat) {
		this(null, simpleDateTimeFormat);
	}

	public AbleXmlMapper(ApplicationContext applicationContext, String simpleDateTimeFormat) {
		//Jaxb Annotation을 사용할 수 있도록 모듈 추가
		JaxbAnnotationModule jaxbAnnotationModule = new JaxbAnnotationModule();
		registerModule(jaxbAnnotationModule);

		//Register Java8 DateTime, Joda DateTime Module
		registerWellKnownModulesIfAvailable(this);

		//Features 설정
		enable(SerializationFeature.INDENT_OUTPUT); //Pretty Print
		disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

		// @see org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.customizeDefaultFeatures(ObjectMapper)
		disable(MapperFeature.DEFAULT_VIEW_INCLUSION); //@JsonView의 사용을 위해 기본포함기능을 비활성화
		disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); //Deserialization에서 알려지지 않은 속성에 대해 오류 방지

		//TimeZone 설정 (Jackson은 기본적으로 GMT를 사용함)
		//http://wiki.fasterxml.com/JacksonFAQDateHandling
		//How come this time is off by 9 hours? (5 hours, 3 hours etc)
		//You may be thinking in terms of your local time zone. Remember that Jackson defaults to using GMT (Greenwich time, one hour behind central European timezone; multiple hours ahead of US time zones).
		setTimeZone(TimeZone.getDefault()); //Java Default Time
		//setTimeZone(DateTimeZone.getDefault()); //Joda Default Time

		//기본 DateTime Format 지정
		if(simpleDateTimeFormat != null) {
			disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			DateFormat dateFormat = new SimpleDateFormat(simpleDateTimeFormat);
			setDateFormat(dateFormat);
		}

		//기타 features는 다음 소스참고: org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean

		registerCustomModules();
	}

	private void registerCustomModules() {
		//SimpleModule module = new SimpleModule("Ablecoms HTML XSS Serializer", new Version(1, 0, 0, "FINAL"));
		SimpleModule module = new SimpleModule("Ablecoms HTML XSS Serializer");

		//JS에 Json을 내려보낼때 JS가 깨지지 않게 포매팅
		//module.addSerializer(new AbleJsonHtmlXssSerializer());

		//Joda LocalDateTime Deserializer - 대부분의 날짜형식을 다 받아줌...
		module.addDeserializer(LocalDateTime.class, new AbleLocalDateTimeDeserialiser());

		registerModule(module);
	}

	@SuppressWarnings("unchecked")
	private void registerWellKnownModulesIfAvailable(ObjectMapper mapper) {
		// Java 8 java.time package present?
		if (ClassUtils.isPresent("java.time.LocalDate", this.moduleClassLoader)) {
			try {
				Class<? extends Module> jsr310Module = (Class<? extends Module>)
						ClassUtils.forName("com.fasterxml.jackson.datatype.jsr310.JSR310Module", this.moduleClassLoader);
				mapper.registerModule(BeanUtils.instantiate(jsr310Module));
			}
			catch (ClassNotFoundException ex) {
				// jackson-datatype-jsr310 not available
			}
		}
		// Joda-Time present?
		if (ClassUtils.isPresent("org.joda.time.LocalDate", this.moduleClassLoader)) {
			try {
				Class<? extends Module> jodaModule = (Class<? extends Module>)
						ClassUtils.forName("com.fasterxml.jackson.datatype.joda.JodaModule", this.moduleClassLoader);
				mapper.registerModule(BeanUtils.instantiate(jodaModule));
			}
			catch (ClassNotFoundException ex) {
				// jackson-datatype-joda not available
			}
		}
		//Hibernate present? - added by deepfree@gmail.com
		if (ClassUtils.isPresent("com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module", this.moduleClassLoader)) {
			try {
				Class<? extends Module> hibernate4Module = (Class<? extends Module>)
						ClassUtils.forName("com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module", this.moduleClassLoader);
				mapper.registerModule(BeanUtils.instantiate(hibernate4Module));
			}
			catch (ClassNotFoundException ex) {
				// jackson-datatype-hibernate4 not available
			}
		}
	}
}
