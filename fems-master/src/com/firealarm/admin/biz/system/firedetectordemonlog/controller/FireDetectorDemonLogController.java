package com.firealarm.admin.biz.system.firedetectordemonlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.firealarm.admin.biz.system.firedetectordemonlog.service.FireDetectorDemonLogService;
import com.firealarm.admin.common.support.ControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

/**
 * Demon로그조회 Controller
 * @author ovcoimf
 *
 */
@Controller
@RequestMapping(value = "/system/firedetectordemonlog")
@PreAuthorize("hasAuthority('ROLE_SYSTEM_ADMIN')")
public class FireDetectorDemonLogController extends ControllerSupport {

	@Autowired FireDetectorDemonLogService fireDetectorDemonLogService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model, @AppLoginUser AppUserDetails user) {
		return "system/firedetectordemonlog/firedetectordemonlog";
	}


}