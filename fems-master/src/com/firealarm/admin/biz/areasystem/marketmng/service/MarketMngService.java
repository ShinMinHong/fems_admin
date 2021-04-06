package com.firealarm.admin.biz.areasystem.marketmng.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.firealarm.admin.biz.areasystem.marketmng.dao.MarketMngDAO;
import com.firealarm.admin.biz.areasystem.marketmng.vo.MarketMngActiveVO;
import com.firealarm.admin.biz.areasystem.marketmng.vo.MarketMngVO;
import com.firealarm.admin.common.support.ServiceSupport;

import framework.vo.SearchMap;

/**
 * 전통시장 관리 Service
 * @author JKS
 */

@Service
public class MarketMngService extends ServiceSupport {

	@Autowired MarketMngDAO marketMngDAO;

	/** 전체 목록 조회 */
	public List<MarketMngVO> getListAll(Sort sort, SearchMap search) {
		return marketMngDAO.getListAll(sort, search);
	}

	/** 페이징 목록 조회 */
	public Page<MarketMngVO> getList(Pageable pageable, SearchMap search) {
		return marketMngDAO.getList(pageable, search);
	}

	/** 특정 관제지역의 시장목록 조회*/
	public List<MarketMngVO> getListByMngAreaSeq(long mngAreaSeq) {
		return marketMngDAO.getListByMngAreaSeq(mngAreaSeq);
	}

	/** 상세 */
	public MarketMngVO getByMarketSeq(long marketSeq) {
		return marketMngDAO.getByMarketSeq(marketSeq);
	}

	/** 등록 */
	public void insert(MarketMngActiveVO vo) {
		marketMngDAO.insert(vo);
	}

	/** 수정 */
	public void update(MarketMngActiveVO vo) {
		marketMngDAO.update(vo);
	}

	/** 삭제 */
	public void delete(long adminSeq) {
		marketMngDAO.delete(adminSeq);
	}

	/** 시장 전체 점포의 연기이벤트 전송제외/복구 저장 */
	public void smokealarm(long marketSeq, String alarmYn) {
		boolean nAlarmYn = false;
		if (alarmYn.equals("On")) {
			nAlarmYn = true;
		}

		marketMngDAO.smokealarm(marketSeq, nAlarmYn);
	}

}
