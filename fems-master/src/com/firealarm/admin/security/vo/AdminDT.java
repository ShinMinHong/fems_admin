package com.firealarm.admin.security.vo;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.firealarm.admin.appconfig.CodeMap.APP_USER_GRADE;

import framework.base.model.AbleView;
import lombok.Data;

/**
 * 관리자 객체
 *
 * 사용자 로그인 객체인 {@link AppUserDetails}와 Pair로 수정필요
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminDT {

	/** 관리자고유번호 */
	@JsonView({BaseView.class})
	private long adminSeq;

	/** 권한그룹코드 */
	@JsonView({BaseView.class})
	private APP_USER_GRADE rolegroupCode;

	/** 관제지역고유번호 */
	@JsonView({BaseView.class})
	private Long mngAreaSeq;

	/** 전통시장고유번호 */
	@JsonView({BaseView.class})
	private Long marketSeq;

	/** 사용자아이디 */
	@JsonView({BaseView.class})
	private String adminId;

	/** 사용자비밀번호 */
	@JsonView({BaseView.class})
	private String adminPassword;

	/** 사용자명 */
	@JsonView({BaseView.class})
	private String adminName;

	/** 직책 */
	@JsonView({BaseView.class})
	private String dutyName;

	/** 휴대폰번호 */
	@JsonView({BaseView.class})
	private String phoneNo;

	/** SMS수신여부 */
	@JsonView({BaseView.class})
	private boolean smsReceiveYn;

	/** 사용여부 */
	@JsonView({BaseView.class})
	private boolean useYn;

	/** 최종로그인일시 */
	@JsonView({BaseView.class})
	private LocalDateTime lastLoginDate;

	/** 등록일시 */
	@JsonView({BaseView.class})
	private LocalDateTime regDate;

	/** 등록자ID */
	@JsonView({BaseView.class})
	private String regAdminId;

	/** 최종수정일시 */
	@JsonView({BaseView.class})
	private LocalDateTime updDate;

	/** 최종수정자ID */
	@JsonView({BaseView.class})
	private String updAdminId;

	/** 비밀번호실패횟수 */
	@JsonView({BaseView.class})
	private int passwordFailCo;

	/** 최종비밀번호변경일시 */
	@JsonView({BaseView.class})
	private LocalDateTime lastPwChangeDt;

	/** 비밀번호변경주기초과여부 **/
	@JsonView({BaseView.class})
	private Boolean pswdExpired;

	/** 공통 View */
	public interface BaseView extends AbleView.CommonBaseView {}

	@Override
	public String toString() {
		return "AdminDT [adminSeq=" + adminSeq + ", rolegroupCode=" + rolegroupCode + ", mngAreaSeq=" + mngAreaSeq
				+ ", marketSeq=" + marketSeq + ", adminId=" + adminId + ", adminPassword=" + adminPassword
				+ ", adminName=" + adminName + ", dutyName=" + dutyName + ", phoneNo=" + phoneNo + ", smsReceiveYn="
				+ smsReceiveYn + ", useYn=" + useYn + ", lastLoginDate=" + lastLoginDate + ", regDate=" + regDate
				+ ", regAdminId=" + regAdminId + ", updDate=" + updDate + ", updAdminId=" + updAdminId + ", passwordFailCo=" + passwordFailCo + ", lastPwChangeDt=" + lastPwChangeDt + "]";
	}
}
