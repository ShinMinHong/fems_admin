package framework.apache.common.lang3.builder;

import org.apache.commons.lang3.builder.StandardToStringStyle;

public class ToStringExcludeNullStyle extends StandardToStringStyle {
	private static final long serialVersionUID = 6917179108001497800L;

	public ToStringExcludeNullStyle() {
		super();
		this.setUseClassName(true);
		this.setUseShortClassName(true);
		this.setUseIdentityHashCode(false);
	}

	@Override
	public void append(StringBuffer buffer, String fieldName, Object value, Boolean fullDetail) {
		if(value != null) {
			super.append(buffer, fieldName, value, fullDetail);
		}
	}
}
