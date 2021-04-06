package com.firealarm.admin.biz.login.vo;

import lombok.Data;

@Data
public class LoginVO {

	/** 아이디 */
	protected String userId;

	/** 비밀번호 */
	private String userPassword;

}