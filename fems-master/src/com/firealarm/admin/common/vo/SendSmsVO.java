package com.firealarm.admin.common.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SendSmsVO {

	//수신자 연락처
	protected String toTelNo;

	//발송자 연락처
	protected String fromTelNo;

	//sms 메시지
	protected String smsMessage;

	//sms 제목
	protected String smsTitle;

}
