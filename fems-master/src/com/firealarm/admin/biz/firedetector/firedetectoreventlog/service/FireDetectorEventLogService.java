package com.firealarm.admin.biz.firedetector.firedetectoreventlog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.firealarm.admin.biz.firedetector.firedetectoreventlog.dao.FireDetectorEventLogDAO;
import com.firealarm.admin.biz.firedetector.firedetectoreventlog.vo.FireDetectorEventLogVO;
import com.firealarm.admin.common.support.ServiceSupport;

import framework.vo.SearchMap;

/**
 * 화재감지기 이벤트조회 Service
 * @author JKS
 *
 */
@Service
public class FireDetectorEventLogService extends ServiceSupport {

	@Autowired FireDetectorEventLogDAO fireDetectorEventLogDAO;

	/** 전체 목록 조회 */
	public List<FireDetectorEventLogVO> getListAll(Sort sort, SearchMap search) {
		return fireDetectorEventLogDAO.getListAll(sort, search);
	}

	/** 페이징 목록 조회 */
	public Page<FireDetectorEventLogVO> getList(Pageable pageable, SearchMap search) {
		return fireDetectorEventLogDAO.getList(pageable, search);
	}

}