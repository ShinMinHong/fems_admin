package com.firealarm.admin.common.vo;

import org.springframework.web.multipart.MultipartFile;

import com.firealarm.admin.appconfig.AppConst;

import framework.annotation.Display;
import framework.base.model.AbleView;
import framework.validation.constraints.AbleAllowExtensions;
import framework.validation.constraints.AbleAllowFileSize;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ExcelUploadVO {

	/** 엑셀 업로드 파일 */
	@AbleAllowExtensions(groups = { BaseAction.class}, value=AppConst.ALLOWED_FILE_EXTENSION_FOR_EXCEL)
	@AbleAllowFileSize(groups = { BaseAction.class}, value=AppConst.ALLOWED_FILE_SIZE_FOR_EXCEL, allowSizeText=AppConst.ALLOWED_DISPLAY_FILE_SIZE_FOR_EXCEL)

	@Display(name="업로드파일")
	private MultipartFile uploadExcelFile;

	/** 공통 View */
	public interface BaseView extends AbleView.CommonBaseView {}

	/** 공통 Action */
	public interface BaseAction extends AbleView.CommonBaseView {}
	public interface CreateAction extends BaseAction {}
	public interface UpdateAction extends BaseAction {}

}
