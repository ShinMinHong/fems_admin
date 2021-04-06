package com.firealarm.admin.biz.system.firedetectordemonlog.controller;

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

import com.firealarm.admin.biz.system.firedetectordemonlog.service.FireDetectorDemonLogService;
import com.firealarm.admin.biz.system.firedetectordemonlog.vo.FireDetectorDemonLogVO;
import com.firealarm.admin.common.support.ApiControllerSupport;

import framework.spring.web.rest.AbleResponseEntity;
import framework.spring.web.rest.AbleResponseEntityBuilder;
import framework.vo.SearchMap;

/**
 * Demon Log API Controller
 * @author ovcoimf
 */
@Controller
@RequestMapping(value = "/system/firedetectordemonlog/api")
@PreAuthorize("hasAuthority('ROLE_SYSTEM_ADMIN')")
public class FireDetectorDemonLogApiController extends ApiControllerSupport{

	@Autowired FireDetectorDemonLogService fireDetectorDemonLogService;

	/** 목록 조회 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public AbleResponseEntity<?> getListAll(
			HttpServletRequest request,
			@SortDefault(sort = "fireDetectorDemonSignalSeq", direction = Direction.DESC) Sort sort, Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		List<FireDetectorDemonLogVO> list = fireDetectorDemonLogService.getListAll(sort, searchMap);
		return AbleResponseEntityBuilder.success(list);
	}

	/** 페이징 조회 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> getList(HttpServletRequest request,
			@PageableDefault(sort = "fireDetectorDemonSignalSeq", direction = Direction.DESC) Pageable pageable, Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		Page<FireDetectorDemonLogVO> page = fireDetectorDemonLogService.getList(pageable, searchMap);
		return AbleResponseEntityBuilder.success(page);
	}
}