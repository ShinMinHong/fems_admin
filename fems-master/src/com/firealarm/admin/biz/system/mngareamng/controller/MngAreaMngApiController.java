package com.firealarm.admin.biz.system.mngareamng.controller;

import java.util.List;

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

import com.fasterxml.jackson.annotation.JsonView;
import com.firealarm.admin.biz.areasystem.marketmng.service.MarketMngService;
import com.firealarm.admin.biz.areasystem.marketmng.vo.MarketMngVO;
import com.firealarm.admin.biz.system.mngareamng.service.MngAreaMngService;
import com.firealarm.admin.biz.system.mngareamng.vo.MngAreaMngActiveVO;
import com.firealarm.admin.biz.system.mngareamng.vo.MngAreaMngVO;
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
 * 관제지역 API Controller
 * @author ovcoimf
 */
@Controller
@RequestMapping(value = "/system/mngareamng/api")
@PreAuthorize("hasAuthority('ROLE_SYSTEM_ADMIN')")
public class MngAreaMngApiController extends ApiControllerSupport{

	@Autowired MngAreaMngService mngAreaMngService;
	@Autowired MarketMngService marketMngService;

	/** 목록 조회 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public AbleResponseEntity<?> getListAll(
			HttpServletRequest request,
			@SortDefault(sort = "regDate", direction = Direction.DESC) Sort sort, Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		List<MngAreaMngVO> list = mngAreaMngService.getListAll(sort, searchMap);
		return AbleResponseEntityBuilder.success(list);
	}

	/** 페이징 조회 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> getList(HttpServletRequest request,
			@PageableDefault(sort = "regDate", direction = Direction.DESC) Pageable pageable, Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		Page<MngAreaMngVO> page = mngAreaMngService.getList(pageable, searchMap);
		return AbleResponseEntityBuilder.success(page);
	}

	/** 상세 */
	@RequestMapping(value = "/details/{mngAreaSeq}", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> details(@PathVariable("mngAreaSeq") long mngAreaSeq) {
		MngAreaMngVO ro = mngAreaMngService.getByMngAreaSeq(mngAreaSeq);
		if (ro == null) {
			throw new AbleRuntimeException("관제지역 상세정보를 찾을 수 없습니다.");
		}
		return AbleResponseEntityMap.success(ro);
	}

	/** 등록 */
	@RequestMapping(value = "/details/insert", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> insert(
			@AppLoginUser AppUserDetails me,
			@RequestBody @JsonView({MngAreaMngActiveVO.CreateAction.class}) @Validated({MngAreaMngActiveVO.CreateAction.class}) MngAreaMngActiveVO actionVO,
			BindingResult error) {
		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}
		//이미 존재하는 권한코드인지 체크
		MngAreaMngVO mngAreaInfo = mngAreaMngService.getByMngAreaName(actionVO.getMngAreaName());
		if ( mngAreaInfo != null ){
			throw new AbleRuntimeException("이름이 중복되는 관제지역이 존재합니다.");
		}

		actionVO.setRegAdminId(me.getAdminId());
		mngAreaMngService.insert(actionVO);
		return AbleResponseEntityBuilder.success(null);
	}

	/** 수정 */
	@RequestMapping(value = "/details/update/{mngAreaSeq}", method = RequestMethod.PUT)
	public @ResponseBody AbleResponseEntity<?> update(
			@PathVariable("mngAreaSeq") String mngAreaSeq,
			@AppLoginUser AppUserDetails me,
			@RequestBody @JsonView({MngAreaMngActiveVO.UpdateAction.class}) @Validated({MngAreaMngActiveVO.UpdateAction.class}) MngAreaMngActiveVO actionVO,
			BindingResult error) {

		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}

		actionVO.setUpdAdminId(me.getAdminId());
		mngAreaMngService.update(actionVO);
		return AbleResponseEntityBuilder.success(null);
	}

	/** 삭제 */
	@RequestMapping(value = "/details/delete/{mngAreaSeq}", method = RequestMethod.DELETE)
	public @ResponseBody AbleResponseEntity<?> delete(
		@PathVariable("mngAreaSeq") long mngAreaSeq) {
		MngAreaMngVO targetDt = mngAreaMngService.getByMngAreaSeq(mngAreaSeq);

		if(targetDt  != null) {
			List<MarketMngVO> subMarketList = marketMngService.getListByMngAreaSeq(mngAreaSeq);
			if(CollectionUtils.isNotEmpty(subMarketList)) {
				throw new AbleRuntimeException("해당 관제지역에 속하는 전통시장이 존재하여 삭제할 수 없습니다.");
			}
			mngAreaMngService.delete(mngAreaSeq);
		}
		return AbleResponseEntityBuilder.success(null);
	}

	/** HQ관리자 관제지역 선택 */
	@PreAuthorize("hasAuthority('ROLE_SYSTEM_ADMIN')")
	@RequestMapping(value = "/setmngarea/{mngAreaSeq}", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> setMngArea(
			HttpServletRequest request,
			@PathVariable("mngAreaSeq") long mngAreaSeq,
			@AppLoginUser AppUserDetails user) {
		user.setMngAreaSeq(mngAreaSeq);
		MngAreaMngVO mngAreaInfo = mngAreaMngService.getByMngAreaSeq(user.getMngAreaSeq());
		user.setMngAreaName(mngAreaInfo.getMngAreaName());
		return AbleResponseEntityMap.success(null);
	}
}