package com.firealarm.admin.security.vo;

import java.io.Serializable;
import java.util.Collection;

import org.joda.time.LocalDateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.firealarm.admin.appconfig.CodeMap.APP_USER_GRADE;

/**
 * Spring Security 로그인 사용자 정보
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AppUserDetails extends User implements Serializable {

	private static final long serialVersionUID = 1835738337865341415L;

	protected long adminSeq;
	protected String adminId;
	protected String adminName;

	protected APP_USER_GRADE rolegroupCode;

	protected LocalDateTime lastLoginDate;
	protected LocalDateTime regDate;

	protected Long mngAreaSeq;
	protected String mngAreaName;
	protected Long marketSeq;
	protected String marketName;

	protected int passwordFailCo;
	protected LocalDateTime lastPwChangeDt;

	private boolean pswdExpired;


	/** 사용자 이름은 userNm. 스프링시큐리티의 Username은 Identity Column으로 사용됨 */
	@Override public String getUsername() {
		return super.getUsername();
	}

	public Long getAdminSeq() {
		return adminSeq;
	}
	public String getAdminId() {
		return adminId;
	}
	public String getAdminName() {
		return adminName;
	}
	public APP_USER_GRADE getRolegroupCode() {
		return rolegroupCode;
	}

	public LocalDateTime getLastLoginDate() {
		return lastLoginDate;
	}
	public LocalDateTime getRegDate() {
		return regDate;
	}

	public Long getMngAreaSeq() {
		return mngAreaSeq;
	}
	public void setMngAreaSeq(Long mngAreaSeq) {
		this.mngAreaSeq = mngAreaSeq;
	}
	public boolean hasMngArea() {
		return mngAreaSeq != null;
	}

	public String getMngAreaName() {
		return mngAreaName;
	}
	public void setMngAreaName(String mngAreaName) {
		this.mngAreaName = mngAreaName;
	}
	public Long getMarketSeq() {
		return marketSeq;
	}
	public String getMarketName() {
		return marketName;
	}

	public int getPasswordFailCo() {
		return passwordFailCo;
	}
	public LocalDateTime getLastPwChangeDt() {
		return lastPwChangeDt;
	}
	public boolean isPswdExpired() {
		return pswdExpired;
	}



	/** 생성자  */
	public AppUserDetails(AdminDT admin, String mngAreaName, String marketName, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(admin.getAdminId(), admin.getAdminPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.adminSeq = admin.getAdminSeq();
		this.adminId = admin.getAdminId();
		this.adminName = admin.getAdminName();
		this.rolegroupCode = admin.getRolegroupCode();
		this.lastLoginDate = admin.getLastLoginDate();
		this.regDate = admin.getRegDate();

		this.mngAreaSeq = admin.getMngAreaSeq();
		this.mngAreaName = mngAreaName;
		this.marketSeq = admin.getMarketSeq();
		this.marketName = marketName;

		this.passwordFailCo = admin.getPasswordFailCo();
		this.lastPwChangeDt = admin.getLastPwChangeDt();
		this.pswdExpired = admin.getPswdExpired() == null ? true : admin.getPswdExpired().booleanValue();
	}

	/** 기본생성자 */
	public AppUserDetails() {
		super(null, null, null);
	}

	/**
	 * 주어진 권한을 가지고 있는지 확인
	 * @param role
	 * @return Boolean
	 */
	public Boolean hasRole(String role) {
		Collection<GrantedAuthority> authorities = this.getAuthorities();
		for (GrantedAuthority authority : authorities) {

			if (authority.getAuthority() != null && authority.getAuthority().equals(role)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "AppUserDetails [adminSeq=" + adminSeq + ", adminId=" + adminId + ", adminName=" + adminName
				+ ", rolegroupCode=" + rolegroupCode + ", lastLoginDate=" + lastLoginDate + ", regDate=" + regDate
				+ ", mngAreaSeq=" + mngAreaSeq + ", mngAreaName=" + mngAreaName
				+ ", marketSeq=" + marketSeq + ", marketName=" + marketName + ", passwordFailCo=" + passwordFailCo + ", lastPwChangeDt=" + lastPwChangeDt
				+ "]";
	}
}
