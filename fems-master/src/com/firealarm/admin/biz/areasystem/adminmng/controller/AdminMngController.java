package com.firealarm.admin.biz.areasystem.adminmng.controller;

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

import framework.util.AbleUtil;

/**
 * 관리자 관리 Controller
 * @author jks
 *
 */
@Controller
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN_MNG','ROLE_ADMIN_READ')")
@RequestMapping(value = "/areasystem/adminmng")
public class AdminMngController extends MngAreaControllerSupport {

	@Autowired CommonCodeMapService commonCodeMapService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model, @AppLoginUser AppUserDetails me) {

		validateMngArea();

		long mngAreaSeq = me.getMngAreaSeq();

		/** 지역 Seq */
		model.addAttribute("MNG_AREA_SEQ", mngAreaSeq);

		/** 관리자의 접근가능한 관리자구분  */
		model.addAttribute("USER_GRADE_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getUserGradeCodeMap()));

		/** 관제지역명 */
		model.addAttribute("MNG_AREA_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getMngAreaNameCodeMapByMngAreaSeq(mngAreaSeq)));

		/** 관제지역 - 시장  코드맵 */
		model.addAttribute("MARKET_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getMarketNameCodeMapByMngAreaSeq(mngAreaSeq)));

		return "areasystem/adminmng/adminmng";
	}

}
