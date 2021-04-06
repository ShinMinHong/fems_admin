package com.firealarm.admin.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import framework.vo.SearchMap;
import com.firealarm.admin.common.dao.ManagerAuditDAO;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.common.vo.ManagerAuditDT;


@Service
public class ManagerAuditService extends ServiceSupport {

	@Autowired ManagerAuditDAO managerAuditDAO;

	/** 목록 조회 */
	public List<ManagerAuditDT> getListAll(Sort sort, SearchMap search) {
		return managerAuditDAO.getListAll(sort, search);
	}

	/** 페이징 조회 */
	public Page<ManagerAuditDT> getList(Pageable pageable, SearchMap search) {
		return managerAuditDAO.getList(pageable, search);
	}

	/** 상세 정보 */
	public ManagerAuditDT getManagerAuditBySeq(int opertInfoEsntlNo){
		return managerAuditDAO.getManagerAuditBySeq(opertInfoEsntlNo);
	}

	/** 등록 */
	public void insert(ManagerAuditDT managerAuditDT) {
		managerAuditDAO.insert(managerAuditDT);
	}
}
