package framework.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * Collection관련 Util
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleCollectionUtil {

	private AbleCollectionUtil() { /**/ }

	/** Map<K, V>를 K로 정렬 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <K extends Comparable, V extends Comparable> Map<K, V> sortByKeys(Map<K, V> map){
        List<K> keys = new LinkedList<K>(map.keySet());
        Collections.sort(keys);

        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K,V> sortedMap = new LinkedHashMap<K,V>();
        for(K key: keys){
            sortedMap.put(key, map.get(key));
        }

        return sortedMap;
    }

	/** Map<K, V>를 V로 정렬 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <K extends Comparable, V extends Comparable> Map<K, V> sortByValues(Map<K, V> map){
        List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());

        Collections.sort(entries, (o1, o2) -> o1.getValue().compareTo(o2.getValue()) );

        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K, V> sortedMap = new LinkedHashMap<K, V>();

        for(Map.Entry<K, V> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

	/** Map<String, ?> 를 List<NameValuePair>로 변환 */
	public static List<NameValuePair> convertMapToNameValuePairList(Map<String, ?> parameters) {
		List<NameValuePair> paramNameValuePairList = new ArrayList<NameValuePair>();
		for (Map.Entry<String, ?> item : parameters.entrySet()) {
			String key = item.getKey();
			Object value = item.getValue();
			String valueString = (value == null) ? "" : value.toString();
			paramNameValuePairList.add(new BasicNameValuePair(key, valueString));
		}
		return paramNameValuePairList;
	}

}
