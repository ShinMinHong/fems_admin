package com.firealarm.admin.appconfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 코드 정의 설정
 *
 * 분양출처: WAS - mobile.util.CodeMap
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class CodeMap {

	/**
	 * 로그인 사용자의 권한목록
	 *
	 */
	public enum APP_USER_ROLE {
		/** 시스템관리권한 */ ROLE_SYSTEM_ADMIN,
		/** 관리자관리권한 */ ROLE_ADMIN_MNG,
		/** 관리자조회권한 */ ROLE_ADMIN_READ,
		/** 전통시장관리권한 */ ROLE_MARKET_MNG,
		/** 전통시장조회권한 */ ROLE_MARKET_READ,
		/** 관제지도접근권한 */ ROLE_CONTROLMAP,
		/** 전통시장점포관리권한 */ ROLE_STORE_MNG,
		/** 전통시장점포조회권한 */ ROLE_STORE_READ,
		/** 화재감지기관리권한 */ ROLE_FIRE_DETECTOR_MNG,
		/** 화재감지기조회권한 */ ROLE_FIRE_DETECTOR_READ,
		/** 통계조회권한 */ ROLE_STATISTICS_READ
	}

	/**
	 * 로그인 사용자의 등급
	 */
	public enum APP_USER_GRADE {
		/** 본부관리자 */ HQ_ADMIN,
		/** 관제지역관리자 */ AREA_ADMIN,
		/** 시장관리자 */ MARKET_ADMIN;

		private static Map<String, String> codeMap = new HashMap<String, String>();
		public static final Map<String, String> getCodeMap() {return codeMap;}
		public static String getCodeName(String key) {return (codeMap.containsKey(key))? codeMap.get(key) : key; }
		public static String getCodeName(APP_USER_GRADE value) { return (value == null) ? "": value.getCodeName(); }
		static {
			codeMap.put("HQ_ADMIN", "본부관리자");
			codeMap.put("AREA_ADMIN", "관제지역관리자");
			codeMap.put("MARKET_ADMIN", "전통시장관리자");
		}

		public String getCodeName() {return getCodeName(this.toString());}
	}

	/* 업로드 타입 - 업로드 타입별 파일 저장 위치 달라짐 - filestorageManager 참조 */
	public enum UPLOAD_SERVICE_TYPE {
		/** 화재감지기 */ FIREDETECTOR,
		/** EXCEL 대량등록 */ EXCEL_UPLOAD;
	}

	/**
	 * 감지기 신호 타입
	 */
	public enum DETECTOR_SIGNAL_TYPE {
		/** 주기데이터 */ PERIOD,
		/** 점검데이터 */ CHECK,
		/** 화재경보데이터 */ EVENT,
		/** 경보해제이벤트 */ CANCEL;

		private static Map<String, String> codeMap = new HashMap<String, String>();
		public static final Map<String, String> getCodeMap() {return codeMap;}
		public static String getCodeName(String key) {return (codeMap.containsKey(key))? codeMap.get(key) : key; }
		public static String getCodeName(APP_USER_GRADE value) { return (value == null) ? "": value.getCodeName(); }
		static {
			codeMap.put("PERIOD", "주기데이터");
			codeMap.put("CHECK", "점검데이터");
			codeMap.put("EVENT", "화재경보데이터");
			codeMap.put("CANCEL", "경보해제이벤트");
		}

		public String getCodeName() {return getCodeName(this.toString());}

		public static DETECTOR_SIGNAL_TYPE getSignalTypeFromDemonValue(String demonValue) {
			if("0002".equals(demonValue)) return DETECTOR_SIGNAL_TYPE.CHECK;
			else if("0010".equals(demonValue)) return DETECTOR_SIGNAL_TYPE.EVENT;
			else if("0020".equals(demonValue)) return DETECTOR_SIGNAL_TYPE.CANCEL;

			return DETECTOR_SIGNAL_TYPE.PERIOD;
		}
	}

	/**
	 * SMS 수신자 타입
	 */
	public enum SMS_USER_TYPE {
		/** 본부관리자 */ HQ_USER,
		/** 본부SMS관리자 */ HQ_SMS_USER,
		/** 지역관리자 */ MNG_AREA_USER,
		/** 시장관리자 */ MARKET_USER,
		/** 점포담당자 */ STORE_USER;

		private static Map<String, String> codeMap = new HashMap<String, String>();
		public static final Map<String, String> getCodeMap() {return codeMap;}
		public static String getCodeName(String key) {return (codeMap.containsKey(key))? codeMap.get(key) : key; }
		public static String getCodeName(APP_USER_GRADE value) { return (value == null) ? "": value.getCodeName(); }
		static {
			codeMap.put("HQ_USER", "본부관리자");
			codeMap.put("HQ_SMS_USER", "본부SMS관리자");
			codeMap.put("MNG_AREA_USER", "지역관리자");
			codeMap.put("MARKET_USER", "시장관리자");
			codeMap.put("STORE_USER", "점포담당자");
		}

		public String getCodeName() {return getCodeName(this.toString());}
	}

	/**
	 * 관제지도 관리자 타입
	 */
	public enum MAP_USER_TYPE {
		/** 통합관리자 */ HQ_ADMIN,
		/** 지역관리자 */ MNG_AREA_USER,
		/** 시장관리자 */ MARKET_USER;

		private static Map<String, String> codeMap = new HashMap<String, String>();
		public static final Map<String, String> getCodeMap() {return codeMap;}
		public static String getCodeName(String key) {return (codeMap.containsKey(key))? codeMap.get(key) : key; }
		public static String getCodeName(APP_USER_GRADE value) { return (value == null) ? "": value.getCodeName(); }
		static {
			codeMap.put("HQ_ADMIN", "통합관리자");
			codeMap.put("MNG_AREA_USER", "지역관리자");
			codeMap.put("MARKET_USER", "시장관리자");
		}

		public String getCodeName() {return getCodeName(this.toString());}
	}


	/** 이벤트 여부 코드 */
	public enum EVENT_YN {
		Y,
		N;
		private static Map<String, String> codeMap = new HashMap<String, String>();
		public static final Map<String, String> getCodeMap() {return codeMap;}
		public static String getCodeName(String key) {return (codeMap.containsKey(key))? codeMap.get(key) : key; }
		public static String getCodeName(EVENT_YN value) { return (value == null) ? "": value.getCodeName(); }
		static {
			codeMap.put("true", "ON");
			codeMap.put("false", "OFF");
		}

		public String getCodeName() {return getCodeName(this.toString());}
	}

	/** 비화재 여부 코드 */
	public enum NOT_FIRE_YN {
		TRUE,
		FALSE;
		private static Map<String, String> codeMap = new HashMap<String, String>();
		public static final Map<String, String> getCodeMap() {return codeMap;}
		public static String getCodeName(String key) {return (codeMap.containsKey(key))? codeMap.get(key) : key; }
		public static String getCodeName(EVENT_YN value) { return (value == null) ? "": value.getCodeName(); }
		static {
			codeMap.put("true", "비화재");
			codeMap.put("false", "화재");
		}

		public String getCodeName() {return getCodeName(this.toString());}
	}

	/** 감지기 상태 코드 */
	public enum FIRE_DETECTOR_STATUS {
		;
		private static Map<String, String> codeMap = new HashMap<String, String>();
		public static final Map<String, String> getCodeMap() {return codeMap;}
		public static String getCodeName(String key) {return (codeMap.containsKey(key))? codeMap.get(key) : key; }
		public static String getCodeName(FIRE_DETECTOR_STATUS value) { return (value == null) ? "": value.getCodeName(); }
		static {
			codeMap.put("1", "정상");
			codeMap.put("2", "대기");
			codeMap.put("3", "통신장애");
		}
		public String getCodeName() {return getCodeName(this.toString());}
	}

	/** 감지기 ACK 요청 코드맵 */
	public enum FIRE_DETECTOR_ACK_VALUE {
		;
		private static Map<String, String> codeMap = new HashMap<String, String>();
		public static final Map<String, String> getCodeMap() {return codeMap;}
		public static String getCodeName(String key) {return (codeMap.containsKey(key))? codeMap.get(key) : key; }
		public static String getCodeName(FIRE_DETECTOR_STATUS value) { return (value == null) ? "": value.getCodeName(); }
		static {
			codeMap.put("0", "정상");
			codeMap.put("1", "슬립요청");
		}
		public String getCodeName() {return getCodeName(this.toString());}
	}

	/** SMS 수신여부 */
	public enum SMS_RECEIVE_YN {
		;
		private static Map<String, String> codeMap = new HashMap<String, String>();
		public static final Map<String, String> getCodeMap() {return codeMap;}
		public static String getCodeName(String key) {return (codeMap.containsKey(key))? codeMap.get(key) : key; }
		public static String getCodeName(FIRE_DETECTOR_STATUS value) { return (value == null) ? "": value.getCodeName(); }
		static {
			codeMap.put("true", "수신");
			codeMap.put("false", "미수신");
		}
		public String getCodeName() {return getCodeName(this.toString());}
	}

	/** 전송여부 */
	public enum SEND_YN {
		;
		private static Map<String, String> codeMap = new HashMap<String, String>();
		public static final Map<String, String> getCodeMap() {return codeMap;}
		public static String getCodeName(String key) {return (codeMap.containsKey(key))? codeMap.get(key) : key; }
		public static String getCodeName(FIRE_DETECTOR_STATUS value) { return (value == null) ? "": value.getCodeName(); }
		static {
			codeMap.put("true", "전송");
			codeMap.put("false", "미전송");
		}
		public String getCodeName() {return getCodeName(this.toString());}
	}

	/** 화재감지기 정렬순서 */
	public enum FIRE_DETECTOR_SORT_TYPE {
		;
		private static Map<String, String> codeMap = new HashMap<String, String>();
		public static final Map<String, String> getCodeMap() {return codeMap;}
		public static String getCodeName(String key) {return (codeMap.containsKey(key))? codeMap.get(key) : key; }
		public static String getCodeName(FIRE_DETECTOR_STATUS value) { return (value == null) ? "": value.getCodeName(); }
		static {
			codeMap.put("STORE_NAME", "점포명");
			codeMap.put("CTN_NO", "CTN번호");
		}
		public String getCodeName() {return getCodeName(this.toString());}
	}

}
