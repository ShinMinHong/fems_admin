package com.firealarm.admin.biz.home.vo;

import lombok.Data;

/**
 * 비밀번호 변경 VO
 *
 */
@Data
public class PasswordChangeVO {

	/** 아이디 */
	protected String loginId;

	/** 비밀번호 */
	protected String originalPassword;

	/** 변경할 비밀번호 */
	protected String newPassword;

}

