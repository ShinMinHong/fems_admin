package com.firealarm.admin.biz.firedetector.firedetectormng.controller;

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
import com.firealarm.admin.biz.firedetector.firedetectormng.service.FireDetectorMngService;
import com.firealarm.admin.biz.firedetector.firedetectormng.vo.FireDetectorMngActiveVO;
import com.firealarm.admin.biz.firedetector.firedetectormng.vo.FireDetectorMngVO;
import com.firealarm.admin.biz.store.storemng.service.StoreMngService;
import com.firealarm.admin.biz.store.storemng.vo.StoreMngActiveVO;
import com.firealarm.admin.common.service.FileStorageManager;
import com.firealarm.admin.common.support.ApiControllerSupport;
import com.firealarm.admin.common.vo.DownloadFileVO;
import com.firealarm.admin.common.vo.ExcelUploadRO;
import com.firealarm.admin.common.vo.ExcelUploadVO;
import com.firealarm.admin.common.vo.UploadFileVO;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.exception.AbleRuntimeException;
import framework.exception.AbleValidationException;
import framework.spring.web.rest.AbleResponseEntity;
import framework.spring.web.rest.AbleResponseEntityBuilder;
import framework.vo.SearchMap;

/**
 * ??????????????? ?????? Api Controller
 * @author JKS
 */
@Controller
@PreAuthorize("hasAnyAuthority('ROLE_FIRE_DETECTOR_MNG','ROLE_FIRE_DETECTOR_READ') AND @fireAlarmCustomSecurityService.hasMngArea()")
@RequestMapping(value = "/firedetector/firedetectormng/api")
public class FireDetectorMngApiController extends ApiControllerSupport {

@Autowired FireDetectorMngService fireDetectorMngService;
@Autowired StoreMngService storeMngService;
@Autowired FileStorageManager fileStorageManager;

	/** ?????? ?????? */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public AbleResponseEntity<?> getListAll(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@SortDefault(sort = "fireDetectorSeq", direction=Direction.DESC) Sort sort,
			Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);

		// ?????????????????? ?????? ????????? ??????
		if(APP_USER_GRADE.MARKET_ADMIN.equals(user.getRolegroupCode())) {
			searchMap.put("marketSeq", user.getMarketSeq());
		}

		// ??????????????????(????????? ASC, CTN?????? DESC, default ????????? DESC)
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
		List<FireDetectorMngVO> list = fireDetectorMngService.getListAll(sort, searchMap);
		return AbleResponseEntityBuilder.success(list);
	}

	/** ????????? ?????? */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody @Validated({FireDetectorMngVO.ListView.class,UploadFileVO.ListView.class}) AbleResponseEntity<?> getList(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@PageableDefault(sort = "fireDetectorSeq", direction=Direction.DESC) Pageable pageable,
			Model model) {
		SearchMap searchMap = SearchMap.buildFrom(request);

		// ?????????????????? ?????? ????????? ??????
		if(APP_USER_GRADE.MARKET_ADMIN.equals(user.getRolegroupCode())) {
			searchMap.put("marketSeq", user.getMarketSeq());
		}

		// ??????????????????(????????? ASC, CTN?????? DESC, default ????????? DESC)
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
		Page<FireDetectorMngVO> page = fireDetectorMngService.getList(pageable, searchMap);
		return AbleResponseEntityBuilder.success(page);
	}

	/** ?????? */
	@RequestMapping(value = "/details/{fireDetectorSeq}", method = RequestMethod.GET)
	public @ResponseBody @Validated({FireDetectorMngVO.DetailsView.class, UploadFileVO.DetailsView.class}) AbleResponseEntity<?> details(
			@PathVariable("fireDetectorSeq") long fireDetectorSeq,
			Model model) {
		FireDetectorMngVO ro = fireDetectorMngService.getByFireDetectionSeq(fireDetectorSeq);
		logger.debug("detail FireDetectorMngVO start : {}", ro);
		return AbleResponseEntityBuilder.success(ro);
	}

	/** ?????? */
	@PreAuthorize("hasAuthority('ROLE_FIRE_DETECTOR_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/insert", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> insert(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@ModelAttribute @Validated({FireDetectorMngActiveVO.CreateAction.class}) FireDetectorMngActiveVO actionVO, BindingResult error) {
		logger.debug("insert FireDetectorMngActiveVO start: {}", actionVO);

		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}

		if(actionVO.getStoreSeq()!=null) {// ???????????? ?????? ????????? ???????????? ???????????? ????????? ??????
			StoreMngActiveVO ro = storeMngService.getByStoreSeq(actionVO.getStoreSeq());
			if(ro.getMngAreaSeq() != user.getMngAreaSeq().longValue()) {
				throw new AbleRuntimeException("???????????? ???????????????, ??????????????? ?????????????????????.");
			}
		}
		actionVO.setRegAdminId(user.getAdminId());

		fireDetectorMngService.insert(actionVO);
		return AbleResponseEntityBuilder.success(null);
	}

	/** ?????? */
	@PreAuthorize("hasAuthority('ROLE_FIRE_DETECTOR_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/update/{fireDetectorSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> update(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@PathVariable("fireDetectorSeq") long fireDetectorSeq,
			@ModelAttribute @Validated({FireDetectorMngActiveVO.UpdateAction.class}) FireDetectorMngActiveVO actionVO, BindingResult error) {
		logger.debug("fireDetectorSeq: {}, vo: {}", fireDetectorSeq, actionVO);
		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}

		FireDetectorMngVO vo= fireDetectorMngService.getByFireDetectionSeq(fireDetectorSeq);
		if ( vo == null ){
			throw new AbleRuntimeException("??????????????? ?????????????????? ???????????? ????????????.");
		}

		if(actionVO.getStoreSeq()!=null) {// ?????? ????????? ?????? ?????????
			StoreMngActiveVO ro = storeMngService.getByStoreSeq(actionVO.getStoreSeq());
			if(ro.getMngAreaSeq() != user.getMngAreaSeq().longValue()) {
				throw new AbleRuntimeException("???????????? ??????????????? ???????????? ??????????????? ????????? ?????????.");
			}
		}

		actionVO.setFireDetectorSeq(fireDetectorSeq);
		actionVO.setUpdAdminId(user.getAdminId());
		fireDetectorMngService.update(actionVO);

		return AbleResponseEntityBuilder.success(null);
	}

	/** ?????? */
	@PreAuthorize("hasAuthority('ROLE_FIRE_DETECTOR_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/delete/{fireDetectorSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> delete(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
		@PathVariable("fireDetectorSeq") long fireDetectorSeq) {
		logger.debug("fireDetectorSeq: {}", fireDetectorSeq);
		// ?????????  ?????? ????????? ??????
		FireDetectorMngVO vo = fireDetectorMngService.getByFireDetectionSeq(fireDetectorSeq);
		if(vo.getStoreSeq()!=null) { // ????????? ????????? ?????? ?????????
			StoreMngActiveVO ro = storeMngService.getByStoreSeq(vo.getStoreSeq());
			if(ro.getMngAreaSeq() != user.getMngAreaSeq().longValue()) { // ???????????? ???????????? ???????????? ?????? ?????? ??????
				throw new AbleRuntimeException("???????????? ??????????????? ???????????? ??????????????? ????????? ?????????.");
			}
		}
		// ????????? ??????????????? ???????????? ?????? ??? ??????????????? ??????
		if ( vo != null ){
			fireDetectorMngService.delete(fireDetectorSeq);
		}
		return AbleResponseEntityBuilder.success(null);
	}

	/** ???????????? */
	@RequestMapping(value = "/details/deletefile/{attachedFileSeq}", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('ROLE_FIRE_DETECTOR_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	public @ResponseBody AbleResponseEntity<?> deletefile(@PathVariable("attachedFileSeq") long attachedFileSeq,
			@AppLoginUser AppUserDetails user){
		// ?????????????????? ?????? ?????? ?????? (??????????????? ?????????????????? READ??????)
		if(APP_USER_GRADE.HQ_ADMIN.equals(user.getRolegroupCode())) {
			fireDetectorMngService.deletefile(attachedFileSeq);
			return AbleResponseEntityBuilder.success(null);
		} else {
			return AbleResponseEntityBuilder.fail().msg("?????? ????????? ?????? ????????? ????????????.").build();
		}
	}

	/** ?????? ???????????? */
	@RequestMapping(value = "/details/filedownload/{attachedFileSeq}", method = RequestMethod.GET)
	public void download(@PathVariable("attachedFileSeq") long attachedFileSeq, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		logger.debug("attachedFileSeq : {}", attachedFileSeq);
		if(!fireDetectorMngService.hasAttachedFileSeq(attachedFileSeq)) {
			throw new AbleRuntimeException("???????????? ?????? ?????? ?????????.");
		}
		DownloadFileVO fileInfo = fireDetectorMngService.getFileInfoByAttachedFileSeq(attachedFileSeq);
		fileStorageManager.download(fileInfo.getAttachedFilePath(),fileInfo.getOrginalFileName(),false, request, response);
	}

	/** ?????????????????? */
	@PreAuthorize("hasAuthority('ROLE_FIRE_DETECTOR_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/disconnectionWithStore/{ctnNo}/{fireDetectorSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> disconnectionWithStore(@PathVariable("ctnNo") String ctnNo,@PathVariable("fireDetectorSeq") long fireDetectorSeq) {
		fireDetectorMngService.disconnectionWithStore(ctnNo,fireDetectorSeq);
		return AbleResponseEntityBuilder.success(null);
	}

	/** ????????? ACK??? ??????(????????????) */
	@PreAuthorize("hasAuthority('ROLE_FIRE_DETECTOR_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "details/preventsubmission/{ctnNo}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> preventsubmission(@PathVariable("ctnNo") String ctnNo) {
		fireDetectorMngService.preventsubmission(ctnNo);
		return AbleResponseEntityBuilder.success(null).setMsg("??????????????? ?????? ???????????????");
	}

	/** ??????????????? */
	@PreAuthorize("hasAuthority('ROLE_FIRE_DETECTOR_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/firedetectorexcelupload", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> excelUpload(
			HttpServletRequest request,
			Model model,
			@Validated({ExcelUploadVO.CreateAction.class}) ExcelUploadVO uploadVO, BindingResult error) {
		logger.debug(" ::: Upload ExceluploadVO start :: {}" , uploadVO);

		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}
		ExcelUploadRO ro = fireDetectorMngService.insertExcelUpload(uploadVO);
		return AbleResponseEntityBuilder.success(ro);
	}
}
