package com.firealarm.admin.biz.statistics.areaeventstats.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.firealarm.admin.appconfig.CodeMap.APP_USER_GRADE;
import com.firealarm.admin.biz.statistics.areaeventstats.service.AreaEventStatsService;
import com.firealarm.admin.biz.statistics.areaeventstats.vo.AreaEventStatsVO;
import com.firealarm.admin.common.support.ApiControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.exception.AbleRuntimeException;
import framework.spring.web.rest.AbleResponseEntity;
import framework.spring.web.rest.AbleResponseEntityBuilder;
import framework.spring.web.rest.AbleResponseEntityMap;
import framework.vo.SearchMap;

/**
 *  지역별 화재 신호 통계 APIController
 * @author rodem4_pc1
 *
 */
@Controller
@RequestMapping(value = "/statistics/areaeventstats/api")
@PreAuthorize("hasAuthority('ROLE_STATISTICS_READ') AND @fireAlarmCustomSecurityService.hasMngArea()")
public class AreaEventStatsAPIController extends ApiControllerSupport {

	@Autowired AreaEventStatsService areaEventStatsService;

	/** 목록 조회 */
	@RequestMapping(value = "/areaeventstatslist", method = RequestMethod.GET)
	public AbleResponseEntity<?> getAreaEventStatsListAll(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@SortDefault(sort = "regDate", direction = Direction.DESC) Sort sort) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		
		// 시장관리자는 해당 시장만 조회 
		if(APP_USER_GRADE.MARKET_ADMIN.equals(user.getRolegroupCode())) {
			searchMap.put("marketSeq", user.getMarketSeq());
		}
		
		List<AreaEventStatsVO> list = areaEventStatsService.getAreaEventStatsListAll(sort, searchMap);
		return AbleResponseEntityBuilder.success(list);
	}

	/** 페이징 조회 */
	@RequestMapping(value = "/areaeventstatspage", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> getList(HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@PageableDefault(sort = "regDate", direction = Direction.DESC) Pageable pageable) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		
		// 시장관리자는 해당 시장만 조회 
		if(APP_USER_GRADE.MARKET_ADMIN.equals(user.getRolegroupCode())) {
			searchMap.put("marketSeq", user.getMarketSeq());
		}
		
		Page<AreaEventStatsVO> page = areaEventStatsService.getAreaEventStatsList(pageable, searchMap);
		return AbleResponseEntityBuilder.success(page);
	}

	/** 상세 */
	@RequestMapping(value = "/details/{marketSeq}/{searchStartDate}/{searchEndDate}", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> details(
			@PathVariable("marketSeq") long marketSeq,
			@PathVariable("searchStartDate") String searchStartDate,
			@PathVariable("searchEndDate") String searchEndDate) {

		AreaEventStatsVO ro = areaEventStatsService.getDetailsPageByMarketSeq(marketSeq,searchStartDate,searchEndDate);

		if (ro == null) {
			throw new AbleRuntimeException("상세정보를 찾을 수 없습니다.");
		}

		return AbleResponseEntityMap.success(ro);
	}

}