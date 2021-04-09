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
			@SortDefault(sort = "fireDetectorSeq", direction=Direction.DESC) Sort sort,
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
	public @ResponseBody @Validated({FireDetectorSetVO.ListView.class,UploadFileVO.ListView.class}) AbleResponseEntity<?> getList(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@PageableDefault(sort = "fireDetectorSeq", direction=Direction.DESC) Pageable pageable,
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
	@RequestMapping(value = "/details/{fireDetectorSeq}", method = RequestMethod.GET)
	public @ResponseBody @Validated({FireDetectorSetVO.DetailsView.class, UploadFileVO.DetailsView.class}) AbleResponseEntity<?> details(
			@PathVariable("fireDetectorSeq") long fireDetectorSeq,
			Model model) {
		FireDetectorSetVO ro = fireDetectorSetService.getByFireDetectionSeq(fireDetectorSeq);
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

		if(actionVO.getStoreSeq()!=null) {// 감지기가 점포 연결이 되었다면 관제지역 유효성 체크
			StoreMngActiveVO ro = storeMngService.getByStoreSeq(actionVO.getStoreSeq());
			if(ro.getMngAreaSeq() != user.getMngAreaSeq().longValue()) {
				throw new AbleRuntimeException("데이터를 작성하는중, 관제지역이 변경되었습니다.");
			}
		}
		actionVO.setRegAdminId(user.getAdminId());

		fireDetectorSetService.insert(actionVO);
		return AbleResponseEntityBuilder.success(null);
	}

	/** 수정 */
	@PreAuthorize("hasAuthority('ROLE_FIRE_DETECTOR_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/update/{fireDetectorSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> update(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
			@PathVariable("fireDetectorSeq") long fireDetectorSeq,
			@ModelAttribute @Validated({FireDetectorSetActiveVO.UpdateAction.class}) FireDetectorSetActiveVO actionVO, BindingResult error) {
		logger.debug("fireDetectorSeq: {}, vo: {}", fireDetectorSeq, actionVO);
		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}

		FireDetectorSetVO vo= fireDetectorSetService.getByFireDetectionSeq(fireDetectorSeq);
		if ( vo == null ){
			throw new AbleRuntimeException("수정하려는 화재감지기가 존재하지 않습니다.");
		}

		if(actionVO.getStoreSeq()!=null) {// 점포 연결된 화재 감지기
			StoreMngActiveVO ro = storeMngService.getByStoreSeq(actionVO.getStoreSeq());
			if(ro.getMngAreaSeq() != user.getMngAreaSeq().longValue()) {
				throw new AbleRuntimeException("데이터의 관제지역과 관리자의 관제지역이 불일치 합니다.");
			}
		}

		actionVO.setFireDetectorSeq(fireDetectorSeq);
		actionVO.setUpdAdminId(user.getAdminId());
		fireDetectorSetService.update(actionVO);

		return AbleResponseEntityBuilder.success(null);
	}

	/** 삭제 */
	@PreAuthorize("hasAuthority('ROLE_FIRE_DETECTOR_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/delete/{fireDetectorSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> delete(
			HttpServletRequest request,
			@AppLoginUser AppUserDetails user,
		@PathVariable("fireDetectorSeq") long fireDetectorSeq) {
		logger.debug("fireDetectorSeq: {}", fireDetectorSeq);
		// 삭제될  화재 감지기 정보
		FireDetectorSetVO vo = fireDetectorSetService.getByFireDetectionSeq(fireDetectorSeq);
		if(vo.getStoreSeq()!=null) { // 점포에 설치된 화재 감지기
			StoreMngActiveVO ro = storeMngService.getByStoreSeq(vo.getStoreSeq());
			if(ro.getMngAreaSeq() != user.getMngAreaSeq().longValue()) { // 관리자와 데이터의 관제지역 일치 여부 확인
				throw new AbleRuntimeException("데이터의 관제지역과 관리자의 관제지역이 불일치 합니다.");
			}
		}
		// 삭제될 화재감지기 존재여부 확인 후 화재감지기 삭제
		if ( vo != null ){
			fireDetectorSetService.delete(fireDetectorSeq);
		}
		return AbleResponseEntityBuilder.success(null);
	}

	/** 파일삭제 */
	@RequestMapping(value = "/details/deletefile/{attachedFileSeq}", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('ROLE_FIRE_DETECTOR_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	public @ResponseBody AbleResponseEntity<?> deletefile(@PathVariable("attachedFileSeq") long attachedFileSeq,
			@AppLoginUser AppUserDetails user){
		// 본부관리자만 파일 삭제 가능 (지역관리자 마켓관리자는 READ기능)
		if(APP_USER_GRADE.HQ_ADMIN.equals(user.getRolegroupCode())) {
			fireDetectorSetService.deletefile(attachedFileSeq);
			return AbleResponseEntityBuilder.success(null);
		} else {
			return AbleResponseEntityBuilder.fail().msg("해당 파일의 삭제 권한이 없습니다.").build();
		}
	}

	/** 파일 다운로드 */
	@RequestMapping(value = "/details/filedownload/{attachedFileSeq}", method = RequestMethod.GET)
	public void download(@PathVariable("attachedFileSeq") long attachedFileSeq, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		logger.debug("attachedFileSeq : {}", attachedFileSeq);
		if(!fireDetectorSetService.hasAttachedFileSeq(attachedFileSeq)) {
			throw new AbleRuntimeException("존재하지 않는 파일 입니다.");
		}
		DownloadFileVO fileInfo = fireDetectorSetService.getFileInfoByAttachedFileSeq(attachedFileSeq);
		fileStorageManager.download(fileInfo.getAttachedFilePath(),fileInfo.getOrginalFileName(),false, request, response);
	}

	/** 점포선택해제 */
	@PreAuthorize("hasAuthority('ROLE_FIRE_DETECTOR_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/details/disconnectionWithStore/{ctnNo}/{fireDetectorSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> disconnectionWithStore(@PathVariable("ctnNo") String ctnNo,@PathVariable("fireDetectorSeq") long fireDetectorSeq) {
		fireDetectorSetService.disconnectionWithStore(ctnNo,fireDetectorSeq);
		return AbleResponseEntityBuilder.success(null);
	}

	/** 감지기 ACK값 변경(슬립요청) */
	@PreAuthorize("hasAuthority('ROLE_FIRE_DETECTOR_MNG') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "details/preventsubmission/{ctnNo}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> preventsubmission(@PathVariable("ctnNo") String ctnNo) {
		fireDetectorSetService.preventsubmission(ctnNo);
		return AbleResponseEntityBuilder.success(null).setMsg("전송금지가 등록 되었습니다");
	}

	/** 엑셀업로드 */
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
		ExcelUploadRO ro = fireDetectorSetService.insertExcelUpload(uploadVO);
		return AbleResponseEntityBuilder.success(ro);
	}
}
