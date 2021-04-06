package com.firealarm.admin.biz.system.rolemng.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.firealarm.admin.biz.system.rolemng.service.RoleMngService;
import com.firealarm.admin.common.support.ControllerSupport;

import framework.util.AbleUtil;

/**
 * 권한관리 Controller
 * @author JKS
 *
 */
@Controller
@RequestMapping(value = "/system/rolemng")
@PreAuthorize("hasAuthority('ROLE_SYSTEM_ADMIN')")
public class RoleMngController extends ControllerSupport {

	@Autowired RoleMngService roleMngService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("rolegroupList", AbleUtil.toJson( roleMngService.getRoleGroupList() ));
		return "system/rolemng/rolemng";
	}
}	