package com.firealarm.admin.biz.system.mngareamng.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonView;

import framework.annotation.Display;
import framework.base.model.AbleView;
import framework.validation.constraints.AbleMaxLength;
import lombok.Data;

@Data
public class MngAreaMngActiveVO {

	@Display(name = "관제지역고유번호")
	@JsonView({UpdateAction.class})
	@NotNull(groups = { UpdateAction.class })
	private Long mngAreaSeq;

	@Display(name = "관제지역명")
	@JsonView({BaseAction.class})
	@NotBlank(groups = { BaseAction.class })
	@NotNull(groups = { BaseAction.class })
	@AbleMaxLength(value=50, groups = { BaseAction.class })
	private String mngAreaName;

	@Display(name = "책임자명")
	@JsonView({BaseAction.class})
	@NotBlank(groups = { BaseAction.class })
	@NotNull(groups = { BaseAction.class })
	@AbleMaxLength(value=50, groups = { BaseAction.class })
	private String managerName;

	@Display(name = "휴대폰번호")
	@JsonView({BaseAction.class})
	@NotBlank(groups = { BaseAction.class })
	@NotNull(groups = { BaseAction.class })
	@AbleMaxLength(value=20, groups = { BaseAction.class })
	private String phoneNo;

	@Display(name = "일반전화번호")
	@JsonView({BaseAction.class})
	@AbleMaxLength(value=20, groups = { BaseAction.class })
	private String telephoneNo;

	@Display(name = "우편번호")
	@JsonView({BaseAction.class})
	@NotBlank(groups = { BaseAction.class })
	@NotNull(groups = { BaseAction.class })
	@AbleMaxLength(value=6, groups = { BaseAction.class })
	private String zipCode;

	@Display(name = "도로명주소")
	@JsonView({BaseAction.class})
	@NotBlank(groups = { BaseAction.class })
	@NotNull(groups = { BaseAction.class })
	@AbleMaxLength(value=2000, groups = { BaseAction.class })
	private String roadAddress;

	@Display(name = "지번주소")
	@JsonView({BaseAction.class})
	@NotBlank(groups = { BaseAction.class })
	@NotNull(groups = { BaseAction.class })
	@AbleMaxLength(value=2000, groups = { BaseAction.class })
	private String parcelAddress;

	@Display(name = "위도")
	@JsonView({BaseAction.class})
	@NotBlank(groups = { BaseAction.class })
	@AbleMaxLength(value=50, groups = { BaseAction.class })
	private String latitude;

	@Display(name = "경도")
	@JsonView({BaseAction.class})
	@NotBlank(groups = { BaseAction.class })
	@AbleMaxLength(value=50, groups = { BaseAction.class })
	private String longitude;

	@Display(name = "축척")
	@JsonView({BaseAction.class})
	@NotNull(groups = { BaseAction.class })
	private Integer scale;

	@Display(name = "알림제한시간")
	@JsonView({BaseAction.class})
	@NotNull(groups = { BaseAction.class })
	private Integer noAlarmTime;

	@Display(name = "알림대상_점포담당자")
	@JsonView({BaseAction.class})
	@NotNull(groups = { BaseAction.class })
	private Boolean alarmStore;

	@Display(name = "알림대상_시장담당자")
	@JsonView({BaseAction.class})
	@NotNull(groups = { BaseAction.class })
	private Boolean alarmMarket;

	@Display(name = "알림대상_지역담당자")
	@JsonView({BaseAction.class})
	@NotNull(groups = { BaseAction.class })
	private Boolean alarmArea;

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
