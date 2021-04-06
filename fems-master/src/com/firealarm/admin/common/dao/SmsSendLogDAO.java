package com.firealarm.admin.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.common.vo.SmsSendLogDT;

import framework.util.AbleUtil;

@Repository
public class SmsSendLogDAO extends DAOSupport {

	/**
	 * 특정시간동안 해당 Store에 발송된 SMS 갯수 조회
	 */
	public int getListCountByConditionAndDuraion(long storeSeq, int durationMin) {
		HashMap<String, Object> param = new HashMap<>();
		param.put("storeSeq", storeSeq);
		param.put("durationMin", Integer.toString(durationMin)+" minutes");
		return sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/**
	 * SMS발송로그 저장
	 */
	public int insertAll(List<SmsSendLogDT> list) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("list", list);
		return sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

}
