package com.firealarm.admin.biz.integration.totcontrolmap.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.firealarm.admin.biz.home.vo.FireDetectorNowStatusVO;
import com.firealarm.admin.biz.home.vo.MapFireDetectorVO;
import com.firealarm.admin.biz.home.vo.MapMarketVO;
import com.firealarm.admin.common.support.DAOSupport;

import framework.util.AbleUtil;

/**
 * 통합관제지도 DAO
 * @author ovcoimf
 *
 */
@Repository
public class TotControlMapDAO extends DAOSupport {

	/**
	 * 통합 관제 지도에 표시할 시장목록을 조회
	 */
	public List<MapMarketVO> getAllMapMarketInfoList() {
		HashMap<String, Object> param = new HashMap<>();
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/**
	 * 통합 관제 지도에 표시할 화재감지기목록을 조회
	 */
	public List<MapFireDetectorVO> getAllMapFireDetectorList() {
		HashMap<String, Object> param = new HashMap<>();
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/**
	 * 전체 감지기 이벤트 상태의 목록 조회
	 */
	public List<FireDetectorNowStatusVO> getAllStatusListInEvent() {
		HashMap<String, Object> param = new HashMap<>();
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

}
