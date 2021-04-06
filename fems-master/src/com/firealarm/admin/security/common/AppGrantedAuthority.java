package com.firealarm.admin.security.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import framework.util.AbleEnumUtil;
import com.firealarm.admin.appconfig.CodeMap.APP_USER_ROLE;

/**
 * Spring Security User Granted Authority
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AppGrantedAuthority implements GrantedAuthority {
	private static final long serialVersionUID = 2952194450006950470L;

	static Logger logger = LoggerFactory.getLogger(AppGrantedAuthority.class);

	/** 권한 */
	private APP_USER_ROLE role;

	/* (non-Javadoc)
	 * @see org.springframework.security.core.GrantedAuthority#getAuthority()
	 */
	@Override
	public String getAuthority() {
		return (role == null) ? null : role.toString();
	}

	/**
	 * 생성자
	 * @param role 로그인사용자 권한
	 */
	public AppGrantedAuthority(APP_USER_ROLE role) {
		this.role = role;
	}

	/**
	 * 생성자
	 * @param roleName 로그인사용자 권한 문자열
	 */
	public AppGrantedAuthority(String roleName) {
		this.role = AbleEnumUtil.parseEnumValueOf(APP_USER_ROLE.class, roleName);
		if(this.role == null) {
			logger.error("APP_USER_ROLE에 {}이 정의되어 있지 않습니다.", roleName);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (obj instanceof SimpleGrantedAuthority) {
			String authority = ((SimpleGrantedAuthority)obj).getAuthority();
			return this.role.toString().equals(authority);
		}

		if (obj instanceof AppGrantedAuthority) {
			AppGrantedAuthority other = (AppGrantedAuthority) obj;
			if (this.role.equals(other.role))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return getAuthority();
	}

}
