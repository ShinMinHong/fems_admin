package com.firealarm.admin.biz.areasystem.firedetectorset.vo;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonView;

import framework.annotation.ExcelColumn;
import framework.base.model.AbleView;
import lombok.Data;

@Data
public class FireDetectorSetVO {

	/** No */
	@JsonView({ListView.class})
	private String rn;

	/** 화재감지기설정고유번호 */
	@JsonView({DetailsView.class})
	private long fireDetectorSetSeq;

	/** 화재감지기고유번호 */
	@JsonView({DetailsView.class})
	private long fireDetectorSeq;

	/** 화재감지기설정구분 */
	@JsonView({DetailsView.class})
	private String fireDetectorSetType;

	/** 화재감지기설정값 */
	@JsonView({DetailsView.class})
	private String fireDetectorSetValue;

	/** 화재감지기설정시작일시 */
	@JsonView({DetailsView.class})
	private LocalDateTime fireDetectorSetStrDate;

	/** 화재감지기설정종료일시 */
	@JsonView({DetailsView.class})
	private LocalDateTime fireDetectorSetEndDate;

	/** 화재감지기설정전송유무 */
	@JsonView({DetailsView.class})
	private String fireDetectorSetSendYn;

	/** 화재감지기설정전송일시 */
	@JsonView({DetailsView.class})
	private LocalDateTime fireDetectorSetSendDate;

	/** CTN 번호 */
	@JsonView({DetailsView.class})
	private String ctnNo;

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

	/**관리지역명 */
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

	/** 공통 Action */
	public interface BaseView extends AbleView.CommonBaseView {}
	public interface ListView extends BaseView {}
	public interface DetailsView extends BaseView {}

	@Override
	public String toString() {
		return "FireDetectorSetVO [rn=" + rn + ", fireDetectorSetSeq=" + fireDetectorSetSeq + ", fireDetectorSeq=" + fireDetectorSeq
				+ ", fireDetectorSetType=" + fireDetectorSetType + ", fireDetectorSetValue=" + fireDetectorSetValue
				+ ", fireDetectorSetStrDate=" + fireDetectorSetStrDate + ", fireDetectorSetEndDate=" + fireDetectorSetEndDate
				+ ", fireDetectorSetSendYn=" + fireDetectorSetSendYn + ", fireDetectorSetSendDate=" + fireDetectorSetSendDate
				+ ", ctnNo=" + ctnNo + ", regDate=" + regDate + ", regAdminId=" + regAdminId + ", updDate=" + updDate
				+ ", updAdminId=" + updAdminId + ", mngAreaName=" + mngAreaName + ", marketName=" + marketName
				+ ", marketSeq=" + marketSeq + ", storeName=" + storeName + "]";
	}
}
