package com.firealarm.admin.biz.system.rolemng.vo;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonView;
import com.firealarm.admin.common.vo.RoleDT;

import framework.annotation.Display;
import framework.validation.constraints.AbleMaxLength;
import lombok.Data;

@Data
public class RoleMngActiveVO {

	/* 관리자권한코드 */
	@Display(name = "권한코드")
	@JsonView({ BaseAction.class})
	@NotBlank(groups = { CreateAction.class})
	@AbleMaxLength(value=50, groups = { CreateAction.class })
	private String roleCode;

	/* 관리자권한명 */
	@Display(name = "권한명")
	@JsonView({ BaseAction.class})
	@NotBlank(groups = { BaseAction.class})
	@AbleMaxLength(value=50, groups = { BaseAction.class })
	private String roleName;

	/* 관리자권한명 */
	@Display(name = "권한그룹코드")
	@JsonView({ BaseAction.class})
	private String rolegroupCode;

	/* 관리자권한그룹코드 */
	@JsonView({ BaseAction.class})
	private List<RoleMngRoleDetailsVO> roleDetailsList = new ArrayList<RoleMngRoleDetailsVO>();

	/** 공통 Action */
	public interface BaseAction extends RoleDT.BaseAction {}
	/** 수정 Action */
	public interface UpdateAction extends BaseAction {}
	/** 등록 Action */
	public interface CreateAction extends BaseAction {}

	@Override
	public String toString() {
		return "RoleMngActiveVO [roleCode=" + roleCode + ", roleName=" + roleName + ", rolegroupCode=" + rolegroupCode
				+ ", roleDetailsList=" + roleDetailsList + "]";
	}


}