package com.firealarm.admin.extlibrary.fire119.vo;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;

import com.firealarm.admin.extlibrary.fire119.Seedx;

import lombok.Data;

@Data
public class Fire119SendVO {
	/** 신고유형 - 필수 - 700000033: 고정(모바일 App 신고) */
	private String sttemntTyCode = "700000033";

	/** 신고상세유형 - 필수 - 700000118: IOT신고 */
	private String sttemntTyDetailCode = "700000118";

	/** 주소구분값 - 필수 - 법정동 구분의 경우 1 */
	private String addrTy = "1";

	/** ★신고내용 - 필수 - IOT신고 + Store.지번주소. */
	private String sttemntCn;

	/** 신고자명 */
	private String callName;

	/** 신고자번호 */
	private String callTel;

	/** 대표자명 */
	private String aplcntNm;

	/** 대표자번호 */
	private String aplcntTelno;

	/** 시도코드 - 필수 */
	private String ctrdCode;

	/** 시군구코드 - 필수 */
	private String signguCode;

	/** 동코드 - 필수 */
	private String dongCode;

	/** 리코드 - 필수 */
	private String liCode;

	/** 첨부파일갯수 - 필수 */
	private String atchmnflAt = "0";

	/** 재난종별코드 - 필수 - 재난종별코드 (화재, 기타) 13306(기타)로 고정 */
	private String msfrtnKnCode = "13306";

	/** 요청시도별시장규격코드 - 필수 */
	private String location = "11000";

	/**
	 * 119 다매체에 전송할 전문 문자열 생성. (항목별 암호화 및 base64 인코딩 포함)
	 * @return 전송 문자열
	 */
	public String getEncodedXmlString() throws UnsupportedEncodingException {
		Seedx seedObj = new Seedx();
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
		sb.append("<WDS>");
		sb.append("<Set name=\"XmlRequest\" version=\"1.0\">");
		sb.append("<Service key=\"\" name=\"");
		sb.append("XmlRequestController");
		sb.append("\" version=\"1.0\">");
		sb.append("<Values>");

		if(StringUtils.isNotBlank(this.sttemntTyCode)){
			sb.append("<STTEMNT_TY_CODE>");
			sb.append(seedObj.EncryptByte(this.sttemntTyCode));
			sb.append("</STTEMNT_TY_CODE>");
		}

		if(StringUtils.isNotBlank(this.sttemntTyDetailCode)){
			sb.append("<STTEMNT_TY_DETAIL_CODE>");
			sb.append(seedObj.EncryptByte(this.sttemntTyDetailCode));
			sb.append("</STTEMNT_TY_DETAIL_CODE>");
		}

		if(StringUtils.isNotBlank(this.sttemntCn)){
			sb.append("<STTEMNT_CN>");
			sb.append(seedObj.EncryptByte(this.sttemntCn));
			sb.append("</STTEMNT_CN>");
		}

		if(StringUtils.isNotBlank(this.callName)){
			sb.append("<CALL_NAME>");
			sb.append(seedObj.EncryptByte(this.callName));
			sb.append("</CALL_NAME>");
		}

		if(StringUtils.isNotBlank(this.callTel)){
			sb.append("<CALL_TEL>");
			sb.append(seedObj.EncryptByte(this.callTel));
			sb.append("</CALL_TEL>");
		}

		if(StringUtils.isNotBlank(this.aplcntNm)){
			sb.append("<APLCNT_NM>");
			sb.append(seedObj.EncryptByte(this.aplcntNm));
			sb.append("</APLCNT_NM>");
		}

		if(StringUtils.isNotBlank(this.aplcntTelno)){
			sb.append("<APLCNT_TELNO>");
			sb.append(seedObj.EncryptByte(this.aplcntTelno));
			sb.append("</APLCNT_TELNO>");
		}

		if(StringUtils.isNotBlank(this.addrTy)){
			sb.append("<ADDR_TY>");
			sb.append(seedObj.EncryptByte(this.addrTy));
			sb.append("</ADDR_TY>");
		}

		if(StringUtils.isNotBlank(this.ctrdCode)){
			sb.append("<CTRD_CODE>");
			sb.append(seedObj.EncryptByte(this.ctrdCode));
			sb.append("</CTRD_CODE>");
		}

		if(StringUtils.isNotBlank(this.signguCode)){
			sb.append("<SIGNGU_CODE>");
			sb.append(seedObj.EncryptByte(this.signguCode));
			sb.append("</SIGNGU_CODE>");
		}

		if(StringUtils.isNotBlank(this.dongCode)){
			sb.append("<DONG_CODE>");
			sb.append(seedObj.EncryptByte(this.dongCode));
			sb.append("</DONG_CODE>");
		}

		if(StringUtils.isNotBlank(this.liCode)){
			sb.append("<LI_CODE>");
			sb.append(seedObj.EncryptByte(this.liCode));
			sb.append("</LI_CODE>");
		}

		if(StringUtils.isNotBlank(this.atchmnflAt)){
			sb.append("<ATCHMNFL_AT>");
			sb.append(seedObj.EncryptByte(this.atchmnflAt));
			sb.append("</ATCHMNFL_AT>");
		}

		if(StringUtils.isNotBlank(this.msfrtnKnCode)){
			sb.append("<MSFRTN_KN_CODE>");
			sb.append(seedObj.EncryptByte(this.msfrtnKnCode));
			sb.append("</MSFRTN_KN_CODE>");
		}

		if(StringUtils.isNotBlank(this.location)){
			sb.append("<LOCATION>");
			sb.append(seedObj.EncryptByte(this.location));
			sb.append("</LOCATION>");
		}
		sb.append("</Values>");
		sb.append("</Service>");
		sb.append("</Set>");
		sb.append("</WDS>");

		return sb.toString();
	}

	public boolean hasFire119AreaCode() {
		return StringUtils.isNotBlank(this.ctrdCode) && StringUtils.isNotBlank(this.signguCode)
				&& StringUtils.isNotBlank(this.dongCode) && StringUtils.isNotBlank(this.liCode);
	}
}
