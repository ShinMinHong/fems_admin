package com.firealarm.admin.biz.home.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firealarm.admin.biz.firedetector.firedetectormng.service.FireDetectorMngService;
import com.firealarm.admin.biz.firedetector.firedetectormng.vo.FireDetectorMngVO;
import com.firealarm.admin.biz.home.dao.HomeDAO;
import com.firealarm.admin.biz.home.vo.FireDetectorNowStatusVO;
import com.firealarm.admin.biz.home.vo.MapFireDetectorDetailVO;
import com.firealarm.admin.biz.home.vo.MapFireDetectorEventLogVO;
import com.firealarm.admin.biz.home.vo.MapFireDetectorVO;
import com.firealarm.admin.biz.home.vo.MapInitConfVO;
import com.firealarm.admin.biz.home.vo.MapMarketVO;
import com.firealarm.admin.biz.store.storemng.service.StoreMngService;
import com.firealarm.admin.biz.store.storemng.vo.StoreMngActiveVO;
import com.firealarm.admin.common.service.FireDetectorLogService;
import com.firealarm.admin.common.service.FireDetectorNowStatusService;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.common.vo.FireDetectorNowStatusDT;

/**
 * Home Service
 * @author ovcoimf
 */
@Service
public class HomeService extends ServiceSupport {

	@Autowired HomeDAO homeDAO;

	@Autowired FireDetectorMngService fireDetectorMngService;
	@Autowired FireDetectorNowStatusService fireDetectorNowStatusService;
	@Autowired StoreMngService storeMngService;
	@Autowired FireDetectorLogService fireDetectorLogService;

	/**
	 * 해당 관제지역의 관제지도 초기 데이터 조회
	 */
	public MapInitConfVO getMngAreaMapInitInfo(long mngAreaSeq) {
		return homeDAO.getMngAreaMapInitInfo(mngAreaSeq);
	}

	/**
	 * 해당 시장의 관제지도 초기 데이터 조회
	 */
	public MapInitConfVO getMarketMapInitInfo(long mngAreaSeq, long marketSeq) {
		return homeDAO.getMarketMapInitInfo(mngAreaSeq, marketSeq);
	}

	/**
	 * 지도에 표시할 시장목록을 조회. 시장관리자는 1개 시장만, 그외에는 관제지역의 시장목록 조회
	 */
	public List<MapMarketVO> getMapMarketInfoList(long mngAreaSeq, Long marketSeq) {
		return homeDAO.getMapMarketInfoList(mngAreaSeq, marketSeq);
	}

	/**
	 * 지도에 표시할 감지기목록을 조회. 권한에 따라서 시장에 속한, 관제지역에 속한 감지기 목록을 조회
	 */
	public List<MapFireDetectorVO> getMapFireDetectorList(long mngAreaSeq, Long marketSeq) {
		return homeDAO.getMapFireDetectorList(mngAreaSeq, marketSeq);
	}

	/**
	 * 이벤트 상태의 목록을 전달
	 */
	public List<FireDetectorNowStatusVO> getStatusListInEvent(long mngAreaSeq, Long marketSeq) {
		return homeDAO.getStatusListInEvent(mngAreaSeq, marketSeq);
	}

	/**
	 * 해당 단말기의 상세정보 조회(단말기+단말기현재상태+상점+단말기최근로그)
	 */
	public MapFireDetectorDetailVO getMapDetailBySeq(long fireDetectorSeq) {
		MapFireDetectorDetailVO detail = new MapFireDetectorDetailVO();
		//단말 정보 조회
		FireDetectorMngVO detector = fireDetectorMngService.getByFireDetectionSeq(fireDetectorSeq);
		detail.setDetector(detector);

		//현재 감지기에 상점 정보가 없다면, 유효하지 않은 감지기로 판단
		if(detector == null || detector.getStoreSeq() == null) {
			return null;
		}

		//단말 상태 조회
		FireDetectorNowStatusDT detectorNowStatus = fireDetectorNowStatusService.getByDetectorSeq(fireDetectorSeq);
		detail.setDetectorNowStatus(detectorNowStatus);
		//상점 정보 조회
		StoreMngActiveVO store = storeMngService.getByStoreSeq(detector.getStoreSeq());
		detail.setStore(store);
		//단말기 이벤트 목록 조회
		List<MapFireDetectorEventLogVO> eventList = fireDetectorLogService.getRecentListsByDeviceSeqAndStoreSeq(fireDetectorSeq, detector.getStoreSeq());
		detail.setEventList(eventList);

		return detail;
	}
}