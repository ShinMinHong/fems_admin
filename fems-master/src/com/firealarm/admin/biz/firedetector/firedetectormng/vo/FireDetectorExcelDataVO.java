package com.firealarm.admin.biz.firedetector.firedetectormng.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import framework.annotation.Display;
import framework.annotation.ExcelUploadColumn;
import framework.base.model.AbleView;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FireDetectorExcelDataVO {

	protected int rowIndex;

	/** CTN번호 */
	@ExcelUploadColumn(colIndex=1)
	@Display(name="CTN번호")
	@NotNull(groups = {BaseAction.class})
	@NotEmpty(groups = { BaseAction.class})
	private String ctnNo;

	/** 유심번호 */
	@ExcelUploadColumn(colIndex=2)
	@Display(name="유심번호")
	@NotBlank(groups = { BaseAction.class})
	private String usimNo;

	/** 단말기일련번호 */
	@ExcelUploadColumn(colIndex=3)
	@Display(name="단말기일련번호")
//	@NotBlank(groups = { BaseAction.class})
	private String serialNo;

	/** 제조번호 */
	@ExcelUploadColumn(colIndex=4)
	@Display(name="제조번호")
	private String productNo;

	/** 모델번호 */
	@ExcelUploadColumn(colIndex=5)
	@Display(name="모델번호")
	private String modelNo;

	/** 점포고유번호 */
	@ExcelUploadColumn(colIndex=6)
	@Display(name="점포고유번호")
	private String storeSeq;

	/** 점포명 */
	@ExcelUploadColumn(colIndex=7)
	@Display(name="점포명")
	private String storeName;


	/** 우편번호 */
	@ExcelUploadColumn(colIndex=8)
	@Display(name="우편번호")
	private String zipCode;

	/** 도로명주소 */
	@ExcelUploadColumn(colIndex=9)
	@Display(name="도로명주소")
	private String roadAddress;

	/** 지번주소 */
	@ExcelUploadColumn(colIndex=10)
	@Display(name="지번주소")
	private String parcelAddress;

	/** 위도 */
	@ExcelUploadColumn(colIndex=11)
	@Display(name="위도")
	private String latitude;

	/** 경도 */
	@ExcelUploadColumn(colIndex=12)
	@Display(name="경도")
	private String longitude;

	/** 설치위치 */
	@ExcelUploadColumn(colIndex=13)
	@Display(name="설치위치")
	private String installPlace;

	/** 화재감지기명 */
	@ExcelUploadColumn(colIndex=14)
	@Display(name="화재감지기명")
	private String fireDetectorName;

	/** 공통 View */
	public interface BaseView extends AbleView.CommonBaseView {}

	/** 공통 Action */
	public interface BaseAction extends AbleView.CommonBaseView {}
	public interface CreateAction extends BaseAction {}
	public interface UpdateAction extends BaseAction {}
}
