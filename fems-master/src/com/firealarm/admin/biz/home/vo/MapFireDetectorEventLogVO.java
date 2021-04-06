package com.firealarm.admin.biz.home.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.firealarm.admin.common.vo.FireDetectorLogDT;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 화재감지기 현재 상태 상세정보의 이벤트 목록 정보
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MapFireDetectorEventLogVO extends FireDetectorLogDT {

	@JsonProperty("signalTypeAsString")
	public String getSignalTypeAsString() {
		return signalType.getCodeName();
	}

	@JsonProperty("demonRegDateAsString")
	public String getDemonRegDateAsString() {
		return demonRegDate==null?"":demonRegDate.toString("yyyy-MM-dd hh:mm:ss").replace(" ", "<br />");
	}
}

