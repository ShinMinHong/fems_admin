package com.firealarm.admin.biz.system.firedetectordemonlog.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.firealarm.admin.biz.system.firedetectordemonlog.vo.FireDetectorDemonLogVO;
import com.firealarm.admin.common.support.DAOSupport;

import framework.spring.web.rest.jsonview.AblePageImpl;
import framework.util.AbleUtil;
import framework.vo.SearchMap;

/**
 * Demon Log DAO
 * @role ovcoimf
 */
@Repository
public class FireDetectorDemonLogDAO extends DAOSupport {

	/** 목록 조회 */
	public List<FireDetectorDemonLogVO> getListAll(Sort sort, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sort", prepareSortParameter(sort, FireDetectorDemonLogVO.class));
		param.put("search", search);
		List<FireDetectorDemonLogVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		return list;
	}

	/** 페이지 조회 */
	public Page<FireDetectorDemonLogVO> getList(Pageable pageable, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("search", search);
		int total = sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName() + "Count", param);
		Pageable pageableToApply = ensureValidPageable(pageable, total);
		param.put("page", pageableToApply);
		param.put("sort", prepareSortParameter(pageableToApply.getSort(), FireDetectorDemonLogVO.class));
		List<FireDetectorDemonLogVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName() + "Page", param);
		Page<FireDetectorDemonLogVO> page = new AblePageImpl<>(list, pageableToApply, total);
		return page;
	}
}