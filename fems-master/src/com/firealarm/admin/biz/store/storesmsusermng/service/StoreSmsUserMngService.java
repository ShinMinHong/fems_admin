package com.firealarm.admin.biz.store.storesmsusermng.service;

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

import com.firealarm.admin.appconfig.CodeMap.UPLOAD_SERVICE_TYPE;
import com.firealarm.admin.biz.store.storemng.service.StoreMngService;
import com.firealarm.admin.biz.store.storesmsusermng.dao.StoreSmsUserMngDAO;
import com.firealarm.admin.biz.store.storesmsusermng.vo.StoreSmsUserExcelDataRO;
import com.firealarm.admin.biz.store.storesmsusermng.vo.StoreSmsUserExcelDataVO;
import com.firealarm.admin.biz.store.storesmsusermng.vo.StoreSmsUserMngVO;
import com.firealarm.admin.common.service.CommonCodeMapService;
import com.firealarm.admin.common.service.FileStorageManager;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.common.vo.AreaStoreNameVO;
import com.firealarm.admin.common.vo.ExcelUploadRO;
import com.firealarm.admin.common.vo.ExcelUploadVO;
import com.firealarm.admin.common.vo.UploadFileVO;
import com.firealarm.admin.security.util.UserSecurityUtil;

import framework.exception.AbleRuntimeException;
import framework.spring.validation.AbleExcelUploadObjectError;
import framework.util.AbleExcelLoader;
import framework.util.AbleUtil;
import framework.vo.SearchMap;

/**
 * 점포 SMS 수신 대상 관리 Service
 * @author rodem4_pc1
 *
 */
@Service
public class StoreSmsUserMngService extends ServiceSupport {

	@Autowired StoreSmsUserMngDAO storeSmsUserMngDAO;
	@Autowired StoreMngService storeMngService;
	@Autowired FileStorageManager fileStorageManager;
	@Autowired CommonCodeMapService commonCodeMapService;
	@Autowired SmartValidator smartValidator;

	/** 전체 목록 조회 */
	public List<StoreSmsUserMngVO> getListAll(Sort sort, SearchMap search) {
		return storeSmsUserMngDAO.getListAll(sort, search);
	}

	/** 권한코드관리 페이징 조회 */
	public Page<StoreSmsUserMngVO> getList(Pageable pageable, SearchMap search) {
		return storeSmsUserMngDAO.getList(pageable, search);
	}

	/** 상세 */
	public StoreSmsUserMngVO getBySmsUserSeq(long smsUserSeq) {
		return storeSmsUserMngDAO.getBySmsUserSeq(smsUserSeq);
	}

	/** 등록 */
	public void insert(StoreSmsUserMngVO vo) {
		//점포정보 등록
		storeSmsUserMngDAO.insert(vo);
	}

	/** 삭제 */
	public void delete(long smsUserSeq) {
		storeSmsUserMngDAO.delete(smsUserSeq);
	}

	/** 수정 */
	public void update(StoreSmsUserMngVO actionVO) {
		// 점포정보 수정
		storeSmsUserMngDAO.update(actionVO);
	}

	/** 소방 대상물 엑셀업로드 */
	@Transactional
	public ExcelUploadRO insertExcelUpload(ExcelUploadVO uploadVO){
		// 파일저장
		UploadFileVO uploadFileVO = fileStorageManager.saveUploadedFile(UPLOAD_SERVICE_TYPE.EXCEL_UPLOAD , uploadVO.getUploadExcelFile(), null, false);
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
			logger.info("test"+storeSeqList.toString());
			int rowCnt=sheet.getPhysicalNumberOfRows();
			for(int rowIdx=1; rowIdx<rowCnt; rowIdx++) {
				//실패 상세내용 그리드 행순번을 찍기 위해 사용 - 헤더를 제외하고 첫번째 행을 3번으로 설정
				int printResultRowIdx = rowIdx+1;
				StoreSmsUserExcelDataRO dataRO = new StoreSmsUserExcelDataRO();
				StoreSmsUserExcelDataVO vo = new StoreSmsUserExcelDataVO();
				//VO에 엑셀데이터 바인딩
				vo = (StoreSmsUserExcelDataVO) AbleExcelLoader.getDataFromSheetrow(sheet.getRow(rowIdx), StoreSmsUserExcelDataVO.class );
				vo.setRowIndex(printResultRowIdx);
				//dataVO를 생성하는 과정에서 reflection으로 인하여 downcasting 불가.. copyProperties 사용Z
				PropertyUtils.copyProperties(dataRO, vo);
				String excelField= null;

				AbleExcelUploadObjectError annotationValidationError = AbleUtil.annotationValidationForExcel(vo, smartValidator, messageSource, StoreSmsUserExcelDataVO.CreateAction.class);

				// step1:: >>>> 애노테이션 유효성 검사
				if ( annotationValidationError != null ){
					dataRO.setError(annotationValidationError);
					ro.addFail(dataRO);
					continue;
				}

				excelField = vo.getStoreSeq();
				// step2:: >>>> 점포명 숫자여부 확인
				if(!StringUtils.isNumeric(excelField)) {
					dataRO.setError(new AbleExcelUploadObjectError("", "대상물 일련번호", "ojtftSn", "점포명고유번호는 숫자만 입력 가능합니다."));
					ro.addFail(dataRO);
					continue;
				}

				// step3:: >>>> 점포 존재 여부 확인
				boolean isStoreSeq = storeSeqList.contains(Long.valueOf(excelField));
				if(!isStoreSeq) {
					dataRO.setError(new AbleExcelUploadObjectError("", "점포고유번호", "storeSeq", "해당지역의  점포고유번호는 존재하지 않습니다"));
					ro.addFail(dataRO);
					continue;
				}

				// step4:: >>>> 핸드폰 번호 DB전화번호 포멧에 맞게 변환
				//ex.01011111111 -> 010-1111-1111 형식으로 변환  , 010-1111-1111->010-1111-1111
				excelField = vo.getPhoneNo().replace("-", "").trim();
				String convertedPhoneNum = "";
			    if (excelField.length() == 8) {
			    	convertedPhoneNum=excelField.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
			    } else if (excelField.length() == 12) {
			    	convertedPhoneNum=excelField.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
			    }else {
			    	convertedPhoneNum=excelField.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
			    }
			    dataRO.setPhoneNo(convertedPhoneNum);

				// step5:: >>>> SMS 수신여부 유효성 검사
				excelField = vo.getSmsReceiveYnString();
				if(!this.hasExcelData(excelField)) {
					excelField = "Y";
					dataRO.setSmsReceiveYn(true);
				}else { // 액셀데이터 있을떄
					if(!(excelField.toUpperCase().equals("Y") || excelField.toUpperCase().equals("N"))) { // Y,N 이외값 값 입력 에러처리
						dataRO.setError(new AbleExcelUploadObjectError("", "SMS수신여부", "smsReceiveYnString", "SMS 수신여부가 'Y'/'N' 형식이 아닙니다."));
						ro.addFail(dataRO);
						logger.info(".에러");
						continue;
					}else { // Y, N 형식으로 들어왔을떄 DB 에 맞는 데이터로 변경
						boolean convertedYN = "Y".equals(excelField.toUpperCase()) ? true : false;
						dataRO.setSmsReceiveYn(convertedYN);

					}
				}

				// step6:: >>>> 액셀내 중복데이터 검사
				StringBuffer sbExcelUniqueKey = new StringBuffer();
				sbExcelUniqueKey.append('!'+dataRO.getStoreSeq());
				sbExcelUniqueKey.append('!'+dataRO.getPhoneNo());
				if ( excelUniqueKeyList.contains(sbExcelUniqueKey.toString()) ){
					dataRO.setError(new AbleExcelUploadObjectError("", "전화번호", "phoneNo", "액셀 내  동일한 점포명 및 핸드폰 번호가 존재합니다."));
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
		if (ro.getFailCount() == 0 && ro.getTotalCount() > 0 ){ //실패건이 없고 엑셀 데이터가 존재한다면.. DB INSERT/UPDATE 진행
			for( Object dataOb : ro.getExcelResultVO()  ){
				// 등록자 , 수정자 정보
				StoreSmsUserExcelDataRO dataRO = (StoreSmsUserExcelDataRO) dataOb;
				StoreSmsUserMngVO actionVO = new StoreSmsUserMngVO();
				long storeSeq = Long.parseLong(dataRO.getStoreSeq());
				actionVO.setStoreSeq(storeSeq);
				actionVO.setManagerName(dataRO.getManagerName());
				actionVO.setPhoneNo(dataRO.getPhoneNo());
				actionVO.setSmsReceiveYn(dataRO.isSmsReceiveYn());
				if(!this.hasDuplicatedData(storeSeq,dataRO.getPhoneNo())) {
					storeSmsUserMngDAO.insert(actionVO);
				}else {
					long smsUserSeq = storeSmsUserMngDAO.getSmsUserSeqByStoreSeqAndPhoneNo(storeSeq,dataRO.getPhoneNo());
					actionVO.setSmsUserSeq(smsUserSeq);
					storeSmsUserMngDAO.update(actionVO);
				}
			}
		}
		return ro;
	}

	/** 액셀 데이터가 입력되었는지 체크 */
	public boolean hasExcelData(String excelField) {
		return (excelField  != null && !StringUtils.isEmpty(excelField));
	}

	/** 같은 점포내에 동일한 이름이 있는지 중복 체크 */
	public boolean hasDuplicatedData(long storeSeq, String phoneNo){
		return storeSmsUserMngDAO.hasDuplicatedData(storeSeq,phoneNo);
	}

}