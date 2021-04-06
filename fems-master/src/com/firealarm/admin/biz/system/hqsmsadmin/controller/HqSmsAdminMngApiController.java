package com.firealarm.admin.biz.system.hqsmsadmin.controller;

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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.firealarm.admin.biz.system.hqsmsadmin.service.HqSmsAdminMngService;
import com.firealarm.admin.biz.system.hqsmsadmin.vo.HqSmsAdminMngActiveVO;
import com.firealarm.admin.biz.system.hqsmsadmin.vo.HqSmsAdminMngVO;
import com.firealarm.admin.common.support.ApiControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.exception.AbleRuntimeException;
import framework.exception.AbleValidationException;
import framework.spring.web.rest.AbleResponseEntity;
import framework.spring.web.rest.AbleResponseEntityBuilder;
import framework.vo.SearchMap;

/**
 * 통합SMS수신자 API Controller
 * @author SHH
 */

@Controller
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN_MNG')")
@RequestMapping(value = "/system/hqsmsadminmng/api")
public class HqSmsAdminMngApiController extends ApiControllerSupport {

	@Autowired HqSmsAdminMngService smsAdminMngService;

	/** 목록 조회 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public AbleResponseEntity<?> getListAll(
			HttpServletRequest request,
			@SortDefault(sort = "regDate", direction=Direction.DESC) Sort sort,
			Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		searchMap.add("rolegroupCode", "HQ_ADMIN");
		List<HqSmsAdminMngVO> list = smsAdminMngService.getListAll(sort, searchMap);
		return AbleResponseEntityBuilder.success(list);
	}

	/** 페이징 조회 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> getList(
			HttpServletRequest request,
			@PageableDefault(sort = "regDate", direction=Direction.DESC) Pageable pageable,
			Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		Page<HqSmsAdminMngVO> page = smsAdminMngService.getList(pageable, searchMap);

		return AbleResponseEntityBuilder.success(page);
	}

	/** 상세 */
	@RequestMapping(value = "/details/{smsAdminSeq}", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> details(
			@PathVariable("smsAdminSeq") long smsAdminSeq,
			Model model) {
		HqSmsAdminMngVO ro = smsAdminMngService.getSmsAdminDetailsBySmsAdminSeq(smsAdminSeq);
		return AbleResponseEntityBuilder.success(ro);
	}

	/** 등록 */
	@RequestMapping(value = "/details/insert", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> insert(
			HttpServletRequest request,
			@RequestBody @Validated({HqSmsAdminMngActiveVO.CreateAction.class}) HqSmsAdminMngActiveVO actionVO, BindingResult error) {
		logger.debug("vo: {}", actionVO);
		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}

		smsAdminMngService.insert(actionVO);

		return AbleResponseEntityBuilder.success(null);
	}

	/** 수정 */
	@RequestMapping(value = "/details/update/{smsAdminSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> update(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@PathVariable("smsAdminSeq") long smsAdminSeq,
			@RequestBody @Validated({HqSmsAdminMngActiveVO.UpdateAction.class}) HqSmsAdminMngActiveVO actionVO, BindingResult error) {
		logger.debug("::::::::::::::::::smsAdminSeq: {}, vo: {}", smsAdminSeq, actionVO);
		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}

		HqSmsAdminMngVO originalSmsAdminMngVO = smsAdminMngService.getSmsAdminDetailsBySmsAdminSeq(smsAdminSeq);
		if ( originalSmsAdminMngVO == null ){
			throw new AbleRuntimeException("수정하려는 회원이 존재하지 않습니다.");
		}

		actionVO.setUpdAdminId(user.getAdminId());
		actionVO.setSmsAdminSeq(smsAdminSeq);

		logger.debug("::::::::::::::::::smsAdminSeq: {}, vo: {}", smsAdminSeq, actionVO);

		smsAdminMngService.update(actionVO);

		return AbleResponseEntityBuilder.success(null);
	}

	/** 삭제 */
	@RequestMapping(value = "/details/delete/{smsAdminSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> delete(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
		@PathVariable("smsAdminSeq") long smsAdminSeq) {
		logger.debug("smsAdminSeq: {}", smsAdminSeq);

		HqSmsAdminMngVO originalSmsAdminMngVO = smsAdminMngService.getSmsAdminDetailsBySmsAdminSeq(smsAdminSeq);

		if ( originalSmsAdminMngVO != null ){
			smsAdminMngService.delete(smsAdminSeq);
		}
		return AbleResponseEntityBuilder.success(null);
	}
}
