package com.firealarm.admin.common.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.security.vo.AdminDT;

import framework.util.AbleUtil;

@Repository
public class AdminDAO extends DAOSupport {

	//SMS 수신 통합 관리자 조회
	public List<AdminDT> getSmsRecieveHqAdmin() {
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName());
	}


	//SMS 수신동의된 관제지역 관리자 목록 조회
	public List<AdminDT> getSmsRecieveMngAreaAdminByMngAreaSeq(long mngAreaSeq) {
		HashMap<String, Object> param = new HashMap<>();
		param.put("mngAreaSeq", mngAreaSeq);
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	//SMS 수신동의된 시장 관리자 목록 조회
	public List<AdminDT> getSmsRecieveMarketAdminByMngAreaSeq(long marketSeq) {
		HashMap<String, Object> param = new HashMap<>();
		param.put("marketSeq", marketSeq);
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

}
