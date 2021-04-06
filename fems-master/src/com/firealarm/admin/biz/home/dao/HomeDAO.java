package com.firealarm.admin.biz.home.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.firealarm.admin.appconfig.CodeMap.MAP_USER_TYPE;
import com.firealarm.admin.biz.home.vo.MapInitConfVO;
import com.firealarm.admin.biz.home.vo.MapMarketVO;
import com.firealarm.admin.biz.home.vo.FireDetectorNowStatusVO;
import com.firealarm.admin.biz.home.vo.MapFireDetectorVO;
import com.firealarm.admin.common.support.DAOSupport;

import framework.util.AbleUtil;

/**
 * Home DAO
 * @role ovcoimf
 *
 */
@Repository
public class HomeDAO extends DAOSupport {
	/**
	 * 해당 관제지역의 관제지도 초기 데이터 조회
	 */
	public MapInitConfVO getMngAreaMapInitInfo(long mngAreaSeq) {
		HashMap<String, Object> param = new HashMap<>();
		param.put("mngAreaSeq", mngAreaSeq);
		MapInitConfVO mapInitInfo = sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		mapInitInfo.setMapUserType(MAP_USER_TYPE.MNG_AREA_USER);
		return mapInitInfo;
	}

	/**
	 * 해당 시장의 관제지도 초기 데이터 조회
	 */
	public MapInitConfVO getMarketMapInitInfo(long mngAreaSeq, long marketSeq) {
		HashMap<String, Object> param = new HashMap<>();
		param.put("mngAreaSeq", mngAreaSeq);
		param.put("marketSeq", marketSeq);
		MapInitConfVO mapInitInfo = sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		mapInitInfo.setMapUserType(MAP_USER_TYPE.MARKET_USER);
		return mapInitInfo;
	}

	/**
	 * 지도에 표시할 시장목록을 조회
	 */
	public List<MapMarketVO> getMapMarketInfoList(long mngAreaSeq, Long marketSeq) {
		HashMap<String, Object> param = new HashMap<>();
		param.put("mngAreaSeq", mngAreaSeq);
		param.put("marketSeq", marketSeq);
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/**
	 * 지도에 표시할 화재감지기목록을 조회
	 */
	public List<MapFireDetectorVO> getMapFireDetectorList(long mngAreaSeq, Long marketSeq) {
		HashMap<String, Object> param = new HashMap<>();
		param.put("mngAreaSeq", mngAreaSeq);
		param.put("marketSeq", marketSeq);
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/**
	 * 이벤트 상태의 목록을 전달
	 */
	public List<FireDetectorNowStatusVO> getStatusListInEvent(long mngAreaSeq, Long marketSeq) {
		HashMap<String, Object> param = new HashMap<>();
		param.put("mngAreaSeq", mngAreaSeq);
		param.put("marketSeq", marketSeq);
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}
}
