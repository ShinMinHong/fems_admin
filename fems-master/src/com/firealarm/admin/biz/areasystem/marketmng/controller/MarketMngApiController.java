package com.firealarm.admin.biz.areasystem.marketmng.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.firealarm.admin.biz.areasystem.marketmng.service.MarketMngService;
import com.firealarm.admin.biz.areasystem.marketmng.vo.MarketMngActiveVO;
import com.firealarm.admin.biz.areasystem.marketmng.vo.MarketMngVO;
import com.firealarm.admin.biz.store.storemng.service.StoreMngService;
import com.firealarm.admin.biz.store.storemng.vo.StoreMngVO;
import com.firealarm.admin.common.support.ApiControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.exception.AbleRuntimeException;
import framework.exception.AbleValidationException;
import framework.spring.web.rest.AbleResponseEntity;
import framework.spring.web.rest.AbleResponseEntityBuilder;
import framework.vo.SearchMap;

/**
 * 전통시장 관리 APIController
 * @author JKS
 *
 */

@Controller
@PreAuthorize("(hasAnyAuthority('ROLE_MARKET_READ','ROLE_MARKET_MNG')) AND @fireAlarmCustomSecurityService.hasMngArea()")
@RequestMapping(value = "/areasystem/marketmng/api")
public class MarketMngApiController extends ApiControllerSupport {

	@Autowired MarketMngService marketMngService;
	@Autowired StoreMngService storeMngService;

	/** 목록 조회 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public AbleResponseEntity<?> getListAll(
			HttpServletRequest request,
			@SortDefault(sort = "regDate", direction=Direction.DESC) Sort sort,
			Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		List<MarketMngVO> list = marketMngService.getListAll(sort, searchMap);
		return AbleResponseEntityBuilder.success(list);
	}

	/** 페이징 조회 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> getList(
			HttpServletRequest request,
			@PageableDefault(sort = "regDate", direction=Direction.DESC) Pageable pageable,
			Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		Page<MarketMngVO> page = marketMngService.getList(pageable, searchMap);
		return AbleResponseEntityBuilder.success(page);
	}

	/** 상세 */
	@RequestMapping(value = "/details/{marketSeq}", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> details(
			@PathVariable("marketSeq") long marketSeq,
			Model model) {
		MarketMngVO ro = marketMngService.getByMarketSeq(marketSeq);
		return AbleResponseEntityBuilder.success(ro);
	}

	/** 등록 */
	@PreAuthorize("hasAuthority('ROLE_MARKET_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/insert", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> insert(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@RequestBody @Validated({MarketMngActiveVO.CreateAction.class}) MarketMngActiveVO actionVO, BindingResult error) {
		logger.debug(" insert MarketMngActiveVO start: {}", actionVO);
		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}

		if(actionVO.getMngAreaSeq() != user.getMngAreaSeq().longValue()) {
			throw new AbleRuntimeException("데이터를 작성 하는중, 관제지역이 변경되었습니다.");
		}

		marketMngService.insert(actionVO);

		return AbleResponseEntityBuilder.success(null);
	}

	/** 수정 */
	@PreAuthorize("hasAuthority('ROLE_MARKET_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/update/{marketSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> update(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@PathVariable("marketSeq") long marketSeq,
			@RequestBody @Validated({MarketMngActiveVO.UpdateAction.class}) MarketMngActiveVO actionVO, BindingResult error) {
		logger.debug(" update MarketMngActiveVO start: {}", actionVO);
		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}

		MarketMngVO marketVO = marketMngService.getByMarketSeq(marketSeq);
		if ( marketVO == null ){
			throw new AbleRuntimeException("수정하려는 시장이 존재하지 않습니다. 다시 한번 확인해 주세요.");
		}

		if(marketVO.getMngAreaSeq() != user.getMngAreaSeq().longValue()) {
			throw new AbleRuntimeException("데이터의 관제지역과 관리자의 관제지역이 불일치 합니다.");
		}

		actionVO.setMarketSeq(marketSeq);

		marketMngService.update(actionVO);

		return AbleResponseEntityBuilder.success(null);
	}

	/** 삭제 */
	@PreAuthorize("hasAuthority('ROLE_MARKET_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/delete/{marketSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> delete(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@PathVariable("marketSeq") long marketSeq) {

		MarketMngVO marketVO = marketMngService.getByMarketSeq(marketSeq);

		if ( marketVO != null ){
			if(marketVO.getMngAreaSeq() != user.getMngAreaSeq().longValue()) {
				throw new AbleRuntimeException("데이터의 관제지역과 관리자의 관제지역이 불일치 합니다.");
			}
			// 시장 삭제 ( DELETE REULS : Restrict)
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("searchMarketSeq", marketSeq);
			SearchMap search = SearchMap.buildFrom(param);
			List<StoreMngVO> storeList = storeMngService.getListAll(null, search);
			if(CollectionUtils.isNotEmpty(storeList )) {
				throw new AbleRuntimeException("전통시장에 해당되는 점포들이 존재하여 삭제가 불가능 합니다. 해당 점포들을 삭제 하신 후 다시 삭제해 주세요.");
			}
			marketMngService.delete(marketSeq);
		}
		return AbleResponseEntityBuilder.success(null);
	}

	/** 연기이벤트 전송제외/복구 저장. */
	@PreAuthorize("hasAuthority('ROLE_ADMIN_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/smokealarm/{marketSeq}/{alarmYn}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> smokealarm(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@PathVariable("marketSeq") long marketSeq,
			@PathVariable("alarmYn") String alarmYn) {

		//logger.info(":::::::::::::::: alarmYn = {}", alarmYn);
		MarketMngVO marketVO = marketMngService.getByMarketSeq(marketSeq);

		if ( marketVO != null ){
			if(marketVO.getMngAreaSeq() != user.getMngAreaSeq().longValue()) {
				throw new AbleRuntimeException("데이터의 관제지역과 관리자의 관제지역이 불일치 합니다.");
			}
			// 시장 전체 점포의 연기이벤트 전송제외/복구 저장
			marketMngService.smokealarm(marketSeq, alarmYn);
		}
		return AbleResponseEntityBuilder.success(null);
	}

}
