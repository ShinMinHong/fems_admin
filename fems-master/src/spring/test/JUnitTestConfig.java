package spring.test;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import javax.validation.Validator;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ServletContextAware;

import com.fasterxml.jackson.databind.ObjectMapper;

import framework.fasterxml.jackson.AbleObjectMapper;
import framework.mybatis.spring.RefreshableSqlSessionFactoryBean;
import framework.spring.web.log.AbleControllerLoggingAspect;
import framework.util.AbleUtil;

@Configuration
@ComponentScan(
	basePackages = "com.firealarm.admin",
	useDefaultFilters = false,
	excludeFilters = {
			@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = { Controller.class }),
			@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = { RestController.class })
	},
	includeFilters = {
			@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = { Configuration.class }),
			@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = { Repository.class }),
			@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = { Service.class })
	}
)
@PropertySource("classpath:/config/config.${spring.profiles.active}.properties")
public class JUnitTestConfig implements ApplicationContextAware, ServletContextAware, EnvironmentAware {
	protected static Logger logger = LoggerFactory.getLogger(JUnitTestConfig.class);

	protected ApplicationContext applicationContext;
	protected ServletContext servletContext;
	protected Environment environment;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Bean
	public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
		PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
		String activeProfile = AbleUtil.getActiveProfile();
		logger.debug("ACTIVE SPRING PROFILE => {}", activeProfile);
		configurer.setLocations(
				this.applicationContext.getResource("classpath:/config/config." + activeProfile + ".properties")
		);
		return configurer;
	}

	@Bean
	public FormattingConversionService conversionService() {
		// Use the DefaultFormattingConversionService but do not register defaults
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);

        // Ensure @NumberFormat is still supported
        conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());

        // Register date conversion with a specific global format
        DateFormatterRegistrar registrar = new DateFormatterRegistrar();
        registrar.setFormatter(new DateFormatter("yyyyMMdd"));
        registrar.registerFormatters(conversionService);

        return conversionService;
	}

	// ===========================================================================
	// [Message Source]
	// ===========================================================================
	@Bean(name="messageSource")
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:/config/i18n/message");
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setCacheSeconds(1);
		return messageSource;
	}

	// ===========================================================================
	// [Data Access]
	// ===========================================================================

	/** DataSource */
	@Bean
	public DataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(environment.getProperty("jdbc.driverClass"));
		dataSource.setUrl(environment.getProperty("jdbc.url"));
		dataSource.setUsername(environment.getProperty("jdbc.username"));
		dataSource.setPassword(environment.getProperty("jdbc.password"));
		return dataSource;
	}

	/** TransactionManager */
	//@Bean
	//public PlatformTransactionManager txManager(DataSource dataSource) {
	//	return new DataSourceTransactionManager(dataSource);
	//}

	/** MyBatis SqlSessionTempate */
	@Bean(destroyMethod="clearCache")
	public SqlSessionTemplate sqlSession(SqlSessionFactory sqlFactory) {
		return new SqlSessionTemplate(sqlFactory);
	}

	/** MyBatis SqlSessionFactory (local profile) */
	@Bean
	@Profile("local")
	public SqlSessionFactoryBean sqlFactoryLocal(DataSource dataSource, final ApplicationContext ctx) throws IOException {
		return setupSqlFactory(new RefreshableSqlSessionFactoryBean(), dataSource, ctx);
	}

	/** MyBatis SqlSessionFactory (dev, stage, real profile) */
	@Bean
	@Profile("!local")
	public SqlSessionFactoryBean sqlFactory(DataSource dataSource, final ApplicationContext ctx) throws IOException {
		return setupSqlFactory(new SqlSessionFactoryBean(), dataSource, ctx);
	}

	protected SqlSessionFactoryBean setupSqlFactory(SqlSessionFactoryBean sqlFactory, DataSource dataSource, ApplicationContext ctx) throws IOException {
		sqlFactory.setDataSource(dataSource);
		sqlFactory.setConfigLocation(ctx.getResource("classpath:config/mybatis-config.xml"));
		sqlFactory.setMapperLocations(ctx.getResources("classpath:mapper/**/*.xml"));
		return sqlFactory;
	}


	///////////////////////////////////////////////////////////////////////////
	/** MappingJackson ObjectMapper */
	@Bean
	public ObjectMapper objectMapper() {
		return new AbleObjectMapper();
	}

	/** Controller Logging AOP */
	@Bean
	public AbleControllerLoggingAspect ableControllerLoggingAspect() {
		return new AbleControllerLoggingAspect();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public Validator validator(){
		return new OptionalValidatorFactoryBean();
	}

}
