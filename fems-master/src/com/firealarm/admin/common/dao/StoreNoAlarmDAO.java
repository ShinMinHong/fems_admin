package com.firealarm.admin.common.dao;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.common.vo.StoreNoAlarmDT;

import framework.util.AbleUtil;

@Repository
public class StoreNoAlarmDAO extends DAOSupport {
	/**
	 * 특정 Store의 noAlarm 설정 목록을 조회
	 */
	public StoreNoAlarmDT getByStoreSeq(long storeSeq) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("storeSeq", storeSeq);
		return sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}
}
