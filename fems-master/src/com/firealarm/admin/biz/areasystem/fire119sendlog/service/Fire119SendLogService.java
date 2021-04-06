package com.firealarm.admin.biz.areasystem.fire119sendlog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.firealarm.admin.biz.areasystem.fire119sendlog.dao.Fire119SendLogDAO;
import com.firealarm.admin.biz.areasystem.fire119sendlog.vo.Fire119SendLogVO;
import com.firealarm.admin.common.support.ServiceSupport;

import framework.vo.SearchMap;

/**
 *  119 다매체 발신 관리 Service
 * @author SMH
 */
@Service
public class Fire119SendLogService extends ServiceSupport {

	@Autowired Fire119SendLogDAO fire119SendLogDAO;

	/** 전체 목록 조회 */
	public List<Fire119SendLogVO> getListAll(Sort sort, SearchMap search) {
		return fire119SendLogDAO.getListAll(sort, search);
	}

	/** 페이징 목록 조회 */
	public Page<Fire119SendLogVO> getList(Pageable pageable, SearchMap search) {
		return fire119SendLogDAO.getList(pageable, search);
	}

}
