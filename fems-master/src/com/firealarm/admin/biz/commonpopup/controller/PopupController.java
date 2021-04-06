package com.firealarm.admin.biz.commonpopup.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.firealarm.admin.biz.commonpopup.service.PopupService;
import com.firealarm.admin.common.service.CommonCodeMapService;
import com.firealarm.admin.common.support.ControllerSupport;
import com.firealarm.admin.common.vo.SelectOption;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.util.AbleUtil;

/**
 * 공통팝업 Controller
 * @author KDH
 *
 */
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/commonpopup")
public class PopupController extends ControllerSupport {

	@Autowired CommonCodeMapService commonCodeMapService;
	@Autowired PopupService popupService;


	/** 슈퍼관리자 관제지역 선택 팝업 */
	@PreAuthorize("hasAuthority('ROLE_CONTROLMAP')")
	@RequestMapping(value = "/openselectmngarea", method = RequestMethod.GET)
	public String openSelectMngArea(Model model, @AppLoginUser AppUserDetails user){
		return "commonpopup/openselectmngarea";
	}

	/** 지도 좌표 선택 팝업 */
	@RequestMapping(value = "/openselectlocation", method = RequestMethod.GET)
	public String openselectlocation(Model model, @AppLoginUser AppUserDetails user){
		return "commonpopup/openselectlocation";
	}

	/** 점포 선택 팝업 */
	@RequestMapping(value = "/openselectstore", method = RequestMethod.GET)
	public String openselectionstore(Model model, @AppLoginUser AppUserDetails user){

		if(user.getMngAreaSeq() != null) {
			long mngAreaSeq = user.getMngAreaSeq().longValue();
			model.addAttribute("MARKET_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getMarketNameCodeMapByMngAreaSeq(mngAreaSeq)));
			model.addAttribute("MNG_AREA_SEQ", mngAreaSeq);
		} else {
			model.addAttribute("MARKET_CODE_MAP", AbleUtil.toJson(new ArrayList<SelectOption>()));
			model.addAttribute("MNG_AREA_SEQ", "");
		}


		return "commonpopup/openselectstore";
	}

	/** 화재감지기 정보 엑셀 업로드 */
	@PreAuthorize("hasAuthority('ROLE_FIRE_DETECTOR_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/openfiredetectorexcelupload", method = RequestMethod.GET)
	public String openfiredetectorexcelupload(Model model){
		return "commonpopup/openfiredetectorexcelupload";
	}

	/** 점포 SMS 수신대상관리 엑셀 업로드 */
	@PreAuthorize("hasAuthority('ROLE_STORE_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/openstoresmsuserexcelupload", method = RequestMethod.GET)
	public String openstoresmsuserexcelupload(Model model){
		return "commonpopup/openstoresmsuserexcelupload";
	}

	/** 점포관리 정보 액셀 업로드*/
	@PreAuthorize("hasAuthority('ROLE_STORE_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/openstoreexcelupload", method = RequestMethod.GET)
	public String openstoreexcelupload(Model model){
		return "commonpopup/openstoreexcelupload";
	}

}
