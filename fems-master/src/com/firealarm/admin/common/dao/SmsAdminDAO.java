package com.firealarm.admin.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.common.vo.SmsAdminDT;

import framework.util.AbleUtil;

@Repository
public class SmsAdminDAO extends DAOSupport {

	//SMS 수신 통합SMS 관리자 조회
	public List<SmsAdminDT> getSmsRecieveHqSmsAdmin() {
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName());
	}

}
