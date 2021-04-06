package com.firealarm.admin.biz.firedetector.firedetectormng.vo;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;

import framework.base.model.AbleView;
import framework.validation.constraints.AbleAllowExtensions;
import lombok.Data;

@Data
public class FireDetectorMngActiveVO {

	/** 화재감지기고유번호 */
	private long fireDetectorSeq;

	/** 점포고유번호 */
	@JsonView({BaseAction.class})
	private Long storeSeq;

	/** 모델번호 */
	@JsonView({BaseAction.class})
	private String modelNo;

	/** 제조번호 */
	@JsonView({BaseAction.class})
	private String productNo;

	/** CTN번호 - 수정모드에서는 변경불가 */
	@JsonView({CreateAction.class})
	@NotNull(groups = {CreateAction.class})
	@NotBlank(groups = { CreateAction.class})
	private String ctnNo;

	/** 단말기일련번호 */
	@JsonView({BaseAction.class})
	@NotNull(groups = {BaseAction.class})
	@NotBlank(groups = { BaseAction.class})
	private String serialNo;

	/** 유심번호 */
	@JsonView({BaseAction.class})
	@NotNull(groups = {BaseAction.class})
	@NotBlank(groups = { BaseAction.class})
	private String usimNo;

	/** 우편번호 */
	@JsonView({BaseAction.class})
	private String zipCode;

	/** 도로명주소 */
	@JsonView({BaseAction.class})
	private String roadAddress;

	/** 지번주소 */
	@JsonView({BaseAction.class})
	private String parcelAddress;

	/** 위도 */
	@JsonView({BaseAction.class})
	private String latitude;

	/** 경도 */
	@JsonView({BaseAction.class})
	private String longitude;

	/** 설치위치 */
	@JsonView({BaseAction.class})
	private String installPlace;

	/** 감지기ACK */
	@JsonView({BaseAction.class})
	private String fireDetectorAckValue;

	/** 등록자 */
	private String regAdminId;

	/** 수정자 */
	private String updAdminId;

	/** 화재감지기명 */
	@JsonView({BaseAction.class})
	private String fireDetectorName;

	/** SMS 추가메시지 */
	@JsonView({BaseAction.class})
	private String smsAddMessage;

	// 업로드 파일 목록
	@JsonView({BaseAction.class})
	@AbleAllowExtensions(value="png,gif,jpg", groups = {BaseAction.class})
	private List<MultipartFile> fileInput = new ArrayList<MultipartFile>();

	/** 공통 Action */
	public interface BaseAction extends AbleView.CommonBaseView {}
	public interface CreateAction extends BaseAction {}
	public interface UpdateAction extends BaseAction {}

	@Override
	public String toString() {
		return "FireDetectorMngActiveVO [fireDetectorSeq=" + fireDetectorSeq + ", storeSeq=" + storeSeq + ", modelNo="
				+ modelNo + ", productNo=" + productNo + ", ctnNo=" + ctnNo + ", serialNo=" + serialNo + ", usimNo="
				+ usimNo + ", zipCode=" + zipCode + ", roadAddress=" + roadAddress + ", parcelAddress=" + parcelAddress
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", installPlace=" + installPlace
				+ ", fireDetectorAckValue=" + fireDetectorAckValue + ", fileInput=" + fileInput + "]";
	}
}
