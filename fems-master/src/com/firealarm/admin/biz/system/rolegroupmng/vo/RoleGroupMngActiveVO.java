package com.firealarm.admin.biz.system.rolegroupmng.vo;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonView;

import framework.annotation.Display;
import framework.base.model.AbleView;
import framework.validation.constraints.AbleMaxLength;
import lombok.Data;

@Data
public class RoleGroupMngActiveVO {

	/* 관리자권한그룹코드 */
	@Display(name = "권한그룹코드")
	@JsonView({ BaseView.class, BaseAction.class})
	@NotBlank(groups = { CreateAction.class})
	@AbleMaxLength(value=20, groups = { CreateAction.class })
	private String rolegroupCode;

	/* 관리자권한그룹명 */
	@Display(name = "권한그룹명")
	@JsonView({ BaseView.class , BaseAction.class })
	@NotBlank(groups =	 { BaseView.class})
	@AbleMaxLength(value=20, groups = { BaseView.class })
	private String rolegroupName;

	/* 관리자권한코드 */
	@JsonView({ BaseAction.class })
	private List<RoleGroupMngRoleDetailsVO> roleDetailsList = new ArrayList<RoleGroupMngRoleDetailsVO>() ;

	/** 공통 View */
	public interface BaseView extends AbleView.CommonBaseView {}

	/** 공통 Action */
	public interface BaseAction extends AbleView.CommonBaseAction {}

	/** 수정 Action */
	public interface UpdateAction extends BaseAction {}
	/** 등록 Action */
	public interface CreateAction extends BaseAction {}

	@Override
	public String toString() {
		return "RoleGroupMngActiveVO [rolegroupCode=" + rolegroupCode + ", rolegroupName=" + rolegroupName
				+ ", roleDetailsList=" + roleDetailsList + "]";
	}
}