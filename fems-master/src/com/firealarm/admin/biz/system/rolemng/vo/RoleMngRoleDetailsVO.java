package com.firealarm.admin.biz.system.rolemng.vo;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonView;
import com.firealarm.admin.biz.system.rolemng.vo.RoleMngActiveVO.BaseAction;

import lombok.Data;

@Data
public class RoleMngRoleDetailsVO {

	/* 권한 그룹 코드 */
	@JsonView({ BaseAction.class})
	private String rolegroupCode;

	/* 권한 코드 */
	@JsonView({ BaseAction.class})
	private String roleCode;

	/* 권한명 */
	@JsonView({ BaseAction.class})
	private String roleName;

	/* 권한그룹 */
	@JsonView({ BaseAction.class})
	private String rolegroupName ;

	/* 등록일 */
	@JsonView({ BaseAction.class})
	private LocalDateTime regDate;

	/* 체크 상태 */
	@JsonView({ BaseAction.class})
	private boolean checked;


	public boolean getChecked() {
		return this.checked;
	}

	public boolean setChecked() {
		return this.checked;
	}

	@Override
	public String toString() {
		return "RoleMngRoleDetailsVO [rolegroupCode=" + rolegroupCode + ", roleCode=" + roleCode + ", roleName="
				+ roleName + ", rolegroupName=" + rolegroupName + ", regDate=" + regDate + ", checked=" + checked + "]";
	}
}