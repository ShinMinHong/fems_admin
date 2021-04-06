package framework.util;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * String 관련 유틸
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleStringUtil {
	public static final Logger logger = LoggerFactory.getLogger(AbleStringUtil.class);

	private AbleStringUtil() { /**/ }

	/**
	 * 파일의 bytes 크기를 읽기좋게 변경 ("B", "KB", "MB", "GB", "TB")
	 * @param size 파일 크기
	 * @return 파일 크기 표시용 문자열
	 */
	public static String readableFileSize(long size) {
	    if(size <= 0) return "0";
	    final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	/**
	 *  br 태그를 제외한 모든 태그를 escape처리
	 */
	public static String htmlEscapeExcludeBR(String text) {
		if(StringUtils.isEmpty(text)) {
			return text;
		}
		return StringEscapeUtils.escapeHtml(text.replace("\n", "<br/>\n")).replace("&lt;br/&gt;", "<br/>");
	}


	/**
	 * 문자열에 대문자 포함여부
	 */
	public static boolean isPatternUpperCaseInclude(String str) {
		Pattern p = Pattern.compile(".*[A-Z].*", Pattern.UNICODE_CASE);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 문자열에 소문자 포함여부
	 */
	public static boolean isPatternLowerCaseInclude(String str) {
		Pattern p = Pattern.compile(".*[a-z].*", Pattern.UNICODE_CASE);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/** 연속된 숫자, 반목된 문자(숫자) 비허용 체크 */
	public static boolean validateBadSequenceLength(String pw, Integer badSequenceLength/* 3 */) {
		Integer badSequenceLengthToApply = badSequenceLength;
		if(badSequenceLengthToApply == null) {
			badSequenceLengthToApply = 3;
		}
		String numbers = "01234567890";
		int start = badSequenceLengthToApply - 1;
		String seq = "_" + pw.substring(0, start);
		for (int i = start; i < pw.length(); i++) {
			seq = seq.substring(1) + pw.charAt(i);
			logger.debug("seq: {}", seq);
			if (numbers.indexOf(seq) > -1) {
				logger.debug("sequencial pw invalid.");
				return false; // sequencial
			}
			if ("".equals(seq.replace(seq.substring(0, 1), ""))) {
				logger.debug("equivalant pw invalid.");
				return false; // equivalant
			}
		}
		return true;
	}

	/**
	 * 입력된 숫자를 세자리마다 컴마를 추가하여 문자열로 반환한다.
	 * @param number 입력된 금액
	 * @return 컴마가 추가된 문자열
	 */
	public static String formatCurrency(Number number) {
		DecimalFormat df = new DecimalFormat("#,##0");
		return df.format(number);
	}

	/**
	 * 문자열을 주어진 Bytes이하로 절삭 (문자 Bytes 중간에 걸려서 마지막글자가 깨지지않게...)
	 * @param text 문자열
	 * @param limitBytes 제한 Bytes
	 * @param charset 처리시 사용한 문자열 셋
	 * @return 길이가 제한된 문자열
	 */
	public static String elipsisStringWithBytes(String text, int limitBytes, Charset charset) {
		if(StringUtils.isEmpty(text)) {
			return "";
		}
		byte[] textBytes = text.getBytes(charset);
		String textElipsised = new String(textBytes, 0, Math.min(textBytes.length, limitBytes), charset);
		if(text.startsWith(textElipsised)) {
			return textElipsised;
		}
		return textElipsised.substring(0, textElipsised.length() - 1);
	}


	public static boolean isValidHpNo(String hpNo){
		if ( StringUtils.isEmpty(hpNo) ){
			return false;
		}
		String regExp = "";
		regExp = "^01(0|1|6|7|8|9)-([0-9]{4}|[0-9]{3})-[0-9]{4}$";
		return hpNo.matches(regExp);
	}

	public static boolean isValidDate(String date){
		if ( StringUtils.isEmpty(date) ){
			return false;
		}
		String regExp = "";
		regExp = "^((1[9]|2[0-1])({2})(0[1-9]|1[0-2])([0-3])({1}))$";
		return date.matches(regExp);
	}

	/**
	 * 이메일 체크
	 * @param value
	 * @return
	 */
	public static boolean isEMailValid(String value) {
		String EMAIL_PATTERN = "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

		if ( StringUtils.isEmpty(value) ){
			return false;
		}
		String strValue = value.toString();
		boolean isMatch = strValue.matches(EMAIL_PATTERN);
		return isMatch;
	}
}
