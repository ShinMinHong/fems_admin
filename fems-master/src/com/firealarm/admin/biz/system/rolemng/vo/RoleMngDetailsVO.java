package com.firealarm.admin.biz.system.rolemng.vo;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;

import lombok.Data;

@Data
public class RoleMngDetailsVO {

	/* 관리자권한코드 */
	private String roleCode;

	/* 관리자권한명 */
	private String roleName;

	/* 등록일 */
	private LocalDateTime regDate;

	/* 롤권한 리스트 */
	private List<RoleMngRoleDetailsVO> roleDetailsList = new ArrayList<RoleMngRoleDetailsVO>();

	@Override
	public String toString() {
		return "RoleMngDetailsVO [roleCode=" + roleCode + ", roleName=" + roleName + ", regDate=" + regDate
				+ ", roleDetailsList=" + roleDetailsList + "]";
	}
}