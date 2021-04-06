package com.firealarm.admin.biz.firedetector.nosignaldetector.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.firealarm.admin.biz.firedetector.nosignaldetector.dao.NoSignalDetectorDAO;
import com.firealarm.admin.biz.firedetector.nosignaldetector.vo.NoSignalDetectorVO;
import com.firealarm.admin.common.support.ServiceSupport;

import framework.vo.SearchMap;


/**
 * 통신장애 감지기 조회 Service
 * @author rodem4_pc1
 */
@Service
public class NoSignalDetectorService extends ServiceSupport {

	@Autowired NoSignalDetectorDAO noSignalDetectorDAO;


	/** 전체 목록 조회 */
	public List<NoSignalDetectorVO> getListAll(Sort sort, SearchMap search) {
		return noSignalDetectorDAO.getListAll(sort, search);
	}

	/** 페이징 목록 조회 */
	public Page<NoSignalDetectorVO> getList(Pageable pageable, SearchMap search) {
		return noSignalDetectorDAO.getList(pageable, search);
	}

}
