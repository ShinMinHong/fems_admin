package com.firealarm.admin.common.vo;


import lombok.Data;

@Data
public class AreaStoreNameVO {

	/** 관제지역 시장 SEQ */
	private long marketSeq;

	/** 관제지역 점포 SEQ */
	private long storeSeq;

	/** 관제지역 점포명 */
	private String storeName;
}