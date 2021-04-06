package com.firealarm.admin.biz.home.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 비밀번호 변경 VO
 *
 */
@Data
public class MapFireDetectorVO {

	/** 상점 - 관제지역Seq */
	private long mngAreaSeq;

	/** 상점 - 시장Seq */
	private long marketSeq;

	/** 상점 - 시장 이름 */
	private String marketName;

	/** 상점 - Seq */
	private long storeSeq;

	/** 상점 - 이름 */
	private String storeName;

	/** 단말기 - Seq */
	private long fireDetectorSeq;

	/** 단말기 - ctn */
	private String ctnNo;

	/** 단말기 - 위도 */
	private String latitude;

	/** 단말기 - 경도 */
	private String longitude;

	/** 단말기상태 - 화재 */
	private boolean alarmFire;

	/** 단말기상태 - 비화재보 여부 */
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

