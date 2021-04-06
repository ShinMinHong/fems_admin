package framework.vo;

import com.google.common.base.CaseFormat;

public class SortVO {

	/** 정렬대상 필드 */
	protected String field;

	/** 정렬방향 asc/desc */
	protected String dir;

	/** 정렬대상 필드 (UnderScored) */
	public String getFieldByUnderScore() {
		return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field);
	}

	/** order by 절에 넣을 구문 */
	@Override
	public String toString() {
		return getFieldByUnderScore() + " " + (("asc".equals(dir))?"asc":"desc");
	}
}
