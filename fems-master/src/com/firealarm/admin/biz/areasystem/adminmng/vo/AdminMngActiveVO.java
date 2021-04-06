package com.firealarm.admin.biz.areasystem.adminmng.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonView;
import com.firealarm.admin.appconfig.CodeMap.APP_USER_GRADE;

import framework.annotation.Display;
import framework.base.model.AbleView;
import framework.validation.constraints.AbleMaxLength;
import lombok.Data;

@Data
public class AdminMngActiveVO {

	/** 관리자고유번호 */
    @Display(name="관리자고유번호")
    @JsonView({BaseView.class})
    private Long adminSeq;

    /** 권한그룹코드 */
    @Display(name="권한그룹코드")
    private APP_USER_GRADE rolegroupCode;

    /** 관제지역고유번호 */
    @Display(name="관제지역고유번호")
    @JsonView({BaseView.class})
    private Long mngAreaSeq;

    /** 전통시장고유번호 */
    @Display(name="전통시장고유번호")
    @JsonView({BaseView.class})
    private Long marketSeq;

    /** 사용자아이디 */
    @Display(name="사용자아이디")
    @JsonView({BaseView.class, CreateAction.class})
    @NotBlank(groups = {BaseAction.class})
    private String adminId;

    /** 사용자 기존비밀번호 */
    @Display(name="사용자비밀번호")
    @JsonView({BaseView.class})
    private String oldPassword;

    /** 사용자 신규비밀번호 */
    @Display(name="사용자비밀번호")
    @JsonView({BaseView.class})
    private String adminPassword;

    /** 사용자명 */
    @Display(name="사용자명")
    @JsonView({BaseView.class, BaseAction.class})
    @NotBlank(groups = {BaseAction.class})
    @AbleMaxLength(value=50, groups = { BaseAction.class })
    private String adminName;

    /** 직책 */
    @Display(name="직책")
    @JsonView({BaseView.class, BaseAction.class})
    @AbleMaxLength(value=50, groups = { BaseAction.class })
    private String dutyName;

    /** 휴대폰번호 */
    @Display(name="휴대폰번호")
    @JsonView({BaseView.class, BaseAction.class})
    @AbleMaxLength(value=13, groups = { BaseAction.class })
    @NotBlank(groups = {BaseAction.class})
    private String phoneNo;

    /** SMS수신여부 */
    @Display(name="SMS수신여부")
    @JsonView({BaseView.class, BaseAction.class})
    @NotNull(groups = {BaseAction.class})
    private boolean smsReceiveYn;

    /** 사용여부 */
    @Display(name="사용여부")
    @JsonView({BaseView.class, BaseAction.class})
    @NotNull(groups = {BaseAction.class})
    private boolean useYn;

    /** 등록일시 */
    @Display(name="등록일시")
    @JsonView({BaseView.class})
    private LocalDateTime regDate;

    /** 등록자ID */
    @Display(name="등록자ID")
    @JsonView({BaseView.class})
    private String regAdminId;

    /** 최종수정일시 */
    @Display(name="최종수정일시")
    @JsonView({BaseView.class})
    private LocalDateTime updDate;

    /** 최종수정자ID */
    @Display(name="최종수정자ID")
    @JsonView({BaseView.class})
    private String updAdminId;

    /** 비밀번호변경 여부 **/
    @Display(name="비밀번호변경 여부")
    @JsonView({UpdateAction.class})
    private boolean checkChangePassword;

	/** 공통 View */
	public interface BaseView extends AbleView.CommonBaseView {}

	/** 공통 Action */
	public interface BaseAction extends AbleView.CommonBaseView {}
	public interface CreateAction extends BaseAction {}
	public interface UpdateAction extends BaseAction {}

	@Override
	public String toString() {
		return "AdminMngActiveVO [adminSeq=" + adminSeq + ", rolegroupCode=" + rolegroupCode + ", mngAreaSeq="
				+ mngAreaSeq + ", marketSeq=" + marketSeq + ", adminId=" + adminId + ", adminName=" + adminName
				+ ", dutyName=" + dutyName + ", phoneNo=" + phoneNo + ", smsReceiveYn=" + smsReceiveYn + ", useYn="
				+ useYn + ", regDate=" + regDate + ", regAdminId=" + regAdminId + ", updDate=" + updDate
				+ ", updAdminId=" + updAdminId + ", checkChangePassword=" + checkChangePassword + "]";
	}

}
