package com.firealarm.admin.biz.areasystem.msgsendlog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.firealarm.admin.biz.areasystem.msgsendlog.dao.MsgSendLogDAO;
import com.firealarm.admin.biz.areasystem.msgsendlog.vo.MsgSendLogVO;
import com.firealarm.admin.common.support.ServiceSupport;

import framework.vo.SearchMap;

/**
 *  문자메시지 발신 관리 Service
 * @author JKS
 */
@Service
public class MsgSendLogService extends ServiceSupport {

	@Autowired MsgSendLogDAO msgSendLogDAO;

	/** 전체 목록 조회 */
	public List<MsgSendLogVO> getListAll(Sort sort, SearchMap search) {
		return msgSendLogDAO.getListAll(sort, search);
	}

	/** 페이징 목록 조회 */
	public Page<MsgSendLogVO> getList(Pageable pageable, SearchMap search) {
		return msgSendLogDAO.getList(pageable, search);
	}

}
