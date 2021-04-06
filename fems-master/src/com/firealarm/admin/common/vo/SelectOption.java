package com.firealarm.admin.common.vo;

import lombok.Data;

@Data
public class SelectOption {

	private String text;
	private String value;

	@Override
	public String toString() {
		return "SelectOption [text=" + text + ", value=" + value + "]";
	}
}



