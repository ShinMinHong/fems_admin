package com.firealarm.admin.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firealarm.admin.common.dao.SmsSendLogDAO;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.common.vo.SmsSendLogDT;

@Service
public class SmsSendLogService extends ServiceSupport {
	@Autowired SmsSendLogDAO smsSendLogDAO;

	/**
	 *
	 */
	/**
	 * 특정시간(분)동안 해당 Store에 발송된 SMS가 있는지 조회. durationMin가 0인 경우는 검사하지 않음.
	 * @param storeSeq 상점 Seq
	 * @param durationMin 알림제한 시간(분)
	 * @return
	 */
	public boolean hasSmsInDuration(long storeSeq, int durationMin) {
		//durationMin이 0으로 설정된 경우는 시간내 발송한 SMS가 없다고 판단.
		if(durationMin == 0)
			return false;

		int count = smsSendLogDAO.getListCountByConditionAndDuraion(storeSeq, durationMin);
		return count > 0;
	}

	/**
	 * SMS발송로그 저장
	 */
	public int insertAll(List<SmsSendLogDT> smsSendLogList) {
		return smsSendLogDAO.insertAll(smsSendLogList);
	}

}
