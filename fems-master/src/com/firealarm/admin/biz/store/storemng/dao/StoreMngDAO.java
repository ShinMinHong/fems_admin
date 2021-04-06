package com.firealarm.admin.biz.store.storemng.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.firealarm.admin.biz.store.storemng.vo.StoreMngActiveVO;
import com.firealarm.admin.biz.store.storemng.vo.StoreMngVO;
import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.security.util.UserSecurityUtil;

import framework.util.AbleUtil;
import framework.vo.SearchMap;

/**
 * 전통시장 점포관리 DAO
 * @author rodem4_pc1
 *
 */
@Repository
public class StoreMngDAO extends DAOSupport {


	/** 전체 목록 조회 */
	public List<StoreMngVO> getListAll(Sort sort, SearchMap search) {
		Map<String, Object> param = new HashMap<>();
		param.put("sort", prepareSortParameter(sort, StoreMngVO.class));
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 페이지 목록 조회 */
	public Page<StoreMngVO> getList(Pageable pageable, SearchMap search) {
		Map<String, Object> param = new HashMap<>();
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		int total = sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName() + "Count", param);
		Pageable pageableToApply = ensureValidPageable(pageable, total);
		param.put("page", pageableToApply);
		param.put("sort", prepareSortParameter(pageableToApply.getSort(), StoreMngVO.class));
		List<StoreMngVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName() + "Page", param);
		return new PageImpl<>(list, pageableToApply, total);
	}

	/** 상세 */
	public StoreMngActiveVO getByStoreSeq(long storeSeq) {
		Map<String, Object> param = new HashMap<>();
		param.put("storeSeq", storeSeq);
		return sqlSession.selectOne(mapperNamespace  + AbleUtil.getCurrentMethodName(), param);
	}

	/** 등록 */
	public long insert(StoreMngVO vo) {
		Map<String, Object> param = new HashMap<>();
		param.put("vo", vo);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		return vo.getStoreSeq();
	}

	/** 점포알림제한정보 등록 */
	public void insertStoreNoAlarm(long storeSeq) {
		Map<String, Object> param = new HashMap<>();
		param.put("storeSeq", storeSeq);
		sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 삭제 */
	public int delete(long storeSeq) {
		Map<String, Object> param = new HashMap<>();
		param.put("storeSeq", storeSeq);
		return sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 수정 */
	public void update(StoreMngActiveVO actionVO) {
		Map<String, Object> param = new HashMap<>();
		param.put("vo", actionVO);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	public void updateStoreNoAlarm(StoreMngActiveVO actionVO) {
		Map<String, Object> param = new HashMap<>();
		param.put("vo", actionVO);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	public Long findStoreSeqByID(long marketSeq,String storeName) {
		Map<String, Object> param = new HashMap<>();
		param.put("marketSeq", marketSeq);
		param.put("storeName", storeName);
		return sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}
}