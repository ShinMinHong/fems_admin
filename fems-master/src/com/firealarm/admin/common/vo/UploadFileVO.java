package com.firealarm.admin.common.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.firealarm.admin.appconfig.CodeMap.UPLOAD_SERVICE_TYPE;

import framework.base.model.AbleView;
import lombok.Data;

@Data
public class UploadFileVO {

	public UploadFileVO() {
		super();
	}

	public UploadFileVO(UPLOAD_SERVICE_TYPE serviceType, String orginalFileName, String attachedFileName, String attachedFilePath, long attachedFileSize) {
		super();
		this.serviceType = serviceType;
		this.orginalFileName = orginalFileName;
		this.attachedFileName = attachedFileName;
		this.attachedFilePath = attachedFilePath;
		this.attachedFileSize = attachedFileSize;
	}

	/** 업로드파일 서비스타입 */
	@JsonView({BaseView.class})
	private UPLOAD_SERVICE_TYPE serviceType;

	/** 원본 파일명 */
	@JsonView({BaseView.class})
	private String orginalFileName;

	/**  서버저장(unique) 파일명 */
	@JsonView({BaseView.class})
	private String attachedFileName;

	/** 업로드 루트폴더내의 파일 전체 경로 */
	@JsonView({BaseView.class})
	private String attachedFilePath;

	/** 업로드 파일 사이즈 */
	@JsonView({BaseView.class})
	private long attachedFileSize;

	/**  파일 SEQ */
	@JsonView({BaseView.class})
	private long attachedFileSeq;

	/** 공통 Action */
	public interface BaseView extends AbleView.CommonBaseView {}
	public interface ListView extends BaseView {}
	public interface DetailsView extends BaseView {}

}
