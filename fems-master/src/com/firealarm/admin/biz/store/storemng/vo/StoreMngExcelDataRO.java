package com.firealarm.admin.biz.store.storemng.vo;

import framework.spring.validation.AbleExcelUploadObjectError;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper=false)
public class StoreMngExcelDataRO extends StoreMngExcelDataVO {

	/** 결과코드 (성공:S, 실패:F) */
	private String resultCode;

	/** 결과메세지 */
	private String resultMsg;

	/** 오류 필드명 */
	private String field;

	public void setError( AbleExcelUploadObjectError error){
		this.resultMsg = error.getErrorMsg();
		this.field = error.getField();
		this.resultCode = "F";
	}

	public void setSuccess (){
		this.resultMsg = "정상";
		this.resultCode = "S";
	}

}
