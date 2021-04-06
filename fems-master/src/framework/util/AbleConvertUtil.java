package framework.util;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * 형변환 관련 유틸
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleConvertUtil {
	protected static Logger logger = LoggerFactory.getLogger(AbleConvertUtil.class);

	private AbleConvertUtil() {}

	/**
	 * 버퍼를 UTF-8 문자열로 변환 (Trim처리)
	 */
	public static String convertToString(byte[] buf) {
		String s = new String(buf, Charset.forName("UTF-8"));
		return StringUtils.trimTrailingWhitespace(s);
	}


	/**
	 * 일반 Map을 RestTemplate의 FormHttpMessageConverter가 변환할 수 있는 MultiValueMap으로 변환
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static MultiValueMap<String, Object> convertToMultiValueMap(Map<String, ?> map) {
		MultiValueMap<String, Object> result = new LinkedMultiValueMap<String, Object>();

		if (result.getClass().isAssignableFrom(map.getClass())) {
			return (MultiValueMap<String, Object>) map;
		}

		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			result.add(key, map.get(key));
		}
		return result;
	}

}
