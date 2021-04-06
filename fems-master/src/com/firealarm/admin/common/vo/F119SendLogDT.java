package com.firealarm.admin.common.vo;

import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.LocalDateTime;

import com.firealarm.admin.biz.iot.vo.DetectorInstallInfoDT;
import com.firealarm.admin.extlibrary.fire119.vo.Fire119SendVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class F119SendLogDT {
	/** 119다매체연동고유번호 */
	private long f119SendLogSeq;

	/** 관제지역고유번호 */
	private long mngAreaSeq;

	/** 전통시장고유번호 */
	private long marketSeq;

	/** 점포고유번호 */
	private long storeSeq;

	/** 신고유형 */
	private String sttemntTyCode;

	/** 신고상세유형 */
	private String sttemntTyDetailCode;

	/** 신고내용 */
	private String sttemntCn;

	/** 신고자명 */
	private String callName;

	/** 신고자번호 */
	private String callTel;

	/** 대표자명 */
	private String aplcntNm;

	/** 대표자번호 */
	private String aplcntTelno;

	/** 주소구분값 */
	private String addrTy;

	/** 시도코드 */
	private String ctrdCode;

	/** 시군구코드 */
	private String signguCode;

	/** 동코드 */
	private String dongCode;

	/** 리코드 */
	private String liCode;

	/** 산 여부 */
	private String mntnAt;

	/** 본번 */
	private String mnnm;

	/** 부번 */
	private String slno;

	/** 재난등급 */
	private String msfrtnGrad;

	/** 센서종류 */
	private String sensorKnd;

	/** 가스누출여부 */
	private String gasLkgAt;

	/** 온도 */
	private String tp;

	/** 연기농도 */
	private String smokeDnsty;

	/** 첨부파일갯수 */
	private int atchmnflAt;

	/** 재난종별코드 */
	private String msfrtnKnCode;

	/** 요청시도별시장규격코드 */
	private String location;

	/** 전송결과 */
	private String sendResult;

	/** 발송일시 */
	private LocalDateTime sendDate;

	public static F119SendLogDT fromFire119SendVOAndInstallInfo(Fire119SendVO fire119SendInfo, DetectorInstallInfoDT detectorInstallInfo) {
		return F119SendLogDT.builder()
				.mngAreaSeq(detectorInstallInfo.getMngAreaSeq())
				.marketSeq(detectorInstallInfo.getMarketSeq())
				.storeSeq(detectorInstallInfo.getStoreSeq())
				.sttemntTyCode(fire119SendInfo.getSttemntTyCode())
				.sttemntTyDetailCode(fire119SendInfo.getSttemntTyDetailCode())
				.sttemntCn(fire119SendInfo.getSttemntCn())
				.callName(fire119SendInfo.getCallName())
				.callTel(fire119SendInfo.getCallTel())
				.aplcntNm(fire119SendInfo.getAplcntNm())
				.aplcntTelno(fire119SendInfo.getAplcntTelno())
				.addrTy(fire119SendInfo.getAddrTy())
				.ctrdCode(fire119SendInfo.getCtrdCode())
				.signguCode(fire119SendInfo.getSignguCode())
				.dongCode(fire119SendInfo.getDongCode())
				.liCode(fire119SendInfo.getLiCode())
				.atchmnflAt(NumberUtils.toInt(fire119SendInfo.getAtchmnflAt(), 0))
				.msfrtnKnCode(fire119SendInfo.getMsfrtnKnCode())
				.location(fire119SendInfo.getLocation())
				.build();
	}
}
