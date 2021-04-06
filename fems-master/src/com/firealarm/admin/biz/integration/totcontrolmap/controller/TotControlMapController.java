package com.firealarm.admin.biz.integration.totcontrolmap.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.firealarm.admin.appconfig.CodeMap.MAP_USER_TYPE;
import com.firealarm.admin.biz.home.vo.MapFireDetectorVO;
import com.firealarm.admin.biz.home.vo.MapInitConfVO;
import com.firealarm.admin.biz.home.vo.MapMarketVO;
import com.firealarm.admin.biz.integration.totcontrolmap.service.TotControlMapService;
import com.firealarm.admin.common.service.CommonCodeMapService;
import com.firealarm.admin.common.service.FileStorageManager;
import com.firealarm.admin.common.support.ControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.util.AbleUtil;

/**
 * Home Controller
 * @author ovcoimf
 */
@Controller
@PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_ADMIN')")
@RequestMapping(value = "/integration/totcontrolmap")
public class TotControlMapController extends ControllerSupport {

	@Autowired TotControlMapService totControlMapService;
	@Autowired CommonCodeMapService commonCodeMapService;
	@Autowired FileStorageManager fileStorageManager;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request, @AppLoginUser AppUserDetails user, Model model) {
		ModelAndView mv = new ModelAndView();

		//지도중심참고 URL. https://ko.wikipedia.org/wiki/%ED%8B%80:%EC%9C%84%EC%B9%98_%EC%A7%80%EB%8F%84_%EB%8C%80%ED%95%9C%EB%AF%BC%EA%B5%AD
		MapInitConfVO mapInitConf = new MapInitConfVO();
		mapInitConf.setMapUserType(MAP_USER_TYPE.HQ_ADMIN);
		mapInitConf.setLatitude("35.95");
		mapInitConf.setLongitude("128.25");
		mapInitConf.setScale(12);

		List<MapMarketVO> mapMarketList = totControlMapService.getAllMapMarketInfoList();

		List<MapFireDetectorVO> mapFireDetectorList = totControlMapService.getAllMapFireDetectorList();

		model.addAttribute("MAP_INIT_CONF", AbleUtil.toJson(mapInitConf));	//관제지도 최초 설정
		model.addAttribute("MAP_MARKET_LIST", AbleUtil.toJson(mapMarketList)); //지도에 노출할 시장 목록
		model.addAttribute("MAP_FIRE_DETECTOR_LIST", AbleUtil.toJson(mapFireDetectorList)); //지도에 노출할 단말기 목록

		model.addAttribute("NAS_URL", fileStorageManager.getUploadRootUrl());

		mv.setViewName("integration/totcontrolmap/totcontrolmap");

		return mv;
	}
}
