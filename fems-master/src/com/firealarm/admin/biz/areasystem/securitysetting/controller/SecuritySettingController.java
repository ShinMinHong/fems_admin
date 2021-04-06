package com.firealarm.admin.biz.areasystem.securitysetting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.firealarm.admin.common.service.CommonCodeMapService;
import com.firealarm.admin.common.support.ControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.spring.web.view.AbleExcelView;

/**
 * 보안설정 Controller
 * @author rodem4
 *
 */
@Controller
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN_MNG','ROLE_ADMIN_READ') AND @fireAlarmCustomSecurityService.hasMngArea()")
@RequestMapping(value = "/areasystem/securitysetting")
public class SecuritySettingController extends ControllerSupport {

	@Autowired AbleExcelView ableExcelView;
	@Autowired CommonCodeMapService commonCodeMapService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model, @AppLoginUser AppUserDetails me) {

		// 관제지역정보 세션에 존재유부 체크. 없다면 Exception발생(ComponentSupport.validateMngArea)
		validateMngArea();

		return "areasystem/securitysetting/securitysetting";
	}

}