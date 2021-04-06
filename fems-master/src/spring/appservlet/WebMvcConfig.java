package spring.appservlet;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.core.env.Environment;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;
import org.springframework.web.servlet.view.xml.MappingJackson2XmlView;

import com.fasterxml.jackson.databind.ObjectMapper;

import framework.spring.core.convert.AbleStringToJodaDateTimeConverter;
import framework.spring.core.convert.AbleStringToJodaLocalDateTimeConverter;
import framework.spring.web.view.AbleExcelView;
import com.firealarm.admin.appconfig.AppConst;

/**
 * Spring AppServlet - Web 설정
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
@Configuration
@EnableSpringConfigured // Using AspectJ to dependency inject domain objects with Spring
@EnableAspectJAutoProxy // Enabling @AspectJ Support
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true, mode = AdviceMode.PROXY, proxyTargetClass = true)
@ComponentScan(
	basePackages = "com.firealarm.admin",
	useDefaultFilters = false,
	excludeFilters = {
			@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = { Configuration.class }),
			@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = { Repository.class }),
			@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = { Service.class })
	},
	includeFilters = {
			@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = { Controller.class }),
			@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = { RestController.class })
	}
)
public class WebMvcConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware, ServletContextAware, EnvironmentAware {
	protected static Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

	@Autowired @Qualifier("objectMapper") ObjectMapper objectMapper;
	@Autowired @Qualifier("xmlMapper") ObjectMapper xmlMapper;

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

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer
			.ignoreAcceptHeader(false)
			.favorPathExtension(true)
			.ignoreUnknownPathExtensions(true)
			.favorParameter(true)
			.mediaType("json", MediaType.APPLICATION_JSON)
			.defaultContentType(AppConst.API_PRODUCES_MEDIATYPE);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/app/**").addResourceLocations("/app/")
			.setCacheControl(CacheControl.noStore()); //prevent cache
		registry.addResourceHandler("/css/**").addResourceLocations("/css/")
			.setCacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).mustRevalidate());
		registry.addResourceHandler("/lib/**").addResourceLocations("/lib/")
			.setCacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).mustRevalidate());
	}

	@Override
	public Validator getValidator() {
		return new OptionalValidatorFactoryBean();
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new AuthenticationPrincipalArgumentResolver()); //@AuthenticationPrincipal Resolver
		argumentResolvers.add(new PageableHandlerMethodArgumentResolver()); //Pageable Resolver
		argumentResolvers.add(new SortHandlerMethodArgumentResolver()); //Sort Resolver
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new AbleStringToJodaDateTimeConverter());
        registry.addConverter(new AbleStringToJodaLocalDateTimeConverter());
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter json = new MappingJackson2HttpMessageConverter(objectMapper);
		//IE 9에서 json을 iframe transport로 전송시 파일로 저장하려는 버그 발생 text 미디어타입으로 처리
		json.setSupportedMediaTypes(Arrays.asList(AppConst.API_PRODUCES_MEDIATYPE, AppConst.APPLICATION_JSON));
		converters.add(json);
        MappingJackson2XmlHttpMessageConverter xml = new MappingJackson2XmlHttpMessageConverter(xmlMapper);
		converters.add(xml);
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		jsonView.setPrettyPrint(true);
		MappingJackson2XmlView xmlView = new MappingJackson2XmlView();
		xmlView.setPrettyPrint(true);
		registry.enableContentNegotiation(
				jsonView
				//, xmlView
		);
		registry.tiles().viewClass(TilesView.class);
		registry.jsp()
			.prefix("/WEB-INF/views/")
			.suffix(".jsp");
	}

	@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer tilesConfigurer = new TilesConfigurer();
        tilesConfigurer.setDefinitions(new String[]{
                "/WEB-INF/layouts/**/*.tiles.xml",
                "/WEB-INF/views/**/*.tiles.xml"
        });
        tilesConfigurer.setCheckRefresh(true);
        return tilesConfigurer;
	}

	@Bean(name="multipartResolver") //Spring default bean name
	public CommonsMultipartResolver commonsMultipartResolver() {
	    CommonsMultipartResolver resolver=new CommonsMultipartResolver();
	    resolver.setDefaultEncoding("utf-8");
	    return resolver;
	}

	@Bean
	public StandardServletMultipartResolver standardServletMultipartResolver() {
		StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
		return resolver;
	}

	@Bean(name="ableExcelView")
	public AbleExcelView ableExcelView() {
		return new AbleExcelView();
	}
}
