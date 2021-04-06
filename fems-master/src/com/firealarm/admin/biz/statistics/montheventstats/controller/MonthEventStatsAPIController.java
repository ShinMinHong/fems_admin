package com.firealarm.admin.biz.statistics.montheventstats.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.firealarm.admin.appconfig.CodeMap.APP_USER_GRADE;
import com.firealarm.admin.biz.statistics.montheventstats.service.MonthEventStatsService;
import com.firealarm.admin.biz.statistics.montheventstats.vo.MonthEventStatsVO;
import com.firealarm.admin.common.support.ApiControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.spring.web.rest.AbleResponseEntity;
import framework.spring.web.rest.AbleResponseEntityBuilder;
import framework.vo.SearchMap;

/**
 *  월별 화재 신호 통계 APIController
 * @author rodem4_pc1
 *
 */
@Controller
@RequestMapping(value = "/statistics/montheventstats/api")
@PreAuthorize("hasAuthority('ROLE_STATISTICS_READ')")
public class MonthEventStatsAPIController extends ApiControllerSupport {

	@Autowired MonthEventStatsService monthEventStatsService;

	/** 목록 조회 */
	@RequestMapping(value = "/montheventstatslist", method = RequestMethod.GET)
	public AbleResponseEntity<?> getMonthEventStatsListAll(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@SortDefault(sort = "regDate", direction = Direction.DESC) Sort sort) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		
		// 시장관리자는 해당 시장만 조회 
		if(APP_USER_GRADE.MARKET_ADMIN.equals(user.getRolegroupCode())) {
			searchMap.put("marketSeq", user.getMarketSeq());
		}	
		
		List<MonthEventStatsVO> list = monthEventStatsService.getMonthEventStatsListAll(sort, searchMap);
		return AbleResponseEntityBuilder.success(list);
	}

}