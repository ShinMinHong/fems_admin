package com.firealarm.admin.biz.system.hqsmsadmin.controller;

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

/**
 * 관리자 관리 Controller
 * @author SHH
 *
 */
@Controller
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN_MNG')")
@RequestMapping(value = "/system/hqsmsadminmng")
public class HqSmsAdminMngController extends ControllerSupport {

	@Autowired CommonCodeMapService commonCodeMapService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model, @AppLoginUser AppUserDetails me) {

		return "system/hqsmsadminmng/hqsmsadminmng";
	}

}
