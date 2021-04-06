package framework.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;

import framework.annotation.Display;
import framework.annotation.ExcelColumn;
import framework.exception.AbleRuntimeException;

public class AbleVOUtil {
	protected static final Logger logger = LoggerFactory.getLogger(AbleVOUtil.class);

	private AbleVOUtil() { /**/ }

	/**
	 * @param vo 프라퍼티속성을 가진 VO 객체
	 * @return VO객체에서 속성Name-Value Map을 획득한다.
	 */
	public static Map<String, Object> getPropertyMap(Object vo) {
		Map<String, Object> map = new HashMap<String, Object>();
		PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(vo);
		for (PropertyDescriptor descriptor : descriptors) {
			Class<?> classHasReadMethod = descriptor.getReadMethod().getDeclaringClass();
			if(classHasReadMethod.equals(Object.class)) {
				continue; //skip
			}
			String name = descriptor.getName();
			try {
				Object value = PropertyUtils.getProperty(vo, name);
				map.put(name, value);
			} catch (Exception e) {
				logger.trace("{} - {}", e.getClass().getName(), e.getMessage(), e);
			}
		}
		return map;
	}

	////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Display Annotation의 order 속성으로 정렬된 필드-순서 맵을 획득
	 * @param voClass 대상 VO
	 * @return Display Annotation의 order 속성으로 정렬된 필드-순서 맵
	 */
	public static Map<String, Integer> getDisplayOrderMap(Class<?> voClass) {
		Field[] fields = voClass.getDeclaredFields();
		Map<String, Integer> voFieldOrderMap = new HashMap<String, Integer>();
		for (Field field : fields) {
			String fieldName = field.getName();
			// 노출 순서
			int displayOrder = Integer.MAX_VALUE;
			Display displayAnnotation = field.getAnnotation(Display.class);
			if (displayAnnotation != null) {
				displayOrder = displayAnnotation.order();
			}
			voFieldOrderMap.put(fieldName, displayOrder);
		}
		voFieldOrderMap = AbleCollectionUtil.sortByValues(voFieldOrderMap);
		return voFieldOrderMap;
	}

	/**
	 * Display Annotation의 name 속성으로 필드-필드명 맵을 획득 (기본로케일)
	 * @param voClass 대상 VO
	 * @return Display Annotation의 name 속성으로 필드-필드명 맵
	 */
	public static Map<String, String> getDisplayNameMap(Class<?> voClass) {
		return getDisplayNameMap(voClass, null, null, Locale.getDefault());
	}

	/**
	 * Display Annotation의 name 속성으로 필드-필드명 맵을 획득
	 * @param voClass 대상 VO
	 * @param messageSource 메시지 소스
	 * @param prefix 메시지소스로 메시지 변환처리시 필드 앞에 붙일 Prefix (예: com.col.)
	 * @param locale 메시지 적용 로케일
	 * @return Display Annotation의 name 속성으로 필드-필드명 맵
	 */
	public static Map<String, String> getDisplayNameMap(Class<?> voClass, MessageSource messageSource, String prefix, Locale locale) {
		Field[] fields = voClass.getDeclaredFields();
		Map<String, String> voFieldNameMap = new HashMap<String, String>();
		for (Field field : fields) {
			// 기본 필드명
			String fieldName = field.getName();

			// @DisplayName
			String displayName = null;
			Display displayAnnotation = field.getAnnotation(Display.class);

			if (displayAnnotation != null) {
				displayName = displayAnnotation.name();
			} else {
				displayName = fieldName;
				if (StringUtils.isNotEmpty(prefix)) {
					displayName = prefix + displayName;
				}
				displayName = displayName.toLowerCase();
			}

			if (messageSource != null) {
				displayName = messageSource.getMessage(displayName, null, locale);
			}
			voFieldNameMap.put(fieldName, displayName);
		}
		return voFieldNameMap;
	}

	/**
	 * Display Annotation의 name 속성으로 필드명을 획득 (기본로케일)
	 * @param voClass 대상 VO
	 * @param declaredFieldName 선언된 필드명
	 * @return Display Annotation의 name 속성으로 필드명
	 */
	public static String getDisplayName(Class<?> voClass, String declaredFieldName) {
		return getDisplayName(voClass, declaredFieldName, null, null, Locale.getDefault());
	}

	/**
	 * Display Annotation의 name 속성으로 필드명을 획득
	 * @param voClass 대상 VO
	 * @param declaredFieldName 선언된 필드명
	 * @param messageSource 메시지 소스
	 * @param prefix 메시지소스로 메시지 변환처리시 필드 앞에 붙일 Prefix (예: com.col.)
	 * @param locale 메시지 적용 로케일
	 * @return Display Annotation의 name 속성으로 필드명
	 */
	public static String getDisplayName(Class<?> voClass, String declaredFieldName, MessageSource messageSource, String prefix, Locale locale) {
		Field field;
		try {
			field = voClass.getDeclaredField(declaredFieldName);
		} catch (Exception e) {
			throw new AbleRuntimeException("Failed to get declared field.", e);
		}

		// 기본 필드명
		String fieldName = field.getName();

		// @DisplayName
		String displayName = null;
		Display displayAnnotation = field.getAnnotation(Display.class);

		if (displayAnnotation != null) {
			displayName = displayAnnotation.name();
		} else {
			displayName = fieldName;
			if (StringUtils.isNotEmpty(prefix)) {
				displayName = prefix + displayName;
			}
		}

		if (messageSource != null) {
			displayName = messageSource.getMessage(displayName, null, locale);
		}
		return displayName;
	}

	/**
	 * map의 값으로 VO의 필드값을 설정
	 * @param voClass 필드값을 설정한 VO
	 * @param map 키-값 맵
	 */
	public static void setVOProperties(Object voClass, Map<String, Object>map){
		try{
			for(Field field:voClass.getClass().getDeclaredFields()){
				if (field.getType().equals(DateTime.class)) {
					field.set(voClass, AbleDateUtil.parseToDateTime(map.get(field.getName()).toString()));
				} else {
					field.set(voClass, map.get(field.getName()));
				}
			}
		}catch(Exception e){
			logger.trace("{} - {}", e.getClass().getName(), e.getMessage(), e);
		}
	}
	////////////////////////////////////////////////////////////////////////////////////
	/**
	 * VO클래스에서 PropertyDescriptor의 Name과 같은 Field나 getter의 ExcelColumn Annotaion 획득
	 * @param voClass VO 클래스
	 * @param descriptor PropertyDescriptor
	 * @return VO클래스에서 PropertyDescriptor의 Name과 같은 field나 getter의 ExcelColumn Annoraion
	 */
	public static ExcelColumn getExcelColumnAnnotation(Class<?> voClass, PropertyDescriptor descriptor){
		String propName = descriptor.getName();
		ExcelColumn annotation = null;
		//1. from Field

		try{
			Field field = voClass.getDeclaredField(propName);
			annotation = field.getAnnotation(ExcelColumn.class);
			if(annotation != null){
				return annotation;
			}
		}catch(SecurityException|NoSuchFieldException e){
			logger.trace("{} - {}", e.getClass().getName(), e.getMessage(), e);
		}

		//2. getter Method
		Method method = PropertyUtils.getReadMethod(descriptor);
		annotation = method.getAnnotation(ExcelColumn.class);
		if(annotation !=null){
			return annotation;
		}
		return null;
	}

	/**
	 * ExcelColumn Annotation의 order 속성으로 정렬된 필드-순서 맵을 획득
	 * @param voClass 대상 VO
	 * @return ExcelCoumn Annotation의 order 속성으로 정렬된 필드-순서 맵
	 */
	public static Map<String, Integer> getExcelColumnOrderMap(Class<?> voClass) {
		Field[] fields = voClass.getDeclaredFields();
		Map<String, Integer> voFieldOrderMap = new HashMap<String, Integer>();
		for (Field field : fields) {
			ExcelColumn excelColumnAnnotation = field.getAnnotation(ExcelColumn.class);
			if (excelColumnAnnotation == null || excelColumnAnnotation.ignore()) {
				continue;
			}

			String fieldName = field.getName();
			// 노출 순서
			int excelColumnOrder = excelColumnAnnotation.order();
			voFieldOrderMap.put(fieldName, excelColumnOrder);
		}
		voFieldOrderMap = AbleCollectionUtil.sortByValues(voFieldOrderMap);
		return voFieldOrderMap;
	}

	/**
	 * ExcelColumn Annotation의 groupname 속성으로 필드-필드명 맵을 획득 (기본로케일)
	 * @param voClass 대상 VO
	 * @return ExcelColumn Annotation의 groupname 속성으로 필드-필드명 맵
	 */
	public static Map<String, String> getExcelColumnGroupNameMap(Class<?> voClass) {
		return getExcelColumnGroupNameMap(voClass, null, null, Locale.getDefault());
	}

	/**
	 * ExcelColumn Annotation의 groupname 속성으로 필드-필드명 맵을 획득
	 * @param voClass 대상 VO
	 * @param messageSource 메시지 소스
	 * @param prefix 메시지소스로 메시지 변환처리시 필드 앞에 붙일 Prefix (예: com.col.)
	 * @param locale 메시지 적용 로케일
	 * @return ExcelColumn Annotation의 groupname 속성으로 필드-필드명 맵
	 */
	public static Map<String, String> getExcelColumnGroupNameMap(Class<?> voClass, MessageSource messageSource, String prefix, Locale locale) {
		Map<String, String> voFieldNameMap = new HashMap<String, String>();

		PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(voClass);
		for (PropertyDescriptor descriptor : descriptors) {
			ExcelColumn excelColumnAnnotation =getExcelColumnAnnotation(voClass, descriptor);
			if (excelColumnAnnotation == null || excelColumnAnnotation.ignore()) {
				continue;
			}

			// 기본 필드명
			String columnName = descriptor.getName();
			// @ExcelColumn.name
			String excelColumnName = null;
			excelColumnName = excelColumnAnnotation.groupname();
			if (excelColumnName == null) {
				excelColumnName = columnName;
				if (StringUtils.isNotEmpty(prefix)) {
					excelColumnName = prefix + excelColumnName;
				}
				excelColumnName = excelColumnName.toLowerCase();
			}
			if (messageSource != null) {
				excelColumnName = messageSource.getMessage(excelColumnName, null, locale);
			}
			voFieldNameMap.put(columnName, excelColumnName);
		}
		return voFieldNameMap;
	}

	/**
	 * ExcelColumn Annotation의 name 속성으로 필드-필드명 맵을 획득 (기본로케일)
	 * @param voClass 대상 VO
	 * @return ExcelColumn Annotation의 name 속성으로 필드-필드명 맵
	 */
	public static Map<String, String> getExcelColumnNameMap(Class<?> voClass) {
		return getExcelColumnNameMap(voClass, null, null, Locale.getDefault());
	}

	/**
	 * ExcelColumn Annotation의 name 속성으로 필드-필드명 맵을 획득
	 * @param voClass 대상 VO
	 * @param messageSource 메시지 소스
	 * @param prefix 메시지소스로 메시지 변환처리시 필드 앞에 붙일 Prefix (예: com.col.)
	 * @param locale 메시지 적용 로케일
	 * @return ExcelColumn Annotation의 name 속성으로 필드-필드명 맵
	 */
	public static Map<String, String> getExcelColumnNameMap(Class<?> voClass, MessageSource messageSource, String prefix, Locale locale) {
		Field[] fields = voClass.getDeclaredFields();
		Map<String, String> voFieldNameMap = new HashMap<String, String>();
		for (Field field : fields) {
			ExcelColumn excelColumnAnnotation = field.getAnnotation(ExcelColumn.class);
			if (excelColumnAnnotation == null || excelColumnAnnotation.ignore()) {
				continue;
			}

			// 기본 필드명
			String fieldName = field.getName();
			// @ExcelColumn.name
			String excelColumnName = null;
			excelColumnName = excelColumnAnnotation.name();
			if (StringUtils.isEmpty(excelColumnName)) {
				excelColumnName = fieldName;
				if (StringUtils.isNotEmpty(prefix)) {
					excelColumnName = prefix + excelColumnName;
				}
				excelColumnName = excelColumnName.toLowerCase();
			}
			if (messageSource != null) {
				excelColumnName = messageSource.getMessage(excelColumnName, null, locale);
			}
			voFieldNameMap.put(fieldName, excelColumnName);
		}
		return voFieldNameMap;
	}

	/**
	 * ExcelColumn Annotation의 필드(프라퍼티)-Annotation 맵을 획득
	 * @param voClass 대상 VO
	 * @return ExcelCoumn Annotation의 필드(프라퍼티)-Annotation 맵
	 */
	public static Map<String, ExcelColumn> getExcelColumnAnnotationMap(Class<?> voClass) {
		Map<String, ExcelColumn> result = new HashMap<String, ExcelColumn>();
		Map<String, ExcelColumn> mapFromField = getExcelColumnAnnotationMapFromField(voClass);
		Map<String, ExcelColumn> mapFromProperty = getExcelColumnAnnotationMapFromProperty(voClass);
		result.putAll(mapFromField);
		result.putAll(mapFromProperty);
		return result;
	}


	/**
	 * ExcelColumn Annotation의 필드-Annotation 맵을 획득
	 * @param voClass 대상 VO
	 * @return ExcelCoumn Annotation의 필드-Annotation 맵
	 */
	public static Map<String, ExcelColumn> getExcelColumnAnnotationMapFromField(Class<?> voClass) {
		Map<String, ExcelColumn> voExcelColumnMap = new HashMap<String, ExcelColumn>();
		Field[] fields = voClass.getDeclaredFields();
		for (Field field : fields) {
			ExcelColumn excelColumnAnnotation = field.getAnnotation(ExcelColumn.class);
			if (excelColumnAnnotation == null || excelColumnAnnotation.ignore()) {
				continue;
			}

			String fieldName = field.getName();
			voExcelColumnMap.put(fieldName, excelColumnAnnotation);
		}
		return voExcelColumnMap;
	}

	/**
	 * ExcelColumn Annotation의 Property-Annotation 맵을 획득
	 * @param voClass 대상 VO
	 * @return ExcelCoumn Annotation의 Property-Annotation 맵
	 */
	public static Map<String, ExcelColumn> getExcelColumnAnnotationMapFromProperty(Class<?> voClass) {
		Map<String, ExcelColumn> voExcelColumnMap = new HashMap<String, ExcelColumn>();
		PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(voClass);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			Method getter = propertyDescriptor.getReadMethod();
			if(getter != null) {
				ExcelColumn excelColumnAnnotation = getter.getAnnotation(ExcelColumn.class);
				if (excelColumnAnnotation == null || excelColumnAnnotation.ignore()) {
					continue;
				}
				String propertyName = propertyDescriptor.getName();
				voExcelColumnMap.put(propertyName, excelColumnAnnotation);
			}
		}
		return voExcelColumnMap;
	}
}
