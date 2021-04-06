package com.firealarm.admin.biz.iot.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetectorInstallInfoDT{
	/** 관제지역고유번호 */
	private long mngAreaSeq;

	/** 관제지역명 */
	private String mngAreaName;

	/** 알림대상_점포담당자 */
	private boolean alarmStore;

	/** 알림대상_시장담당자 */
	private boolean alarmMarket;

	/** 알림대상_지역담당자 */
	private boolean alarmArea;

	/** 알림제한시간 */
	private int noAlarmTime;

	/** 전통시장고유번호 */
	private long marketSeq;

	/** 전통시장명 */
	private String marketName;

	/** 점포고유번호 */
	private long storeSeq;

	/** 점포명 */
	private String storeName;

	/** 점주명 */
	private String managerName;

	/** 시도코드 - 119다매체 */
	private String ctrdCode;

	/** 시군구코드 - 119다매체 */
	private String signguCode;

	/** 동코드 - 119다매체 */
	private String dongCode;

	/** 리코드 - 119다매체 */
	private String liCode;

	/** 휴대폰번호 */
	private String phoneNo;

	/** 우편번호 */
	private String zipCode;

	/** 도로명주소 */
	private String roadAddress;

	/** 지번주소 */
	private String parcelAddress;

	/** SMS문자알림여부 */
	private boolean smsAlarmYn;

	/** 소방서연동여부 */
	private boolean firestationAlarmYn;

	/** 연기119연동여부 */
	private boolean smokeAlarmYn;

	/** 화재감지기고유번호 */
	private long fireDetectorSeq;

	/** CTN번호 */
	private String ctnNo;

	/** 설치장소 */
	private String installPlace;

	/** 화재 감지기 명 */
	private String fireDetectorName;

	/** SMS 추가 메세지 */
	private String smsAddMessage;
}
