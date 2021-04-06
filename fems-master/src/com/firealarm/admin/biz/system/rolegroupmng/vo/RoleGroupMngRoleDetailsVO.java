package com.firealarm.admin.biz.system.rolegroupmng.vo;

import org.joda.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonView;
import com.firealarm.admin.biz.system.rolegroupmng.vo.RoleGroupMngActiveVO.BaseAction;
import com.firealarm.admin.biz.system.rolegroupmng.vo.RoleGroupMngActiveVO.BaseView;
import lombok.Data;

@Data
public class RoleGroupMngRoleDetailsVO {

	/** 권한 코드 */
	@JsonView({ BaseAction.class})
	private String roleCode;

	/** 권한 그룹 코드 */
	@JsonView({ BaseAction.class})
	private String rolegroupCode;

	/** 권한명 */
	@JsonView({ BaseView.class})
	private String roleName;

	/** 등록일 */
	@JsonView({ BaseView.class})
	private LocalDateTime regDate;

	/** 수정 등록일 */
	@JsonView({ BaseView.class})
	private LocalDateTime updtDt;

	/** 등록자 */
	@JsonView({ BaseView.class})
	private String regAdminId;

	/** 수정자 */
	@JsonView({ BaseView.class})
	private String updusrId;

	/** 체크 상태 */
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
		return "RoleGroupMngRoleDetailsVO [roleCode=" + roleCode + ", rolegroupCode=" + rolegroupCode
				+ ", roleName=" + roleName + ", regDate=" + regDate + ", updtDt=" + updtDt + ", regAdminId=" + regAdminId
				+ ", updusrId=" + updusrId + ", checked=" + checked + "]";
	}
}
