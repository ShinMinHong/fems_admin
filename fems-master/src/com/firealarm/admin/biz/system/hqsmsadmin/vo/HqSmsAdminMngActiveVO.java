package com.firealarm.admin.biz.system.hqsmsadmin.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonView;
import com.firealarm.admin.biz.areasystem.adminmng.vo.AdminMngActiveVO.BaseView;

import framework.annotation.Display;
import framework.base.model.AbleView;
import framework.validation.constraints.AbleMaxLength;
import lombok.Data;

@Data
public class HqSmsAdminMngActiveVO {

	@Display(name = "통합Sms수신자고유번호")
	@JsonView({UpdateAction.class})
	@NotNull(groups = { UpdateAction.class })
	private long smsAdminSeq;

	@Display(name = "사용자명")
	@JsonView({BaseAction.class})
	@NotBlank(groups = { BaseAction.class })
	@NotNull(groups = { BaseAction.class })
	@AbleMaxLength(value=50, groups = { BaseAction.class })
	private String adminName;

	@Display(name="직책")
	@JsonView({BaseAction.class})
	@AbleMaxLength(value=50, groups = { BaseAction.class })
    private String dutyName;

	@Display(name = "휴대폰번호")
	@JsonView({BaseAction.class})
	@NotBlank(groups = { BaseAction.class })
	@NotNull(groups = { BaseAction.class })
	@AbleMaxLength(value=20, groups = { BaseAction.class })
	private String phoneNo;

	@Display(name="SMS수신여부")
    @JsonView({BaseView.class, BaseAction.class})
    @NotNull(groups = {BaseAction.class})
    private boolean smsReceiveYn;

	@Display(name = "등록일시")
	private LocalDateTime regDate;

	@Display(name = "등록자ID")
	private String regAdminId;

	@Display(name = "최종수정일시")
	private LocalDateTime updDate;

	@Display(name = "최종수정자ID")
	private String updAdminId;

	/** 공통 Action */
	public interface BaseAction extends AbleView.CommonBaseView {}
	public interface CreateAction extends BaseAction {}
	public interface UpdateAction extends BaseAction {}
}
