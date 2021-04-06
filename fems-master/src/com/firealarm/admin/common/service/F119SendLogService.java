package com.firealarm.admin.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firealarm.admin.biz.iot.vo.DetectorInstallInfoDT;
import com.firealarm.admin.common.dao.F119SendLogDAO;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.common.vo.F119SendLogDT;
import com.firealarm.admin.extlibrary.fire119.vo.Fire119SendVO;

@Service
public class F119SendLogService extends ServiceSupport {
	@Autowired F119SendLogDAO f119SendLogDAO;

	/**
	 * 119다매체 발송로그 저장.
	 */
	public long insert(Fire119SendVO fire119SendInfo, DetectorInstallInfoDT detectorInstallInfo) {
		F119SendLogDT logInfo = F119SendLogDT.fromFire119SendVOAndInstallInfo(fire119SendInfo, detectorInstallInfo);
		return f119SendLogDAO.insert(logInfo);
	}

	/**
	 * 119다매체 발송로그 갱신
	 */
	public int updateResult(long f119SendLogSeq, String sendResult) {
		return f119SendLogDAO.updateResult(f119SendLogSeq, sendResult);
	}

}
