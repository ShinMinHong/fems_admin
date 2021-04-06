package com.firealarm.admin.biz.home.vo;

import java.util.List;

import com.firealarm.admin.biz.firedetector.firedetectormng.vo.FireDetectorMngVO;
import com.firealarm.admin.biz.store.storemng.vo.StoreMngVO;
import com.firealarm.admin.common.vo.FireDetectorNowStatusDT;

import lombok.Data;

/**
 * 화재감지기 현재 상태 상세정보 - 관제지도에서 감지기 Click시
 */
@Data
public class MapFireDetectorDetailVO {

	private FireDetectorMngVO detector;

	private FireDetectorNowStatusDT detectorNowStatus;

	private StoreMngVO store;

	/** 단말기 이벤트 목록 */
	private List<MapFireDetectorEventLogVO> eventList;

}

