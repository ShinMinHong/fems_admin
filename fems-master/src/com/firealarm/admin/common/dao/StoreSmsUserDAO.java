package com.firealarm.admin.common.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.common.vo.StoreSmsUserDT;

import framework.util.AbleUtil;

@Repository
public class StoreSmsUserDAO extends DAOSupport {

	/**
	 * 특정 Store의 SMS 수신동의 대상자 정보 조회
	 */
	public List<StoreSmsUserDT> getReceiveAcceptListByStoreSeq(long storeSeq) {
		HashMap<String, Object> param = new HashMap<>();
		param.put("storeSeq", storeSeq);
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

}
