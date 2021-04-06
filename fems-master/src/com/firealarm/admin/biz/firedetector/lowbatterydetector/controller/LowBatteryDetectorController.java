package com.firealarm.admin.biz.firedetector.lowbatterydetector.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.firealarm.admin.appconfig.CodeMap.APP_USER_GRADE;
import com.firealarm.admin.appconfig.CodeMap.FIRE_DETECTOR_STATUS;
import com.firealarm.admin.biz.firedetector.lowbatterydetector.service.LowBatteryDetectorService;
import com.firealarm.admin.biz.firedetector.lowbatterydetector.vo.LowBatteryDetectorVO;
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
 * 배터리부족 감지기 조회Controller
 * @author rodem4_pc1
 *
 */

@Controller
@PreAuthorize("hasAnyAuthority('ROLE_FIRE_DETECTOR_MNG', 'ROLE_FIRE_DETECTOR_READ')")
@RequestMapping(value = "/firedetector/lowbatterydetector")
public class LowBatteryDetectorController extends MngAreaControllerSupport {

	@Autowired CommonCodeMapService commonCodeMapService;
	@Autowired LowBatteryDetectorService lowBatteryDetectorService;
	@Autowired AbleExcelView ableExcelView;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model, @AppLoginUser AppUserDetails me) {

		// 관제지역정보 세션에 존재유부 체크. 없다면 Exception발생(ComponentSupport.validateMngArea)
		validateMngArea();

		long mngAreaSeq =me.getMngAreaSeq().longValue();

		/** 전통시장 CodeMap */
		if(APP_USER_GRADE.MARKET_ADMIN.equals(me.getRolegroupCode())) {
			//시장관리자는 자신에게 해당하는 시장만 조회
			long marketSeq = me.getMarketSeq();
			model.addAttribute("MARKET_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getMarketNameCodeMapByMarketSeq(marketSeq)));
		} else {
			model.addAttribute("MARKET_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getMarketNameCodeMapByMngAreaSeq(mngAreaSeq)));
		}

		model.addAttribute("FIRE_DETECTOR_STATUS" , AbleUtil.toJson(FIRE_DETECTOR_STATUS.getCodeMap()));

		return "firedetector/lowbatterydetector/lowbatterydetector";
	}

	@RequestMapping(value = "/excel", method = RequestMethod.GET)
	public View excelDownload(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@RequestParam(required=false, defaultValue="false") boolean mergeExcelCell,
			Model model) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{

		SearchMap search = new SearchMap(request);

		// 시장관리자는 해당 시장만 조회
		if(APP_USER_GRADE.MARKET_ADMIN.equals(user.getRolegroupCode())) {
			search.put("marketSeq", user.getMarketSeq());
		}

		//데이터 목록 획득
		List<LowBatteryDetectorVO> list = lowBatteryDetectorService.getListAll(null, search);

		//엑셀 출력 명령 준비
		AbleExcelCommand command = new AbleExcelCommand(list, LowBatteryDetectorVO.class, messageSource);

		command.setFilename("배터리 부족 감지기 목록");
		command.setTitle("배터리 부족 감지기 목록");
		command.setSheetName("배터리 부족 감지기 목록");

		if(mergeExcelCell) {
			command.setMergeMode(AbleExcelMergeMode.MERGE_VERTICAL_HIERARCHY);
		}

		model.addAttribute(AbleExcelCommand.MODEL_KEY, command);

		return ableExcelView;
	}
}
