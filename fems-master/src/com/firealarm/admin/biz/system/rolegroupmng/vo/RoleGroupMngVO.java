package com.firealarm.admin.biz.system.rolegroupmng.vo;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonView;

import framework.annotation.ExcelColumn;
import framework.base.model.AbleView;
import lombok.Data;

@Data
public class RoleGroupMngVO {

	/** 관리자권한그룹코드 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="권한그룹코드", order=10)
	private String rolegroupCode;

	/** 관리자권한그룹명 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="권한그룹명", order=20)
	private String rolegroupName;

	/** 등록일 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="등록일", order=30)
	private LocalDateTime regDate;

	/** 순번 */
	@JsonView({BaseView.class})
	private int rn;

	/** 공통 View */
	public interface BaseView extends AbleView.CommonBaseView {}

	@Override
	public String toString() {
		return "RoleGroupMngVO [rolegroupCode=" + rolegroupCode + ", rolegroupNm=" + rolegroupName + ", regDate="
				+ regDate + ", rn=" + rn + "]";
	}
}