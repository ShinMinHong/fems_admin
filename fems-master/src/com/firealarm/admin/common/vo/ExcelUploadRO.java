package com.firealarm.admin.common.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.ToString;

/**
 * 엑셀 등록 결과 객체
 * @author kim
 */
@ToString
public class ExcelUploadRO {

	/** 총건수 */
	private Integer totalCount = 0;

	/** 성공건수 */
	private Integer successCount = 0;

	/** 실패건수 */
	private Integer failCount  = 0;

	/** 반환 객체(UPDATE 데이터) */
	private List<Object> excelResultVO = new ArrayList<Object>();

	/** 반환 객체 변경 */
	public void setExcelResultVO( List<Object> vo ){
		this.excelResultVO = vo;
	}

	/** 성공 */
	public void addSuccess(Object dt){
		this.successCount++;
		this.addTotal();
		this.excelResultVO.add(dt);
	}

	/** 실패 */
	public void addFail(Object dt){
		this.failCount++;
		this.addTotal();
		this.excelResultVO.add(dt);
	}

	/** 토탈 */
	protected void addTotal(){
		this.totalCount++;
	}

	/** 성공건수 */ public Integer getSuccessCount() { return successCount; }
	/** 총건수 */ public Integer getTotalCount() { return totalCount; }
	/** 실패건수 */ public Integer getFailCount() { return failCount; }
	/** 반환 객체(UPDATE 데이터) */ public List<Object> getExcelResultVO() { return excelResultVO; }
}
