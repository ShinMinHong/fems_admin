package com.firealarm.admin.biz.store.storemng.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

import com.firealarm.admin.appconfig.CodeMap.UPLOAD_SERVICE_TYPE;
import com.firealarm.admin.biz.firedetector.firedetectormng.dao.FireDetectorMngDAO;
import com.firealarm.admin.biz.store.storemng.dao.StoreMngDAO;
import com.firealarm.admin.biz.store.storemng.vo.StoreMngActiveVO;
import com.firealarm.admin.biz.store.storemng.vo.StoreMngExcelDataRO;
import com.firealarm.admin.biz.store.storemng.vo.StoreMngExcelDataVO;
import com.firealarm.admin.biz.store.storemng.vo.StoreMngVO;
import com.firealarm.admin.biz.store.storemng.vo.StoreSmsUserList;
import com.firealarm.admin.biz.store.storesmsusermng.dao.StoreSmsUserMngDAO;
import com.firealarm.admin.biz.store.storesmsusermng.vo.StoreSmsUserMngVO;
import com.firealarm.admin.common.service.CommonCodeMapService;
import com.firealarm.admin.common.service.FileStorageManager;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.common.vo.ExcelUploadRO;
import com.firealarm.admin.common.vo.ExcelUploadVO;
import com.firealarm.admin.common.vo.SelectOption;
import com.firealarm.admin.common.vo.UploadFileVO;
import com.firealarm.admin.security.util.UserSecurityUtil;

import framework.exception.AbleRuntimeException;
import framework.spring.validation.AbleExcelUploadObjectError;
import framework.util.AbleExcelLoader;
import framework.util.AbleUtil;
import framework.vo.SearchMap;

/**
 * 전통시장 점포관리 Service
 * @author rodem4_pc1
 *
 */
@Service
public class StoreMngService extends ServiceSupport {

	@Autowired FileStorageManager fileStorageManager;
	@Autowired CommonCodeMapService commonCodeMapService;
	@Autowired SmartValidator smartValidator;
	@Autowired StoreMngDAO storeMngDAO;
	@Autowired FireDetectorMngDAO fireDetectorMngDAO;
	@Autowired StoreSmsUserMngDAO storeSmsUserMngDAO;

	/** 전체 목록 조회 */
	public List<StoreMngVO> getListAll(Sort sort, SearchMap search) {
		return storeMngDAO.getListAll(sort, search);
	}

	/** 권한코드관리 페이징 조회 */
	public Page<StoreMngVO> getList(Pageable pageable, SearchMap search) {
		return storeMngDAO.getList(pageable, search);
	}

	/** 상세 */
	public StoreMngActiveVO getByStoreSeq(long storeSeq) {
		return storeMngDAO.getByStoreSeq(storeSeq);
	}

	/** 등록 */
	public void insert(StoreMngVO vo) {
		//점포정보 등록
		long storeSeq = storeMngDAO.insert(vo);
		//점포알림제한시간 등록
		storeMngDAO.insertStoreNoAlarm(storeSeq);
	}

	/** 해당 점포에 화제감지기 등록 여부 체크 */
	public boolean hasFireDetector(long storeSeq) {
		return fireDetectorMngDAO.getFireDetectorCnt(storeSeq) > 0;
	}

	/** 삭제 */
	public void delete(long storeSeq) {
		storeMngDAO.delete(storeSeq);
	}

	/** 수정 */
	public void update(StoreMngActiveVO actionVO) {
		// 점포정보 수정
		storeMngDAO.update(actionVO);
		// 점보알림제한정보 수정
		storeMngDAO.updateStoreNoAlarm(actionVO);
	}

	/** SMS 수신 대상자 저장 */
	public void updateSmsUser(StoreMngActiveVO actionVO) {
		// 점포의 기존 SMS 수신 대상자 모두 삭제
		storeSmsUserMngDAO.deleteSmsUserByStoreSeq(actionVO.getStoreSeq());
		// SMS 대상자 정보 재 등록
		if (actionVO.getStoreSmsUserList() != null) {
			for (StoreSmsUserList smsUser : actionVO.getStoreSmsUserList()) {
				//logger.debug(":::::::::::::::::::::::::::::: smsUser: {}", smsUser);
				StoreSmsUserMngVO storeSmsUser = new StoreSmsUserMngVO();
				storeSmsUser.setStoreSeq(actionVO.getStoreSeq());
				storeSmsUser.setManagerName(smsUser.getManagerName());
				storeSmsUser.setPhoneNo(smsUser.getPhoneNo());
				storeSmsUser.setSmsReceiveYn(smsUser.isSmsReceiveYn());

				//sms 수신 대상자 insert
				storeSmsUserMngDAO.insert(storeSmsUser);
			}
		}
	}

	/** 전통시장점포관리 엑셀업로드 */
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
			int rowCnt=sheet.getPhysicalNumberOfRows();

			// 본인이 속한 관제지역의  점포고유번호 리스트
			long mngAreaSeq  = UserSecurityUtil.getCurrentUserDetails().getMngAreaSeq();
			List<SelectOption> marketCodeMap = commonCodeMapService.getMarketNameCodeMapByMngAreaSeq(mngAreaSeq);
			List<String> marketSeqList = marketCodeMap.stream().map(variant -> variant.getValue()).collect(Collectors.toList());

			for(int rowIdx=1; rowIdx<rowCnt; rowIdx++) {
				//실패 상세내용 그리드 행순번을 찍기 위해 사용 - 헤더를 제외하고 첫번째 행을 3번으로 설정
				int printResultRowIdx = rowIdx+1;
				StoreMngExcelDataRO dataRO = new StoreMngExcelDataRO();
				StoreMngExcelDataVO vo = new StoreMngExcelDataVO();

				//VO에 엑셀데이터 바인딩
				vo = (StoreMngExcelDataVO) AbleExcelLoader.getDataFromSheetrow(sheet.getRow(rowIdx), StoreMngExcelDataVO.class );
				vo.setRowIndex(printResultRowIdx);
				//dataVO를 생성하는 과정에서 reflection으로 인하여 downcasting 불가.. copyProperties 사용Z
				PropertyUtils.copyProperties(dataRO, vo);
				String excelField= null;
				AbleExcelUploadObjectError annotationValidationError = AbleUtil.annotationValidationForExcel(vo, smartValidator, messageSource, StoreMngExcelDataVO.CreateAction.class);

				// step1:: >>>> 애노테이션 유효성 검사
				if ( annotationValidationError != null ){
					dataRO.setError(annotationValidationError);
					ro.addFail(dataRO);
					continue;
				}

				excelField = vo.getMarketSeq();
				// step 2 :: 관제지역내에 속해있는 전통시장SEQ가 제대로 입력되었는지 확인
				boolean isSameOrigin = marketSeqList.contains(excelField);
				if(!isSameOrigin) {
					dataRO.setError(new AbleExcelUploadObjectError("", "전통시장고유번호", "marketSeq", "관제지역내에 해당 전통시장 고유번호가 존재하지 않습니다."));
					ro.addFail(dataRO);
					logger.info(".");
					continue;
				}

				// step3:: >>>> 휴대폰번호,일반전화번호 DB 전화번호포멧에 맞춤
				//ex.01011111111 -> 010-1111-1111 형식으로 변환  , 010-1111-1111->010-1111-1111
				String convertPhoneNo ="";
				excelField = vo.getPhoneNo();
				convertPhoneNo = this.convertPhoneNum(excelField);
				dataRO.setPhoneNo(convertPhoneNo);

				convertPhoneNo ="";
				excelField = vo.getTelephoneNo();
				convertPhoneNo = this.convertPhoneNum(excelField);
				dataRO.setTelephoneNo(convertPhoneNo);

				// step4:: >>>> SMS알림 필드값 (Y,N) DB 형식으로 변경(true,false, default true)
				excelField = vo.getSmsAlarmYnString();
				if(!this.hasExcelData(excelField)) {
					excelField = "Y";
					dataRO.setSmsAlarmYn(true);
				}else { // 액셀데이터 있을떄
					if(!(excelField.toUpperCase().equals("Y") || excelField.toUpperCase().equals("N"))) { // Y,N 이외값 값 입력 에러처리
						dataRO.setError(new AbleExcelUploadObjectError("", "SMS알림", "smsAlarmYnString", "SMS알림여부가 'Y'/'N' 형식이 아닙니다."));
						ro.addFail(dataRO);
						logger.info(".에러");
						continue;
					}else { // Y, N 형식으로 들어왔을떄 DB 에 맞는 데이터로 변경
						boolean convertedYN = "Y".equals(excelField.toUpperCase()) ? true : false;
						dataRO.setSmsAlarmYn(convertedYN);
					}
				}

				// step5:: >>>> 119알림 필드값 (Y,N) DB 형식으로 변경(true,false, default true)
				excelField = vo.getFirestationAlarmYnString();
				if(!this.hasExcelData(excelField)) {
					excelField = "Y";
					dataRO.setFirestationAlarmYn(true);
				}else { // 액셀데이터 있을떄
					if(!(excelField.toUpperCase().equals("Y") || excelField.toUpperCase().equals("N"))) { // Y,N 이외값 값 입력 에러처리
						dataRO.setError(new AbleExcelUploadObjectError("", "119알림", "firestationAlarmYnString", "119알림여부가 'Y'/'N' 형식이 아닙니다."));
						ro.addFail(dataRO);
						logger.info(".에러");
						continue;
					}else { // Y, N 형식으로 들어왔을떄 DB 에 맞는 데이터로 변경
						boolean convertedYN = "Y".equals(excelField.toUpperCase()) ? true : false;
						dataRO.setFirestationAlarmYn(convertedYN);
					}
				}

				// - 액셀내 데이터 중복 확인 (전통시장고유번호, 점포명)
				StringBuffer sbExcelUniqueKey = new StringBuffer();
				sbExcelUniqueKey.append('!'+dataRO.getMarketSeq());
				sbExcelUniqueKey.append('!'+dataRO.getStoreName());
				// step6:: >>>> 액셀내 동일한 중복 CTN NO 있는지 확인
				if ( excelUniqueKeyList.contains(sbExcelUniqueKey.toString()) ){
					dataRO.setError(new AbleExcelUploadObjectError("", "점포명", "storeName", "엑셀내에  전통시장고유번호 및 점포명이 동일한 중복건이 존재합니다."));
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
				StoreMngExcelDataRO dataRO = (StoreMngExcelDataRO) dataOb;
				StoreMngActiveVO actionVO = new StoreMngActiveVO();

				actionVO.setMngAreaSeq(UserSecurityUtil.getCurrentUserDetails().getMngAreaSeq());
				long marketSeq = Long.parseLong(dataRO.getMarketSeq());
				String storeName = dataRO.getStoreName();
				actionVO.setMarketSeq(marketSeq);
				actionVO.setStoreName(storeName);
				actionVO.setManagerName(dataRO.getManagerName());
				actionVO.setPhoneNo(dataRO.getPhoneNo());
				actionVO.setTelephoneNo(dataRO.getTelephoneNo());
				actionVO.setZipCode(dataRO.getZipCode());
				actionVO.setRoadAddress(dataRO.getRoadAddress());
				actionVO.setParcelAddress(dataRO.getParcelAddress());
				actionVO.setDetailsAddress(dataRO.getDetailsAddress());
				actionVO.setBusinessDesc(dataRO.getBusinessDesc());
				actionVO.setSmsAlarmYn(dataRO.isSmsAlarmYn());
				actionVO.setFirestationAlarmYn(dataRO.isFirestationAlarmYn());
				// 신윤호부장 요청사항
				// 시장고유번호, 점포명으로 중복체크 후 없으면 insert  있으면 update
				// 위험성에 대해서는 누차 얘기했음.
				Long storeSeq = storeMngDAO.findStoreSeqByID(marketSeq,storeName);
				if(Objects.isNull(storeSeq)) { // 중복된 데이터가 없음, 신규등록
					this.insert(actionVO);
				}else {
					actionVO.setStoreSeq(storeSeq);
					storeMngDAO.update(actionVO);
				}
			}
		}
		return ro;
	}

	/** 액셀 데이터가 입력되었는지 체크 */
	public Boolean hasExcelData(String excelField) {
		return (excelField  != null && !StringUtils.isEmpty(excelField));
	}

	/** 전화번호 형식변환 */
	public String convertPhoneNum(String excelField) {
		excelField = excelField.trim().replace("-", "");
	    if (excelField.length() == 8) {
	    	return excelField.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
	    } else if (excelField.length() == 12) {
	    	return excelField.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
	    }else {
	    	return excelField.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
	    }
	}
}