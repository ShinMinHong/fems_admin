package com.firealarm.admin.biz.areasystem.adminmng.vo;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.firealarm.admin.appconfig.CodeMap.APP_USER_GRADE;

import lombok.Data;

@Data
public class AdminMngVO {

	/** 관리자고유번호 */
	private long adminSeq;

	/** 권한그룹코드 */
	private APP_USER_GRADE rolegroupCode;

	/** 관제지역고유번호 */
	private long mngAreaSeq;

	/** 전통시장고유번호 */
	private Long marketSeq;

	/** 사용자아이디 */
	private String adminId;

	/** 사용자비밀번호 */
	@JsonIgnore
	private String adminPassword;

	/** 사용자명 */
	private String adminName;

	/** 직책 */
	private String dutyName;

	/** 휴대폰번호 */
	private String phoneNo;

	/** SMS수신여부 **/
	private boolean smsReceiveYn;

	/** 사용여부 */
	private boolean useYn;

	/** 최종로그인일시 */
	private LocalDateTime lastLoginDate;

	/** 등록일시 */
	private LocalDateTime regDate;

	/** 등록자ID */
	private String regAdminId;

	/** 최종수정일시 */
	private LocalDateTime updDate;

	/** 최종수정자ID */
	private String updAdminId;

	/** No */
	private String rn;

	/** 비밀번호 실패횟수 */
	private Integer passwordFailCo;

	/** 비밀번호변경일자 */
	private LocalDateTime lastPwChangeDt;

	@Override
	public String toString() {
		return "AdminMngVO [adminSeq=" + adminSeq + ", rolegroupCode=" + rolegroupCode + ", mngAreaSeq=" + mngAreaSeq
				+ ", marketSeq=" + marketSeq + ", adminId=" + adminId + ", adminName=" + adminName + ", dutyName="
				+ dutyName + ", phoneNo=" + phoneNo + ", smsReceiveYn=" + smsReceiveYn + ", useYn=" + useYn
				+ ", lastLoginDate=" + lastLoginDate + ", regDate=" + regDate + ", regAdminId=" + regAdminId
				+ ", updDate=" + updDate + ", updAdminId=" + updAdminId + "]";
	}

}
