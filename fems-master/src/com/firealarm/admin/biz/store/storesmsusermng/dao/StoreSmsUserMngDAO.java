package com.firealarm.admin.biz.store.storesmsusermng.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.firealarm.admin.biz.store.storesmsusermng.vo.StoreSmsUserMngVO;
import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.security.util.UserSecurityUtil;

import framework.util.AbleUtil;
import framework.vo.SearchMap;

/**
 * 점포 SMS 수신 대상 관리 DAO
 * @author rodem4_pc1
 *
 */
@Repository
public class StoreSmsUserMngDAO extends DAOSupport {


	/** 전체 목록 조회 */
	public List<StoreSmsUserMngVO> getListAll(Sort sort, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sort", prepareSortParameter(sort, StoreSmsUserMngVO.class));
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		List<StoreSmsUserMngVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		return list;
	}

	/** 페이지 목록 조회 */
	public Page<StoreSmsUserMngVO> getList(Pageable pageable, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		int total = sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName() + "Count", param);
		Pageable pageableToApply = ensureValidPageable(pageable, total);
		param.put("page", pageableToApply);
		param.put("sort", prepareSortParameter(pageableToApply.getSort(), StoreSmsUserMngVO.class));
		List<StoreSmsUserMngVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName() + "Page", param);
		Page<StoreSmsUserMngVO> page = new PageImpl<StoreSmsUserMngVO>(list, pageableToApply, total);
		return page;
	}

	/** 상세 */
	public StoreSmsUserMngVO getBySmsUserSeq(long smsUserSeq) {
		return sqlSession.selectOne(mapperNamespace  + AbleUtil.getCurrentMethodName(), smsUserSeq);
	}

	/** 등록 */
	public void insert(StoreSmsUserMngVO vo) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("vo", vo);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 삭제 */
	public int delete(long smsUserSeq) {
		return sqlSession.delete(mapperNamespace + AbleUtil.getCurrentMethodName(), smsUserSeq);
	}

	/** 수정 */
	public void update(StoreSmsUserMngVO actionVO) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("vo", actionVO);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 같은 점포내에 동일한 이름이 있는지 중복 체크 */
	public boolean hasDuplicatedData(long storeSeq, String phoneNo) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("storeSeq", storeSeq);
		param.put("phoneNo", phoneNo);
		return sqlSession.selectOne(mapperNamespace  + AbleUtil.getCurrentMethodName(), param);
	}

	/** 핸드폰번호 및 점포 고유번호로 sms수신대상자고유번호 조회 */
	public long getSmsUserSeqByStoreSeqAndPhoneNo(long storeSeq, String phoneNo) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("storeSeq", storeSeq);
		param.put("phoneNo", phoneNo);
		return sqlSession.selectOne(mapperNamespace  + AbleUtil.getCurrentMethodName(), param);
	}

	/** 해당점포의 모든 대상자 삭제 */
	public int deleteSmsUserByStoreSeq(long storeSeq) {
		return sqlSession.delete(mapperNamespace + AbleUtil.getCurrentMethodName(), storeSeq);
	}

}