package com.firealarm.admin.biz.system.rolemng.controller;

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

import com.fasterxml.jackson.annotation.JsonView;
import com.firealarm.admin.biz.system.rolemng.service.RoleMngService;
import com.firealarm.admin.biz.system.rolemng.vo.RoleMngActiveVO;
import com.firealarm.admin.biz.system.rolemng.vo.RoleMngDetailsVO;
import com.firealarm.admin.biz.system.rolemng.vo.RoleMngVO;
import com.firealarm.admin.common.support.ApiControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.exception.AbleRuntimeException;
import framework.exception.AbleValidationException;
import framework.spring.web.rest.AbleResponseEntity;
import framework.spring.web.rest.AbleResponseEntityBuilder;
import framework.spring.web.rest.AbleResponseEntityMap;
import framework.vo.SearchMap;

/**
 * 권한코드관리 API Controller
 * @author JKS
 *
 */
@Controller
@RequestMapping(value = "/system/rolemng/api")
@PreAuthorize("hasAuthority('ROLE_SYSTEM_ADMIN')")
public class RoleMngApiController extends ApiControllerSupport{

	@Autowired RoleMngService roleMngService;

	/** 목록 조회 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public AbleResponseEntity<?> getListAll(
			HttpServletRequest request,
			@SortDefault(sort = "regDate", direction = Direction.DESC) Sort sort, Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		List<RoleMngVO> list = roleMngService.getListAll(sort, searchMap);
		return AbleResponseEntityBuilder.success(list);
	}

	/** 페이징 조회 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> getList(HttpServletRequest request,
			@PageableDefault(sort = "regDate", direction = Direction.DESC) Pageable pageable, Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		Page<RoleMngVO> page = roleMngService.getList(pageable, searchMap);
		return AbleResponseEntityBuilder.success(page);
	}

	/** 상세 */
	@RequestMapping(value = "/details/{roleCode}", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> details(@PathVariable("roleCode") String roleCode) {
		RoleMngDetailsVO ro = roleMngService.getDetailsPageByRoleCode(roleCode);
		if (ro == null) {
			throw new AbleRuntimeException("권한코드 상세정보를 찾을 수 없습니다.");
		}
		return AbleResponseEntityMap.success(ro);
	}

	/** 등록 */
	@RequestMapping(value = "/details/insert", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> insert(
			@RequestBody @JsonView({RoleMngActiveVO.CreateAction.class}) @Validated({RoleMngActiveVO.CreateAction.class}) RoleMngActiveVO actionVO,
			@AppLoginUser AppUserDetails me,
			BindingResult error) {
		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}
		//이미 존재하는 권한코드인지 체크
		RoleMngVO roleMngVO = roleMngService.getByRole(actionVO.getRoleCode());
		if ( roleMngVO != null ){
			throw new AbleRuntimeException("권한이 이미 존재합니다.");
		}
		String register = me.getAdminId();
		roleMngService.add(actionVO,register);
		return AbleResponseEntityBuilder.success(null);
	}

	/** 수정 */
	@RequestMapping(value = "/details/update/{roleCode}", method = RequestMethod.PUT)
	public @ResponseBody AbleResponseEntity<?> update(
			@PathVariable("roleCode") String roleCode,
			@AppLoginUser AppUserDetails me,
			@RequestBody @JsonView({RoleMngActiveVO.UpdateAction.class}) @Validated({RoleMngActiveVO.UpdateAction.class}) RoleMngActiveVO actionVO, BindingResult error) {

		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}

		String register = me.getAdminId();
		String preRoleCode = roleCode;
		roleMngService.update(actionVO , preRoleCode , register);
		return AbleResponseEntityBuilder.success(null);
	}

	/** 삭제 */
	@RequestMapping(value = "/details/delete/{roleCode}", method = RequestMethod.DELETE)
	public @ResponseBody AbleResponseEntity<?> delete(
		@PathVariable("roleCode") String roleCode) {
		RoleMngVO targetDt = roleMngService.getByRole(roleCode);

		if(targetDt  != null) {
			roleMngService.delete(roleCode);
		}
		return AbleResponseEntityBuilder.success(null);
	}
}