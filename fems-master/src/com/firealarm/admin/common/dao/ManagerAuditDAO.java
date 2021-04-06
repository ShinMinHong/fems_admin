package com.firealarm.admin.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.common.vo.ManagerAuditDT;
import com.firealarm.admin.security.util.UserSecurityUtil;

import framework.spring.web.rest.jsonview.AblePageImpl;
import framework.util.AbleUtil;
import framework.vo.SearchMap;

/**
 * 관리자이력 DAO
 *
 * @author impjs
 */
@Repository
public class ManagerAuditDAO extends DAOSupport {

	/** 전체 목록 조회 */
	public List<ManagerAuditDT> getListAll(Sort sort, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sort", prepareSortParameter(sort, ManagerAuditDT.class));
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		List<ManagerAuditDT> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		return list;
	}

	/** 페이지 목록 조회 */
	public Page<ManagerAuditDT> getList(Pageable pageable, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		int total = sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName() + "Count", param);
		Pageable pageableToApply = ensureValidPageable(pageable, total);
		param.put("page", pageableToApply);
		param.put("sort", prepareSortParameter(pageableToApply.getSort(), ManagerAuditDT.class));
		List<ManagerAuditDT> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName() + "Page", param);
		Page<ManagerAuditDT> page = new AblePageImpl<>(list, pageableToApply, total);
		return page;
	}

	/** 관리자이력 정보획득 */
	public ManagerAuditDT getManagerAuditBySeq(long auditSeq) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("auditSeq", auditSeq);
		return sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 관리자이력 등록 */
	public void insert(ManagerAuditDT dt) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("dt", dt);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}


}
