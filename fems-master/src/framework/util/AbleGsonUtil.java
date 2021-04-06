package framework.util;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * Json 관련 유틸
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleGsonUtil {
	static Logger logger = LoggerFactory.getLogger(AbleGsonUtil.class);

	private AbleGsonUtil() { /**/ }

	protected static Gson gson = null;
	static {
		gson = new GsonBuilder().serializeNulls().create();
	}

	/** Json으로 변환 */
	public static String toJSON(Object object) {
		return gson.toJson(object);
	}

	/** Json을 역직렬화하여 객체 획득 */
	public static <T> T fromJSON(String json, Class<T> classOfT) {
		return gson.fromJson(json, classOfT);
	}
	/** Json을 역직렬화하여 객체 획득 */
	public static <T> T fromJSON(String json, Type typeOfT) {
		return gson.fromJson(json, typeOfT);
	}


	/** JsonElement가 null인경우 빈문자열 처리 */
	public static String getAsSafeString(JsonElement item) {
		if(item == null || item.isJsonNull())
			return "";
		else
			return item.getAsString();
	}

	/** JsonElement가 null인경우 0 처리 */
	public static int getAsSafeInt(JsonElement item) {
		if(item == null || item.isJsonNull())
			return 0;
		else
			return item.getAsInt();
	}

	/**
	 * 주어진 노드에서 자식 노드 JsonElement를 획득 (없으면 null 반환)
	 * @param node 부모노드
	 * @param childNodeName 자식노드명
	 * @return 자식 노드 JsonElement를 획득
	 */
	public static JsonElement getSafeChildNodeElement(JsonElement node, String childNodeName) {
		Set<Entry<String, JsonElement>> entrySet = node.getAsJsonObject().entrySet();
		for (Entry<String, JsonElement> entry : entrySet) {
			if(!entry.getValue().isJsonNull() && entry.getKey().compareToIgnoreCase(childNodeName) == 0) {
				return entry.getValue();
			}
		}
		return null;
	}

	/**
	 * 주어진 노드에서 자식 노드 JsonElement를 획득 (없으면 예외 발생)
	 * @param node 부모노드
	 * @param childNodeName 자식노드명
	 * @return 자식 노드 JsonElement를 획득
	 */
	public static JsonElement getChildNodeElement(JsonElement node, String childNodeName) {
		JsonElement childNodeElement = getSafeChildNodeElement(node, childNodeName);
		if(childNodeElement != null) {
			return childNodeElement;
		}
		throw new IllegalArgumentException(String.format("Cannot found childNode %s at %s", childNodeName, node));
	}


	/**
	 * 주어진 노드에서 자식 노드의 값을 String으로 획득 (없으면 "")
	 * @param node 부모노드
	 * @param childNodeName 자식노드명
	 * @return 자식 노드의 값을 String으로 획득
	 */
	public static String getChildNodeStringSafe(JsonElement node, String childNodeName) {
		return getAsSafeString(getSafeChildNodeElement(node, childNodeName));
	}

	/**
	 * 주어진 노드에서 자식 노드의 값을 String으로 획득 (없으면 오류)
	 * @param node 부모노드
	 * @param childNodeName 자식노드명
	 * @return 자식 노드의 값을 String으로 획득
	 */
	public static String getChildNodeString(JsonElement node, String childNodeName) {
		return getChildNodeElement(node, childNodeName).getAsString();
	}

	/**
	 * 주어진 노드에서 자식 노드의 값을 String으로 획득 (없으면 기본값)
	 * @param node 부모노드
	 * @param childNodeName 자식노드명
	 * @param defaultValue 자식노드가 JsonNull일 경우 리턴할 기본 문자열 값
	 * @return 자식 노드의 값을 String으로 획득
	 */
	public static String getChildNodeString(JsonElement node, String childNodeName, String defaultValue) {
		return getChildNodeString(node, childNodeName, defaultValue, false/*silent*/);
	}

	/**
	 * 주어진 노드에서 자식 노드의 값을 String으로 획득 (없으면 기본값)
	 * @param node 부모노드
	 * @param childNodeName 자식노드명
	 * @param defaultValue 자식노드가 JsonNull일 경우 리턴할 기본 문자열 값
	 * @return 자식 노드의 값을 String으로 획득
	 */
	public static String getChildNodeString(JsonElement node, String childNodeName, String defaultValue, boolean silent) {
		JsonElement child = getSafeChildNodeElement(node, childNodeName);
		if(child==null || child.isJsonNull()) {
			if(!silent) {
				logger.debug("Cannot found childNode {}. defaultValue: '{}'", childNodeName, defaultValue);
			}
			return defaultValue;
		}
		return child.getAsString();
	}

	/**
	 * 주어진 노드에서 자식 노드의 값을 Integer로 획득 (없으면 오류)
	 * @param node 부모노드
	 * @param childNodeName 자식노드명
	 * @return 자식 노드의 값을 Integer로 획득
	 */
	public static Integer getChildNodeInteger(JsonElement node, String childNodeName) {
		String strValue = getChildNodeString(node, childNodeName);
		try {
			return Integer.parseInt(strValue);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(String.format("Cannot parse to Integer childNode %s value %s", childNodeName, strValue));
		}
	}

	/**
	 * 주어진 노드에서 자식 노드의 값을 Integer로 획득 (없으면 기본값)
	 * @param node 부모노드
	 * @param childNodeName 자식노드명
	 * @param defaultValue 자식노드가 JsonNull일 경우 리턴할 기본 Integer 값
	 * @return 자식 노드의 값을 Integer로 획득
	 */
	public static Integer getChildNodeInteger(JsonElement node, String childNodeName, Integer defaultValue) {
		String strValue = getChildNodeString(node, childNodeName, "0");
		try {
			return Integer.parseInt(strValue);
		} catch (NumberFormatException e) {
			logger.debug("childNode {} value: {} - NumberFormatException Occured. -> return defaultValue: {}", childNodeName, strValue, defaultValue);
			return 0;
		}
	}

	/**
     * 주어진 노드에서 자식 노드의 값을 JsonArray로 획득 (없으면 빈 JsonArray)
     * @param node 부모노드
     * @param childNodeName 자식노드명
     * @return 자식 노드의 값을 JsonArray로 획득
     */
    public static JsonArray getSafeChildNodeArray(JsonElement node, String childNodeName) {
        return getChildNodeArray(node, childNodeName, new JsonArray());
    }

	/**
     * 주어진 노드에서 자식 노드의 값을 JsonArray로 획득 (없으면 오류)
     * @param node 부모노드
     * @param childNodeName 자식노드명
     * @return 자식 노드의 값을 JsonArray로 획득
     */
    public static JsonArray getChildNodeArray(JsonElement node, String childNodeName) {
        return getChildNodeElement(node, childNodeName).getAsJsonArray();
    }

    /**
     * 주어진 노드에서 자식 노드의 값을 JsonArray로 획득 (없으면 기본값)
     * @param node 부모노드
     * @param childNodeName 자식노드명
     * @param defaultValue 자식노드가 JsonNull일 경우 리턴할 기본 값
     * @return 자식 노드의 값을 JsonArray로 획득
     */
    public static JsonArray getChildNodeArray(JsonElement node, String childNodeName, JsonArray defaultValue) {
        JsonElement child = getSafeChildNodeElement(node, childNodeName);
        if(child==null || child.isJsonNull()) {
            logger.debug("Cannot found childNode {}. defaultValue: {}", childNodeName, defaultValue);
            return defaultValue;
        }
        return child.getAsJsonArray();
    }

	/**
	 * 주어진 맵에서 key로 Value를 획득한다. key가 없는 경우 null 반환
	 * @param map 맵
	 * @param key 찾을 키
	 * @param defaultValue 기본값
	 * @return 맵에서 획득한 Value 또는 Key가 존재하지 않을 경우 null
	 */
	public static <T> T getMapValue(Map<String, T> map, String key) {
		Set<Entry<String, T>> entrySet = map.entrySet();
		for (Entry<String, T> entry : entrySet) {
			if(entry.getKey().compareToIgnoreCase(key) == 0) {
				return entry.getValue();
			}
		}
		return null;
	}

	/**
	 * 주어진 맵에서 key로 Value를 획득한다. key가 없는 경우 기본값 반환
	 * @param map 맵
	 * @param key 찾을 키
	 * @param defaultValue 기본값
	 * @return 맵에서 획득한 Value 또는 Key가 존재하지 않을 경우 기본값
	 */
	public static <T> T getMapValue(Map<String, T> map, String key, T defaultValue) {
		T mapValue = getMapValue(map, key);
		if(mapValue == null) {
			return defaultValue;
		}
		return mapValue;
	}

}
