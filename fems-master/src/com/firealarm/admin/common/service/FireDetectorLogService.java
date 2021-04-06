package com.firealarm.admin.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firealarm.admin.biz.home.vo.MapFireDetectorEventLogVO;
import com.firealarm.admin.common.dao.FireDetectorLogDAO;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.common.vo.FireDetectorLogDT;

@Service
public class FireDetectorLogService extends ServiceSupport {
	@Autowired FireDetectorLogDAO fireDetectorLogDAO;

	/**
	 * 단말기 및 점포의 최근 신호 목록 조회
	 */
	public List<MapFireDetectorEventLogVO> getRecentListsByDeviceSeqAndStoreSeq(long detectorSeq, long storeSeq) {
		return fireDetectorLogDAO.getRecentListsByDeviceSeqAndStoreSeq(detectorSeq, storeSeq);
	}

	/**
	 * 신호목록을 fa_fire_detector_log에 Insert All
	 */
	public int insertAll(List<FireDetectorLogDT> list) {
		return fireDetectorLogDAO.insertAll(list);
	}
}
