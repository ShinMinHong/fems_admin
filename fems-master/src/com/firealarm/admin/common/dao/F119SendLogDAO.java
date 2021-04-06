package com.firealarm.admin.common.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.common.vo.F119SendLogDT;

import framework.util.AbleUtil;

@Repository
public class F119SendLogDAO extends DAOSupport {

	/**
	 * 119다매체 발송로그 저장.
	 */
	public long insert(F119SendLogDT dt) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("dt", dt);
		sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		return dt.getF119SendLogSeq();
	}

	/**
	 * 119다매체 발송로그 저장.
	 */
	public int updateResult(long f119SendLogSeq, String sendResult) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("f119SendLogSeq", f119SendLogSeq);
		param.put("sendResult", sendResult);
		return sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

}
