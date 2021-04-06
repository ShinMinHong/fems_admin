package com.firealarm.admin.biz.store.storesmsusermng.vo;

import org.hibernate.validator.constraints.NotBlank;

import framework.annotation.Display;
import framework.annotation.ExcelUploadColumn;
import framework.base.model.AbleView;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StoreSmsUserExcelDataVO {

	protected int rowIndex;

	/** 점포고유번호 */
	@Display(name="점포고유번호")
	@ExcelUploadColumn(colIndex=1)
	@NotBlank(groups = {BaseAction.class})
	private String storeSeq;

	/** 점포명 */
	@Display(name=" 점포명")
	@ExcelUploadColumn(colIndex=2)
	private String storeName;

	/** 책임자명 */
	@Display(name=" 책임자명")
	@ExcelUploadColumn(colIndex=3)
	@NotBlank(groups = {BaseAction.class})
	private String managerName;

	/** 휴대폰번호 */
	@Display(name=" 휴대폰번호")
	@ExcelUploadColumn(colIndex=4)
	@NotBlank(groups = {BaseAction.class})
	private String phoneNo;

	/** SMS수신여부 */
	@Display(name=" SMS수신여부")
	@ExcelUploadColumn(colIndex=5)
	private String smsReceiveYnString;

	private boolean smsReceiveYn;

	/** 공통 View */
	public interface BaseView extends AbleView.CommonBaseView {}

	/** 공통 Action */
	public interface BaseAction extends AbleView.CommonBaseView {}
	public interface CreateAction extends BaseAction {}
	public interface UpdateAction extends BaseAction {}

}
