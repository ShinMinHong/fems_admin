package com.firealarm.admin.common.vo;

import org.joda.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreNoAlarmDT {
	/** 점포고유번호 */
	private long storeSeq;

	/** 00시 */
	private boolean noAlarm00;

	/** 01시 */
	private boolean noAlarm01;

	/** 02시 */
	private boolean noAlarm02;

	/** 03시 */
	private boolean noAlarm03;

	/** 04시 */
	private boolean noAlarm04;

	/** 05시 */
	private boolean noAlarm05;

	/** 06시 */
	private boolean noAlarm06;

	/** 07시 */
	private boolean noAlarm07;

	/** 08시 */
	private boolean noAlarm08;

	/** 09시 */
	private boolean noAlarm09;

	/** 10시 */
	private boolean noAlarm10;

	/** 11시 */
	private boolean noAlarm11;

	/** 12시 */
	private boolean noAlarm12;

	/** 13시 */
	private boolean noAlarm13;

	/** 14시 */
	private boolean noAlarm14;

	/** 15시 */
	private boolean noAlarm15;

	/** 16시 */
	private boolean noAlarm16;

	/** 17시 */
	private boolean noAlarm17;

	/** 18시 */
	private boolean noAlarm18;

	/** 19시 */
	private boolean noAlarm19;

	/** 20시 */
	private boolean noAlarm20;

	/** 21시 */
	private boolean noAlarm21;

	/** 22시 */
	private boolean noAlarm22;

	/** 23시 */
	private boolean noAlarm23;

	/** 최종수정일시 */
	private LocalDateTime updDate;

	/** 최종수정자ID */
	private String updAdminId;


	/**
	 * 요청한 시간이 알림제한시간인지 판단
	 * @param eventTime
	 * @return
	 */
	public boolean isNoAlarmEvent(LocalDateTime eventTime) {
		if(eventTime == null) {
			return false;
		}

		int hourOfDay = eventTime.getHourOfDay();
		if(hourOfDay == 0) {
			return noAlarm00;
		} else if(hourOfDay == 1) {
			return noAlarm01;
		} else if(hourOfDay == 2) {
			return noAlarm02;
		} else if(hourOfDay == 3) {
			return noAlarm03;
		} else if(hourOfDay == 4) {
			return noAlarm04;
		} else if(hourOfDay == 5) {
			return noAlarm05;
		} else if(hourOfDay == 6) {
			return noAlarm06;
		} else if(hourOfDay == 7) {
			return noAlarm07;
		} else if(hourOfDay == 8) {
			return noAlarm08;
		} else if(hourOfDay == 9) {
			return noAlarm09;
		} else if(hourOfDay == 10) {
			return noAlarm10;
		} else if(hourOfDay == 11) {
			return noAlarm11;
		} else if(hourOfDay == 12) {
			return noAlarm12;
		} else if(hourOfDay == 13) {
			return noAlarm13;
		} else if(hourOfDay == 14) {
			return noAlarm14;
		} else if(hourOfDay == 15) {
			return noAlarm15;
		} else if(hourOfDay == 16) {
			return noAlarm16;
		} else if(hourOfDay == 17) {
			return noAlarm17;
		} else if(hourOfDay == 18) {
			return noAlarm18;
		} else if(hourOfDay == 19) {
			return noAlarm19;
		} else if(hourOfDay == 20) {
			return noAlarm20;
		} else if(hourOfDay == 21) {
			return noAlarm21;
		} else if(hourOfDay == 22) {
			return noAlarm22;
		} else if(hourOfDay == 23) {
			return noAlarm23;
		}
		return false;
	}
}
