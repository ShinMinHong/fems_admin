package com.firealarm.admin.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firealarm.admin.common.dao.SmsAdminDAO;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.common.vo.SmsAdminDT;

@Service
public class SmsAdminService extends ServiceSupport {
	@Autowired SmsAdminDAO smsAdminDAO;

	/**
	 * SMS 수신 통합SMS 관리자 조회
	 */
	public List<SmsAdminDT> getSmsRecieveHqSmsAdmin() {
		return smsAdminDAO.getSmsRecieveHqSmsAdmin();
	}
}
