package com.firealarm.admin.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendDT {

	/** 사용자id. (appConfig) */
	private String userId;

	/** 인증용 API Key. (appConfig) */
	private String apiKey;

	/** 발신자 전화번호. (appConfig) */
	private String sender;

	/** (필수) 수신번호(콤마(,)로 구분하여 최대 1000개까지 입력 가능. 번호 형식(01012345678,01012451247...)  ) */
	private String receiver;

	/** (필수) %고객명% 치환용 입력. 형식(01012345678ㅣ인왕떡집,01012451247ㅣKFC포방점...)  */
	private String destination;

	/** 문자제목(LMS,MMS만 허용) */
	private String title;

	/** (필수) 메시지 내용. 형식("%고객명%점포에 화재발생알림...")-(%고객명%)이 용어만 치환 됨. */
	private String msg;

	/** (필수) 연동테스트시 Y 적용(Y 인경우 실제문자 전송X) */
	private String testmodeYn;

	/** 메시지 타입. SMS(단문) , LMS(장문), MMS(그림문자) 구분 (없으면 길이에 따라 자동 선택됨)  */
	private String msgType;

	/** 예약일 (현재일이상)(없으면 즉시발송) */
	private String rdate;

	/** 예약시간 - 현재시간기준 10분이후(없으면 즉시발송) */
	private String rtime;

	/** 첨부이미지. 형식("/tmp/pic_57f358af08cf7_sms_.jpg"; // MMS 이미지 파일 위치) */
	private String image;
}