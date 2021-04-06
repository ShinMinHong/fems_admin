package framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import framework.annotation.ExcelUploadColumn;
import framework.exception.AbleRuntimeException;

public class AbleExcelLoader {
	protected static Logger logger = LoggerFactory.getLogger(AbleExcelLoader.class);

	private AbleExcelLoader() { /**/ }

	/**
	 * 엑셀파일 체크 후 Sheet 반환
	 */
	public static Sheet getSheetFromExcel(String filename, int limitExcelLine) throws EncryptedDocumentException, InvalidFormatException, IOException {
		if(filename.isEmpty()) {
			throw new FileNotFoundException("파일을 찾을 수 없습니다.");
		}
		String ext=FilenameUtils.getExtension(filename);
		File excelfile=new File(filename);
		if(!excelfile.exists()) {
			throw new FileNotFoundException("파일을 찾을 수 없습니다.");
		}
		Sheet sheet = null;
		if(ext.contains("xls")) {	//xls, xlsx -> poi 사용
			FileInputStream inp = new FileInputStream(excelfile);
			try {
				Workbook book = WorkbookFactory.create(inp);
		        sheet = book.getSheetAt(0);
				//excel 파일 데이터 라인수 체크
				int rowCnt=sheet.getPhysicalNumberOfRows()-1;		//헤더 제외
				if(limitExcelLine > 0 && rowCnt>limitExcelLine) {
					DecimalFormat df = new DecimalFormat("#,##0");
					throw new AbleRuntimeException("Excel 파일 등록은 "+ df.format((long)limitExcelLine)+"건까지 가능합니다.");
				} else if(rowCnt <= 0) {
					throw new AbleRuntimeException("엑셀파일에 업로드 할 데이터가 존재하지 않습니다.");
				}
			} catch (Exception e) {
				//TODO 에러처리 김근목
			}
		} else {
			throw new AbleRuntimeException("열수있는 파일 형식이 아닙니다.");
		}
		return sheet;
	}

	/**
	 * 엑셀Row를 전달받은 파라미터의 VO로 반환
	 * @param row 엑셀 row
	 * @param colCnt 엑셀 col 갯수
	 * @param cls 엑셀을 담을 객체
	 * @return 엑셀로우정보가 담긴 cls의 객체
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static <E> Object getDataFromSheetrow(Row row, Class<E> cls) throws InstantiationException, IllegalAccessException {
		Object result = null;

		try {
			result = getExcelrowData(row, cls);
		} catch (Exception e) {
			logger.trace("{} - {}", e.getClass().getName(), e.getMessage(), e);
			throw new AbleRuntimeException("엑셀파일의 양식이 잘못되었습니다." + e.getMessage());
		}
		return result;
	}


	/**
	 * 엑셀 로우 데이터 획득
	 * @return Object (파라미터로 전달받은 VO객체)
	 * */
	private static <E> Object getExcelrowData(Row row, Class<E> cls) throws InstantiationException, IllegalAccessException {

		E elm = cls.newInstance();
		Field[] allFields = FieldUtils.getAllFields(elm.getClass());

		for (Field field : allFields) {
			ExcelUploadColumn excelUploadColumnAnnotation = field.getAnnotation(ExcelUploadColumn.class);
			if (excelUploadColumnAnnotation == null) {
				continue;
			}
			//VO의 annotation colIndex는 1번부터 시작하고 cell은 0번부터 시작.. sync를 위해
			int cellColIndex = excelUploadColumnAnnotation.colIndex() - 1;
			if ( excelUploadColumnAnnotation.colIndex() > 0 ) {

				field.setAccessible(true);
				Cell cell = null;
				try{
					cell = row.getCell(cellColIndex);
				} catch(Exception e) {
					throw new AbleRuntimeException("셀을 읽을수 없습니다.(마지막 열 다음에 빈 값이 있을 수 있습니다. 삭제 후 다시 시도해 주세요.)" );
				}

				if ( cell != null ){
					field.set(elm, getCellStringValue(cell));
				} else {
					field.set(elm, "");
				}
			}

		}
		return elm;
	}

	/**
	 * 엑셀,CSV 파일을 읽어서 체크 후 List로 반환
	 *
	 * @return 헤더를 제외한 데이터 String List
	 */
	public static List<String[]> getExcelData(String filename, int colCnt, int limitExcelLine, int limitCsvLine) throws EncryptedDocumentException, InvalidFormatException, IOException {
		if(filename.isEmpty()) {
			throw new FileNotFoundException("파일을 찾을 수 없습니다.");
		}
		String ext=FilenameUtils.getExtension(filename);
		File excelfile=new File(filename);
		if(!excelfile.exists()) {
			throw new FileNotFoundException("파일을 찾을 수 없습니다.");
		}
		List<String[]> list = null;
		if(ext.contains("xls")) {	//xls, xlsx -> poi 사용
			FileInputStream inp = new FileInputStream(excelfile);
			try {
				Workbook book = WorkbookFactory.create(inp);
		        Sheet sheet = book.getSheetAt(0);

				//excel 파일 데이터 라인수 체크
				int rowCnt=sheet.getPhysicalNumberOfRows()-1;		//헤더 제외
				if(limitExcelLine > 0 && rowCnt>limitExcelLine) {
					DecimalFormat df = new DecimalFormat("#,##0");
					throw new AbleRuntimeException("Excel 파일 등록은 "+ df.format((long)limitExcelLine)+"건까지 가능합니다.");
				} else if(rowCnt == 0) {
					throw new AbleRuntimeException("엑셀파일에 업로드 할 데이터가 존재하지 않습니다.");
				}
				try{
					list=getExcelData(sheet, colCnt);
				} catch(Exception e) {
					logger.trace("{} - {}", e.getClass().getName(), e.getMessage(), e);
					throw new AbleRuntimeException("엑셀파일의 양식이 잘못되었습니다.");
				}
			} catch (Exception e) {
				//TODO 에러처리 김근목
			}
		}  else {
			throw new AbleRuntimeException("열수있는 파일 형식이 아닙니다.");
		}
		return list;
	}

	private static List<String[]> getExcelData(Sheet sheet, int colCnt) {
		List<String[]> result=new ArrayList<String[]>();
		int rowCnt=sheet.getPhysicalNumberOfRows();
		for(int rowIdx=1; rowIdx<rowCnt; rowIdx++) {
			Row rowExcel=sheet.getRow(rowIdx);
			String[] row=new String[colCnt];
			for(int colIdx=0; colIdx<colCnt; colIdx++) {
				Cell cell=rowExcel.getCell(colIdx);
				if( cell!=null ) {
					row[colIdx]=getCellStringValue(cell);
				} else {
					row[colIdx]= "";
				}
			}
			result.add(row);
		}
		return result;
	}

	/** 엑셀의 셀에서 스트링 값으로 Value를 변환하여 리턴 */
	private static String getCellStringValue(Cell cell) {
		int cellType=cell.getCellType();
		if(HSSFCell.CELL_TYPE_NUMERIC == cellType) {

			/* 셀이 Date형의 경우 문자열로 변환한다. */
			if( DateUtil.isCellDateFormatted(cell) ){
				Date date = cell.getDateCellValue();
				String callString = new SimpleDateFormat("yyyy-MM-dd").format(date);
				return callString;
			}else {
				//소수점 이하 자리가 삭제되어 들어온 형태대로 형을 유지하기 위해서 아래 NumberToTextConverter를 사용
				/*return new DecimalFormat("#").format(cell.getNumericCellValue());*/
				return NumberToTextConverter.toText(cell.getNumericCellValue());
			}


		} else if(HSSFCell.CELL_TYPE_STRING == cellType) {
			return cell.getStringCellValue().trim();
		}
		return "";
	}

	/** 엑셀의 셀에서 Double 값으로 Value를 변환하여 리턴 */
	@SuppressWarnings("unused")
	private static Double getCellNumberValue(HSSFCell cell) {
		int cellType=cell.getCellType();
		if (HSSFCell.CELL_TYPE_NUMERIC == cellType) {
			return cell.getNumericCellValue();
		} else if (HSSFCell.CELL_TYPE_STRING == cellType) {
			return Double.parseDouble(cell.getStringCellValue());
		}
		return 0d;
	}

	/** String[] 데이터를 VO 객체로 변환하여 리턴 @ExcelUploadColumn(colIndex)를 사용하여 col 순번설정 */
	public static <E> Object setExcelDataToVO(String[] excelData, Class<E> cls) throws IllegalAccessException, InstantiationException {
		E elm = cls.newInstance();
		Field[] allFields = FieldUtils.getAllFields(elm.getClass());
		for (Field field : allFields) {
			ExcelUploadColumn excelUploadColumnAnnotation = field.getAnnotation(ExcelUploadColumn.class);
			if (excelUploadColumnAnnotation == null) {
				continue;
			}
			//VO의 엑셀 colIndex는 1번부터 시작하고 배열은 0번부터 시작.. sync를 위해
			int arrayColIndex = excelUploadColumnAnnotation.colIndex() - 1;
			if ( arrayColIndex > -1 && excelData.length >= arrayColIndex ) {
				field.setAccessible(true);
				field.set(elm, excelData[arrayColIndex]);
			}
		}
		return elm;
	}
}
