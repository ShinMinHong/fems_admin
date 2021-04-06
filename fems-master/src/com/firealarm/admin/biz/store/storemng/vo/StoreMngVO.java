package com.firealarm.admin.biz.store.storemng.vo;

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
public class StoreMngVO {

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
	@JsonView({BaseView.class})
	@ExcelColumn(name="점포고유번호", order=15)
	private long storeSeq;

	@Display(name = "점포명")
	@NotNull(groups = {BaseAction.class})
	@NotBlank(groups = {BaseAction.class})
	@AbleMaxLength(value=50, groups = { BaseAction.class })
	@JsonView({BaseView.class, BaseAction.class})
	@ExcelColumn(name="점포명", order=20)
	private String storeName;

	@Display(name = "점주명")
	//@NotNull(groups = {BaseAction.class})
	//@NotBlank(groups = {BaseAction.class})
	@AbleMaxLength(value=50, groups = { BaseAction.class })
	@JsonView({BaseView.class, BaseAction.class})
	@ExcelColumn(name="점주명", order=25)
	private String managerName;

	@Display(name = "휴대폰번호")
	@NotNull(groups = {BaseAction.class})
	@NotBlank(groups = {BaseAction.class})
	@AbleMaxLength(value=20, groups = { BaseAction.class })
	@JsonView({BaseView.class, BaseAction.class})
	@ExcelColumn(name="휴대폰번호", order=30)
	private String phoneNo;

	@Display(name = "일반전화번호")
	@AbleMaxLength(value=20, groups = { BaseAction.class })
	@JsonView({BaseView.class, BaseAction.class})
	@ExcelColumn(name="일반전화번호", order=35)
	private String telephoneNo;

	@Display(name = "우편번호")
	@NotNull(groups = {BaseAction.class})
	@NotBlank(groups = {BaseAction.class})
	@AbleMaxLength(value=6, groups = { BaseAction.class })
	@JsonView({BaseView.class, BaseAction.class})
	@ExcelColumn(name="우편번호", order=40)
	private String zipCode;

	@Display(name = "도로명주소")
	@NotNull(groups = {BaseAction.class})
	@NotBlank(groups = {BaseAction.class})
	@AbleMaxLength(value=200, groups = { BaseAction.class })
	@JsonView({BaseView.class, BaseAction.class})
	@ExcelColumn(name="도로명주소", order=45)
	private String roadAddress;

	@Display(name = "지번주소")
	@NotNull(groups = {BaseAction.class})
	@NotBlank(groups = {BaseAction.class})
	@AbleMaxLength(value=200, groups = { BaseAction.class })
	@JsonView({BaseView.class, BaseAction.class})
	@ExcelColumn(name="지번주소", order=50)
	private String parcelAddress;

	@Display(name = "상세주소")
	@AbleMaxLength(value=200, groups = { BaseAction.class })
	@JsonView({BaseView.class, BaseAction.class})
	@ExcelColumn(name="상세주소", order=51)
	private String detailsAddress;

	@Display(name = "업종설명")
	@AbleMaxLength(value=200, groups = { BaseAction.class })
	@JsonView({BaseView.class, BaseAction.class})
	@ExcelColumn(name="업종설명", order=55)
	private String businessDesc;

	@Display(name = "SMS알림여부")
	@NotNull(groups = {BaseAction.class})
	@JsonView({BaseView.class, BaseAction.class})
	@ExcelColumn(name="SMS알림여부", order=60)
	private boolean smsAlarmYn;

	@Display(name = "119알림")
	@NotNull(groups = {BaseAction.class})
	@JsonView({BaseView.class, BaseAction.class})
	@ExcelColumn(name="119알림", order=65)
	private boolean firestationAlarmYn;

	@Display(name = "연기화재알림")
	@NotNull(groups = {BaseAction.class})
	@JsonView({BaseView.class, BaseAction.class})
	@ExcelColumn(name="연기화재알림", order=66)
	private boolean smokeAlarmYn;

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

	@Display(name = "관할소방서")
	@JsonView({BaseView.class})
	private String firestationName;

	@Display(name = "소방서담당자")
	@JsonView({BaseView.class})
	private String firestationManagerName;

	@Display(name = "소방서연락처")
	@JsonView({BaseView.class})
	private String firestationTelephoneNo;


	/** 공통 View */
	public interface BaseView extends AbleView.CommonBaseView {}

	/** 공통 Action */
	public interface BaseAction extends AbleView.CommonBaseView {}
	public interface CreateAction extends BaseAction {}
	public interface UpdateAction extends BaseAction {}


}