package com.firealarm.admin.biz.statistics.storeeventstats.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.firealarm.admin.biz.statistics.storeeventstats.vo.StoreEventStatsVO;
import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.security.util.UserSecurityUtil;

import framework.spring.web.rest.jsonview.AblePageImpl;
import framework.util.AbleUtil;
import framework.vo.SearchMap;


/**
 *  점포별 화재 신호 통계 DAO
 * @author rodem4_pc1
 *
 */
@Repository
public class StoreEventStatsDAO extends DAOSupport {

	/** 목록 조회 */
	public List<StoreEventStatsVO> getStoreEventStatsListAll(Sort sort, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sort", prepareSortParameter(sort, StoreEventStatsVO.class));
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		List<StoreEventStatsVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		return list;
	}

	/** 페이징 조회 */
	public Page<StoreEventStatsVO> getStoreEventStatsList(Pageable pageable, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		int total = sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName() + "Count", param);
		Pageable pageableToApply = ensureValidPageable(pageable, total);
		param.put("page", pageableToApply);
		param.put("sort", prepareSortParameter(pageableToApply.getSort(), StoreEventStatsVO.class));
		List<StoreEventStatsVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName() + "Page", param);
		Page<StoreEventStatsVO> page = new AblePageImpl<>(list, pageableToApply, total);
		return page;
	}

	/** 상세 */
	public StoreEventStatsVO getDetailsPageByStoreSeq(long storeSeq,String searchStartDate,String searchEndDate) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("storeSeq", storeSeq);
		param.put("searchStartDate", searchStartDate);
		param.put("searchEndDate", searchEndDate);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		return sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}
}
