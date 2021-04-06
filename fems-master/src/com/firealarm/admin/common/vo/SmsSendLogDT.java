package com.firealarm.admin.common.vo;

import org.joda.time.LocalDateTime;

import com.firealarm.admin.appconfig.CodeMap.APP_USER_GRADE;
import com.firealarm.admin.appconfig.CodeMap.SMS_USER_TYPE;
import com.firealarm.admin.biz.iot.vo.DetectorInstallInfoDT;
import com.firealarm.admin.security.vo.AdminDT;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendLogDT {
	/** SMS발송이력고유번호 */
	private long smsSendLogSeq;

	/** 관제지역고유번호 */
	private long mngAreaSeq;

	/** 관제지역명 */
	private String mngAreaName;

	/** 전통시장고유번호 */
	private long marketSeq;

	/** 전통시장명 */
	private String marketName;

	/** 점포고유번호 */
	private long storeSeq;

	/** 점포명 */
	private String storeName;

	/** 수신자타입 */
	private SMS_USER_TYPE smsUserType;

	/** 수신자명 */
	private String receiveUserNm;

	/** 수신번호 */
	private String receivePhoneNo;

	/** SMS제목 */
	private String smsTitle;

	/** SMS내용 */
	private String smsMessage;

	/** 발송일시 */
	private LocalDateTime sendDate;

	public static SmsSendLogDT fromStoreSmsUserDTAndInstallInfo(StoreSmsUserDT user, DetectorInstallInfoDT detectorInstallInfo, String smsTitle, String smsMessage) {
		return SmsSendLogDT.builder()
			.mngAreaSeq(detectorInstallInfo.getMngAreaSeq())
			.mngAreaName(detectorInstallInfo.getMngAreaName())
			.marketSeq(detectorInstallInfo.getMarketSeq())
			.marketName(detectorInstallInfo.getMarketName())
			.storeSeq(detectorInstallInfo.getStoreSeq())
			.storeName(detectorInstallInfo.getStoreName())
			.smsUserType(SMS_USER_TYPE.STORE_USER)
			.receiveUserNm(user.getManagerName())
			.receivePhoneNo(user.getPhoneNo().replaceAll("[^0-9]", ""))
			.smsTitle(smsTitle)
			.smsMessage(smsMessage)
			.build();
	}

	public static SmsSendLogDT fromAdminDTAndInstallInfo(AdminDT user, DetectorInstallInfoDT detectorInstallInfo, String smsTitle, String smsMessage) {
		SMS_USER_TYPE smsUserType = SMS_USER_TYPE.MARKET_USER;
		if( APP_USER_GRADE.AREA_ADMIN.equals(user.getRolegroupCode()) ) {
			smsUserType = SMS_USER_TYPE.MNG_AREA_USER;
		} else if( APP_USER_GRADE.HQ_ADMIN.equals(user.getRolegroupCode()) ) {
			smsUserType = SMS_USER_TYPE.HQ_USER;
		}

		return SmsSendLogDT.builder()
			.mngAreaSeq(detectorInstallInfo.getMngAreaSeq())
			.mngAreaName(detectorInstallInfo.getMngAreaName())
			.marketSeq(detectorInstallInfo.getMarketSeq())
			.marketName(detectorInstallInfo.getMarketName())
			.storeSeq(detectorInstallInfo.getStoreSeq())
			.storeName(detectorInstallInfo.getStoreName())
			.smsUserType(smsUserType)
			.receiveUserNm(user.getAdminName())
			.receivePhoneNo(user.getPhoneNo().replaceAll("[^0-9]", ""))
			.smsTitle(smsTitle)
			.smsMessage(smsMessage)
			.build();
	}

	public static SmsSendLogDT fromSmsAdminDTAndInstallInfo(SmsAdminDT user, DetectorInstallInfoDT detectorInstallInfo, String smsTitle, String smsMessage) {
		SMS_USER_TYPE smsUserType = SMS_USER_TYPE.HQ_SMS_USER;
		return SmsSendLogDT.builder()
			.mngAreaSeq(detectorInstallInfo.getMngAreaSeq())
			.mngAreaName(detectorInstallInfo.getMngAreaName())
			.marketSeq(detectorInstallInfo.getMarketSeq())
			.marketName(detectorInstallInfo.getMarketName())
			.storeSeq(detectorInstallInfo.getStoreSeq())
			.storeName(detectorInstallInfo.getStoreName())
			.smsUserType(smsUserType)
			.receiveUserNm(user.getAdminName())
			.receivePhoneNo(user.getPhoneNo().replaceAll("[^0-9]", ""))
			.smsTitle(smsTitle)
			.smsMessage(smsMessage)
			.build();
	}
}
