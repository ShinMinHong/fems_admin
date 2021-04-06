package com.firealarm.admin.common.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SendMmsVO {

	//수신자 연락처
	protected String toTelNo;

	//발송자 연락처
	protected String fromTelNo;

	//mms 메시지
	protected String mmsMessage;

	//mms 제목
	protected String mmsTitle;

}
