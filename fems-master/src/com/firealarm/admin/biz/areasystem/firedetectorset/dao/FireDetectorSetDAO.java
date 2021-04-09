package com.firealarm.admin.biz.areasystem.firedetectorset.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.firealarm.admin.biz.areasystem.firedetectorset.vo.FireDetectorSetActiveVO;
import com.firealarm.admin.biz.areasystem.firedetectorset.vo.FireDetectorSetVO;
import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.common.vo.DownloadFileVO;
import com.firealarm.admin.common.vo.FireDetectorNowStatusDT;
import com.firealarm.admin.common.vo.UploadFileVO;
import com.firealarm.admin.security.util.UserSecurityUtil;

import framework.util.AbleUtil;
import framework.vo.SearchMap;

/**
 * 화재감지기 관리 DAO
 * @author JKS
 *
 */
@Repository
public class FireDetectorSetDAO extends DAOSupport {

	/** 전체 목록 조회 */
	public List<FireDetectorSetVO> getListAll(Sort sort, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sort", prepareSortParameter(sort, FireDetectorSetVO.class));
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		List<FireDetectorSetVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		return list;
	}

	/** 페이지 목록 조회 */
	public Page<FireDetectorSetVO> getList(Pageable pageable, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		int total = sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName() + "Count", param);
		Pageable pageableToApply = ensureValidPageable(pageable, total);
		param.put("page", pageableToApply);
		param.put("sort", prepareSortParameter(pageableToApply.getSort(), FireDetectorSetVO.class));
		List<FireDetectorSetVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName() + "Page", param);
		Page<FireDetectorSetVO> page = new PageImpl<FireDetectorSetVO>(list, pageableToApply, total);
		return page;
	}

	/** 화재감지기 상세 */
	public FireDetectorSetVO getByFireDetectionSeq(long fireDetectorSeq) {
		return sqlSession.selectOne(mapperNamespace  + AbleUtil.getCurrentMethodName(), fireDetectorSeq);
	}

	/**파일 목록 */
	public List<UploadFileVO> getFileListByFireDetectorSeq(long fireDetectorSeq) {
		return sqlSession.selectList(mapperNamespace  + AbleUtil.getCurrentMethodName(), fireDetectorSeq);
	}

	/** 파일 상세 */
	public DownloadFileVO getFileInfoByAttachedFileSeq(long attachedFileSeq) {
		return sqlSession.selectOne(mapperNamespace  + AbleUtil.getCurrentMethodName(), attachedFileSeq);
	}

	/** 화재감지기 등록 */
	public long insert(FireDetectorSetActiveVO vo) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("vo", vo);
		sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		return vo.getFireDetectorSeq();
	}

	/** 파일  리스트 DB 등록 */
	public void insertFileInfoListToDB(List<UploadFileVO> fileList,long fireDetectorSeq) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("fileList", fileList);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		param.put("fireDetectorSeq", fireDetectorSeq);
		sqlSession.insert(mapperNamespace  + AbleUtil.getCurrentMethodName(), param);
	}

	/** 화재감지기 수정 */
	public int update(FireDetectorSetActiveVO vo) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("vo", vo);
		return sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 화재감지기 삭제 */
	public int delete(long fireDetectorSeq) {
		return sqlSession.delete(mapperNamespace + AbleUtil.getCurrentMethodName(), fireDetectorSeq);
	}

	/** 파일경로  */
	public String getFilePathByAttachedFileSeq(long attachedFileSeq) {
		return sqlSession.selectOne(mapperNamespace  + AbleUtil.getCurrentMethodName(), attachedFileSeq);
	}

	/** 파일 삭제 */
	public void deleteFileByAttachedFileSeq(long attachedFileSeq) {
		sqlSession.delete(mapperNamespace  + AbleUtil.getCurrentMethodName(), attachedFileSeq);
	}

	/** 해당 점포에 설치된 화제감지기 수량 체크 */
	public int getFireDetectorCnt(long storeSeq) {
		return sqlSession.selectOne(mapperNamespace  + AbleUtil.getCurrentMethodName(), storeSeq);
	}

	/** CTN 중복 여부 */
	public Boolean hasDuplicatedCtnNo(String ctnNo) {
		return sqlSession.selectOne(mapperNamespace  + AbleUtil.getCurrentMethodName(), ctnNo);
	}

	/** 파일 Sequence */
	public Boolean hasAttachedFileSeq(long attachedFileSeq) {
		return sqlSession.selectOne(mapperNamespace  + AbleUtil.getCurrentMethodName(), attachedFileSeq);
	}

	/** 화재감지기 하위 File Sequence List */
	public List<Long> getAttachedFileSeqByFireDetectorSeq(long fireDetectorSeq) {
		 return sqlSession.selectList(mapperNamespace  + AbleUtil.getCurrentMethodName(), fireDetectorSeq);
	}

	/** 점포선택해제 */
	public void disconnectionWithStore(String ctnNo) {
		 sqlSession.update(mapperNamespace  + AbleUtil.getCurrentMethodName(), ctnNo);
	}

	/** 사용중지[슬립요청] 으로 ACK 신호 변경  */
	public void preventsubmission(String ctnNo) {
		sqlSession.update(mapperNamespace  + AbleUtil.getCurrentMethodName(), ctnNo);
	}

	/** 화재감지기 현재상태 */
	public List<FireDetectorNowStatusDT> getFireDetectorNowStatusDT(long fireDetectorSeq) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("fireDetectorSeq",fireDetectorSeq);
		return sqlSession.selectList(mapperNamespace  + AbleUtil.getCurrentMethodName(), param);
	}
}