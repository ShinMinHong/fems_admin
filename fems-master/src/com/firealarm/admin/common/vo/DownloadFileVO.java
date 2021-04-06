package com.firealarm.admin.common.vo;

import lombok.Data;

@Data
public class DownloadFileVO {

	/** 원본 파일명 */
	private String orginalFileName;

	/** DB에 저장된 파일 경로 */
	private String attachedFilePath;

}
