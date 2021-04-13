package com.firealarm.admin.biz.areasystem.firedetectorset.vo;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDateTime;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.firealarm.admin.biz.areasystem.firedetectorset.vo.FireDetectorSetVO.DetailsView;

import framework.base.model.AbleView;
import framework.validation.constraints.AbleAllowExtensions;
import lombok.Data;

@Data
public class FireDetectorSetActiveVO {

	/** 화재감지기설정고유번호 */
	private long fireDetectorSetSeq;

	/** 화재감지기고유번호 */
	private long fireDetectorSeq;

	/** 화재감지기설정구분 */
	@JsonView({BaseAction.class})
	@NotNull(groups = {BaseAction.class})
	@NotBlank(groups = { BaseAction.class})
	private String fireDetectorSetType;

	/** 화재감지기설정값 */
	@JsonView({BaseAction.class})
	@NotNull(groups = {BaseAction.class})
	@NotBlank(groups = { BaseAction.class})
	private String fireDetectorSetValue;

	/** 화재감지기설정시작일시 */
	@JsonView({BaseAction.class})
	@NotNull(groups = {BaseAction.class})
	@NotBlank(groups = { BaseAction.class})
	private LocalDateTime fireDetectorSetStrDate;

	/** 화재감지기설정종료일시 */
	@JsonView({BaseAction.class})
	private LocalDateTime fireDetectorSetEndDate;

	/** 화재감지기설정전송유무 */
	@JsonView({BaseAction.class})
	private String fireDetectorSetSendYn;

	/** 화재감지기설정전송일시 */
	@JsonView({BaseAction.class})
	private LocalDateTime fireDetectorSetSendDate;

	/** 등록자 */
	private String regAdminId;

	/** 수정자 */
	private String updAdminId;

	/** 공통 Action */
	public interface BaseAction extends AbleView.CommonBaseView {}
	public interface CreateAction extends BaseAction {}
	public interface UpdateAction extends BaseAction {}

	@Override
	public String toString() {
		return "FireDetectorSetActiveVO [fireDetectorSetSeq=" + fireDetectorSetSeq + ", fireDetectorSeq=" + fireDetectorSeq
				+ ", fireDetectorSetType=" + fireDetectorSetType + ", fireDetectorSetValue=" + fireDetectorSetValue
				+ ", fireDetectorSetStrDate=" + fireDetectorSetStrDate + ", fireDetectorSetEndDate=" + fireDetectorSetEndDate
				+ ", fireDetectorSetSendYn=" + fireDetectorSetSendYn + ", fireDetectorSetSendDate=" + fireDetectorSetSendDate
				+ ", regAdminId=" + regAdminId + ", updAdminId=" + updAdminId + "]";
	}
}
