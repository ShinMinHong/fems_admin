package com.firealarm.admin.biz.areasystem.loginhistory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.firealarm.admin.biz.areasystem.loginhistory.service.LoginHistoryService;
import com.firealarm.admin.common.service.CommonCodeMapService;
import com.firealarm.admin.common.support.MngAreaControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.spring.web.view.AbleExcelView;
import framework.util.AbleUtil;

/**
 * 접속정보이력관리 Controller
 * @author JKS
 */
@Controller
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN_MNG', 'ROLE_ADMIN_READ')")
@RequestMapping(value = "/areasystem/loginhistory")
public class LoginHistoryController extends MngAreaControllerSupport {

	@Autowired AbleExcelView ableExcelView;
	@Autowired CommonCodeMapService commonCodeMapService;
	@Autowired LoginHistoryService loginHistoryService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model, @AppLoginUser AppUserDetails user) {

		// 관제지역정보 세션에 존재유부 체크. 없다면 Exception발생(ComponentSupport.validateMngArea)
		validateMngArea();

		/** 관리자 구분  */
		model.addAttribute("APP_USER_GRADE_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getAppUserGradeCodeMap()));

		/** 관제지역명 */
		model.addAttribute("MNG_AREA_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getMngAreaNameCodeMap()));

		/** 관제지역 - 시장  코드맵 */
		model.addAttribute("MARKET_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getMarketNameCodeMap()));

		return "areasystem/loginhistory/loginhistory";
	}
}