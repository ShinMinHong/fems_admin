package com.firealarm.admin.biz.iot.dao;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.firealarm.admin.biz.iot.vo.DetectorInstallInfoDT;
import com.firealarm.admin.common.support.DAOSupport;

import framework.util.AbleUtil;

@Repository
public class IotDAO extends DAOSupport {
	/**
	 * 화재감지기 신호의 식별정보인 CtnNo를 기반으로 감지기, 상점, 시장, 관제지역 정보 조회
	 */
	public DetectorInstallInfoDT getDetectorAndInstallInfoByCtnNo(String ctnNo) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("ctnNo", ctnNo);
		return sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}
}
