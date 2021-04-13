package com.firealarm.admin.biz.areasystem.firedetectorset.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.firealarm.admin.appconfig.AppConst;
import com.firealarm.admin.appconfig.CodeMap.APP_USER_GRADE;
import com.firealarm.admin.appconfig.CodeMap.FIRE_DETECTOR_ACK_VALUE;
import com.firealarm.admin.appconfig.CodeMap.FIRE_DETECTOR_SORT_TYPE;
import com.firealarm.admin.appconfig.CodeMap.FIRE_DETECTOR_SET_TYPE;
import com.firealarm.admin.biz.areasystem.firedetectorset.service.FireDetectorSetService;
import com.firealarm.admin.biz.areasystem.firedetectorset.vo.FireDetectorSetVO;
import com.firealarm.admin.common.service.CommonCodeMapService;
import com.firealarm.admin.common.support.MngAreaControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.spring.web.view.AbleExcelCommand;
import framework.spring.web.view.AbleExcelMergeMode;
import framework.spring.web.view.AbleExcelView;
import framework.util.AbleUtil;
import framework.vo.SearchMap;

/**
 * 화재감지기 관리 Controller
 * @author jks
 *
 */
@Controller
@PreAuthorize("hasAnyAuthority('ROLE_FIRE_DETECTOR_MNG','ROLE_FIRE_DETECTOR_READ')")
@RequestMapping(value = "/areasystem/firedetectorset")
public class FireDetectorSetController extends MngAreaControllerSupport {

	@Autowired FireDetectorSetService fireDetectorSetService;
	@Autowired CommonCodeMapService commonCodeMapService;
	@Autowired AbleExcelView ableExcelView;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model, @AppLoginUser AppUserDetails me) {
		// 관제지역정보 세션에 존재유부 체크. 없다면 Exception발생(ComponentSupport.validateMngArea)
		validateMngArea();
		long mngAreaSeq =me.getMngAreaSeq().longValue();

		/** 관제지역명 */
		model.addAttribute("AREA_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getMngAreaNameCodeMapByMngAreaSeq(mngAreaSeq)));

		/** 전통시장 CodeMap */
		if(APP_USER_GRADE.MARKET_ADMIN.equals(me.getRolegroupCode())) {
			//시장관리자는 자신에게 해당하는 시장만 조회
			long marketSeq = me.getMarketSeq();
			model.addAttribute("MARKET_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getMarketNameCodeMapByMarketSeq(marketSeq)));
		} else {
			model.addAttribute("MARKET_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getMarketNameCodeMapByMngAreaSeq(mngAreaSeq)));
		}

		/** 감지기 설정 상태 */
		model.addAttribute("FIRE_DETECTOR_SET_TYPE" , AbleUtil.toJson(FIRE_DETECTOR_SET_TYPE.getCodeMap()));

		return "areasystem/firedetectorset/firedetectorset";
	}
}