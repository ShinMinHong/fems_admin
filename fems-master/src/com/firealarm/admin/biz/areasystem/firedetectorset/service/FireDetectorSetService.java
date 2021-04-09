package com.firealarm.admin.biz.areasystem.firedetectorset.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.SmartValidator;
import org.springframework.web.multipart.MultipartFile;

import com.firealarm.admin.appconfig.CodeMap.UPLOAD_SERVICE_TYPE;
import com.firealarm.admin.biz.areasystem.firedetectorset.dao.FireDetectorSetDAO;
import com.firealarm.admin.biz.areasystem.firedetectorset.vo.FireDetectorExcelDataRO;
import com.firealarm.admin.biz.areasystem.firedetectorset.vo.FireDetectorExcelDataVO;
import com.firealarm.admin.biz.areasystem.firedetectorset.vo.FireDetectorSetActiveVO;
import com.firealarm.admin.biz.areasystem.firedetectorset.vo.FireDetectorSetVO;
import com.firealarm.admin.biz.store.storemng.service.StoreMngService;
import com.firealarm.admin.common.service.CommonCodeMapService;
import com.firealarm.admin.common.service.FileStorageManager;
import com.firealarm.admin.common.service.FireDetectorNowStatusService;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.common.vo.AreaStoreNameVO;
import com.firealarm.admin.common.vo.DownloadFileVO;
import com.firealarm.admin.common.vo.ExcelUploadRO;
import com.firealarm.admin.common.vo.ExcelUploadVO;
import com.firealarm.admin.common.vo.FireDetectorNowStatusDT;
import com.firealarm.admin.common.vo.UploadFileVO;
import com.firealarm.admin.security.util.UserSecurityUtil;

import framework.exception.AbleRuntimeException;
import framework.spring.validation.AbleExcelUploadObjectError;
import framework.util.AbleExcelLoader;
import framework.util.AbleUtil;
import framework.vo.SearchMap;


/**
 * 화재감지기 관리 Service
 * @author JKS
 */
@Service
public class FireDetectorSetService extends ServiceSupport {

	@Autowired FileStorageManager fileStorageManager;
	@Autowired FireDetectorNowStatusService fireDetectorNowStatusService;
	@Autowired FireDetectorSetDAO FireDetectorSetDAO;
	@Autowired StoreMngService storeMngService;
	@Autowired CommonCodeMapService commonCodeMapService;
	@Autowired SmartValidator smartValidator;

	/** 전체 목록 조회 */
	public List<FireDetectorSetVO> getListAll(Sort sort, SearchMap search) {
		return FireDetectorSetDAO.getListAll(sort, search);
	}

	/** 페이징 목록 조회 */
	public Page<FireDetectorSetVO> getList(Pageable pageable, SearchMap search) {
		return FireDetectorSetDAO.getList(pageable, search);
	}

	/** 파일 상세 */
	public DownloadFileVO getFileInfoByAttachedFileSeq(long attachedFileSeq) {
		return FireDetectorSetDAO.getFileInfoByAttachedFileSeq(attachedFileSeq);
	}

	/** 파일  존재유무 */
	public Boolean hasAttachedFileSeq(long attachedFileSeq) {
		return FireDetectorSetDAO.hasAttachedFileSeq(attachedFileSeq);
	}

	/** 상세 */
	public FireDetectorSetVO getByFireDetectionSeq(long fireDetectorSeq) {
		FireDetectorSetVO vo = FireDetectorSetDAO.getByFireDetectionSeq(fireDetectorSeq);
		List<FireDetectorNowStatusDT> fireDetectorNowStatus = FireDetectorSetDAO.getFireDetectorNowStatusDT(fireDetectorSeq);
		vo.setFireDetectorNowStatusDT(fireDetectorNowStatus);
		vo.setBoardFiles(FireDetectorSetDAO.getFileListByFireDetectorSeq(fireDetectorSeq));
		return vo;
	}

	/** 등록[화재감지기/화재감지기파일/화재감지기현재상태] */
	@Transactional
	public void insert(FireDetectorSetActiveVO vo) {
		// CTN 번호 중복 확인
		if(FireDetectorSetDAO.hasDuplicatedCtnNo(vo.getCtnNo())) {
			throw new AbleRuntimeException("CTN 번호가 중복되었습니다.");
		}

		//화재감지기 등록 , 화재 감지기 현재상태 초기화 한 후 등록
		long fireDetectorSeq = FireDetectorSetDAO.insert(vo);
		vo.setFireDetectorSeq(fireDetectorSeq);
		FireDetectorNowStatusDT dt =FireDetectorNowStatusDT.builder()
			.fireDetectorSeq(fireDetectorSeq)
			.alarmFire(false)
			.smokeEvent(false)
			.temperatureEvent(false)
			.flameEvent(false)
			.coEvent(false)
			.notFireYn(false)
			.batteryValue(100)
			.battery2Value(100)
			.build();
		fireDetectorNowStatusService.insert(dt);

		// 파일 업로드
		List<MultipartFile> uploadfiles =vo.getFileInput();
		if (null != uploadfiles) {
			// 파일 저장
			List<UploadFileVO> uplodedFiles = fileStorageManager.saveUploadFiles(uploadfiles,UPLOAD_SERVICE_TYPE.FIREDETECTOR,false);
			// 파일 정보 DB Insert
			if (uplodedFiles.size() > 0) {
				FireDetectorSetDAO.insertFileInfoListToDB(uplodedFiles,fireDetectorSeq);
			}
		}
	}

	/** 수정 */
	@Transactional
	public void update(FireDetectorSetActiveVO vo) {
		// 감지기 정보 수정
		FireDetectorSetDAO.update(vo);

		List<MultipartFile> uploadfiles = vo.getFileInput();
		if (null != uploadfiles) {
			// 파일 저장
			List<UploadFileVO> uplodedFiles = fileStorageManager.saveUploadFiles(uploadfiles,UPLOAD_SERVICE_TYPE.FIREDETECTOR,false);
			logger.debug("uplodedFiles ==> {}", uplodedFiles);
			if (uplodedFiles.size() > 0) {
				// 파일 정보 DB Insert
				FireDetectorSetDAO.insertFileInfoListToDB(uplodedFiles, vo.getFireDetectorSeq());
			}
		}
	}

	/** 화재감지기 삭제 */
	@Transactional
	public void delete(long fireDetectorSeq) {
		//파일들 삭제
		List<Long> attachedFileSeqList =FireDetectorSetDAO.getAttachedFileSeqByFireDetectorSeq(fireDetectorSeq);
		if(attachedFileSeqList != null) {
			for(Long attachedFileSeq : attachedFileSeqList) {
				this.deletefile(attachedFileSeq);
			}
		}
		// DB삭제 ([화재감지기 파일 /현재상태 ]ON DELETE CASCADE)
		FireDetectorSetDAO.delete(fireDetectorSeq);
	}

	/** 파일 단건 삭제 */
	@Transactional
	public void deletefile(long attachedFileSeq) {
		String pathToDelete = FireDetectorSetDAO.getFilePathByAttachedFileSeq(attachedFileSeq);
		fileStorageManager.deleteUploadedFile(pathToDelete, false);
		FireDetectorSetDAO.deleteFileByAttachedFileSeq(attachedFileSeq);
	}

	/** 점포선택해제 */
	public void disconnectionWithStore(String ctnNo, long fireDetectorSeq) {

		if(FireDetectorSetDAO.hasDuplicatedCtnNo(ctnNo)) {
			// 화재감지기 점포정보 초기화
			FireDetectorSetDAO.disconnectionWithStore(ctnNo);
			// 현재 상태 로그 초기화
			FireDetectorNowStatusDT dt =FireDetectorNowStatusDT.builder()
					.fireDetectorSeq(fireDetectorSeq)
					.alarmFire(false)
					.smokeEvent(false)
					.temperatureEvent(false)
					.flameEvent(false)
					.coEvent(false)
					.notFireYn(false)
					.batteryValue(100)
					.battery2Value(100)
					.build();
					fireDetectorNowStatusService.update(dt);
		}
	}

	/** 화재감지기 엑셀업로드 */
	@Transactional
	public ExcelUploadRO insertExcelUpload(ExcelUploadVO uploadVO){

		// 파일저장
		UploadFileVO uploadFileVO = fileStorageManager.saveUploadedFile(UPLOAD_SERVICE_TYPE.EXCEL_UPLOAD , uploadVO.getUploadExcelFile(), null, false);
		//
		String filePathToSaveAtUploadRoot = uploadFileVO.getAttachedFilePath();
		String filePath = appConfig.getNasPath() + filePathToSaveAtUploadRoot;

		//반환객체
		ExcelUploadRO ro = new ExcelUploadRO();
		List<String> excelUniqueKeyList = new ArrayList<String>();
		try {
			//엑셀을 읽어오는 부분
			Sheet sheet = AbleExcelLoader.getSheetFromExcel(filePath, 0);

			// 해당관제지역의 점포고유번호 리스트
			long mngAreaSeq  = UserSecurityUtil.getCurrentUserDetails().getMngAreaSeq();
			List<AreaStoreNameVO> storeNameList = commonCodeMapService.getStoreNameListByMngAreaSeq(mngAreaSeq);
			List<Long> storeSeqList = storeNameList.stream().map(variant -> variant.getStoreSeq()).collect(Collectors.toList());

			int rowCnt=sheet.getPhysicalNumberOfRows();
			for(int rowIdx=1; rowIdx<rowCnt; rowIdx++) {
				//실패 상세내용 그리드 행순번을 찍기 위해 사용 - 헤더를 제외하고 첫번째 행을 3번으로 설정
				int printResultRowIdx = rowIdx+1;
				FireDetectorExcelDataRO dataRO = new FireDetectorExcelDataRO();
				FireDetectorExcelDataVO vo = new FireDetectorExcelDataVO();

				//VO에 엑셀데이터 바인딩
				vo = (FireDetectorExcelDataVO) AbleExcelLoader.getDataFromSheetrow(sheet.getRow(rowIdx), FireDetectorExcelDataVO.class );
				vo.setRowIndex(printResultRowIdx);
				//dataVO를 생성하는 과정에서 reflection으로 인하여 downcasting 불가.. copyProperties 사용Z
				PropertyUtils.copyProperties(dataRO, vo);
				String excelField= null;

				AbleExcelUploadObjectError annotationValidationError = AbleUtil.annotationValidationForExcel(vo, smartValidator, messageSource, FireDetectorExcelDataVO.CreateAction.class);

				// step1:: >>>> 애노테이션 유효성 검사
				if ( annotationValidationError != null ){
					dataRO.setError(annotationValidationError);
					ro.addFail(dataRO);
					continue;
				}

				// step2:: >>>> 화재감지기 상점정보가 있을떄 필수위치정보 있는지 체크 및 해당관제지역에 상점정보존재유무 확인
				excelField = vo.getStoreSeq();
				if(this.hasExcelData(excelField)) {

					boolean isStoreSeq = storeSeqList.contains(Long.valueOf(excelField));
					if(!isStoreSeq) {
						dataRO.setError(new AbleExcelUploadObjectError("", "점포고유번호", "storeSeq", "해당지역의  점포고유번호는 존재하지 않습니다."));
						ro.addFail(dataRO);
						continue;
					}

					if(storeMngService.getByStoreSeq(Long.parseLong(excelField))==null) {
						dataRO.setError(new AbleExcelUploadObjectError("", "점포명", "storeSeq", "해당 점포고유번호의 점포가 존재하지 않습니다."));
						ro.addFail(dataRO);
						continue;
					}
					if(!StringUtils.isNumeric(excelField)) {
						dataRO.setError(new AbleExcelUploadObjectError("", "점포명고유번호", "storeSeq", "점포고유번호는 숫자만 입력 가능합니다."));
						ro.addFail(dataRO);
						continue;
					}else { // 액셀 데이터가 있고 점포 고유번호가 입력되었을떄 필수 입력사항 처리
						if(!this.hasExcelData(vo.getZipCode())) {
							dataRO.setError(new AbleExcelUploadObjectError("", "우편번호", "zipCode", "점포고유번호 입력시 우편번호는 필수 입력 사항입니다."));
							ro.addFail(dataRO);
							continue;
						}
						if(!this.hasExcelData(vo.getRoadAddress())) {
							dataRO.setError(new AbleExcelUploadObjectError("", "도로명주소", "roadAddress", "점포고유번호 입력시 도로명주소는 필수 입력 사항입니다."));
							ro.addFail(dataRO);
							continue;
						}
						if(!this.hasExcelData(vo.getParcelAddress())) {
							dataRO.setError(new AbleExcelUploadObjectError("", "지번주소", "parcelAddress", "점포고유번호 입력시 지번주소는 필수 입력 사항입니다."));
							ro.addFail(dataRO);
							continue;
						}
						if(!this.hasExcelData(vo.getLatitude())) {
							dataRO.setError(new AbleExcelUploadObjectError("", "위도", "latitude", "점포고유번호 입력시 위도는 필수 입력 사항입니다."));
							ro.addFail(dataRO);
							continue;
						}
						if(!this.hasExcelData(vo.getLongitude())) {
							dataRO.setError(new AbleExcelUploadObjectError("", "경도", "longitude", "점포고유번호 입력시 경도는 필수 입력 사항입니다."));
							ro.addFail(dataRO);
							continue;
						}
						if(!this.hasExcelData(vo.getInstallPlace())) {
							dataRO.setError(new AbleExcelUploadObjectError("", "설치위치", "installPlace", "점포고유번호 입력시 설치위치는 필수 입력 사항입니다."));
							ro.addFail(dataRO);
							continue;
						}
					}
				}

				StringBuffer sbExcelUniqueKey = new StringBuffer();
				sbExcelUniqueKey.append('!'+dataRO.getCtnNo());
				// step3:: >>>> 액셀내 동일한 중복 CTN NO 있는지 확인
				if ( excelUniqueKeyList.contains(sbExcelUniqueKey.toString()) ){
					dataRO.setError(new AbleExcelUploadObjectError("", "CTN번호", "ctnNo", "엑셀내에  CTN 번호가 동일한 중복건이 존재합니다."));
					ro.addFail(dataRO);
					continue;
				}
				excelUniqueKeyList.add(sbExcelUniqueKey.toString());
				dataRO.setSuccess();
				ro.addSuccess(dataRO);
			}
		} catch (Exception e) {
			e.getMessage(); //시큐어코딩 가이드
			throw new AbleRuntimeException(e.getMessage());
		}

		// DB 등록
		if (ro.getFailCount() == 0 && ro.getTotalCount() > 0 ){ //실패건 없을시  DB 등록
			for( Object dataOb : ro.getExcelResultVO() ){
				FireDetectorExcelDataRO dataRO = (FireDetectorExcelDataRO) dataOb;
				FireDetectorSetActiveVO actionVO = new FireDetectorSetActiveVO();
				// 등록자 , 수정자 정보
				String adminId = UserSecurityUtil.getCurrentUserDetails().getAdminId();
				actionVO.setCtnNo(dataRO.getCtnNo());
				actionVO.setUsimNo(dataRO.getUsimNo());
				actionVO.setSerialNo(dataRO.getSerialNo());
				if(this.hasExcelData(dataRO.getProductNo())) {actionVO.setProductNo(dataRO.getProductNo());}
				if(this.hasExcelData(dataRO.getModelNo())) {actionVO.setModelNo(dataRO.getModelNo());}
				if(this.hasExcelData(dataRO.getStoreSeq())) {actionVO.setStoreSeq(Long.parseLong(dataRO.getStoreSeq()));}
				actionVO.setZipCode(dataRO.getZipCode());
				actionVO.setRoadAddress(dataRO.getRoadAddress());
				actionVO.setParcelAddress(dataRO.getParcelAddress());
				actionVO.setLatitude(dataRO.getLatitude());
				actionVO.setLongitude(dataRO.getLongitude());
				actionVO.setInstallPlace(dataRO.getInstallPlace());
				actionVO.setFireDetectorName(dataRO.getFireDetectorName());

				if(FireDetectorSetDAO.hasDuplicatedCtnNo(actionVO.getCtnNo())){//  중복된 CTN 있을시 화재감지기 Update
					actionVO.setUpdAdminId(adminId);
					FireDetectorSetDAO.update(actionVO);
				}else{ // 중복된 CTN 없을시 화재감지기 신규등록
					actionVO.setFireDetectorAckValue("0"); // ACK VALUE 초기화 상태
					actionVO.setRegAdminId(adminId);
					// 화재수신기 정보 등록
					long fireDetectorSeq = FireDetectorSetDAO.insert(actionVO);
					// 화재수신기 현재상태 정보 등록
					FireDetectorNowStatusDT dt =FireDetectorNowStatusDT.builder()
							.fireDetectorSeq(fireDetectorSeq)
							.alarmFire(false)
							.smokeEvent(false)
							.temperatureEvent(false)
							.flameEvent(false)
							.coEvent(false)
							.notFireYn(false)
							.batteryValue(100)
							.battery2Value(100)
							.build();
					fireDetectorNowStatusService.insert(dt);
				}
			}
		}
		return ro;
	}

	/** 액셀 데이터가 입력되었는지 체크 */
	public Boolean hasExcelData(String excelField) {
		return (excelField  != null && !StringUtils.isEmpty(excelField));
	}

	/** 사용중지[슬립요청] 으로 ACK 신호 변경 */
	public void preventsubmission(String ctnNo) {
		if(FireDetectorSetDAO.hasDuplicatedCtnNo(ctnNo)) {
			FireDetectorSetDAO.preventsubmission(ctnNo);
		}else {
			throw new AbleRuntimeException("전송중단 할려는  감지기 정보가 존재하지 않습니다. 다시 한번 확인해 주세요");
		}
	}
}
