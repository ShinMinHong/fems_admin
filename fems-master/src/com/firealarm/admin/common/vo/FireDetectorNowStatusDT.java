package com.firealarm.admin.common.vo;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.firealarm.admin.biz.iot.vo.DetectorInstallInfoDT;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FireDetectorNowStatusDT {
	/** 화재감지기고유번호 */
	private long fireDetectorSeq;

	/** 화재여부 */
	private boolean alarmFire;

	/** 연기이벤트여부 */
	private boolean smokeEvent;

	/** 온도이벤트여부 */
	private boolean temperatureEvent;

	/** 불꽃이벤트여부 */
	private boolean flameEvent;

	/** CO이벤트여부 */
	private boolean coEvent;

	/** 비화재보여부 */
	private boolean notFireYn;

	/** 배터리값 */
	private int batteryValue;

	/** 최종수집일 */
	private LocalDateTime lastUpdtDt;

	/** 배터리값 - 추가값 */
	private int battery2Value;

	/** 조회시 Column - 신호없음 */
	private boolean noSignal;

	@JsonProperty("lowBattery")
	public boolean isLowBattery() {
		return batteryValue < 20 || battery2Value < 20;
	}

	@JsonProperty("errorStatus")
	public String errorStatus() {
		boolean lowBattery = isLowBattery();
		if (alarmFire && notFireYn)
			return "fakeFire";
		else if(alarmFire)
			return "fire";
		else if (noSignal)
			return "noSignal";
		else if (lowBattery)
			return "lowBattery";
		return "normal";
	}

	public static FireDetectorNowStatusDT fromIotDemonLogDT(IotDemonLogDT demonLog, DetectorInstallInfoDT detectorInstallInfo, boolean notFireYn) {
		return FireDetectorNowStatusDT.builder()
				.fireDetectorSeq(detectorInstallInfo.getFireDetectorSeq())
				.alarmFire(demonLog.isFireEvent())
				.smokeEvent(demonLog.isFireEventBySmoke())
				.temperatureEvent(demonLog.isFireEventByTemperature())
				.flameEvent(demonLog.isFireEventByFlame())
				.coEvent(demonLog.isFireEventByCo())
				.notFireYn(notFireYn)
				.batteryValue(demonLog.getBatteryValueAsInteger())
				.battery2Value(demonLog.getBattery2ValueAsInteger())
				.build();
	}
}
