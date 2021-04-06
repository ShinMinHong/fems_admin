package com.firealarm.admin.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.firealarm.admin.security.vo.AppUserDetails;

public class UserSecurityUtil {

	/**
	 * 현재 Context의 Authentication 획득
	 * @return 현재 Context의 Authentication
	 */
	public static Authentication getAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication;
	}

	/**
	 * SPEL: isAnonymous() 와 동일한 함수
	 * @return 로그인되어 있지 않거나 Principle이 Anonymous이면 true
	 */
	public static Boolean isAnonymous() {
		Authentication authentication = getAuthentication();
		return (authentication.isAuthenticated() == false) || (authentication.getPrincipal() instanceof AppUserDetails == false);
	}

	/**
	 * SPEL: isAuthenticated() 와 동일한 함수 (Anonymous가 아닌 유저로 로그인 되어 있는지...)
	 * @return 로그인되어 있고, Principle이 Anoymous가 아니면 true
	 */
	public static Boolean isAuthenticated() {
		return !isAnonymous();
	}

	/**
	 * AppUserDetails를 획득 (로그인 되지 않은 경우 null)
	 * @return AppUserDetails, 로그인 되지 않은 경우 null 반환
	 */
	public static AppUserDetails getCurrentUserDetails() {
		Authentication authentication = getAuthentication();
		return getCurrentUserDetails(authentication);
	}

	/**
	 * AppUserDetails를 획득 (로그인 되지 않은 경우 null)
	 * @return AppUserDetails, 로그인 되지 않은 경우 null 반환
	 */
	public static AppUserDetails getCurrentUserDetails(Authentication authentication) {
		if(authentication == null || authentication.isAuthenticated() == false)
			return null;
		Object principal = authentication.getPrincipal();
		if(principal instanceof AppUserDetails) {
			return (AppUserDetails) principal;
		}
		return null;
	}
}
