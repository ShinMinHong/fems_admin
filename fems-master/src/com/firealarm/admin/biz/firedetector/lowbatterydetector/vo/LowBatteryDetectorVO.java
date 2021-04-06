package com.firealarm.admin.biz.firedetector.lowbatterydetector.vo;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonView;

import framework.annotation.ExcelColumn;
import framework.base.model.AbleView;
import lombok.Data;

@Data
public class LowBatteryDetectorVO {

	/** No */
	@JsonView({BaseView.class})
	private String rn;

	/** 감지기고유번호 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="감지기고유번호", order=25)
	private long fireDetectorSeq;

	/** 관제지역고유번호 */
	@JsonView({BaseView.class})
	private Long mngAreaSeq;

	/** 관제지역명 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="관제지역명", order=1)
	private String mngAreaName;

	/** 전통시장고유번호 */
	@JsonView({BaseView.class})
	private Long marketSeq;

	/** 전통시장명 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="전통시장명", order=5)
	private String marketName;

	/** 점포고유번호 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="점포고유번호", order=15)
	private long storeSeq;

	/** 점포명 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="점포명", order=20)
	private String storeName;

	/** 모델번호 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="모델번호", order=26)
	private String modelNo;

	/** 제조번호 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="제조번호", order=27)
	private String productNo;

	/** CTN번호 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="CTN번호", order=28)
	private String ctnNo;

	/** 단말기일련번호 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="단말기일련번호", order=29)
	private String serialNo;

	/** 유심번호 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="유심번호", order=30)
	private String usimNo;

	/** 우편번호 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="우편번호", order=31)
	private String zipCode;

	/** 도로명주소 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="도로명주소", order=32)
	private String roadAddress;

	/** 지번주소 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="지번주소", order=33)
	private String parcelAddress;

	/** 위도 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="위도", order=34)
	private String latitude;

	/** 경도 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="경도", order=35)
	private String longitude;

	/** 설치위치 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="설치위치", order=36)
	private String installPlace;

	/** 등록일시 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="등록일시", order=41)
	private LocalDateTime regDate;

	/** 등록자ID */
	@JsonView({BaseView.class})
	private String regAdminId;

	/** 최종수정일시 */
	@JsonView({BaseView.class})
	private LocalDateTime updDate;

	/** 최종수정자ID */
	@JsonView({BaseView.class})
	private String updAdminId;

	/** 감지기ACK */
	@JsonView({BaseView.class})
	@ExcelColumn(name="감지기ACK", order=37)
	private String fireDetectorAckValue;

	/** 감지기상태 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="감지기상태", order=38)
	private String fireDetectorStatus;

	/** 배터리1잔량(%) */
	@JsonView({BaseView.class})
	@ExcelColumn(name="3V 배터리(%)", order=39)
	private String remaindBattery;

	/** 최종수집일 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="최종수집일", order=42)
	private LocalDateTime lastUpdtDt;

	/** 배터리2잔량(%) */
	@JsonView({BaseView.class})
	@ExcelColumn(name="3.6V 배터리(%)", order=40)
	private String remaindBattery2;

	/** 공통 Action */
	public interface BaseView extends AbleView.CommonBaseView {}


}
