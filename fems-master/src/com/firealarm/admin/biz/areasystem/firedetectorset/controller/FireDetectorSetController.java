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
import com.firealarm.admin.appconfig.CodeMap.FIRE_DETECTOR_STATUS;
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

		model.addAttribute("FIRE_DETECTOR_STATUS" , AbleUtil.toJson(FIRE_DETECTOR_STATUS.getCodeMap()));
		model.addAttribute("FIRE_DETECTOR_ACK_VALUE" , AbleUtil.toJson(FIRE_DETECTOR_ACK_VALUE.getCodeMap()));
		model.addAttribute("FIRE_DETECTOR_SORT_TYPE" , AbleUtil.toJson(FIRE_DETECTOR_SORT_TYPE.getCodeMap()));
		/** 파일 설정 */
		model.addAttribute("FILE_MAX_COUNT" , AppConst.ALLOWED_BOARDFILE_COUNT);
		model.addAttribute("FILE_UPLOAD_MAX_SIZE" , AppConst.ALLOWED_BOARDFILE_SIZE);
		model.addAttribute("FILE_DISPLAY_UPLOAD_MAX_SIZE" , AppConst.ALLOWED_BOARDFILE_DISPLAY_SIZE);
		model.addAttribute("ALLOWED_EXTENSION" , AppConst.ALLOWED_EXTENSION_FOR_IMAGE);

		return "areasystem/firedetectorset/firedetectormng";
	}

	@RequestMapping(value = "/excel", method = RequestMethod.GET)
	public View excelDownload(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@SortDefault(sort = "authorgroupCode", direction=Direction.ASC) Sort sort,
			@RequestParam(required=false, defaultValue="false") boolean mergeExcelCell,
			Model model, @AppLoginUser AppUserDetails admin) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{

		// 관제지역정보 세션에 존재유부 체크. 없다면 Exception발생(ComponentSupport.validateMngArea)
		validateMngArea();
		SearchMap search = new SearchMap(request);

		// 시장관리자는 해당 시장만 조회
		if(APP_USER_GRADE.MARKET_ADMIN.equals(user.getRolegroupCode())) {
			search.put("marketSeq", user.getMarketSeq());
		}

		List<FireDetectorSetVO> list = fireDetectorSetService.getListAll(sort, search);
		AbleExcelCommand command = new AbleExcelCommand(list, FireDetectorSetVO.class, messageSource);

		command.setFilename("화재감지기 관리");
		command.setTitle("화재감지기 관리");
		command.setSheetName("화재감지기 관리");

		if(mergeExcelCell) {
			command.setMergeMode(AbleExcelMergeMode.MERGE_VERTICAL_HIERARCHY);
		}
		command.addCodeMap("fireDetectorStatus", FIRE_DETECTOR_STATUS.getCodeMap());

		model.addAttribute(AbleExcelCommand.MODEL_KEY, command);
		return ableExcelView;
	}
}