package com.firealarm.admin.biz.commonpopup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firealarm.admin.biz.commonpopup.dao.PopupDAO;
import com.firealarm.admin.common.support.ServiceSupport;

/**
 * 공통팝업 Service
 * @author KDH
 *
 */
@Service
public class PopupService extends ServiceSupport {

	@Autowired PopupDAO popupDAO;

//	/** SMS발송 */
//	public void sendSms(String id, SendSmsVO vo) {
//		// 휴대폰 수신번호 유효성 검사 후 Send
//		if(AbleStringUtil.isValidHpNo(vo.getToTelNo())) {
//			if(AppProfiles.getInstance().isRuntimeProduction()) {
//				vo.setToTelNo(vo.getToTelNo().replaceAll("-", ""));
//				popupDAO.sendSms(vo);
//			} else {
//				logger.debug("SMS 발송 테이블(SDK_SMS_SEND) 등록. 운영기가 아니라 실제로는 SDK_SMS_SEND에 Insert하지는 않음");
//			}
//		}
//	}

}
