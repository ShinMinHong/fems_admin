package com.firealarm.admin.biz.store.storemng.controller;

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
import com.firealarm.admin.appconfig.CodeMap.SEND_YN;
import com.firealarm.admin.biz.store.storemng.service.StoreMngService;
import com.firealarm.admin.biz.store.storemng.vo.StoreMngVO;
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
 * 전통시장 점포관리 Controller
 * @author rodem4_pc1
 *
 */
@Controller
@RequestMapping(value = "/store/storemng")
@PreAuthorize("hasAnyAuthority('ROLE_STORE_MNG', 'ROLE_STORE_READ')")
public class StoreMngController extends MngAreaControllerSupport {

	@Autowired AbleExcelView ableExcelView;
	@Autowired CommonCodeMapService commonCodeMapService;
	@Autowired StoreMngService storeMngService;

	@RequestMapping(value="", method = RequestMethod.GET)
	public String index(Model model, @AppLoginUser AppUserDetails me) {
		// 관제지역정보 세션에 존재유부 체크. 없다면 Exception발생(ComponentSupport.validateMngArea)
		validateMngArea();

		long mngAreaSeq = me.getMngAreaSeq();

		model.addAttribute("MNG_AREA_SEQ", mngAreaSeq);

		/** 전통시장 CodeMap */
		if(APP_USER_GRADE.MARKET_ADMIN.equals(me.getRolegroupCode())) {
			//시장관리자는 자신에게 해당하는 시장만 조회
			long marketSeq = me.getMarketSeq();
			model.addAttribute("MARKET_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getMarketNameCodeMapByMarketSeq(marketSeq)));
		} else {
			model.addAttribute("MARKET_CODE_MAP", AbleUtil.toJson(commonCodeMapService.getMarketNameCodeMapByMngAreaSeq(mngAreaSeq)));
		}

		return "store/storemng/storemng";
	}

	@RequestMapping(value = "/excel", method = RequestMethod.GET)
	public View excelDownload(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@SortDefault(sort = "regDate", direction=Direction.DESC) Sort sort,
			@RequestParam(required=false, defaultValue="false") boolean mergeExcelCell,
			Model model) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{

		// 관제지역정보 세션에 존재유부 체크. 없다면 Exception발생(ComponentSupport.validateMngArea)
		validateMngArea();

		SearchMap search = new SearchMap(request);

		// 시장관리자는 해당 시장만 조회
		if(APP_USER_GRADE.MARKET_ADMIN.equals(user.getRolegroupCode())) {
			search.put("marketSeq", user.getMarketSeq());
		}

		//데이터 목록 획득
		List<StoreMngVO> list = storeMngService.getListAll(sort,search);

		//엑셀 출력 명령 준비
		AbleExcelCommand command = new AbleExcelCommand(list, StoreMngVO.class, messageSource);

		String name = "전통시장 점포 관리 정보";
		command.setFilename(name);
		command.setTitle(name);
		command.setSheetName(name);

		command.addCodeMap("smsAlarmYn", SEND_YN.getCodeMap());
		command.addCodeMap("firestationAlarmYn", SEND_YN.getCodeMap());

		if(mergeExcelCell) {
			command.setMergeMode(AbleExcelMergeMode.MERGE_VERTICAL_HIERARCHY);
		}

		model.addAttribute(AbleExcelCommand.MODEL_KEY, command);

		return ableExcelView;
	}
}