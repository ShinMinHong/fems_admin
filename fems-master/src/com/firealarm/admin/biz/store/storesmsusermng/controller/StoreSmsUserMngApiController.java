package com.firealarm.admin.biz.store.storesmsusermng.controller;

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
import com.firealarm.admin.biz.store.storesmsusermng.service.StoreSmsUserMngService;
import com.firealarm.admin.biz.store.storesmsusermng.vo.StoreSmsUserMngVO;
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
 * 점포 SMS 수신 대상 관리 API Controller
 * @author rodem4_pc1
 *
 */
@Controller
@RequestMapping(value = "/store/storesmsusermng/api")
@PreAuthorize("hasAnyAuthority('ROLE_STORE_MNG','ROLE_STORE_READ') AND @fireAlarmCustomSecurityService.hasMngArea()")
public class StoreSmsUserMngApiController extends ApiControllerSupport{

	@Autowired StoreSmsUserMngService storeSmsUserMngService;

	/** 목록 조회 */
	public AbleResponseEntity<?> getListAll(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails me,
			@SortDefault(sort="regDate", direction= Direction.DESC) Sort sort, Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);
		if(APP_USER_GRADE.MARKET_ADMIN.equals(me.getRolegroupCode())){
			searchMap.put("marketSeq", me.getMarketSeq());
		}
		List<StoreSmsUserMngVO> list = storeSmsUserMngService.getListAll(sort, searchMap);
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
		Page<StoreSmsUserMngVO> page = storeSmsUserMngService.getList(pageable, searchMap);
		return AbleResponseEntityBuilder.success(page);
	}

	/** 상세 */
	@RequestMapping(value = "/details/{smsUserSeq}", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> details(
			@PathVariable("smsUserSeq") long smsUserSeq,
			Model model) {
		StoreSmsUserMngVO ro = storeSmsUserMngService.getBySmsUserSeq(smsUserSeq);
		return AbleResponseEntityBuilder.success(ro);
	}

	/** 등록 */
	@PreAuthorize("hasAuthority('ROLE_STORE_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/insert", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> insert(
			HttpServletRequest request,
			@RequestBody @Validated({StoreSmsUserMngVO.CreateAction.class}) StoreSmsUserMngVO actionVO, BindingResult error) {
		logger.debug("StoreSmsUserMng Insert Start. VO: {}", actionVO);
		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}

		//관제지역 변경여부 체크
		AppUserDetails me = UserSecurityUtil.getCurrentUserDetails();
		if (actionVO.getMngAreaSeq() != me.getMngAreaSeq().longValue()) {
			throw new AbleRuntimeException("관제지역오류. 새로고침 후 다시 등록해 주세요.");
		}

		if(!storeSmsUserMngService.hasDuplicatedData(actionVO.getStoreSeq(), actionVO.getPhoneNo())) {
			storeSmsUserMngService.insert(actionVO);
		}

		return AbleResponseEntityBuilder.success(null);
	}

	/** 수정 */
	@PreAuthorize("hasAuthority('ROLE_STORE_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/update/{smsUserSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> update(
			HttpServletRequest request,
			@PathVariable("smsUserSeq") long smsUserSeq,
			@RequestBody @Validated({StoreSmsUserMngVO.UpdateAction.class}) StoreSmsUserMngVO actionVO, BindingResult error) {
		logger.debug("StoreSmsUserMng Update Start. SmsUserSeq: {}, vo: {}", smsUserSeq, actionVO);
		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}

		//수정할 대상자가 있는지 체크
		StoreSmsUserMngVO ro = storeSmsUserMngService.getBySmsUserSeq(smsUserSeq);
		if ( ro == null ){
			throw new AbleRuntimeException("수정할 SMS 수신대상자 정보가 없습니다.");
		}
		actionVO.setStoreSeq(ro.getStoreSeq());

		// 수정
		storeSmsUserMngService.update(actionVO);

		return AbleResponseEntityBuilder.success(null);
	}

	/** 삭제 */
	@PreAuthorize("hasAuthority('ROLE_STORE_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/delete/{smsUserSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> delete(
			HttpServletRequest request,
		@PathVariable("smsUserSeq") long smsUserSeq) {
		logger.debug("StoreSmsUserMng Delete Start. SmsUserSeq: {}", smsUserSeq);

		//삭제할 대상자가 있는지 체크
		StoreSmsUserMngVO ro = storeSmsUserMngService.getBySmsUserSeq(smsUserSeq);
		if ( ro == null ){
			throw new AbleRuntimeException("삭제할 SMS 수신대상자 정보가 없습니다.");
		}

		//점포 삭제
		storeSmsUserMngService.delete(smsUserSeq);

		return AbleResponseEntityBuilder.success(null);
	}

	/** 점포 SMS 수신 대상  엑셀업로드 */
	@PreAuthorize("hasAuthority('ROLE_STORE_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/storesmsuserexcelupload", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> excelUpload(
			HttpServletRequest request,
			Model model,
			@Validated({ExcelUploadVO.CreateAction.class}) ExcelUploadVO uploadVO, BindingResult error) {
		logger.debug("StoreSmsUserMng Excel Upload Start.");
		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}
		ExcelUploadRO ro = storeSmsUserMngService.insertExcelUpload(uploadVO);
		return AbleResponseEntityBuilder.success(ro);
	}

}