package com.firealarm.admin.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firealarm.admin.common.dao.StoreSmsUserDAO;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.common.vo.StoreSmsUserDT;

@Service
public class StoreSmsUserService extends ServiceSupport {
	@Autowired StoreSmsUserDAO storeSmsUserDAO;

	/**
	 * 특정 Store의 SMS 수신동의 대상자 정보 조회
	 */
	public List<StoreSmsUserDT> getReceiveAcceptListByStoreSeq(long storeSeq) {
		return storeSmsUserDAO.getReceiveAcceptListByStoreSeq(storeSeq);
	}
}
