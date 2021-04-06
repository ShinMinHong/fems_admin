package com.firealarm.admin.biz.system.hqsmsadmin.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.firealarm.admin.biz.system.hqsmsadmin.vo.HqSmsAdminMngActiveVO;
import com.firealarm.admin.biz.system.hqsmsadmin.vo.HqSmsAdminMngVO;
import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.security.util.UserSecurityUtil;

import framework.util.AbleUtil;
import framework.vo.SearchMap;


/**
 * 통합SMS수신자 DAO
 * @author SHH
 */
@Repository
public class HqSmsAdminMngDAO extends DAOSupport {

	/** 전체 목록 조회 */
	public List<HqSmsAdminMngVO> getListAll(Sort sort, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sort", prepareSortParameter(sort, HqSmsAdminMngVO.class));
		param.put("search", search);
		List<HqSmsAdminMngVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		return list;
	}

	/** 페이지 목록 조회 */
	public Page<HqSmsAdminMngVO> getList(Pageable pageable, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		int total = sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName() + "Count", param);
		Pageable pageableToApply = ensureValidPageable(pageable, total);
		param.put("page", pageableToApply);
		param.put("sort", prepareSortParameter(pageableToApply.getSort(), HqSmsAdminMngVO.class));
		List<HqSmsAdminMngVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName() + "Page", param);
		Page<HqSmsAdminMngVO> page = new PageImpl<HqSmsAdminMngVO>(list, pageableToApply, total);
		return page;
	}

	/** 상세 */
	public HqSmsAdminMngVO getSmsAdminDetailsBySmsAdminSeq(long smsAdminSeq) {
		return sqlSession.selectOne(mapperNamespace  + AbleUtil.getCurrentMethodName(), smsAdminSeq);
	}

	/** 등록 */
	public void insert(HqSmsAdminMngActiveVO vo) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("vo", vo);
		sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 수정 */
	public int update(HqSmsAdminMngActiveVO vo) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("vo", vo);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		return sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 삭제 */
	public int delete(long smsAdminSeq) {
		return sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), smsAdminSeq);
	}

}
