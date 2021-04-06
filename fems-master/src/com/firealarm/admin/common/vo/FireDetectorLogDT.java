package com.firealarm.admin.common.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.LocalDateTime;

import com.firealarm.admin.appconfig.CodeMap.DETECTOR_SIGNAL_TYPE;
import com.firealarm.admin.biz.iot.vo.DetectorInstallInfoDT;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FireDetectorLogDT {
	/** 화재감지기신호고유번호 */
	protected long fireDetectorSignalSeq;

	/** 화재감지기고유번호 */
	protected long fireDetectorSeq;

	/** 관제지역고유번호 */
	protected long mngAreaSeq;

	/** 전통시장고유번호 */
	protected long marketSeq;

	/** 점포고유번호 */
	protected long storeSeq;

	/** CTN번호 */
	protected String ctnNo;

	/** 메시지버전 */
	protected String msgVer;

	/** 신호타입 */
	protected DETECTOR_SIGNAL_TYPE signalType;

	/** 배터리값 */
	protected int batteryValue;

	/** 연기값 */
	protected String smokeValue;

	/** 온도값 */
	protected String temperatureValue;

	/** 불꽃1값 */
	protected String flame1Value;

	/** 불꽃2값 */
	protected String flame2Value;

	/** CO값 */
	protected String coValue;

	/** 연기이벤트여부 */
	protected boolean smokeEvent;

	/** 온도이벤트여부 */
	protected boolean temperatureEvent;

	/** 불꽃이벤트여부 */
	protected boolean flameEvent;

	/** CO이벤트여부 */
	protected boolean coEvent;

	/** 비화재보여부 */
	protected boolean notFireYn;

	/** 신호발생일시 */
	protected LocalDateTime demonRegDate;

	/** 등록일시 */
	protected LocalDateTime regDate;

	/** 배터리값 - 추가값 */
	protected int battery2Value;

	//발송 SMS등에 표시되는 화재 종류 문자열.
	public String getFireEventString() {
		String fireEventString = "";
		List<String> fireEventStringList = new ArrayList<>();
		if(smokeEvent) fireEventStringList.add("화재(연기)");
		if(temperatureEvent) fireEventStringList.add("화재(온도)");
		if(flameEvent) fireEventStringList.add("화재(불꽃)");
		if(coEvent) fireEventStringList.add("화재(CO)");

		if(CollectionUtils.isNotEmpty(fireEventStringList)) {
			fireEventString = String.join(",", fireEventStringList);
			if(notFireYn) {
				fireEventString = "[비화재보] "+fireEventString;
			}
		}

		return fireEventString;
	}

	public static FireDetectorLogDT fromIotDemonLogDT(IotDemonLogDT demonLog, DetectorInstallInfoDT detectorInstallInfo, boolean notFireYn) {
		return FireDetectorLogDT.builder()
				.fireDetectorSeq(detectorInstallInfo.getFireDetectorSeq())
				.mngAreaSeq(detectorInstallInfo.getMngAreaSeq())
				.marketSeq(detectorInstallInfo.getMarketSeq())
				.storeSeq(detectorInstallInfo.getStoreSeq())
				.ctnNo(demonLog.getCtnNo())
				.msgVer(demonLog.getMsgVer())
				.signalType(demonLog.getSignalType())
				.batteryValue(demonLog.getBatteryValueAsInteger())
				.smokeValue(demonLog.getSmokeValue())
				.temperatureValue(demonLog.getTemperatureValue())
				.flame1Value(demonLog.getFlame1Value())
				.flame2Value(demonLog.getFlame2Value())
				.coValue(demonLog.getCoValue())
				.smokeEvent(demonLog.isFireEventBySmoke())
				.temperatureEvent(demonLog.isFireEventByTemperature())
				.flameEvent(demonLog.isFireEventByFlame())
				.coEvent(demonLog.isFireEventByCo())
				.notFireYn(notFireYn)
				.demonRegDate(demonLog.getDemonRegDate())
				.battery2Value(demonLog.getBattery2ValueAsInteger())
				.build();
	}
}
