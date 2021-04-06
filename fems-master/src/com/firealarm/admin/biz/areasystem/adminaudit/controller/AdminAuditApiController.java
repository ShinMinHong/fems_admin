package com.firealarm.admin.biz.areasystem.adminaudit.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.firealarm.admin.common.service.ManagerAuditService;
import com.firealarm.admin.common.support.ApiControllerSupport;
import com.firealarm.admin.common.vo.ManagerAuditDT;

import framework.spring.web.rest.AbleResponseEntity;
import framework.spring.web.rest.AbleResponseEntityBuilder;
import framework.vo.SearchMap;


/**
 * 관리자 감사로그 Controller
 * @author rodem4
 *
 */
@Controller
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN_MNG','ROLE_ADMIN_READ') AND @fireAlarmCustomSecurityService.hasMngArea()")
@RequestMapping(value = "/areasystem/adminaudit/api")
public class AdminAuditApiController extends ApiControllerSupport {

	@Autowired ManagerAuditService managerAuditService;

	/** 목록 조회 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public AbleResponseEntity<?> getListAll(
			HttpServletRequest request,
			@SortDefault(sort = "auditSeq", direction=Direction.DESC) Sort sort,
			Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);

		List<ManagerAuditDT> list = managerAuditService.getListAll(sort, searchMap);
		return AbleResponseEntityBuilder.success(list);
	}

	/** 페이징 조회 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> getList(
			HttpServletRequest request,
			@PageableDefault(sort = "auditSeq", direction=Direction.DESC) Pageable pageable,
			@RequestParam(required=false, defaultValue="true")Boolean includeAuthorGroup,
			Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);

		Page<ManagerAuditDT> page = managerAuditService.getList(pageable, searchMap);
		return AbleResponseEntityBuilder.success(page);
	}
}
