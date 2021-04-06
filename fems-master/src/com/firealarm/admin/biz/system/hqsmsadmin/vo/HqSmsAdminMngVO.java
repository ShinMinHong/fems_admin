package com.firealarm.admin.biz.system.hqsmsadmin.vo;

import org.joda.time.LocalDateTime;

import framework.annotation.Display;
import framework.base.model.AbleView;
import lombok.Data;

@Data
public class HqSmsAdminMngVO {

	//순번
	private Integer rn;

	@Display(name = "통합Sms수신자고유번호")
	private long smsAdminSeq;

	@Display(name = "사용자명")
	private String adminName;

	@Display(name = "직책")
	private String dutyName;

	@Display(name = "휴대폰번호")
	private String phoneNo;

	@Display(name = "SMS수신여부")
	private boolean smsReceiveYn;

	@Display(name = "등록일시")
	private LocalDateTime regDate;

	@Display(name = "등록자ID")
	private String regAdminId;

	@Display(name = "최종수정일시")
	private LocalDateTime updDate;

	@Display(name = "최종수정자ID")
	private String updAdminId;

	/** 공통 View */
	public interface BaseView extends AbleView.CommonBaseView {}

	@Override
	public String toString() {
		return "HqSmsAdminMngVO [smsAdminSeq=" + smsAdminSeq + ", adminName=" + adminName + ", dutyName=" + dutyName
				+ ", phoneNo=" + phoneNo + ", smsReceiveYn=" + smsReceiveYn + ", regDate=" + regDate + ", regAdminId="
				+ regAdminId + ", updDate=" + updDate + ", updAdminId=" + updAdminId + "]";
	}


}
