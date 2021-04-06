package com.firealarm.admin.biz.system.firedetectordemonlog.vo;

import org.joda.time.LocalDateTime;

import framework.annotation.Display;
import framework.base.model.AbleView;
import lombok.Data;

@Data
public class FireDetectorDemonLogVO {

	@Display(name = "화재감지기원본신호고유번호")
	private long fireDetectorDemonSignalSeq;

	@Display(name = "메시지버전")
	private String msgVer;

	@Display(name = "IP주소")
	private String ip;

	@Display(name = "Port번호")
	private String port;

	@Display(name = "CTN번호")
	private String ctnNo;

	@Display(name = "배터리값")
	private String batteryValue;

	@Display(name = "연기값")
	private String smokeValue;

	@Display(name = "온도값")
	private String temperatureValue;

	@Display(name = "불꽃1값")
	private String flame1Value;

	@Display(name = "불꽃2값")
	private String flame2Value;

	@Display(name = "CO값")
	private String coValue;

	@Display(name = "신호타입값")
	private String signalTypeValue;

	@Display(name = "화재이벤트타입")
	private String fireEventValue;

	@Display(name = "신호발생일시")
	private LocalDateTime demonRegDate;

	@Display(name = "확인여부")
	private boolean confirmYn;

	@Display(name = "확인일시")
	private LocalDateTime confirmDate;

	@Display(name = "등록일시")
	private LocalDateTime regDate;

	/** 공통 View */
	public interface BaseView extends AbleView.CommonBaseView {}
}
