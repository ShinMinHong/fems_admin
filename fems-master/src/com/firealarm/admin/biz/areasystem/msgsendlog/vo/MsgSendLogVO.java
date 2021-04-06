package com.firealarm.admin.biz.areasystem.msgsendlog.vo;

import org.joda.time.LocalDateTime;

import com.firealarm.admin.appconfig.CodeMap.SMS_USER_TYPE;

import framework.annotation.ExcelColumn;
import lombok.Data;

@Data
public class MsgSendLogVO {
	//순번
	private Integer rn;

	/** SMS발송이력고유번호(PK) */
	private long smsSendLogSeq;

	/* 관제지역고유번호 */
	private Long mngAreaSeq;

	/* 관제지역명 */
	@ExcelColumn(name="관제지역명", order=1)
	private String mngAreaName;

	/* 전통시장고유번호 */
	private Long marketSeq;

	/* 전통시장명 */
	@ExcelColumn(name="전통시장명", order=2)
	private String marketName;

	/* 점포고유번호 */
	private Long storeSeq;

	/* 점포명 */
	@ExcelColumn(name="점포명", order=3)
	private String storeName;

	/* SMS수신자타입 */
	@ExcelColumn(name="SMS수신자타입", order=4)
	private SMS_USER_TYPE smsUserType;

	/* 수신자명 */
	@ExcelColumn(name="수신자명", order=5)
	private String receiveUserNm;

	/* 수신번호 */
	@ExcelColumn(name="수신번호", order=6)
	private String receivePhoneNo;

	/* SMS제목 */
	@ExcelColumn(name="SMS제목", order=7)
	private String smsTitle;

	/* SMS내용 */
	@ExcelColumn(name="SMS내용", order=8)
	private String smsMessage;

	/* 발송일시 */
	@ExcelColumn(name="발송일시", order=9)
	private LocalDateTime sendDate;

}
