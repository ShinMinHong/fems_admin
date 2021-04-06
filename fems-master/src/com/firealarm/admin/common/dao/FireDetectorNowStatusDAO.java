package com.firealarm.admin.common.dao;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.common.vo.FireDetectorNowStatusDT;

import framework.util.AbleUtil;

@Repository
public class FireDetectorNowStatusDAO extends DAOSupport {
	/** 조회 */
	public FireDetectorNowStatusDT getByDetectorSeq(long fireDetectorSeq) {
		HashMap<String, Object> param = new HashMap<>();
		param.put("fireDetectorSeq", fireDetectorSeq);
		return sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 등록 */
	public int insert(FireDetectorNowStatusDT dt) {
		return sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), dt);
	}

	/** 수정 */
	public int update(FireDetectorNowStatusDT dt) {
		return sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), dt);
	}
}
