package com.firealarm.admin.biz.statistics.areaeventstats.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.firealarm.admin.biz.statistics.areaeventstats.dao.AreaEventStatsDAO;
import com.firealarm.admin.biz.statistics.areaeventstats.vo.AreaEventStatsVO;
import com.firealarm.admin.common.support.ServiceSupport;

import framework.vo.SearchMap;



/**
 *  지역별 화재 신호 통계 Service
 * @author rodem4_pc1
 *
 */
@Service
public class AreaEventStatsService extends ServiceSupport {

	@Autowired AreaEventStatsDAO areaEventStatsDAO;

	/** 목록 조회 */
	public List<AreaEventStatsVO> getAreaEventStatsListAll(Sort sort, SearchMap search) {
		return areaEventStatsDAO.getAreaEventStatsListAll(sort, search);
	}

	/** 페이징 조회 */
	public Page<AreaEventStatsVO> getAreaEventStatsList(Pageable pageable, SearchMap search) {
		return areaEventStatsDAO.getAreaEventStatsList(pageable, search);
	}

	/** 상세 */
	public AreaEventStatsVO getDetailsPageByMarketSeq(long marketSeq,String searchStartDate,String searchEndDate) {
		return areaEventStatsDAO.getDetailsPageByMarketSeq(marketSeq,searchStartDate,searchEndDate);
	}

}
