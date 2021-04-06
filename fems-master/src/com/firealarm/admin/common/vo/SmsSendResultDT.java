package com.firealarm.admin.common.vo;

import framework.annotation.Display;
import lombok.Data;

@Data
public class SmsSendResultDT {
	/** 결과코드 */
	@Display(name="결과코드")
	private String resultCode;

	/** 결과메시지 */
	@Display(name="결과메시지")
	private String message;

	/** 메세지 고유 ID */
	@Display(name="메세지 고유 ID")
	private String msgId;

	/** 요청성공 건수 */
	@Display(name="요청성공 건수")
	private Integer successCnt;

	/** 요청실패 건수 */
	@Display(name="요청실패 건수")
	private Integer errorCnt;

	/** 메시지 타입 */
	@Display(name="메세지 타입")
	private String msgType;
}