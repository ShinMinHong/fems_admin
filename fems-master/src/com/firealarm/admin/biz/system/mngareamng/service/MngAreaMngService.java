package com.firealarm.admin.biz.system.mngareamng.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.firealarm.admin.biz.system.mngareamng.dao.MngAreaMngDAO;
import com.firealarm.admin.biz.system.mngareamng.vo.MngAreaMngActiveVO;
import com.firealarm.admin.biz.system.mngareamng.vo.MngAreaMngVO;
import com.firealarm.admin.common.support.ServiceSupport;

import framework.vo.SearchMap;

/**
 * 관제지역관리 Service
 * @author ovcoimf
 *
 */
@Service
public class MngAreaMngService extends ServiceSupport {

	@Autowired MngAreaMngDAO mngAreaMngDAO;

	/** 관제지역 목록 조회 */
	public List<MngAreaMngVO> getListAll(Sort sort, SearchMap search) {
		return mngAreaMngDAO.getListAll(sort, search);
	}

	/** 관제지역 페이징 조회 */
	public Page<MngAreaMngVO> getList(Pageable pageable, SearchMap search) {
		return mngAreaMngDAO.getList(pageable, search);
	}

	/** 상세 - Seq로 검색 */
	public MngAreaMngVO getByMngAreaSeq(long mngAreaSeq) {
		return mngAreaMngDAO.getByMngAreaSeq(mngAreaSeq);
	}

	/** 상세 - 이름중복검사용 */
	public MngAreaMngVO getByMngAreaName(String mngAreaName) {
		return mngAreaMngDAO.getByMngAreaName(mngAreaName);
	}

	/** 등록 */
	@Transactional
	public void insert(MngAreaMngActiveVO actionVO) {
		mngAreaMngDAO.insert(actionVO);
	}

	/** 수정 */
	@Transactional
	public void update(MngAreaMngActiveVO actionVO) {
		mngAreaMngDAO.update(actionVO);
	}

	/** 삭제  */
	@Transactional
	public void delete(long mngAreaSeq) {
		mngAreaMngDAO.delete(mngAreaSeq);
	}
}