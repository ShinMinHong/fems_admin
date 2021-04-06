package com.firealarm.admin.biz.firedetector.firedetectoreventlog.controller;

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

import com.firealarm.admin.appconfig.CodeMap.APP_USER_GRADE;
import com.firealarm.admin.appconfig.CodeMap.DETECTOR_SIGNAL_TYPE;
import com.firealarm.admin.appconfig.CodeMap.EVENT_YN;
import com.firealarm.admin.appconfig.CodeMap.NOT_FIRE_YN;
import com.firealarm.admin.biz.firedetector.firedetectoreventlog.service.FireDetectorEventLogService;
import com.firealarm.admin.biz.firedetector.firedetectoreventlog.vo.FireDetectorEventLogVO;
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
 * 화재감지기이벤트 조회 Controller
 * @author JKS
 *
 */

@Controller
@PreAuthorize("hasAnyAuthority('ROLE_FIRE_DETECTOR_MNG','ROLE_FIRE_DETECTOR_READ')")
@RequestMapping(value = "/firedetector/firedetectoreventlog")
public class FireDetectorEventLogController extends MngAreaControllerSupport {

	@Autowired CommonCodeMapService commonCodeMapService;
	@Autowired AbleExcelView ableExcelView;
	@Autowired FireDetectorEventLogService fireDetectorEventLogService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model, @AppLoginUser AppUserDetails me) {

		validateMngArea();

		long mngAreaSeq =me.getMngAreaSeq().longValue();

		/** 관제지역명 */
		model.addAttribute("MNG_AREA_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getMngAreaNameCodeMapByMngAreaSeq(mngAreaSeq)));

		/** 전통시장 CodeMap */
		if(APP_USER_GRADE.MARKET_ADMIN.equals(me.getRolegroupCode())) {
			//시장관리자는 자신에게 해당하는 시장만 조회
			long marketSeq = me.getMarketSeq();
			model.addAttribute("MARKET_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getMarketNameCodeMapByMarketSeq(marketSeq)));
		} else {
			model.addAttribute("MARKET_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getMarketNameCodeMapByMngAreaSeq(mngAreaSeq)));
		}

		model.addAttribute("DETECTOR_SIGNAL_TYPE", AbleUtil.toJson(DETECTOR_SIGNAL_TYPE.getCodeMap()));

		return "firedetector/firedetectoreventlog/firedetectoreventlog";
	}

	@RequestMapping(value = "/excel", method = RequestMethod.GET)
	public View excelDownload(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@SortDefault(sort = "authorgroupCode", direction=Direction.ASC) Sort sort,
			@RequestParam(required=false, defaultValue="false") boolean mergeExcelCell,
			Model model, @AppLoginUser AppUserDetails me) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{

		validateMngArea();

		SearchMap search = new SearchMap(request);

		// 시장관리자는 해당 시장만 조회
		if(APP_USER_GRADE.MARKET_ADMIN.equals(user.getRolegroupCode())) {
			search.put("marketSeq", user.getMarketSeq());
		}

		/* 데이터 목록 획득 */
		List<FireDetectorEventLogVO> list = fireDetectorEventLogService.getListAll(sort, search);

		/* 엑셀 출력 명령 준비 */
		AbleExcelCommand command = new AbleExcelCommand(list, FireDetectorEventLogVO.class, messageSource);

		Long mngAreaSeq =me.getMngAreaSeq();

		command.addCodeMap("mngAreaSeq",AbleUtil.getCodepMapFromSelectOptions(commonCodeMapService.getMngAreaNameCodeMap()));
		/** 전통시장 CodeMap */
		if(APP_USER_GRADE.MARKET_ADMIN.equals(me.getRolegroupCode())) {
			long marketSeq = me.getMarketSeq();
			command.addCodeMap("marketSeq",AbleUtil.getCodepMapFromSelectOptions(commonCodeMapService.getMarketNameCodeMapByMarketSeq(marketSeq)));
		}else {
			command.addCodeMap("marketSeq", AbleUtil.getCodepMapFromSelectOptions(commonCodeMapService.getMarketNameCodeMapByMngAreaSeq(mngAreaSeq)));
		}

		command.addCodeMap("signalType",DETECTOR_SIGNAL_TYPE.getCodeMap());
        command.addCodeMap("smokeEvent", EVENT_YN.getCodeMap());
        command.addCodeMap("temperatureEvent",EVENT_YN.getCodeMap());
        command.addCodeMap("flameEvent",EVENT_YN.getCodeMap());
        command.addCodeMap("coEvent", EVENT_YN.getCodeMap());
        command.addCodeMap("notFireYn",NOT_FIRE_YN.getCodeMap());
		command.setFilename("화재감지기 이벤트 조회");
		command.setTitle("화재감지기 이벤트 조회");
		command.setSheetName("화재감지기 이벤트 조회");

		if(mergeExcelCell) {
			command.setMergeMode(AbleExcelMergeMode.MERGE_VERTICAL_HIERARCHY);
		}

		model.addAttribute(AbleExcelCommand.MODEL_KEY, command);

		return ableExcelView;
	}

}
