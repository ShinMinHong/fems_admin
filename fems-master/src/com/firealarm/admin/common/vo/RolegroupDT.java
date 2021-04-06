package com.firealarm.admin.common.vo;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonView;

import framework.base.model.AbleView;
import lombok.Data;

@Data
public class RolegroupDT {

	/** 관리자권한그룹코드 */
	@JsonView({BaseView.class, BaseAction.class})
	private String authorgroupCode;

	/** 관리자권한그룹명 */
	@JsonView({BaseView.class, BaseAction.class})
	private String authorgroupName;

	/** 등록일 */
	@JsonView({BaseView.class, BaseAction.class})
	private LocalDateTime rgsde;

	/** 공통 View */
	public interface BaseView extends AbleView.CommonBaseView {}

	/** 공통 Action */
	public interface BaseAction extends AbleView.CommonBaseAction {}
	public interface CreateAction extends BaseAction {}
	public interface UpdateAction extends BaseAction {}

	@Override
	public String toString() {
		return "RolegroupRoleDT [authorgroupCode=" + authorgroupCode + ", authorgroupName=" + authorgroupName + ", rgsde=" + rgsde + "]";
	}
}
