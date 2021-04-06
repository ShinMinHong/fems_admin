package com.firealarm.admin.biz.areasystem.loginhistory.vo;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonView;

import framework.annotation.ExcelColumn;
import framework.base.model.AbleView;
import lombok.Data;

@Data
public class LoginHistoryVO {

	@JsonView( {BaseView.class} )
	private long rn;

	/** 로그인로그고유번호 **/
	@JsonView({BaseView.class})
	private long loginLogSeq;

	/** 관리자고유번호 **/
	@JsonView({BaseView.class})
	private long adminSeq;

	/** 권한그룹코드 **/
	@JsonView({BaseView.class})
	@ExcelColumn(name="관리자구분", order=50)
	private String rolegroupCode;

	/** 관제지역고유번호 **/
	@JsonView({BaseView.class})
	@ExcelColumn(name="관제지역명", order=90)
	private Long mngAreaSeq;

	/** 시장 SEQ **/
	@JsonView({BaseView.class})
	@ExcelColumn(name="시장명", order=100)
	private Long marketSeq;

	/** 사용자아이디 **/
	@JsonView({BaseView.class})
	@ExcelColumn(name="사용자아이디", order=10)
	private String adminId;

	/** 사용자명 **/
	@JsonView({BaseView.class})
	@ExcelColumn(name="사용자명", order=20)
	private String adminName;

	/** 직책 **/
	@JsonView({BaseView.class})
	@ExcelColumn(name="직책", order=70)
	private String dutyName;

	/** 로그인일시 **/
	@JsonView({BaseView.class})
	@ExcelColumn(name="로그인일시", order=30)
	private LocalDateTime loginDate;

	/** 공통 View */
	public interface BaseView extends AbleView.CommonBaseView {}

	@Override
	public String toString() {
		return "LoginHistoryVO [loginLogSeq=" + loginLogSeq + ", adminSeq=" + adminSeq + ", rolegroupCode="
				+ rolegroupCode + ", mngAreaSeq=" + mngAreaSeq + ", marketSeq=" + marketSeq + ", adminId=" + adminId
				+ ", adminName=" + adminName + ", dutyName=" + dutyName + "]";
	}

}