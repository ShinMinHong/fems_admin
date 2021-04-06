package com.firealarm.admin.biz.statistics.storeeventstats.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.firealarm.admin.biz.statistics.storeeventstats.dao.StoreEventStatsDAO;
import com.firealarm.admin.biz.statistics.storeeventstats.vo.StoreEventStatsVO;
import com.firealarm.admin.common.support.ServiceSupport;

import framework.vo.SearchMap;



/**
 *  점포별 화재 신호 통계 Service
 * @author rodem4_pc1
 *
 */
@Service
public class StoreEventStatsService extends ServiceSupport {

	@Autowired StoreEventStatsDAO storeEventStatsDAO;

	/** 목록 조회 */
	public List<StoreEventStatsVO> getStoreEventStatsListAll(Sort sort, SearchMap search) {
		return storeEventStatsDAO.getStoreEventStatsListAll(sort, search);
	}

	/** 페이징 조회 */
	public Page<StoreEventStatsVO> getStoreEventStatsList(Pageable pageable, SearchMap search) {
		return storeEventStatsDAO.getStoreEventStatsList(pageable, search);
	}

	/** 상세 */
	public StoreEventStatsVO getDetailsPageByStoreSeq(long storeSeq,String searchStartDate,String searchEndDate) {
		return storeEventStatsDAO.getDetailsPageByStoreSeq(storeSeq,searchStartDate,searchEndDate);
	}

}
