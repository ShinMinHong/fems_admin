package com.firealarm.admin.biz.system.hqadminmng.controller;

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

import com.firealarm.admin.appconfig.CodeMap.APP_USER_GRADE;
import com.firealarm.admin.biz.areasystem.adminmng.service.AdminMngService;
import com.firealarm.admin.biz.areasystem.adminmng.vo.AdminMngActiveVO;
import com.firealarm.admin.biz.areasystem.adminmng.vo.AdminMngVO;
import com.firealarm.admin.common.support.ApiControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.util.UserSecurityUtil;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.exception.AbleRuntimeException;
import framework.exception.AbleValidationException;
import framework.spring.web.rest.AbleResponseEntity;
import framework.spring.web.rest.AbleResponseEntityBuilder;
import framework.vo.SearchMap;

/**
 * 관리자 관리 API Controller
 * @author JKS
 */

@Controller
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN_MNG')")
@RequestMapping(value = "/system/hqadminmng/api")
public class HqAdminMngApiController extends ApiControllerSupport {

	@Autowired AdminMngService adminMngService;

	/** 목록 조회 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public AbleResponseEntity<?> getListAll(
			HttpServletRequest request,
			@SortDefault(sort = "regDate", direction=Direction.DESC) Sort sort,
			Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		searchMap.add("rolegroupCode", "HQ_ADMIN");
		List<AdminMngVO> list = adminMngService.getListAll(sort, searchMap);
		return AbleResponseEntityBuilder.success(list);
	}

	/** 페이징 조회 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> getList(
			HttpServletRequest request,
			@PageableDefault(sort = "regDate", direction=Direction.DESC) Pageable pageable,
			Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		searchMap.add("rolegroupCode", "HQ_ADMIN");
		Page<AdminMngVO> page = adminMngService.getList(pageable, searchMap);

		return AbleResponseEntityBuilder.success(page);
	}

	/** 상세 */
	@RequestMapping(value = "/details/{adminSeq}", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> details(
			@PathVariable("adminSeq") long adminSeq,
			Model model) {
		AdminMngVO ro = adminMngService.getUserDetailsByAdminSeq(adminSeq);
		return AbleResponseEntityBuilder.success(ro);
	}

	/** 아이디 중복체크 */
	@RequestMapping(value = "/details/checkDuplicateId/{adminId}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> checkDuplicateId(
			HttpServletRequest request,
			@PathVariable("adminId") String adminId) {
		logger.debug("adminId: {}", adminId);
		if(!adminMngService.checkDuplicateId(adminId)) {
			throw new AbleRuntimeException("이미 사용중인 아이디입니다.");
		}else{
			return AbleResponseEntityBuilder.success(null);
		}
	}

	/** 등록 */
	@RequestMapping(value = "/details/insert", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> insert(
			HttpServletRequest request,
			@RequestBody @Validated({AdminMngActiveVO.CreateAction.class}) AdminMngActiveVO actionVO, BindingResult error) {
		logger.debug("vo: {}", actionVO);
		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}

		// 아이디 중복 체크
		AppUserDetails me = UserSecurityUtil.getCurrentUserDetails();
		if(adminMngService.checkDuplicateId(actionVO.getAdminId())) {
			actionVO.setRolegroupCode(APP_USER_GRADE.HQ_ADMIN);
			actionVO.setRegAdminId(me.getAdminId());

			adminMngService.insert(actionVO);
		}

		return AbleResponseEntityBuilder.success(null);
	}

	/** 수정 */
	@RequestMapping(value = "/details/update/{adminSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> update(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@PathVariable("adminSeq") long adminSeq,
			@RequestBody @Validated({AdminMngActiveVO.UpdateAction.class}) AdminMngActiveVO actionVO, BindingResult error) {
		logger.debug("adminSeq: {}, vo: {}", adminSeq, actionVO);
		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}

		AdminMngVO originalAdminMngVO = adminMngService.getUserDetailsByAdminSeq(adminSeq);
		if ( originalAdminMngVO == null ){
			throw new AbleRuntimeException("수정하려는 회원이 존재하지 않습니다.");
		}

		actionVO.setUpdAdminId(user.getAdminId());
		actionVO.setAdminSeq(adminSeq);

		adminMngService.updateByAdmin(actionVO, originalAdminMngVO);

		return AbleResponseEntityBuilder.success(null);
	}

	/** 비밀번호 초기화 */
	@RequestMapping(value = "/details/resetPswd/{adminSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> resetPassword(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@PathVariable("adminSeq") long adminSeq) {

		//회원이 존재하는지 체크
		AdminMngVO adminInfo = adminMngService.getUserDetailsByAdminSeq(adminSeq);
		if ( adminInfo == null ){
			throw new AbleRuntimeException("비밀번호 초기화하려는 회원이 존재하지 않습니다.");
		}

		adminMngService.resetPassword(adminInfo);

		return AbleResponseEntityBuilder.success(null);
	}

	/** 삭제 */
	@RequestMapping(value = "/details/delete/{adminSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> delete(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
		@PathVariable("adminSeq") long adminSeq) {
		logger.debug("adminSeq: {}", adminSeq);

		AdminMngVO originalAdminMngVO = adminMngService.getUserDetailsByAdminSeq(adminSeq);

		if ( originalAdminMngVO != null ){
			adminMngService.delete(adminSeq);
		}
		return AbleResponseEntityBuilder.success(null);
	}
}
