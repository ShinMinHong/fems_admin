package com.firealarm.admin.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firealarm.admin.common.dao.FireDetectorNowStatusDAO;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.common.vo.FireDetectorNowStatusDT;

@Service
public class FireDetectorNowStatusService extends ServiceSupport {
	@Autowired FireDetectorNowStatusDAO fireDetectorNowStatusDAO;

	/**
	 * fa_fire_detector_now_status 조회
	 */
	public FireDetectorNowStatusDT getByDetectorSeq(long fireDetectorSeq) {
		return fireDetectorNowStatusDAO.getByDetectorSeq(fireDetectorSeq);
	}

	/**
	 * fa_fire_detector_now_status에 Insert
	 */
	public int insert(FireDetectorNowStatusDT dt) {
		return fireDetectorNowStatusDAO.insert(dt);
	}

	/**
	 * fa_fire_detector_now_status에 Update
	 */
	public int update(FireDetectorNowStatusDT dt) {
		return fireDetectorNowStatusDAO.update(dt);
	}
}
