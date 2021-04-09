package com.firealarm.admin.biz.areasystem.firedetectorset.vo;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonView;
import com.firealarm.admin.common.vo.FireDetectorNowStatusDT;
import com.firealarm.admin.common.vo.UploadFileVO;

import framework.annotation.Display;
import framework.annotation.ExcelColumn;
import framework.base.model.AbleView;
import lombok.Data;

@Data
public class FireDetectorSetVO {

	/** No */
	@JsonView({ListView.class})
	private String rn;

	/** 화재감지기고유번호 */
	@JsonView({DetailsView.class})
	@ExcelColumn(name="화재감지기고유번호", order=0)
	private long fireDetectorSeq;

	/** 점포고유번호 */
	@JsonView({BaseView.class})
	private Long storeSeq;

	/** 화재감지기명 */
	@ExcelColumn(name="화재감지기명", order=3)
	@JsonView({BaseView.class})
	private String fireDetectorName;

	/** 모델번호 */
	@ExcelColumn(name="모델번호", order=4)
	@JsonView({BaseView.class})
	private String modelNo;

	/** 제조번호 */
	@ExcelColumn(name="제조번호", order=5)
	@JsonView({DetailsView.class})
	private String productNo;

	/** CTN번호 */
	@ExcelColumn(name="CTN번호", order=6)
	@JsonView({BaseView.class})
	private String ctnNo;

	/** 단말기일련번호 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="단말기일련번호", order=7)
	private String serialNo;

	/** 유심번호 */
	@JsonView({DetailsView.class})
	@ExcelColumn(name="유심번호", order=8)
	private String usimNo;

	/** 우편번호 */
	@JsonView({DetailsView.class})
	@ExcelColumn(name="우편번호", order=9)
	private String zipCode;

	/** 도로명주소 */
	@ExcelColumn(name="도로명주소", order=10)
	@JsonView({DetailsView.class})
	private String roadAddress;

	/** 지번주소 */
	@ExcelColumn(name="지번주소", order=11)
	@JsonView({DetailsView.class})
	private String parcelAddress;

	/** 위도 */
	@ExcelColumn(name="위도", order=12)
	@JsonView({DetailsView.class})
	private String latitude;

	/** 경도 */
	@JsonView({DetailsView.class})
	@ExcelColumn(name="경도", order=13)
	private String longitude;

	/** 설치위치 */
	@JsonView({DetailsView.class})
	@ExcelColumn(name="설치위치", order=14)
	private String installPlace;

	/** 감지기ACK */
	@JsonView({DetailsView.class})
	private String fireDetectorAckValue;

	/** SMS 추가메시지 */
	@JsonView({DetailsView.class})
	private String smsAddMessage;

	/** 등록일시 */
	@JsonView({DetailsView.class})
	private LocalDateTime regDate;

	/** 등록자ID */
	@JsonView({DetailsView.class})
	private String regAdminId;

	/** 최종수정일시 */
	@JsonView({DetailsView.class})
	private LocalDateTime updDate;

	/** 최종수정자ID */
	@JsonView({DetailsView.class})
	private String updAdminId;

	/** 관제지역명 */
	@ExcelColumn(name="관제지역명", order=1)
	@JsonView({BaseView.class})
	private String mngAreaName;

	/** 전통시장명 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="전통시장명", order=2)
	private String marketName;

	/** 전통시장 Seq */
	@JsonView({BaseView.class})
	private Long marketSeq;

	/** 지역  Seq */
	private Long mngAreaSeq;

	/** 점포명 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="점포명", order=3)
	private String storeName;

	/** 감지기 상태 */
	@JsonView({BaseView.class})
	@ExcelColumn(name="감지기 상태", order=15)
	private String fireDetectorStatus;

	/** 최종수집일 */
	@JsonView({DetailsView.class})
	@ExcelColumn(name="최종수집일", order=16)
	private LocalDateTime lastUpdtDt;

	/** 첨부된 파일 목록 */
	@Display(name="첨부된 파일 목록")
	@JsonView({DetailsView.class})
	private List<UploadFileVO> boardFiles = new ArrayList<UploadFileVO>();

	/** 화재감지기 현재 상태 */
	@JsonView({DetailsView.class})
	private List<FireDetectorNowStatusDT> fireDetectorNowStatusDT;

	/** 공통 Action */
	public interface BaseView extends AbleView.CommonBaseView {}
	public interface ListView extends BaseView {}
	public interface DetailsView extends BaseView {}

	@Override
	public String toString() {
		return "FireDetectorSetVO [rn=" + rn + ", fireDetectorSeq=" + fireDetectorSeq + ", storeSeq=" + storeSeq
				+ ", modelNo=" + modelNo + ", productNo=" + productNo + ", ctnNo=" + ctnNo + ", serialNo=" + serialNo
				+ ", usimNo=" + usimNo + ", zipCode=" + zipCode + ", roadAddress=" + roadAddress + ", parcelAddress="
				+ parcelAddress + ", latitude=" + latitude + ", longitude=" + longitude + ", installPlace="
				+ installPlace + ", regDate=" + regDate + ", regAdminId=" + regAdminId + ", updDate=" + updDate
				+ ", updAdminId=" + updAdminId + ", fireDetectorAckValue=" + fireDetectorAckValue + ", mngAreaName="
				+ mngAreaName + ", marketName=" + marketName + ", marketSeq=" + marketSeq + ", storeName=" + storeName
				+ ", fireDetectorStatus=" + fireDetectorStatus + ", lastUpdtDt=" + lastUpdtDt + "]";
	}
}
