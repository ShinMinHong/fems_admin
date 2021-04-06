package com.firealarm.admin.biz.firedetector.firedetectoreventlog.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.firealarm.admin.biz.firedetector.firedetectoreventlog.vo.FireDetectorEventLogVO;
import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.security.util.UserSecurityUtil;

import framework.util.AbleUtil;
import framework.vo.SearchMap;

/**
 * 화재감지기 이벤트조회 DAO
 * @author JKS
 *
 */
@Repository
public class FireDetectorEventLogDAO extends DAOSupport {

	/** 전체 목록 조회 */
	public List<FireDetectorEventLogVO> getListAll(Sort sort, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sort", prepareSortParameter(sort, FireDetectorEventLogVO.class));
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		List<FireDetectorEventLogVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		return list;
	}

	/** 페이지 목록 조회 */
	public Page<FireDetectorEventLogVO> getList(Pageable pageable, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		int total = sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName() + "Count", param);
		Pageable pageableToApply = ensureValidPageable(pageable, total);
		param.put("page", pageableToApply);
		param.put("sort", prepareSortParameter(pageableToApply.getSort(), FireDetectorEventLogVO.class));
		List<FireDetectorEventLogVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName() + "Page", param);
		Page<FireDetectorEventLogVO> page = new PageImpl<FireDetectorEventLogVO>(list, pageableToApply, total);
		return page;
	}
}
