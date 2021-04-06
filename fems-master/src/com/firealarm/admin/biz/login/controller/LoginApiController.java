package com.firealarm.admin.biz.login.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.firealarm.admin.biz.home.vo.PasswordChangeVO;
import com.firealarm.admin.common.service.UserService;
import com.firealarm.admin.common.support.ApiControllerSupport;

import framework.spring.web.rest.AbleResponseEntity;
import framework.spring.web.rest.AbleResponseEntityBuilder;

@Controller
@RequestMapping(value = "/home/api")
public class LoginApiController extends ApiControllerSupport {

	@Autowired UserService userService;

	/** 비밀번호 변경 */
	@RequestMapping(value = "/changepassword", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> changePassword(
			HttpServletRequest request,
			@RequestBody PasswordChangeVO passwordChangeVO,
			Model model) {

		userService.updatePassword(passwordChangeVO);

		return AbleResponseEntityBuilder.success(null);
	}

}
