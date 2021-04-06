package com.firealarm.admin.biz.store.storesmsusermng.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonView;
import framework.annotation.Display;
import framework.annotation.ExcelColumn;
import framework.base.model.AbleView;
import framework.validation.constraints.AbleMaxLength;
import lombok.Data;

@Data
public class StoreSmsUserMngVO {

	@Display(name = "순번")
	@JsonView({BaseView.class})
	private Integer rn;

	@Display(name = "관제지역고유번호")
	@JsonView({BaseView.class, BaseAction.class})
	private long mngAreaSeq;

	@Display(name = "관제지역명")
	@JsonView({BaseView.class})
	@ExcelColumn(name="관제지역명", order=1)
	private String mngAreaName;

	@Display(name = "전통시장고유번호")
	@JsonView({BaseView.class, BaseAction.class})
	@ExcelColumn(name="전통시장고유번호", order=5)
	private long marketSeq;

	@Display(name = "전통시장명")
	@JsonView({BaseView.class})
	@ExcelColumn(name="전통시장명", order=10)
	private String marketName;

	@Display(name = "점포고유번호")
	@JsonView({BaseView.class, BaseAction.class})
	@ExcelColumn(name="점포고유번호", order=15)
	private long storeSeq;

	@Display(name = "점포명")
	@JsonView({BaseView.class})
	@ExcelColumn(name="점포명", order=20)
	private String storeName;

	@Display(name = "SMS대상자고유번호")
	@JsonView({BaseView.class, BaseAction.class})
	@ExcelColumn(name="SMS대상자고유번호", order=21)
	private long smsUserSeq;

	@Display(name = "이름")
	@NotNull(groups = {BaseAction.class})
	@NotBlank(groups = {BaseAction.class})
	@AbleMaxLength(value=50, groups = { BaseAction.class })
	@JsonView({BaseView.class, BaseAction.class})
	@ExcelColumn(name="이름", order=25)
	private String managerName;

	@Display(name = "휴대폰번호")
	@NotNull(groups = {BaseAction.class})
	@NotBlank(groups = {BaseAction.class})
	@AbleMaxLength(value=20, groups = { BaseAction.class })
	@JsonView({BaseView.class, BaseAction.class})
	@ExcelColumn(name="휴대폰번호", order=30)
	private String phoneNo;

	@Display(name = "직책")
	@AbleMaxLength(value=50, groups = { BaseAction.class })
	@JsonView({BaseView.class, BaseAction.class})
	/*@ExcelColumn(name="직책", order=55)*/
	private String dutyName;

	@Display(name = "SMS수신여부")
	@NotNull(groups = {BaseAction.class})
	@JsonView({BaseView.class, BaseAction.class})
	@ExcelColumn(name="SMS수신여부", order=60)
	private boolean smsReceiveYn;

	@Display(name = "등록일시")
	@JsonView({BaseView.class})
	@ExcelColumn(name="등록일시", order=70)
	private LocalDateTime regDate;

	@Display(name = "등록자ID")
	@JsonView({BaseView.class})
	private String regAdminId;

	@Display(name = "최종수정일시")
	@JsonView({BaseView.class})
	private LocalDateTime updDate;

	@Display(name = "최종수정자ID")
	@JsonView({BaseView.class})
	private String updAdminId;


	/** 공통 View */
	public interface BaseView extends AbleView.CommonBaseView {}

	/** 공통 Action */
	public interface BaseAction extends AbleView.CommonBaseView {}
	public interface CreateAction extends BaseAction {}
	public interface UpdateAction extends BaseAction {}


}