package com.firealarm.admin.biz.system.mngareamng.vo;

import org.joda.time.LocalDateTime;

import framework.annotation.Display;
import framework.base.model.AbleView;
import lombok.Data;

@Data
public class MngAreaMngVO {

	@Display(name = "관제지역고유번호")
	private long mngAreaSeq;

	@Display(name = "관제지역명")
	private String mngAreaName;

	@Display(name = "책임자명")
	private String managerName;

	@Display(name = "휴대폰번호")
	private String phoneNo;

	@Display(name = "일반전화번호")
	private String telephoneNo;

	@Display(name = "우편번호")
	private String zipCode;

	@Display(name = "도로명주소")
	private String roadAddress;

	@Display(name = "지번주소")
	private String parcelAddress;

	@Display(name = "위도")
	private String latitude;

	@Display(name = "경도")
	private String longitude;

	@Display(name = "축척")
	private int scale;

	@Display(name = "알림제한시간")
	private int noAlarmTime;

	@Display(name = "알림대상_점포담당자")
	private boolean alarmStore;

	@Display(name = "알림대상_시장담당자")
	private boolean alarmMarket;

	@Display(name = "알림대상_지역담당자")
	private boolean alarmArea;

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
		return "MngAreaMngVO [mngAreaSeq=" + mngAreaSeq + ", mngAreaName=" + mngAreaName + ", managerName="
				+ managerName + ", phoneNo=" + phoneNo + ", telephoneNo=" + telephoneNo
				+ ", zipCode=" + zipCode + ", roadAddress=" + roadAddress + ", parcelAddress=" + parcelAddress
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", scale=" + scale + ", noAlarmTime="
				+ noAlarmTime + ", alarmStore=" + alarmStore + ", alarmMarket=" + alarmMarket + ", alarmArea="
				+ alarmArea + ", regDate=" + regDate + ", regAdminId=" + regAdminId + ", updDate=" + updDate
				+ ", updAdminId=" + updAdminId + "]";
	}
}
