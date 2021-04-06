package com.firealarm.admin.biz.firedetector.nosignaldetector.controller;

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

import com.firealarm.admin.appconfig.CodeMap.APP_USER_GRADE;
import com.firealarm.admin.biz.firedetector.nosignaldetector.service.NoSignalDetectorService;
import com.firealarm.admin.biz.firedetector.nosignaldetector.vo.NoSignalDetectorVO;
import com.firealarm.admin.common.support.ApiControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.spring.web.rest.AbleResponseEntity;
import framework.spring.web.rest.AbleResponseEntityBuilder;
import framework.vo.SearchMap;

/**
 * 통신장애 감지기 조회 Api Controller
 * @author rodem4_pc1
 */
@Controller
@PreAuthorize("hasAnyAuthority('ROLE_FIRE_DETECTOR_MNG','ROLE_FIRE_DETECTOR_READ') AND @fireAlarmCustomSecurityService.hasMngArea()")
@RequestMapping(value = "/firedetector/nosignaldetector/api")
public class NoSignalDetectorApiController extends ApiControllerSupport {

	@Autowired NoSignalDetectorService noSignalDetectorService;

	/** 목록 조회 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public AbleResponseEntity<?> getListAll(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@SortDefault(sort = "lastUpdtDt", direction=Direction.DESC) Sort sort,
			Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);

		// 시장관리자는 해당 시장만 조회
		if(APP_USER_GRADE.MARKET_ADMIN.equals(user.getRolegroupCode())) {
			searchMap.put("marketSeq", user.getMarketSeq());
		}

		List<NoSignalDetectorVO> list = noSignalDetectorService.getListAll(sort, searchMap);
		return AbleResponseEntityBuilder.success(list);
	}

	/** 페이징 조회 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> getList(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@PageableDefault(sort = "lastUpdtDt", direction=Direction.DESC) Pageable pageable,
			Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);

		// 시장관리자는 해당 시장만 조회
		if(APP_USER_GRADE.MARKET_ADMIN.equals(user.getRolegroupCode())) {
			searchMap.put("marketSeq", user.getMarketSeq());
		}

		Page<NoSignalDetectorVO> page = noSignalDetectorService.getList(pageable, searchMap);
		return AbleResponseEntityBuilder.success(page);
	}

}
