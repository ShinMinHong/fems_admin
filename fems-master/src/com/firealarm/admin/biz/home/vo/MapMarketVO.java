package com.firealarm.admin.biz.home.vo;

import lombok.Data;

/**
 * 관제지도 - 시장 VO
 *
 */
@Data
public class MapMarketVO {

	/** 시장 - Seq */
	protected long marketSeq;

	/** 시장 - 이름 */
	protected String marketName;

	/** 최초 중심좌표 - 위도 */
	protected String latitude;

	/** 최초 중심좌표 - 경도 */
	protected String longitude;

	/** 최초 축적 */
	protected int scale;
}

