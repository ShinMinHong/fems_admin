package com.firealarm.admin.biz.login.vo;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

/**
 * 로그인 요청 객체
 *
 */
@Data
public class LoginPO {

	@NotBlank(message="id 항목을 입력해주세요.")
	private String id = "";

	@NotBlank(message="password 항목을 입력해주세요.")
	private String password = "";

	private Boolean rememberMe = false;

	@Override
	public String toString() {
		return "LoginPO [id=" + id + ", rememberMe=" + rememberMe + "]";
	}
}
