package framework.spring.validation;

/**
 * validation ObjectError
 * @author kim
 *
 */
public class AbleExcelUploadObjectError {

	public AbleExcelUploadObjectError(){
		super();
	}

	public AbleExcelUploadObjectError(AbleFieldError error) {
		super();
		this.code = error.getCode();
		this.displayName = error.getFieldName();
		this.field = error.getField();
		this.errorMsg = error.getMessage();
	}

	public AbleExcelUploadObjectError(String code, String displayName, String field, String errorMsg) {
		super();
		this.code = code;
		this.displayName = displayName;
		this.field = field;
		this.errorMsg = errorMsg;
	}

	public AbleExcelUploadObjectError(String code, String displayName, String field, String errorMsg,
			Object excelRowData, int rowIndex) {
		super();
		this.code = code;
		this.displayName = displayName;
		this.field = field;
		this.errorMsg = errorMsg;
		this.excelRowData = excelRowData;
		this.rowIndex = rowIndex;
	}

	public AbleExcelUploadObjectError(String code, String displayName, String field, String errorMsg,
			Object excelRowData, int rowIndex, Throwable cause) {
		super();
		this.code = code;
		this.displayName = displayName;
		this.field = field;
		this.errorMsg = errorMsg;
		this.excelRowData = excelRowData;
		this.rowIndex = rowIndex;
	}

	/** 에러 코드 */
	private String code;

	/** 필드 - displayName */
	private String displayName;

	/** 필드명 */
	private String field;

	/** 에러메세지 */
	private String errorMsg;

	/** 엑셀 업로드 데이터 */
	private Object excelRowData;

	/** 오류가 발생한 열 */
	private Object rowIndex;

    /** 에러 코드 */ public String getCode() { return code; }
    /** 에러 코드 */ public void setCode(String code) { this.code = code; }
    /** 필드 - displayName */ public String getDisplayName() { return displayName; }
    /** 필드 - displayName */ public void setDisplayName(String displayName) { this.displayName = displayName; }
    /** 필드명 */ public String getField() { return field; }
    /** 필드명 */ public void setField(String field) { this.field = field; }
    /** 에러메세지 */ public String getErrorMsg() { return errorMsg; }
    /** 에러메세지 */ public void setErrorMsg(String errorMsg) { this.errorMsg = errorMsg; }
    /** 엑셀 업로드 데이터 */ public Object getExcelRowData() { return excelRowData; }
    /** 엑셀 업로드 데이터 */ public void setExcelRowData(Object excelRowData) { this.excelRowData = excelRowData; }
    /** 오류가 발생한 열 */ public Object getRowIndex() { return rowIndex; }
    /** 오류가 발생한 열 */ public void setRowIndex(Object rowIndex) { this.rowIndex = rowIndex; }


}
