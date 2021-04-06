package com.firealarm.admin.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firealarm.admin.common.dao.DemonLogDAO;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.common.vo.IotDemonLogDT;

@Service
public class DemonLogService extends ServiceSupport {
	@Autowired DemonLogDAO demonLogDAO;

	/**
	 * Demon서버에서 저장한 Iot 로그 기록 조회
	 * @param limtCount 한번에 처리할 최대 미처리 로그. Seq 정렬 후, 과거건부터 처리
	 * @return
	 */
	public List<IotDemonLogDT> getRecentNotConfirmList(int limtCount) {
		return demonLogDAO.getRecentNotConfirmList(limtCount);
	}
}
