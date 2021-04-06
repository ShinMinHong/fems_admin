package com.firealarm.admin.biz.areasystem.msgsendlog.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.firealarm.admin.biz.areasystem.msgsendlog.service.MsgSendLogService;
import com.firealarm.admin.biz.areasystem.msgsendlog.vo.MsgSendLogVO;
import com.firealarm.admin.common.support.ApiControllerSupport;

import framework.spring.web.rest.AbleResponseEntity;
import framework.spring.web.rest.AbleResponseEntityBuilder;
import framework.vo.SearchMap;

/**
 *  문자메시지 발신 관리 API Controller
 * @author JKS
 */
@Controller
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN_MNG', 'ROLE_ADMIN_READ') AND @fireAlarmCustomSecurityService.hasMngArea()")
@RequestMapping(value = "/areasystem/msgsendlog/api")
public class MsgSendLogAPIController extends ApiControllerSupport {

	@Autowired MsgSendLogService msgSendLogService;

	/** 목록 조회 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public AbleResponseEntity<?> getListAll(
			HttpServletRequest request,
			@SortDefault(sort = "sendDate", direction=Direction.DESC) Sort sort,
			Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		List<MsgSendLogVO> list = msgSendLogService.getListAll(sort, searchMap);
		return AbleResponseEntityBuilder.success(list);
	}

	/** 페이징 조회 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> getList(
			HttpServletRequest request,
			@PageableDefault(sort = "sendDate", direction=Direction.DESC) Pageable pageable,
			Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		Page<MsgSendLogVO> page = msgSendLogService.getList(pageable, searchMap);
		return AbleResponseEntityBuilder.success(page);
	}
}
