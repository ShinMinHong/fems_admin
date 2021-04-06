package com.firealarm.admin.biz.system.hqadminmng.controller;

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

import framework.util.AbleUtil;

/**
 * 관리자 관리 Controller
 * @author jks
 *
 */
@Controller
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN_MNG')")
@RequestMapping(value = "/system/hqadminmng")
public class HqAdminMngController extends ControllerSupport {

	@Autowired CommonCodeMapService commonCodeMapService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model, @AppLoginUser AppUserDetails me) {

		/** 관리자의 접근가능한 관리자구분  */
		model.addAttribute("USER_GRADE_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getUserGradeCodeMap()));

		return "system/hqadminmng/hqadminmng";
	}

}
