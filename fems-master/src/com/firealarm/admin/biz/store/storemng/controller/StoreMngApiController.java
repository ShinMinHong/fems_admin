package com.firealarm.admin.biz.store.storemng.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.firealarm.admin.appconfig.CodeMap.APP_USER_GRADE;
import com.firealarm.admin.biz.store.storemng.service.StoreMngService;
import com.firealarm.admin.biz.store.storemng.vo.StoreMngActiveVO;
import com.firealarm.admin.biz.store.storemng.vo.StoreMngVO;
import com.firealarm.admin.common.support.ApiControllerSupport;
import com.firealarm.admin.common.vo.ExcelUploadRO;
import com.firealarm.admin.common.vo.ExcelUploadVO;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.util.UserSecurityUtil;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.exception.AbleRuntimeException;
import framework.exception.AbleValidationException;
import framework.spring.web.rest.AbleResponseEntity;
import framework.spring.web.rest.AbleResponseEntityBuilder;
import framework.vo.SearchMap;

/**
 * 전통시장 점포관리 API Controller
 * @author rodem4_pc1
 *
 */
@Controller
@RequestMapping(value = "/store/storemng/api")
@PreAuthorize("hasAnyAuthority('ROLE_STORE_MNG', 'ROLE_STORE_READ') AND @fireAlarmCustomSecurityService.hasMngArea()")
public class StoreMngApiController extends ApiControllerSupport{

	@Autowired StoreMngService storeMngService;

	/** 목록 조회 */
	public AbleResponseEntity<?> getListAll(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails me,
			@SortDefault(sort="regDate", direction= Direction.DESC) Sort sort,
			Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		if(APP_USER_GRADE.MARKET_ADMIN.equals(me.getRolegroupCode())){
			searchMap.put("marketSeq", me.getMarketSeq());
		}

		// 정렬순서타입(점포명 ASC, default 등록일 DESC)
		String sortString = (String)searchMap.get("searchSort");
		switch(sortString) {
			case "STORE_NAME":
				sort = new Sort(Sort.Direction.ASC, "storeName");
				break;
			default:
				break;
		}

		List<StoreMngVO> list = storeMngService.getListAll(sort, searchMap);
		return AbleResponseEntityBuilder.success(list);
	}

	/** 페이징 조회 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> getList(HttpServletRequest request,
			@AppLoginUser AppUserDetails me,
			@PageableDefault(sort = "regDate", direction = Direction.DESC) Pageable pageable, Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		if(APP_USER_GRADE.MARKET_ADMIN.equals(me.getRolegroupCode())){
			searchMap.put("marketSeq", me.getMarketSeq());
		}

		// 정렬순서타입(점포명 ASC, default 등록일 DESC)
		if(searchMap.containsKey("searchSort")) {
			String sortString = (String)searchMap.get("searchSort");
			int page =Integer.parseInt(request.getParameter("page"));
			int size = Integer.parseInt(request.getParameter("size"));
			switch(sortString) {
				case "STORE_NAME":
					pageable = new PageRequest(page, size, new Sort(Sort.Direction.ASC, "storeName"));
					break;
				default:
					break;
			}
		}

		Page<StoreMngVO> page = storeMngService.getList(pageable, searchMap);
		return AbleResponseEntityBuilder.success(page);
	}

	/** 상세 */
	@RequestMapping(value = "/details/{storeSeq}", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> details(
			@AppLoginUser AppUserDetails me,
			@PathVariable("storeSeq") long storeSeq,
			Model model) {
		StoreMngActiveVO ro = storeMngService.getByStoreSeq(storeSeq);
		return AbleResponseEntityBuilder.success(ro);
	}

	/** 등록 */
	@PreAuthorize("hasAuthority('ROLE_STORE_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/insert", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> insert(
			HttpServletRequest request,
			@RequestBody @Validated({StoreMngVO.CreateAction.class}) StoreMngVO actionVO, BindingResult error) {
		logger.debug("StoreMng Insert Start. VO: {}", actionVO);
		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}

		//관제지역 변경여부 체크
		AppUserDetails me = UserSecurityUtil.getCurrentUserDetails();
		if (actionVO.getMngAreaSeq() != me.getMngAreaSeq().longValue()) {
			throw new AbleRuntimeException("관제지역오류. 새로고침 후 다시 등록해 주세요.");
		}

		storeMngService.insert(actionVO);

		return AbleResponseEntityBuilder.success(null);
	}

	/** 수정 */
	@PreAuthorize("hasAuthority('ROLE_STORE_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/update/{storeSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> update(
			HttpServletRequest request,
			@PathVariable("storeSeq") long storeSeq,
			@RequestBody @Validated({StoreMngActiveVO.UpdateAction.class}) StoreMngActiveVO actionVO, BindingResult error) {
		logger.debug("StoreMng Update Start. StoreSeq: {}, vo: {}", storeSeq, actionVO);

		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}

		//수정할 점포 정보가 있는지 체크
		StoreMngActiveVO ro = storeMngService.getByStoreSeq(storeSeq);
		if ( ro == null ){
			throw new AbleRuntimeException("수정할 점포 정보가 없습니다.");
		}
		actionVO.setStoreSeq(ro.getStoreSeq());

		// 수정
		storeMngService.update(actionVO);

		return AbleResponseEntityBuilder.success(null);
	}

	/** 삭제 */
	@PreAuthorize("hasAuthority('ROLE_STORE_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/delete/{storeSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> delete(
			HttpServletRequest request,
		@PathVariable("storeSeq") long storeSeq) {
		logger.debug("StoreMng Delete Start. StoreSeq: {}", storeSeq);

		//삭제할 점포 정보가 있는지 체크
		StoreMngVO ro = storeMngService.getByStoreSeq(storeSeq);
		if ( ro == null ){
			throw new AbleRuntimeException("삭제할 점포 정보가 없습니다.");
		}

		//점포에 화재감지기가 부착되어 있는지 체크
		if (storeMngService.hasFireDetector(storeSeq)){
			throw new AbleRuntimeException("점포에 화재감지기가 설치되어 있습니다. \r\n화재감지기 삭제 후 점포를 삭제해 주세요.");
		}

		//점포 삭제
		storeMngService.delete(storeSeq);

		return AbleResponseEntityBuilder.success(null);
	}

	/** SMS 수신 대상자 저장 */
	@PreAuthorize("hasAuthority('ROLE_STORE_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/updatesmsuser/{storeSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> updateSmsUser(
			HttpServletRequest request,
			@PathVariable("storeSeq") long storeSeq,
			@RequestBody @Validated({StoreMngActiveVO.UpdateAction.class}) StoreMngActiveVO actionVO, BindingResult error) {

		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}

		//수정할 점포 정보가 있는지 체크
		StoreMngActiveVO ro = storeMngService.getByStoreSeq(storeSeq);
		if ( ro == null ){
			throw new AbleRuntimeException("SMS 수신 대상자를 관리할 점포 정보가 없습니다.");
		}
		actionVO.setStoreSeq(ro.getStoreSeq());

		// 수정
		storeMngService.updateSmsUser(actionVO);

		return AbleResponseEntityBuilder.success(null);
	}

	/** 엑셀업로드 */
	@PreAuthorize("hasAuthority('ROLE_STORE_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/storemngexcelupload", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> excelUpload(
			HttpServletRequest request,
			Model model,
			@Validated({ExcelUploadVO.CreateAction.class}) ExcelUploadVO uploadVO, BindingResult error) {
		logger.debug(" ::: Upload ExceluploadVO start :: {}" , uploadVO);

		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}
		ExcelUploadRO ro = storeMngService.insertExcelUpload(uploadVO);
		return AbleResponseEntityBuilder.success(ro);
	}

}