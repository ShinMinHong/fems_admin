package com.firealarm.admin.biz.system.firedetectordemonlog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.firealarm.admin.biz.system.firedetectordemonlog.dao.FireDetectorDemonLogDAO;
import com.firealarm.admin.biz.system.firedetectordemonlog.vo.FireDetectorDemonLogVO;
import com.firealarm.admin.common.support.ServiceSupport;

import framework.vo.SearchMap;

/**
 * Demon Log Service
 * @author ovcoimf
 *
 */
@Service
public class FireDetectorDemonLogService extends ServiceSupport {

	@Autowired FireDetectorDemonLogDAO fireDetectorDemonLogDAO;

	/** Demon Log 목록 조회 */
	public List<FireDetectorDemonLogVO> getListAll(Sort sort, SearchMap search) {
		return fireDetectorDemonLogDAO.getListAll(sort, search);
	}

	/** Demon Log 페이징 조회 */
	public Page<FireDetectorDemonLogVO> getList(Pageable pageable, SearchMap search) {
		return fireDetectorDemonLogDAO.getList(pageable, search);
	}
}