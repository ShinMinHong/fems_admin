package com.firealarm.admin.biz.statistics.montheventstats.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.firealarm.admin.biz.statistics.montheventstats.vo.MonthEventStatsVO;
import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.security.util.UserSecurityUtil;

import framework.util.AbleUtil;
import framework.vo.SearchMap;


/**
 *  월별 화재 신호 통계  DAO
 * @author rodem4_pc1
 *
 */
@Repository
public class MonthEventStatsDAO extends DAOSupport {

	/** 목록 조회 */
	public List<MonthEventStatsVO> getMonthEventStatsListAll(Sort sort, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sort", prepareSortParameter(sort, MonthEventStatsVO.class));
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		List<MonthEventStatsVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		return list;
	}

}
