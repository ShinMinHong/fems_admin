package framework.log;

import java.util.HashMap;

/**
 * 로깅 컨텍스트 - 로그출력을 위한 맵
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class LoggingContextMap extends HashMap<String, Object> {

	private static final long serialVersionUID = 3982853031996764723L;

	HashMap<String, Object> verboseMap = new HashMap<String, Object>();

	public static LoggingContextMap create() {
		LoggingContextMap map = new LoggingContextMap();
		return map;
	}

	/**
	 * 로깅값 추가 (일반맵, verbose맵에 모두 추가)
	 * map.put과 같지만 함수형프로래밍 지원
	 * @param key
	 * @param value
	 * @return LoggingContextMap this 객체
	 */
	public LoggingContextMap add(String key, Object value) {
		super.put(key, value);
		verboseMap.put(key, value);
		return this;
	}

	/**
	 * 로깅값 추가 (verbose맵에만 추가)
	 * map.put과 같지만 함수형프로래밍 지원
	 * @param key
	 * @param value
	 * @return LoggingContextMap this 객체
	 */
	public LoggingContextMap addVerbose(String key, Object value) {
		verboseMap.put(key, value);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((verboseMap == null) ? 0 : verboseMap.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoggingContextMap other = (LoggingContextMap) obj;
		if (verboseMap == null) {
			if (other.verboseMap != null)
				return false;
		} else if (!verboseMap.equals(other.verboseMap))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public String toString(boolean verbose) {
		if(!verbose) {
			return super.toString();
		}
		return verboseMap.toString();
	}

}
