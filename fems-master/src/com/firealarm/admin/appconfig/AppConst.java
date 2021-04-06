package com.firealarm.admin.appconfig;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

/**
 * Admin 서비스용 Const 정의
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AppConst {
	static Logger logger = LoggerFactory.getLogger(AppConst.class);

	/** 생성자 - 인스턴스 생성 방지 */
	private AppConst() {}

	/** UTF-8 Charset */
	public static final Charset UTF8 = Charset.forName("UTF-8");

	/** 127.0.0.1 */
	public static final String LOCALHOST_IP = "127.0.0.1";

	public static final String APPLICATION_JSON_VALUE = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8";
	public static final MediaType APPLICATION_JSON = MediaType.valueOf(APPLICATION_JSON_VALUE);
	public static final String TEXT_HTML_VALUE = MediaType.TEXT_HTML_VALUE + ";charset=utf-8";
	public static final MediaType TEXT_HTML = MediaType.valueOf(TEXT_HTML_VALUE);

	/** Spring REST API RequestMapping의 Produces 기본 값 */
	//public static final String API_PRODUCES = APPLICATION_JSON_VALUE;
	//IE 9에서 json을 iframe transport로 전송시 파일로 저장하려는 버그 발생. APP은 무관
	public static final String API_PRODUCES = TEXT_HTML_VALUE;

	/** Spring REST API RequestMapping의 Produces MediaType 기본 값 */
	public static final MediaType API_PRODUCES_MEDIATYPE = MediaType.valueOf(API_PRODUCES);

	/** Spring Data Pageable요청의 페이지당 아이템 표시 갯수 제한 */
	public static final int PAGE_SIZE_LIMIT = 1000;

	/** 암호화키 (개발용) */
	public static final String SECRET_KEY_DEV = "123456789012345678901234567890wq"; //DEV Server Key
	/** 암호화키 (실서버, 스테이징서버용) */
	public static final String SECRET_KEY_REAL = "1nTkc1I4itNMwjboDPvlo6lhlCQ2VJIU"; //REAL Server Key

	/** 엑셀업로드파일 허용확장자 */
	public static final String ALLOWED_FILE_EXTENSION_FOR_EXCEL = "xls,xlsx";
	/** 엑셀업로드파일 허용크기 */
	public static final int ALLOWED_FILE_SIZE_FOR_EXCEL = 15*1024*1024;
	/** 엑셀업로드파일 허용크기 표시 메시지 */
	public static final String ALLOWED_DISPLAY_FILE_SIZE_FOR_EXCEL = "15MB";

	/** 게시판 파일 허용 확장자 */
	public static final String ALLOWED_EXTENSION_FOR_IMAGE = "png,gif,jpg";

	/** 게시판 첨부파일 허용 최대 갯수 */
	public static final int ALLOWED_BOARDFILE_COUNT = 3;

	/** 게시판 첨부파일 허용크기 */
	public static final int ALLOWED_BOARDFILE_SIZE = 15*1024*1024;

	/** 게시판 첨부파일 허용크기 표시 메시지 */
	public static final String ALLOWED_BOARDFILE_DISPLAY_SIZE = "15MB";

	public static final String AUTH_TEMPNAME_FOR_USERNAME = "tryusername";

}
