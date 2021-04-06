package com.firealarm.admin.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firealarm.admin.common.dao.AdminDAO;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.security.vo.AdminDT;

@Service
public class AdminService extends ServiceSupport {
	@Autowired AdminDAO adminDAO;

	/**
	 * SMS 수신 통합 관리자 조회
	 */
	public List<AdminDT> getSmsRecieveHqAdmin() {
		return adminDAO.getSmsRecieveHqAdmin();
	}

	/**
	 * SMS 수신동의된 관제지역 관리자 목록 조회
	 */
	public List<AdminDT> getSmsRecieveMngAreaAdminByMngAreaSeq(long mngAreaSeq) {
		return adminDAO.getSmsRecieveMngAreaAdminByMngAreaSeq(mngAreaSeq);
	}

	/**
	 * SMS 수신동의된 시장 관리자 목록 조회
	 */
	public List<AdminDT> getSmsRecieveMarketAdminByMngAreaSeq(long marketSeq) {
		return adminDAO.getSmsRecieveMarketAdminByMngAreaSeq(marketSeq);
	}
}
