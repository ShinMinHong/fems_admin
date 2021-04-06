package com.firealarm.admin.biz.areasystem.marketmng.vo;

import org.joda.time.LocalDateTime;

import framework.annotation.ExcelColumn;
import lombok.Data;

@Data
public class MarketMngVO {

	/** 순번 */
	private long rn;

	/** 관제지역고유번호 */
	@ExcelColumn(name="관제지역명", order=0)
	private long mngAreaSeq;

	/** 전통시장명  */
	@ExcelColumn(name="전통시장명", order=2)
	private String marketName;

	/** 담당자 */
	@ExcelColumn(name="담당자", order=3)
	private String managerName;

	/** 휴대폰번호  */
	@ExcelColumn(name="휴대폰번호", order=4)
	private String phoneNo;

	/** 일반전화번호  */
	@ExcelColumn(name="일반전화번호", order=5)
	private String telephoneNo;

	/** 전통시장고유번호  */
	@ExcelColumn(name="전통시장고유번호", order=1)
	private long marketSeq;

	/** 우편번호 */
	@ExcelColumn(name="우편번호", order=7)
	private String zipCode;

	/** 주소(도로명) */
	@ExcelColumn(name="주소(도로명)", order=8)
	private String roadAddress;

	/** 주소(지번) */
	@ExcelColumn(name="주소(지번)", order=9)
	private String parcelAddress;

	/** 위도  */
	@ExcelColumn(name="위도", order=10)
	private String latitude;

	/** 경도 */
	@ExcelColumn(name="경도", order=11)
	private String longitude;

	/** 지도축적 */
	@ExcelColumn(name="지도축적", order=12)
	private Integer scale;

	/** 관할소방서명  */
	@ExcelColumn(name="관할소방서명", order=13)
	private String firestationName;

	/** 소방서담당자명  */
	@ExcelColumn(name="소방서담당자명", order=14)
	private String firestationManagerName;

	/** 소방서담당자일반전화번호  */
	@ExcelColumn(name="소방서연락처", order=15)
	private String firestationTelephoneNo;

	/** 시도코드  */
	@ExcelColumn(name="시도코드", order=16)
	private String ctrdCode;

	/** 시군구코드  */
	@ExcelColumn(name="시군구코드", order=17)
	private String signguCode;

	/** 동코드  */
	@ExcelColumn(name="동코드", order=18)
	private String dongCode;

	/** 리코드  */
	@ExcelColumn(name="리코드", order=19)
	private String liCode;

	/** 등록일 */
	@ExcelColumn(name="등록일", order=20)
	private LocalDateTime regDate;

	/** 등록자 */
	private String regAdminId;

	/** 최종수정자*/
	private LocalDateTime updDate;

	/** 수정자 */
	private String updAdminId;

	@Override
	public String toString() {
		return "MarketMngVO [marketName=" + marketName + ", managerName=" + managerName + ", phoneNo=" + phoneNo
				+ ", telephoneNo=" + telephoneNo + ", zipCode=" + zipCode + ", latitude=" + latitude + ", longitude="
				+ longitude + ", scale=" + scale + ", firestationName=" + firestationName + ", firestationManagerName="
				+ firestationManagerName + ", firestationTelephoneNo=" + firestationTelephoneNo + ", ctrdCode="
				+ ctrdCode + ", signguCode=" + signguCode + ", dongCode=" + dongCode + ", liCode="
				+ liCode + ", firestationTelephoneNo=" + firestationTelephoneNo + ", regDate="
				+ regDate + ", updDate=" + updDate + ", updAdminId=" + updAdminId + "]";
	}
}
