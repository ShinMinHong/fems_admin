package com.firealarm.admin.biz.areasystem.adminaudit.controller;

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

import com.firealarm.admin.common.service.CommonCodeMapService;
import com.firealarm.admin.common.support.ControllerSupport;
import com.firealarm.admin.common.vo.ManagerAuditDT;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.spring.web.view.AbleExcelCommand;
import framework.spring.web.view.AbleExcelMergeMode;
import framework.spring.web.view.AbleExcelView;
import framework.vo.SearchMap;

/**
 * 관리자 감사로그 Controller
 * @author rodem4
 *
 */
@Controller
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN_MNG','ROLE_ADMIN_READ') AND @fireAlarmCustomSecurityService.hasMngArea()")
@RequestMapping(value = "/areasystem/adminaudit")
public class AdminAuditController extends ControllerSupport {

	@Autowired AbleExcelView ableExcelView;
	@Autowired CommonCodeMapService commonCodeMapService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model, @AppLoginUser AppUserDetails me) {

		// 관제지역정보 세션에 존재유부 체크. 없다면 Exception발생(ComponentSupport.validateMngArea)
		validateMngArea();

		return "areasystem/adminaudit/adminaudit";
	}

	@RequestMapping(value = "/excel", method = RequestMethod.GET)
	public View excelDownload(
			HttpServletRequest request,
			@SortDefault(sort = "regDate", direction=Direction.DESC) Sort sort,
			@RequestParam(required=false, defaultValue="false") boolean mergeExcelCell,
			Model model) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{

		SearchMap search = new SearchMap(request);

		//데이터 목록 획득
		List<ManagerAuditDT> list = managerAuditService.getListAll(sort, search);

		//엑셀 출력 명령 준비
		AbleExcelCommand command = new AbleExcelCommand(list, ManagerAuditDT.class, messageSource);

		command.setFilename("시스템관리 - 관리자감사로그");
		command.setTitle("관리자감사로그");
		command.setSheetName("관리자감사로그");

		if(mergeExcelCell) {
			command.setMergeMode(AbleExcelMergeMode.MERGE_VERTICAL_HIERARCHY);
		}

		model.addAttribute(AbleExcelCommand.MODEL_KEY, command);

		return ableExcelView;
	}

}