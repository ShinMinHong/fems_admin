import org.apache.commons.lang.ObjectUtils;
import org.springframework.util.StringUtils;

public class Ognl {

	private Ognl() { /**/ }

	/** StringUtils.isEmpty */
	public static boolean isEmpty(Object obj) {
		return StringUtils.isEmpty(obj) ;
	}

	public static boolean equalsIgnoreCase(Object obj1, Object obj2) {
		return org.apache.commons.lang3.StringUtils.equalsIgnoreCase(ObjectUtils.toString(obj1), ObjectUtils.toString(obj2));
	}

	/** 문자열 true(대소문자 무시)와 Boolean true 인 경우 true 반환 */
	public static boolean isTrue(Object obj) {
		if ( StringUtils.isEmpty(obj) ) {
			return false;
		}
		if ( (isBooleanType(obj) && (boolean)obj) || org.apache.commons.lang.StringUtils.equalsIgnoreCase(obj.toString().trim(), "true")  ) {
			return true;
		}
		return false;
	}

	/** 문자열 false(대소문자 무시)와 Boolean false 인 경우 true 반환 */
	public static boolean isFalse(Object obj) {
		if ( StringUtils.isEmpty(obj) ) {
			return false;
		}
		if ( (isBooleanType(obj) && !(boolean)obj) || org.apache.commons.lang.StringUtils.equalsIgnoreCase(obj.toString().trim(), "false")  ) {
			return true;
		}
		return false;
	}

	private static boolean isBooleanType(Object obj) {
		return (obj.getClass().isAssignableFrom(boolean.class)) || (obj.getClass().isAssignableFrom(Boolean.class));
	}
}