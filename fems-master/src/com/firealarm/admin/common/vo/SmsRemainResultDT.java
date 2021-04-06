package com.firealarm.admin.common.vo;

import framework.annotation.Display;
import lombok.Data;

@Data
public class SmsRemainResultDT {
	/** 결과코드 */
	@Display(name="결과코드")
	private String resultCode;

	/** 결과메시지 */
	@Display(name="결과메시지")
	private String message;

	/** SMS잔여수 */
	@Display(name="SMS잔여수")
	private int smsCnt;

	/** LMS잔여수 */
	@Display(name="LMS잔여수")
	private int lmsCnt;

	/** MMS잔여수 */
	@Display(name="MMS잔여수")
	private int mmsCnt;
}