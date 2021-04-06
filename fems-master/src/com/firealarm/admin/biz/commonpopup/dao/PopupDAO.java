package com.firealarm.admin.biz.commonpopup.dao;

import org.springframework.stereotype.Repository;

import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.common.vo.SendSmsVO;

import framework.util.AbleUtil;

/**
 * 공통팝업 DAO
 * @author KDH
 *
 */
@Repository
public class PopupDAO extends DAOSupport {

	/** SMS발송 */
	public void sendSms(SendSmsVO vo){
		sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), vo);
	}

}
