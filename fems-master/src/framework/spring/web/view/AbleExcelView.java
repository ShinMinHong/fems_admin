package framework.spring.web.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import framework.exception.AbleRuntimeException;

/**
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 *
 */
@SuppressWarnings("deprecation")
public class AbleExcelView extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) {

		AbleExcelCommand command = (AbleExcelCommand)model.get(AbleExcelCommand.MODEL_KEY);

		if(command == null) {
				logger.error("AbleExcelCommand cannot found at Model");
				throw new AbleRuntimeException("엑셀파일 생성에 실패하였습니다.");
		}

		command.buildExcelDocument(workbook, request, response);

	}

}

