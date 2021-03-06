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
import com.firealarm.admin.biz.areasystem.firedetectorset.vo.FireDetectorSetActiveVO;
import com.firealarm.admin.biz.areasystem.firedetectorset.vo.FireDetectorSetVO;
import com.firealarm.admin.biz.store.storemng.service.StoreMngService;
import com.firealarm.admin.common.service.CommonCodeMapService;
import com.firealarm.admin.common.service.FileStorageManager;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.common.vo.AreaStoreNameVO;
import com.firealarm.admin.security.util.UserSecurityUtil;

import framework.exception.AbleRuntimeException;
import framework.spring.validation.AbleExcelUploadObjectError;
import framework.util.AbleExcelLoader;
import framework.util.AbleUtil;
import framework.vo.SearchMap;


/**
 * 화재감지기 설정 관리 Service
 * @author SMH
 */
@Service
public class FireDetectorSetService extends ServiceSupport {

	@Autowired FireDetectorSetDAO FireDetectorSetDAO;
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

	/** 상세 */
	public FireDetectorSetVO getByFireDetectorSetSeq(long fireDetectorSetSeq) {
		FireDetectorSetVO vo = FireDetectorSetDAO.getByFireDetectorSetSeq(fireDetectorSetSeq);
		return vo;
	}

	/** 등록[화재감지기 설정] */
	@Transactional
	public void insert(FireDetectorSetActiveVO vo) {
		//화재감지기 설정 등록
		long fireDetectorSetSeq = FireDetectorSetDAO.insert(vo);
		vo.setFireDetectorSetSeq(fireDetectorSetSeq);
	}

	/** 수정 */
	@Transactional
	public void update(FireDetectorSetActiveVO vo) {
		// 감지기 정보 수정
		FireDetectorSetDAO.update(vo);
	}

	/** 화재감지기 삭제 */
	@Transactional
	public void delete(long fireDetectorSetSeq) {
		// DB삭제 ([화재감지기 파일 /현재상태 ]ON DELETE CASCADE)
		FireDetectorSetDAO.delete(fireDetectorSetSeq);
	}
}
