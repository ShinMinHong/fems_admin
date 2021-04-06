package com.firealarm.admin.biz.areasystem.msgsendlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.firealarm.admin.common.service.CommonCodeMapService;
import com.firealarm.admin.common.support.MngAreaControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

/**
 *  문자메시지 발신 관리 Controller
 * @author JKS
 */
@Controller
@RequestMapping(value = "/areasystem/msgsendlog")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN_MNG', 'ROLE_ADMIN_READ')")
public class MsgSendLogController extends MngAreaControllerSupport{

	@Autowired CommonCodeMapService commonCodeMapService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model, @AppLoginUser AppUserDetails user) {

		// 관제지역정보 세션에 존재유부 체크. 없다면 Exception발생(ComponentSupport.validateMngArea)
		validateMngArea();

		return "areasystem/msgsendlog/msgsendlog";
	}

}
