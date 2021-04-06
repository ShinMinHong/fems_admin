package com.firealarm.admin.biz.home.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.firealarm.admin.appconfig.CodeMap.APP_USER_GRADE;
import com.firealarm.admin.biz.home.service.HomeService;
import com.firealarm.admin.biz.home.vo.MapFireDetectorVO;
import com.firealarm.admin.biz.home.vo.MapInitConfVO;
import com.firealarm.admin.biz.home.vo.MapMarketVO;
import com.firealarm.admin.common.service.CommonCodeMapService;
import com.firealarm.admin.common.service.FileStorageManager;
import com.firealarm.admin.common.support.MngAreaControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.util.AbleUtil;

/**
 * Home Controller
 * @author ovcoimf
 */
@Controller
@PreAuthorize("isAuthenticated()")
public class HomeController extends MngAreaControllerSupport {

	@Autowired HomeService homeService;
	@Autowired CommonCodeMapService commonCodeMapService;
	@Autowired FileStorageManager fileStorageManager;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request, @AppLoginUser AppUserDetails user, Model model) {

		ModelAndView mv = new ModelAndView();
		if(APP_USER_GRADE.HQ_ADMIN.equals(user.getRolegroupCode()) && !user.hasMngArea()) {
			mv.setViewName("redirect:system/mngareamng");
		} else if(user.hasMngArea()) {
			boolean marketAdmin = APP_USER_GRADE.MARKET_ADMIN.equals(user.getRolegroupCode());
			MapInitConfVO mapInitConf;
			if(marketAdmin) {
				mapInitConf = homeService.getMarketMapInitInfo(user.getMngAreaSeq(), user.getMarketSeq());
			} else {
				mapInitConf = homeService.getMngAreaMapInitInfo(user.getMngAreaSeq());
			}

			List<MapMarketVO> mapMarketList = homeService.getMapMarketInfoList(user.getMngAreaSeq(),
					(marketAdmin ? user.getMarketSeq() : null));

			List<MapFireDetectorVO> mapFireDetectorList = homeService.getMapFireDetectorList(user.getMngAreaSeq(),
					(marketAdmin ? user.getMarketSeq() : null));

			model.addAttribute("MAP_INIT_CONF", AbleUtil.toJson(mapInitConf));	//관제지도 최초 설정
			model.addAttribute("MAP_MARKET_LIST", AbleUtil.toJson(mapMarketList)); //지도에 노출할 시장 목록
			model.addAttribute("MAP_FIRE_DETECTOR_LIST", AbleUtil.toJson(mapFireDetectorList)); //지도에 노출할 단말기 목록

			model.addAttribute("NAS_URL", fileStorageManager.getUploadRootUrl());

			mv.setViewName("home/home");
		}
		return mv;
	}

	@RequestMapping(value = "/home/openchangeuserinfo", method = RequestMethod.GET)
	public String openchangeuserinfo(Model model, @AppLoginUser AppUserDetails user) {

		/** 관리자 Seq */
		model.addAttribute("ADMIN_SEQ", AbleUtil.toJson(user.getAdminSeq()));

		// 권한
		model.addAttribute("ROLEGROUP_CODE", AbleUtil.toJson(user.getRolegroupCode()));

		/** 관리자 구분  */
		model.addAttribute("APP_USER_GRADE", AbleUtil.toJson(commonCodeMapService.getAppUserGradeCodeMap()));

		/** 관제지역명 */
		model.addAttribute("MNG_AREA_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getMngAreaNameCodeMap()));

		/** 관제지역 - 시장  코드맵 */
		model.addAttribute("MARKET_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getMarketNameCodeMap()));

		return "home/changeuserinfo/openchangeuserinfo";
	}
}
