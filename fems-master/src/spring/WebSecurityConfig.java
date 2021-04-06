package spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.firealarm.admin.appconfig.AppProfiles;

import framework.security.web.AbleForceHttpRedirectStrategy;
import framework.security.web.HttpsLoginFailHandler;
import framework.security.web.HttpsLoginSuccessHandler;

/**
 * Spring Security - Web 보안 설정
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
@Configuration
@EnableWebSecurity(debug = false)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired AuthenticationEntryPoint authenticationEntryPoint;

    /**
     * authenticationManager를 노출해야 @EnableGlobalMethodSecurity 를 설정 가능
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#authenticationManagerBean()
     */
    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

	@Override
	public void configure(WebSecurity web) {
		web
			.ignoring()
				.antMatchers("/app/**")
				.antMatchers("/css/**")
				.antMatchers("/lib/**")
			;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/**
		 * 기본 필터 체인
		 *   WebAsyncManagerIntegrationFilter
		 *   SecurityContextPersistenceFilter
		 *   HeaderWriterFilter
		 *   CsrfFilter
		 *   LogoutFilter
		 *   UsernamePasswordAuthenticationFilter
		 *   DefaultLoginPageGeneratingFilter
		 *   RequestCacheAwareFilter
		 *   SecurityContextHolderAwareRequestFilter
		 *   AnonymousAuthenticationFilter
		 *   SessionManagementFilter
		 *   ExceptionTranslationFilter
		 *   FilterSecurityInterceptor
		 */
/*		List<ChannelProcessor> channelProcessors = new ArrayList<>();
		channelProcessors.add(new AppSecureChannelProcessor());
		channelProcessors.add(new AppInsecureChannelProcessor());*/

		//boolean isProductionProfile = AppProfiles.getInstance().isRuntimeProduction();
		boolean isLocalProfile = AppProfiles.getInstance().isRuntimeLocal();
		http
			.csrf()
				.disable()
			.headers()
				.frameOptions().sameOrigin()
				.httpStrictTransportSecurity().disable()
				.cacheControl().and()
				.and()
			.requiresChannel()
//			  	.antMatchers(isProductionProfile?"/**":"/todo").requiresSecure()	// 2021.01.25 기존 : isProductionProfile이면 모두 requiresSecure(https)
			  	.antMatchers(isLocalProfile?"/todo":"/**").requiresSecure()			// 2021.01.23 신규 : isLocalProfile이면  requiresSecure태우지 않음
//				.antMatchers("/login*").requiresInsecure()
			  	.and()
			.authorizeRequests()
				.accessDecisionManager(webAccessDecisionManager())
				.antMatchers(
					"/favicon.ico",
					"/index.htm",
					"/login",
					"/logout",
					"/index.do"
				).permitAll()
				.and()
			.formLogin()
				.loginPage("/login")
				.usernameParameter("username")
				.passwordParameter("password")
				.loginProcessingUrl("/loginProcess")
				.defaultSuccessUrl("/")
				.successHandler(authenticationSuccessHandler())
				.failureHandler(authenticationFailureHandler())
				.permitAll()
				.and()
			.logout()
				.invalidateHttpSession(true)
				.permitAll()
				.and()
			.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint)
//				.accessDeniedPage("/home")
			;

		//참고: http://www.rmarcello.it/spring-security-aumomatic-redirect-on-https/
		//참고: http://kamsi76.tistory.com/entry/Spring4-JavaConfig-%EC%84%A4%EC%A0%95-SecurityConfigjava
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		SavedRequestAwareAuthenticationSuccessHandler successHandler = new HttpsLoginSuccessHandler();
		successHandler.setDefaultTargetUrl("/");
		successHandler.setRedirectStrategy(new AbleForceHttpRedirectStrategy());
		return successHandler;
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		HttpsLoginFailHandler httpsLoginFailHandler = new HttpsLoginFailHandler();
		httpsLoginFailHandler.setDefaultFailureUrl("/login");
		return httpsLoginFailHandler;
	}

	@Bean
	public AccessDecisionManager webAccessDecisionManager() {
		List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<AccessDecisionVoter<? extends Object>>();
		WebExpressionVoter expressionVoter = new WebExpressionVoter();
		expressionVoter.setExpressionHandler(webSecurityExpressionHandler());
		decisionVoters.add(expressionVoter);
		return new UnanimousBased(decisionVoters);
	}

	@Bean
	public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
		DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
		expressionHandler.setTrustResolver(new AuthenticationTrustResolverImpl());
		return expressionHandler;
	}
}
