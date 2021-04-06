package com.firealarm.admin.biz.statistics.montheventstats.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.firealarm.admin.biz.statistics.montheventstats.dao.MonthEventStatsDAO;
import com.firealarm.admin.biz.statistics.montheventstats.vo.MonthEventStatsVO;
import com.firealarm.admin.common.support.ServiceSupport;

import framework.vo.SearchMap;



/**
 * 월별 화재 신호 통계 Service
 * @author rodem4_pc1
 *
 */
@Service
public class MonthEventStatsService extends ServiceSupport {

	@Autowired MonthEventStatsDAO monthEventStatsDAO;

	/** 목록 조회 */
	public List<MonthEventStatsVO> getMonthEventStatsListAll(Sort sort, SearchMap search) {
		return monthEventStatsDAO.getMonthEventStatsListAll(sort, search);
	}
}
