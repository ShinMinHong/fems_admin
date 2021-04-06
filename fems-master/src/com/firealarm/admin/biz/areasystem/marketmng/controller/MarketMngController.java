package com.firealarm.admin.biz.areasystem.marketmng.controller;

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

import com.firealarm.admin.biz.areasystem.marketmng.service.MarketMngService;
import com.firealarm.admin.biz.areasystem.marketmng.vo.MarketMngVO;
import com.firealarm.admin.common.service.CommonCodeMapService;
import com.firealarm.admin.common.support.MngAreaControllerSupport;
import com.firealarm.admin.security.util.UserSecurityUtil;

import framework.spring.web.view.AbleExcelCommand;
import framework.spring.web.view.AbleExcelMergeMode;
import framework.spring.web.view.AbleExcelView;
import framework.util.AbleUtil;
import framework.vo.SearchMap;

/**
 * 전통시장 관리 Controller
 * @author JKS
 *
 */
@Controller
@RequestMapping(value = "/areasystem/marketmng")
@PreAuthorize("hasAnyAuthority('ROLE_MARKET_MNG','ROLE_MARKET_READ')")
public class MarketMngController extends MngAreaControllerSupport  {

	@Autowired AbleExcelView ableExcelView;
	@Autowired CommonCodeMapService commonCodeMapService;
	@Autowired MarketMngService marketMngService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model) {
		// 관제지역정보 세션에 존재유부 체크. 없다면 Exception발생(ComponentSupport.validateMngArea)
		validateMngArea();

		/** 관제지역명 */
		   long mngAreaSeq = UserSecurityUtil.getCurrentUserDetails().getMngAreaSeq();
		   model.addAttribute("MNG_AREA_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getMngAreaNameCodeMapByMngAreaSeq(mngAreaSeq)));
		return "areasystem/marketmng/marketmng";
	}

	@RequestMapping(value = "/excel", method = RequestMethod.GET)
	public View excelDownload(
			HttpServletRequest request,
			@SortDefault(sort = "regDate", direction=Direction.DESC) Sort sort,
			@RequestParam(required=false, defaultValue="false") boolean mergeExcelCell,
			Model model) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{

		// 관제지역정보 세션에 존재유부 체크. 없다면 Exception발생(ComponentSupport.validateMngArea)
		validateMngArea();

		SearchMap search = new SearchMap(request);

		//데이터 목록 획득
		List<MarketMngVO> list = marketMngService.getListAll(sort,search);
		//엑셀 출력 명령 준비
		AbleExcelCommand command = new AbleExcelCommand(list, MarketMngVO.class, messageSource);

		String name = "전통시장 관리 정보";
		command.setFilename(name);
		command.setTitle(name);
		command.setSheetName(name);

		if(mergeExcelCell) {
			command.setMergeMode(AbleExcelMergeMode.MERGE_VERTICAL_HIERARCHY);
		}

		model.addAttribute(AbleExcelCommand.MODEL_KEY, command);
	    long mngAreaSeq =UserSecurityUtil.getCurrentUserDetails().getMngAreaSeq();
		command.addCodeMap("mngAreaSeq", AbleUtil.getCodepMapFromSelectOptions(commonCodeMapService.getMngAreaNameCodeMapByMngAreaSeq(mngAreaSeq)));

		return ableExcelView;
	}
}
