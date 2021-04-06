package com.firealarm.admin.biz.system.rolegroupmng.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.firealarm.admin.common.support.ControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;
import com.firealarm.admin.biz.system.rolegroupmng.service.RoleGroupMngService;

import framework.util.AbleUtil;

/**
 * 권한그룹관리 Controller
 * @author JKS
 *
 */
@Controller
@RequestMapping(value = "/system/rolegroupmng")
@PreAuthorize("hasAuthority('ROLE_SYSTEM_ADMIN')")
public class RoleGroupMngController extends ControllerSupport {

	@Autowired RoleGroupMngService roleGroupMngService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model, @AppLoginUser AppUserDetails admin) {
		model.addAttribute("roleList", AbleUtil.toJson( roleGroupMngService.getRoleList() ));
		return "system/rolegroupmng/rolegroupmng";
	}

}
