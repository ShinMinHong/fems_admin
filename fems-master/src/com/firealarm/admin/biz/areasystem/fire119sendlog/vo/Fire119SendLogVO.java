package com.firealarm.admin.biz.areasystem.fire119sendlog.vo;

import org.joda.time.LocalDateTime;

import framework.annotation.ExcelColumn;
import lombok.Data;

/**
 *  119 다매체 발신 관리 Service
 * @author SMH
 */
@Data
public class Fire119SendLogVO {

	//순번
	private Integer rn;

	/** 119다매체연동고유번호(PK) */
	private long f119SendLogSeq;

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

	/* 신고유형 */
	@ExcelColumn(name="신고유형", order=4)
	private String sttemntTyCode;

	/* 신고상세유형 */
	@ExcelColumn(name="신고상세유형", order=5)
	private String sttemntTyDetailCode;

	/* 신고내용 */
	@ExcelColumn(name="신고내용", order=6)
	private String sttemntCn;

	/* 신고자명 */
	@ExcelColumn(name="신고자명", order=7)
	private String callName;

	/* 신고자전화번호 */
	@ExcelColumn(name="신고자전화번호", order=8)
	private String callTel;

	/* 대표자명 */
	@ExcelColumn(name="대표자명", order=9)
	private String aplcntNm;

	/* 대표자번호 */
	@ExcelColumn(name="대표자번호", order=10)
	private String aplcntTelno;

	/* 주소구분값 */
	@ExcelColumn(name="주소구분값", order=11)
	private String addrTy;

	/* 시도코드 */
	@ExcelColumn(name="시도코드", order=12)
	private String ctrdCode;

	/* 시군구코드 */
	@ExcelColumn(name="시군구코드", order=13)
	private String signguCode;

	/* 동코드 */
	@ExcelColumn(name="동코드", order=14)
	private String dongCode;

	/* 리코드 */
	@ExcelColumn(name="리코드", order=15)
	private String liCode;

	/* 산여부 */
	@ExcelColumn(name="산여부", order=16)
	private String mntnAt;

	/* 본번 */
	@ExcelColumn(name="본번", order=17)
	private String mnnm;

	/* 부번 */
	@ExcelColumn(name="부번", order=18)
	private String slno;

	/* 재난등급 */
	@ExcelColumn(name="재난등급", order=19)
	private String msfrtnGrad;

	/* 센서종류 */
	@ExcelColumn(name="센서종류", order=20)
	private String sensorKnd;

	/* 가스유출여부 */
	@ExcelColumn(name="가스유출여부", order=21)
	private String gasLkgAt;

	/* 온도 */
	@ExcelColumn(name="온도", order=22)
	private Long tp;

	/* 연기농도 */
	@ExcelColumn(name="연기농도", order=23)
	private String smokeDnsty;

	/* 첨부파일갯수 */
	@ExcelColumn(name="첨부파일갯수", order=24)
	private Long atchmnflAt;

	/* 재난종별코드 */
	@ExcelColumn(name="재난종별코드", order=25)
	private String msfrtnKnCode;

	/* 요청시도별시장규격코드 */
	@ExcelColumn(name="요청시도별시장규격코드", order=26)
	private String location;

	/* 전송결과 */
	@ExcelColumn(name="전송결과", order=27)
	private String sendResult;

	/* 발송일시 */
	@ExcelColumn(name="발송일시", order=28)
	private LocalDateTime sendDate;

}
