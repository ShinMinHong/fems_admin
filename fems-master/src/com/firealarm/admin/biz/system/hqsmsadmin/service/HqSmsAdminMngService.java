package com.firealarm.admin.biz.system.hqsmsadmin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.firealarm.admin.biz.system.hqsmsadmin.dao.HqSmsAdminMngDAO;
import com.firealarm.admin.biz.system.hqsmsadmin.vo.HqSmsAdminMngActiveVO;
import com.firealarm.admin.biz.system.hqsmsadmin.vo.HqSmsAdminMngVO;
import com.firealarm.admin.common.support.ServiceSupport;

import framework.vo.SearchMap;

/**
 * 통합SMS수신자 Service
 * @author SHH
 */
@Service
public class HqSmsAdminMngService extends ServiceSupport {

	@Autowired HqSmsAdminMngDAO smsAdminMngDAO;

	/** 전체 목록 조회 */
	public List<HqSmsAdminMngVO> getListAll(Sort sort, SearchMap search) {
		return smsAdminMngDAO.getListAll(sort, search);
	}

	/** 페이징 목록 조회 */
	public Page<HqSmsAdminMngVO> getList(Pageable pageable, SearchMap search) {
		return smsAdminMngDAO.getList(pageable, search);
	}

	/** 상세 */
	public HqSmsAdminMngVO getSmsAdminDetailsBySmsAdminSeq(long smsAdminSeq) {
		return smsAdminMngDAO.getSmsAdminDetailsBySmsAdminSeq(smsAdminSeq);
	}

	/** 등록 */
	public void insert(HqSmsAdminMngActiveVO vo) {
		smsAdminMngDAO.insert(vo);
	}

	/** 수정 */
	public void update(HqSmsAdminMngActiveVO newUploadVo) {
		smsAdminMngDAO.update(newUploadVo);
	}

	/** 삭제 */
	public void delete(long smsAdminSeq) {
		smsAdminMngDAO.delete(smsAdminSeq);
	}
}
