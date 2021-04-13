package com.firealarm.admin.biz.areasystem.firedetectorset.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.firealarm.admin.appconfig.CodeMap.APP_USER_GRADE;
import com.firealarm.admin.biz.areasystem.firedetectorset.service.FireDetectorSetService;
import com.firealarm.admin.biz.areasystem.firedetectorset.vo.FireDetectorSetActiveVO;
import com.firealarm.admin.biz.areasystem.firedetectorset.vo.FireDetectorSetVO;
import com.firealarm.admin.biz.store.storemng.service.StoreMngService;
import com.firealarm.admin.biz.store.storemng.vo.StoreMngActiveVO;
import com.firealarm.admin.common.service.FileStorageManager;
import com.firealarm.admin.common.support.ApiControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.exception.AbleRuntimeException;
import framework.exception.AbleValidationException;
import framework.spring.web.rest.AbleResponseEntity;
import framework.spring.web.rest.AbleResponseEntityBuilder;
import framework.vo.SearchMap;

/**
 * 화재감지기 관리 Api Controller
 * @author JKS
 */
@Controller
@PreAuthorize("hasAnyAuthority('ROLE_FIRE_DETECTOR_MNG','ROLE_FIRE_DETECTOR_READ') AND @fireAlarmCustomSecurityService.hasMngArea()")
@RequestMapping(value = "/areasystem/firedetectorset/api")
public class FireDetectorSetApiController extends ApiControllerSupport {

@Autowired FireDetectorSetService fireDetectorSetService;
@Autowired StoreMngService storeMngService;
@Autowired FileStorageManager fileStorageManager;

	/** 목록 조회 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public AbleResponseEntity<?> getListAll(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@SortDefault(sort = "fireDetectorSetSeq", direction=Direction.DESC) Sort sort,
			Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);

		// 시장관리자는 해당 시장만 조회
		if(APP_USER_GRADE.MARKET_ADMIN.equals(user.getRolegroupCode())) {
			searchMap.put("marketSeq", user.getMarketSeq());
		}

		// 정렬순서타입(점포명 ASC, CTN번호 DESC, default 등록일 DESC)
		String sortString = (String)searchMap.get("searchSort");
		switch(sortString) {
			case "STORE_NAME":
				sort = new Sort(Sort.Direction.ASC, "storeName");
				break;
			case "CTN_NO":
				sort = new Sort(Sort.Direction.DESC, "ctnNo");
				break;
			default:
				break;
		}
		List<FireDetectorSetVO> list = fireDetectorSetService.getListAll(sort, searchMap);
		return AbleResponseEntityBuilder.success(list);
	}

	/** 페이징 조회 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody @Validated({FireDetectorSetVO.ListView.class}) AbleResponseEntity<?> getList(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@PageableDefault(sort = "fireDetectorSetSeq", direction=Direction.DESC) Pageable pageable,
			Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);

		// 시장관리자는 해당 시장만 조회
		if(APP_USER_GRADE.MARKET_ADMIN.equals(user.getRolegroupCode())) {
			searchMap.put("marketSeq", user.getMarketSeq());
		}

		// 정렬순서타입(점포명 ASC, CTN번호 DESC, default 등록일 DESC)
		if(searchMap.containsKey("searchSort")) {
			String sortString = (String)searchMap.get("searchSort");
			int page =Integer.parseInt(request.getParameter("page"));
			int size = Integer.parseInt(request.getParameter("size"));
			switch(sortString) {
				case "STORE_NAME":
					pageable = new PageRequest(page, size, new Sort(Sort.Direction.ASC, "storeName"));
					break;
				case "CTN_NO":
					pageable = new PageRequest(page, size, new Sort(Sort.Direction.DESC, "ctnNo"));
					break;
				default:
					break;
			}
		}
		Page<FireDetectorSetVO> page = fireDetectorSetService.getList(pageable, searchMap);
		return AbleResponseEntityBuilder.success(page);
	}

	/** 상세 */
	@RequestMapping(value = "/details/{fireDetectorSetSeq}", method = RequestMethod.GET)
	public @ResponseBody @Validated({FireDetectorSetVO.DetailsView.class}) AbleResponseEntity<?> details(
			@PathVariable("fireDetectorSetSeq") long fireDetectorSetSeq,
			Model model) {
		FireDetectorSetVO ro = fireDetectorSetService.getByFireDetectorSetSeq(fireDetectorSetSeq);
		logger.debug("detail FireDetectorSetVO start : {}", ro);
		return AbleResponseEntityBuilder.success(ro);
	}

	/** 등록 */
	@PreAuthorize("hasAuthority('ROLE_FIRE_DETECTOR_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/insert", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> insert(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@ModelAttribute @Validated({FireDetectorSetActiveVO.CreateAction.class}) FireDetectorSetActiveVO actionVO, BindingResult error) {
		logger.debug("insert FireDetectorSetActiveVO start: {}", actionVO);

		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}

		actionVO.setRegAdminId(user.getAdminId());

		fireDetectorSetService.insert(actionVO);
		return AbleResponseEntityBuilder.success(null);
	}

	/** 수정 */
	@PreAuthorize("hasAuthority('ROLE_FIRE_DETECTOR_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/update/{fireDetectorSetSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> update(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@PathVariable("fireDetectorSetSeq") long fireDetectorSetSeq,
			@ModelAttribute @Validated({FireDetectorSetActiveVO.UpdateAction.class}) FireDetectorSetActiveVO actionVO, BindingResult error) {
		logger.debug("fireDetectorSetSeq: {}, vo: {}", fireDetectorSetSeq, actionVO);
		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}

		FireDetectorSetVO vo= fireDetectorSetService.getByFireDetectorSetSeq(fireDetectorSetSeq);
		if ( vo == null ){
			throw new AbleRuntimeException("수정하려는 화재감지기 설징이 존재하지 않습니다.");
		}

		actionVO.setFireDetectorSetSeq(fireDetectorSetSeq);
		actionVO.setUpdAdminId(user.getAdminId());
		fireDetectorSetService.update(actionVO);

		return AbleResponseEntityBuilder.success(null);
	}

	/** 삭제 */
	@PreAuthorize("hasAuthority('ROLE_FIRE_DETECTOR_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/delete/{fireDetectorSetSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> delete(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
		@PathVariable("fireDetectorSetSeq") long fireDetectorSetSeq) {
		logger.debug("fireDetectorSetSeq: {}", fireDetectorSetSeq);
		// 삭제될  화재 감지기 정보
		FireDetectorSetVO vo = fireDetectorSetService.getByFireDetectorSetSeq(fireDetectorSetSeq);
		// 삭제될 화재감지기 설정 존재여부 확인 후 화재감지기설정 삭제
		if ( vo != null ){
			fireDetectorSetService.delete(fireDetectorSetSeq);
		}
		return AbleResponseEntityBuilder.success(null);
	}
}