package com.firealarm.admin.common.vo;

import org.joda.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsAdminDT {
	/** 수신자고유번호 */
	private long smsAdminSeq;

	/** 사용자명 */
	private String adminName;

	/** 직책 */
	private String dutyName;

	/** 휴대폰번호 */
	private String phoneNo;

	/** SMS수신여부 */
	private boolean smsReceiveYn;

	/** 등록일시 */
	private LocalDateTime regDate;

	/** 등록자ID */
	private String regAdminId;

	/** 최종수정일시 */
	private LocalDateTime updDate;

	/** 최종수정자ID */
	private String updAdminId;

}
