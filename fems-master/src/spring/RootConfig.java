package spring;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import framework.fasterxml.jackson.AbleObjectMapper;
import framework.fasterxml.jackson.AbleXmlMapper;
import framework.mybatis.spring.RefreshableSqlSessionFactoryBean;
import framework.util.AbleUtil;
import com.firealarm.admin.appconfig.AppConfig;
import com.firealarm.admin.appconfig.AppProfiles;
import com.firealarm.admin.appconfig.ApplicationContextHolder;

/**
 * Spring 루트 설정
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
@Configuration
@EnableSpringConfigured // Using AspectJ to dependency inject domain objects with Spring
@EnableAspectJAutoProxy // Enabling @AspectJ Support
//@EnableLoadTimeWeaving
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
//@EnableCaching
//@Import({})
//@Profile
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
public class RootConfig {
	protected static Logger logger = LoggerFactory.getLogger(RootConfig.class);

    /** ApplicationContext 접근 Util */
    @Bean
    public ApplicationContextHolder applicationContextHolder() {
    	return new ApplicationContextHolder();
    }

    /** Application Profiles접근 Util */
    @Bean
    public AppProfiles appProfiles(@Autowired Environment environment) {
    	return new AppProfiles(environment);
    }

    /** Application 설정접근 Util */
    @Bean
    public AppConfig appConfig() {
    	return new AppConfig();
    }

	@Bean
	public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer(@Autowired ApplicationContext applicationContext) {
		PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
		String activeProfile = AbleUtil.getActiveProfile();
		String propertyResourceLocation = "classpath:/config/config." + activeProfile + ".properties";
		logger.info("### properties: {}", propertyResourceLocation);
		configurer.setLocations(
				applicationContext.getResource(propertyResourceLocation)
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
	public DataSource dataSource(@Autowired AppConfig appConfig) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(appConfig.getJdbcDriverClass());
        dataSource.setUrl(appConfig.getJdbcUrl());
        dataSource.setUsername(appConfig.getJdbcUsername());
        dataSource.setPassword(appConfig.getJdbcPassword());
		return dataSource;
	}

	/** TransactionManager */
	@Bean(name="transactionManager")
	public PlatformTransactionManager txManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	/** MyBatis SqlSessionTempate */
	@Bean
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

	/** MappingJackson ObjectMapper */
	@Bean(name="objectMapper")
	public ObjectMapper objectMapper() {
		return new AbleObjectMapper();
	}

	/** MappingJackson XmlMapper */
	@Bean(name="xmlMapper")
	public ObjectMapper xmlMapper() {
		return new AbleXmlMapper();
	}

	/** validator */
	@Bean
	public SmartValidator validator(){
		return new OptionalValidatorFactoryBean();
	}

}
