package com.firealarm.admin.biz.areasystem.marketmng.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonView;

import framework.annotation.Display;
import framework.base.model.AbleView;
import framework.validation.constraints.AbleMaxLength;
import lombok.Data;

@Data
public class MarketMngActiveVO {

	 /** 시장 Seq*/
    @Display(name="시장고유번호")
    @JsonView({BaseView.class})
    private long marketSeq;

    /** 관제지역명 */
    @Display(name="관제지역명")
    @JsonView({BaseView.class, BaseAction.class})
    private long mngAreaSeq;

    /** 전통시장명 */
    @Display(name="전통시장명")
    @NotNull(groups = {BaseAction.class})
    @NotBlank(groups = {BaseAction.class})
    @JsonView({BaseView.class, BaseAction.class})
    private String marketName;

    /** 담당자 */
    @Display(name="담당자")
    @NotNull(groups = {BaseAction.class})
    @NotBlank(groups = {BaseAction.class})
    @JsonView({BaseView.class, BaseAction.class})
    private String managerName;

    /** 휴대폰번호 */
    @Display(name="휴대폰번호")
    @NotNull(groups = {BaseAction.class})
    @NotBlank(groups = {BaseAction.class})
    @AbleMaxLength(value=20, groups = { BaseAction.class })
    @JsonView({BaseView.class, BaseAction.class})
    private String phoneNo;

    /** 일반전화번호 */
    @Display(name="일반전화번호")
    @AbleMaxLength(value=20, groups = { BaseAction.class })
    @JsonView({BaseView.class, BaseAction.class})
    private String telephoneNo;

    /** 우편번호 */
    @Display(name="우편번호")
    @NotNull(groups = {BaseAction.class})
    @NotBlank(groups = {BaseAction.class})
    @AbleMaxLength(value=6, groups = { BaseAction.class })
    @JsonView({BaseView.class, BaseAction.class})
    private String zipCode;

    /** 주소(도로명) */
    @Display(name="주소(도로명)")
    @NotNull(groups = {BaseAction.class})
    @NotBlank(groups = {BaseAction.class})
    @JsonView({BaseView.class, BaseAction.class})
    private String roadAddress;

    /** 주소(지번) */
    @Display(name="주소(지번)")
    @NotNull(groups = {BaseAction.class})
    @NotBlank(groups = {BaseAction.class})
    @JsonView({BaseView.class, BaseAction.class})
    private String parcelAddress;

    /** 위도(중심좌표) */
    @Display(name="위도(중심좌표)")
    @JsonView({BaseView.class, BaseAction.class})
    @NotBlank(groups = {BaseAction.class})
    private String latitude;

    /** 경도(중심좌표) */
    @Display(name="경도(중심좌표)")
    @JsonView({BaseView.class, BaseAction.class})
    @NotBlank(groups = {BaseAction.class})
    private String longitude;

    /** 지도축적 */
    @Display(name="지도축적")
    @JsonView({BaseAction.class})
    @NotNull(groups = {BaseAction.class})
    private Integer scale;

    /** 관할소방서명 */
    @Display(name="관할소방서명")
    @JsonView({BaseAction.class})
    private String firestationName;

    /** 지도축적 */
    @Display(name="지도축적")
    @JsonView({BaseAction.class})
    private String firestationManagerName;

    /** 관할서 전화번호 */
    @Display(name="관할서 전화번호")
    @JsonView({BaseAction.class})
    private String firestationTelephoneNo;

    /** 시도코드 */
    @Display(name="시도코드")
    @JsonView({BaseAction.class})
    private String ctrdCode;

    /** 시군구코드 */
    @Display(name="시군구코드")
    @JsonView({BaseAction.class})
    private String signguCode;

    /** 동코드 */
    @Display(name="동코드")
    @JsonView({BaseAction.class})
    private String dongCode;

    /** 리코드 */
    @Display(name="리코드")
    @JsonView({BaseAction.class})
    private String liCode;

    /** 공통 View */
    public interface BaseView extends AbleView.CommonBaseView {}

    /** 공통 Action */
    public interface BaseAction extends AbleView.CommonBaseView {}
    public interface CreateAction extends BaseAction {}
    public interface UpdateAction extends BaseAction {}

    @Override
	public String toString() {
		return "MarketMngActiveVO [marketSeq=" + marketSeq + ", mngAreaSeq=" + mngAreaSeq + ", marketName=" + marketName
				+ ", managerName=" + managerName + ", phoneNo=" + phoneNo + ", zipCode=" + zipCode + ", roadAddress="
				+ roadAddress + ", parcelAddress=" + parcelAddress + ", latitude=" + latitude + ", longitude="
				+ longitude + ", scale=" + scale + "]";
	}
}
