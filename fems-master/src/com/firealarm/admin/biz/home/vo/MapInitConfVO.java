package com.firealarm.admin.biz.home.vo;

import com.firealarm.admin.appconfig.CodeMap.MAP_USER_TYPE;

import lombok.Data;

/**
 * 관제지도 최초 설정 값
 */
@Data
public class MapInitConfVO {

	/** 맵 사용자 Type */
	protected MAP_USER_TYPE mapUserType;

	/** 최초 중심좌표 - 위도 */
	protected String latitude;

	/** 최초 중심좌표 - 경도 */
	protected String longitude;

	/** 최초 축적 */
	protected int scale;
}

