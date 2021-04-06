package com.firealarm.admin.biz.system.rolegroupmng.vo;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;

import lombok.Data;

@Data
public class RoleGroupMngDetailsVO {

	/** 관리자권한그룹코드 */
	private String rolegroupCode;

	/** 관리자권한그룹명 */
	private String rolegroupName;

	private LocalDateTime regDate;

	/** 롤권한 리스트 */
	private List<RoleGroupMngRoleDetailsVO> roleDetailsList = new ArrayList<RoleGroupMngRoleDetailsVO>();

	@Override
	public String toString() {
		return "RoleGroupMngDetailsVO [rolegroupCode=" + rolegroupCode + ", rolegroupName=" + rolegroupName
				+ ", regDate=" + regDate + ", roleDetailsList=" + roleDetailsList + "]";
	}

}