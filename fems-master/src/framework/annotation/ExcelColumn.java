package framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel 필드 Annotation
 *
 * @author deepfree@ablecoms.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface ExcelColumn {

	/** @return 컬럼 노출 제외 여부. 데이터 출력에서 제외, 컬럼을 숨기려면 hidden 속성을 이용 */
	public boolean ignore() default false;

	/** @return 컬럼 노출순서 */
	public int order() default Integer.MAX_VALUE;

	/**@return 컬럼 그룹해더명 ('\n'으로 개행처리 가능. 자동 높이 적용) */
	public String groupname() default "";

	/** @return 컬럼 필드 헤더명 ('\n'으로 개행처리 가능. 자동 높이 적용)*/
	public String name() default "";

	/** @return 컬럼 width. 0이면 auto width */
	public int width() default 0;

	/** @return 컬럼 hidden. Excel 컬럼숨김으로 ignore와 달리 Data는 내려감*/
	public boolean hidden() default false;

	/** @return 컬럼 정렬*/
	public ExcelAlign align() default ExcelAlign.DEFAULT_BY_TYPE;

	/** 정렬 기준 Enum*/
	public enum ExcelAlign {
		/** 데이터 타입에 따른 정렬*/ DEFAULT_BY_TYPE,
		/** Laft 정렬*/ LEFT,
		/** Center 정렬*/ CENTER,
		/** Right 정렬*/ RIGHT
	}

	/** @return DateTime 형식 Format 문자열 ("yyyy-MM-dd HH:mm:ss") */
	String dateTimeFormat() default "yyyy-MM-dd HH:mm:ss";
}
