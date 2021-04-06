package com.firealarm.admin.biz.store.storemng.vo;

import org.hibernate.validator.constraints.NotBlank;

import framework.annotation.Display;
import framework.annotation.ExcelUploadColumn;
import framework.base.model.AbleView;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StoreMngExcelDataVO {

	protected int rowIndex;

	/** 전통시장고유번호 */
	@ExcelUploadColumn(colIndex=1)
	@Display(name="전통시장고유번호")
	@NotBlank(groups = { BaseAction.class})
	private String marketSeq;

	/** 전통시장명 */
	@ExcelUploadColumn(colIndex=2)
	@Display(name="전통시장명")
	private String marketName;


	/** 점포명 */
	@ExcelUploadColumn(colIndex=3)
	@Display(name="점포명")
	@NotBlank(groups = { BaseAction.class})
	private String storeName;

	/** 책임자명(점주) */
	@ExcelUploadColumn(colIndex=4)
	@Display(name="책임자명(점주)")
	/*@NotBlank(groups = { BaseAction.class})*/
	private String managerName;

	/** 휴대폰번호 */
	@ExcelUploadColumn(colIndex=5)
	@Display(name="휴대폰번호")
	@NotBlank(groups = { BaseAction.class})
	private String phoneNo;

	/** 일반전화번호 */
	@ExcelUploadColumn(colIndex=6)
	@Display(name="일반전화번호")
	/*@NotBlank(groups = { BaseAction.class})*/
	private String telephoneNo;

	/** 우편번호 */
	@ExcelUploadColumn(colIndex=7)
	@Display(name="우편번호")
	@NotBlank(groups = { BaseAction.class})
	private String zipCode;

	/** 도로명주소 */
	@ExcelUploadColumn(colIndex=8)
	@Display(name="도로명주소")
	@NotBlank(groups = { BaseAction.class})
	private String roadAddress;

	/** 지번주소 */
	@ExcelUploadColumn(colIndex=9)
	@Display(name="지번주소")
	@NotBlank(groups = { BaseAction.class})
	private String parcelAddress;

	/** 지번주소 */
	@ExcelUploadColumn(colIndex=10)
	@Display(name="상세주소")
	private String detailsAddress;


	/** 업종설명 */
	@ExcelUploadColumn(colIndex=11)
	@Display(name="업종설명")
	private String businessDesc;


	/** SMS알림여부(엑셀입력) */
	@ExcelUploadColumn(colIndex=12)
	@Display(name="SMS알림여부")
	private String smsAlarmYnString;

	/** SMS알림여부 (DB값)*/
	private boolean smsAlarmYn;

	/** 소방서연동여부(엑셀입력) */
	@ExcelUploadColumn(colIndex=13)
	@Display(name="소방서연동여부")
	private String firestationAlarmYnString;

	/** 소방서연동여부 (DB값) */
	private boolean firestationAlarmYn;

	/** 공통 View */
	public interface BaseView extends AbleView.CommonBaseView {}

	/** 공통 Action */
	public interface BaseAction extends AbleView.CommonBaseView {}
	public interface CreateAction extends BaseAction {}
	public interface UpdateAction extends BaseAction {}

}


