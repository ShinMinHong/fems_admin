package spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.RedirectStrategy;

import framework.security.web.AbleLoginUrlAuthenticationEntryPoint;
import framework.security.web.AbleRedirectStrategy;

/**
 * Spring Security - 전역 설정
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
@Configuration
@EnableGlobalAuthentication
public class GlobalSecurityConfig {

	@Autowired UserDetailsService userDetailsService;
	@Autowired AuthenticationProvider authenticationProvider;

	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public RedirectStrategy redirectStrategy() {
		return new AbleRedirectStrategy();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new AbleLoginUrlAuthenticationEntryPoint("/login");
	}
}
