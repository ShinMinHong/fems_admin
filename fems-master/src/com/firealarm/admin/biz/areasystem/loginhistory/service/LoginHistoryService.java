package com.firealarm.admin.biz.areasystem.loginhistory.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import framework.vo.SearchMap;

import com.firealarm.admin.biz.areasystem.loginhistory.dao.LoginHistoryDAO;
import com.firealarm.admin.biz.areasystem.loginhistory.vo.LoginHistoryVO;
import com.firealarm.admin.common.support.ServiceSupport;

/**
 * 접속정보이력관리 Service
 * @author JKS
 */
@Service
public class LoginHistoryService extends ServiceSupport{

	@Autowired LoginHistoryDAO loginHistoryDAO;

	/** 전체 목록 조회 */
	public List<LoginHistoryVO> getListAll(Sort sort, SearchMap search) {
		return loginHistoryDAO.getListAll(sort, search);
	}

	/** 페이징 목록 조회 */
	public Page<LoginHistoryVO> getList(Pageable pageable, SearchMap search) {
		return loginHistoryDAO.getList(pageable, search);
	}

}