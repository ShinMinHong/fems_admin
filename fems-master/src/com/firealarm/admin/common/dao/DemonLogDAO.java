package com.firealarm.admin.common.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.common.vo.IotDemonLogDT;

import framework.util.AbleUtil;

@Repository
public class DemonLogDAO extends DAOSupport {
	/**
	 * 처리하지 않은 Iot 로그 기록 조회
	 */
	public List<IotDemonLogDT> getRecentNotConfirmList(int limtCount) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("limtCount", limtCount);
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}
}
