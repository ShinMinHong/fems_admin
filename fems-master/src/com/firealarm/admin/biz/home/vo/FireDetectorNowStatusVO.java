package com.firealarm.admin.biz.home.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 화재감지기 현재 상태정보 - 관제지도 주기적인 정보조회에서 사용
 */
@Data
public class FireDetectorNowStatusVO {

	/** 단말기 - Seq */
	private long fireDetectorSeq;

	/** 화재 상태*/
	private boolean alarmFire;

	/** 화재 상태 - 비화재보 여부 */
	private boolean notFireYn;

	/** 배터리1 값 */
	@JsonIgnore
	private long batteryValue;

	/** 배터리2 값 */
	@JsonIgnore
	private long battery2Value;

	/** 통신두절여부 */
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
}

