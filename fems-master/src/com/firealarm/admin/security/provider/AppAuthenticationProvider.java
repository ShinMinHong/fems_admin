package com.firealarm.admin.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import framework.security.provider.AbleDaoAuthenticationProvider;
import framework.security.provider.AppSHAPasswordEncoder;

/**
 * DAO를 통한 인증 프로바이더
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
@Service
public class AppAuthenticationProvider extends AbleDaoAuthenticationProvider {

	@Autowired(required = false) protected UserDetailsService userDetailsService;
	@Autowired(required = false) protected AppSHAPasswordEncoder passwordEncoder;

	/* (non-Javadoc)
	 * @see com.korearms.security.provider.AbleDaoAuthenticationProvider#doAfterPropertiesSet()
	 */
	protected void doAfterPropertiesSet() {
		if(userDetailsService != null) {
			setUserDetailsService(userDetailsService);
		}
		if(passwordEncoder != null) {
			setPasswordEncoder(passwordEncoder);
		}
		super.doAfterPropertiesSet();
	}

}

