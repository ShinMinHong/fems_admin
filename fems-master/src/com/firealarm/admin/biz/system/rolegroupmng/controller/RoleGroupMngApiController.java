package com.firealarm.admin.biz.system.rolegroupmng.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.firealarm.admin.common.support.ApiControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;
import com.firealarm.admin.biz.system.rolegroupmng.service.RoleGroupMngService;
import com.firealarm.admin.biz.system.rolegroupmng.vo.RoleGroupMngActiveVO;
import com.firealarm.admin.biz.system.rolegroupmng.vo.RoleGroupMngDetailsVO;
import com.firealarm.admin.biz.system.rolegroupmng.vo.RoleGroupMngVO;

import framework.exception.AbleRuntimeException;
import framework.exception.AbleValidationException;
import framework.spring.web.rest.AbleResponseEntity;
import framework.spring.web.rest.AbleResponseEntityBuilder;
import framework.spring.web.rest.AbleResponseEntityMap;
import framework.vo.SearchMap;

/**
 * 권한그룹코드관리 APIController
 * @role JKS
 *
 */
@RestController
@RequestMapping(value = "/system/rolegroupmng/api")
@PreAuthorize("hasAuthority('ROLE_SYSTEM_ADMIN')")
public class RoleGroupMngApiController extends ApiControllerSupport {

	@Autowired RoleGroupMngService roleGroupMngService;

	/** 목록 조회 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public AbleResponseEntity<?> getListAll(HttpServletRequest request,
			@SortDefault(sort = "regDate", direction = Direction.DESC) Sort sort, Model model,
			@AppLoginUser AppUserDetails me) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		List<RoleGroupMngVO> list = roleGroupMngService.getListAll(sort, searchMap);
		return AbleResponseEntityBuilder.success(list);
	}

	/** 페이징 조회 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> getList(HttpServletRequest request,
			@PageableDefault(sort = "regDate", direction = Direction.DESC) Pageable pageable, Model model,
			@AppLoginUser AppUserDetails me) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		Page<RoleGroupMngVO> page = roleGroupMngService.getList(pageable, searchMap);
		return AbleResponseEntityBuilder.success(page);
	}

	/** 상세 */
	@RequestMapping(value = "/details/{rolegroupCode}", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> details(
			@PathVariable("rolegroupCode") String rolegroupCode,
			@AppLoginUser AppUserDetails me ) {
		RoleGroupMngDetailsVO ro = roleGroupMngService.getDetailsPageByRolegroupCode(rolegroupCode);
		if(ro == null) {
			throw new AbleRuntimeException("권한그룹 상세정보를 찾을 수 없습니다.");
		}
		return AbleResponseEntityMap.success(ro);
	}

	/** 등록 */
	@RequestMapping(value = "/details/insert", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> insert(
			@RequestBody @JsonView({RoleGroupMngActiveVO.CreateAction.class}) @Validated({RoleGroupMngActiveVO.CreateAction.class}) RoleGroupMngActiveVO actionVO, BindingResult error,
			@AppLoginUser AppUserDetails me ) {

		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}

		// 등록자
		String register = me.getAdminId();
		String rolegroupCode = actionVO.getRolegroupCode();

		//권한 그룹 존재 여부
		RoleGroupMngVO roleGroupMngVO = roleGroupMngService.getByRolegroup(actionVO.getRolegroupCode());
		if ( roleGroupMngVO != null ){
			throw new AbleRuntimeException("권한그룹이 이미 존재합니다.");
		}

		roleGroupMngService.add(actionVO,rolegroupCode,register);

		return AbleResponseEntityBuilder.success(null);
	}

	/** 수정 */
	@RequestMapping(value = "/details/update/{rolegroupCode}", method = RequestMethod.PUT)
	public @ResponseBody AbleResponseEntity<?> update(
			@PathVariable("rolegroupCode") String rolegroupCode,
			@RequestBody @JsonView({RoleGroupMngActiveVO.UpdateAction.class  }) @Validated({RoleGroupMngActiveVO.UpdateAction.class}) RoleGroupMngActiveVO actionVO, BindingResult error,
			@AppLoginUser AppUserDetails me ) {

		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}


		if ( StringUtils.isBlank(rolegroupCode)){
			throw new AbleRuntimeException("권한그룹 코드는 필수 입니다.");
		}

		/* 권한그룹코드가 존재하는지 체크 */
		if ( !this.validateRolegroupCodeExistance(rolegroupCode) ){
			throw new AbleRuntimeException("수정하려는 권한그룹이 존재하지 않습니다.");
		}

		String register = me.getAdminId();
		roleGroupMngService.update(actionVO,rolegroupCode, register);

		return AbleResponseEntityBuilder.success(null);
	}

	/** 삭제 */
	@RequestMapping(value = "/details/delete/{rolegroupCode}", method = RequestMethod.DELETE)
	public @ResponseBody AbleResponseEntity<?> delete(
		@PathVariable("rolegroupCode") String rolegroupCode,
		@AppLoginUser AppUserDetails me ) {
		logger.debug("rolegroupCode: {}", rolegroupCode);
		RoleGroupMngVO targetDt = roleGroupMngService.getByRolegroup(rolegroupCode);

		if ( targetDt != null ){
			roleGroupMngService.delete(rolegroupCode);
		}
		return AbleResponseEntityBuilder.success(null);
	}

	/** 수정, 삭제 공통 체크 - 권한그룹 코드가 존재하는지 체크 */
	private boolean validateRolegroupCodeExistance(String rolegroupCode){
		return (roleGroupMngService.getByRolegroup(rolegroupCode) != null);
	}

}