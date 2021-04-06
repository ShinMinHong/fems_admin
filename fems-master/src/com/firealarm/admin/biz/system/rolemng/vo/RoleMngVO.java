package com.firealarm.admin.biz.system.rolemng.vo;

import org.joda.time.LocalDate;

import lombok.Data;

@Data
public class RoleMngVO {
	private int rn;
	private String roleCode;
	private String roleName;
	private LocalDate regDate;
	private String regAdminId;
}
