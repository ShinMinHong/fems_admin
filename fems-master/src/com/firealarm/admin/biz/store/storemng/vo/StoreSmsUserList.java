package com.firealarm.admin.biz.store.storemng.vo;

import org.joda.time.LocalDateTime;

import lombok.Data;

@Data
public class StoreSmsUserList {

	/* No */
	private Integer rn;
    /* 점포고유번호 */
    private Long storeSeq;
    /* SMS대상자고유번호 */
    private Long smsUserSeq;
    /* 이름 */
    private String managerName;
	/* 휴대폰번호 */
    private String phoneNo;
	/* 직책 */
    private String dutyName;
	/* SMS수신여부 */
    private boolean smsReceiveYn;
	/* 등록일시 */
    private LocalDateTime regDate;

	@Override
	public String toString() {
		return "StoreSmsUserList [rn=" + rn + ", storeSeq=" + storeSeq + ", smsUserSeq=" + smsUserSeq + ", managerName="
				+ managerName + ", phoneNo=" + phoneNo + ", dutyName=" + dutyName + ", smsReceiveYn=" + smsReceiveYn
				+ ", regDate=" + regDate + "]";
	}
}
