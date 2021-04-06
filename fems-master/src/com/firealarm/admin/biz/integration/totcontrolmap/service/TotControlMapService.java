package com.firealarm.admin.biz.integration.totcontrolmap.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firealarm.admin.biz.home.vo.FireDetectorNowStatusVO;
import com.firealarm.admin.biz.home.vo.MapFireDetectorVO;
import com.firealarm.admin.biz.home.vo.MapMarketVO;
import com.firealarm.admin.biz.integration.totcontrolmap.dao.TotControlMapDAO;
import com.firealarm.admin.common.support.ServiceSupport;

/**
 * 통합관제지도 Service
 * @author ovcoimf
 */
@Service
public class TotControlMapService extends ServiceSupport {

	@Autowired TotControlMapDAO totControlMapDAO;

	/**
	 * 지도에 표시할 전체시장목록을 조회.
	 */
	public List<MapMarketVO> getAllMapMarketInfoList() {
		return totControlMapDAO.getAllMapMarketInfoList();
	}

	/**
	 * 지도에 표시할 전체시장 감지기목록을 조회.
	 */
	public List<MapFireDetectorVO> getAllMapFireDetectorList() {
		return totControlMapDAO.getAllMapFireDetectorList();
	}

	/**
	 * 전체 감지기 이벤트 상태의 목록을 전달
	 */
	public List<FireDetectorNowStatusVO> getAllStatusListInEvent() {
		return totControlMapDAO.getAllStatusListInEvent();
	}


}