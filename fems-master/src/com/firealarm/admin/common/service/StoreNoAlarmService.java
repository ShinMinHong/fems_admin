package com.firealarm.admin.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firealarm.admin.common.dao.StoreNoAlarmDAO;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.common.vo.StoreNoAlarmDT;

@Service
public class StoreNoAlarmService extends ServiceSupport {
	@Autowired StoreNoAlarmDAO storeNoAlarmDAO;

	/**
	 * 특정 Store의 noAlarm 설정 목록을 조회
	 */
	public StoreNoAlarmDT getByStoreSeq(long storeSeq) {
		return storeNoAlarmDAO.getByStoreSeq(storeSeq);
	}
}
