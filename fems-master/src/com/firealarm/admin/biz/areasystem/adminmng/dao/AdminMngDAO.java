package com.firealarm.admin.biz.areasystem.adminmng.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.security.util.UserSecurityUtil;
import com.firealarm.admin.biz.areasystem.adminmng.vo.AdminMngActiveVO;
import com.firealarm.admin.biz.areasystem.adminmng.vo.AdminMngVO;

import framework.util.AbleUtil;
import framework.vo.SearchMap;


/**
 * 관리자관리 DAO
 * @author JKS
 */
@Repository
public class AdminMngDAO extends DAOSupport {

	/** 전체 목록 조회 */
	public List<AdminMngVO> getListAll(Sort sort, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sort", prepareSortParameter(sort, AdminMngVO.class));
		param.put("search", search);
		param.put("mngAreaSeq", UserSecurityUtil.getCurrentUserDetails().getMngAreaSeq());
		List<AdminMngVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		return list;
	}

	/** 페이지 목록 조회 */
	public Page<AdminMngVO> getList(Pageable pageable, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		int total = sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName() + "Count", param);
		Pageable pageableToApply = ensureValidPageable(pageable, total);
		param.put("page", pageableToApply);
		param.put("sort", prepareSortParameter(pageableToApply.getSort(), AdminMngVO.class));
		param.put("mngAreaSeq", UserSecurityUtil.getCurrentUserDetails().getMngAreaSeq());
		List<AdminMngVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName() + "Page", param);
		Page<AdminMngVO> page = new PageImpl<AdminMngVO>(list, pageableToApply, total);
		return page;
	}

	/** 상세 */
	public AdminMngVO getUserDetailsByAdminSeq(long adminSeq) {
		return sqlSession.selectOne(mapperNamespace  + AbleUtil.getCurrentMethodName(), adminSeq);
	}

	/** 아이디 중복체크 */
	public int getSameIdCnt(String adminId) {
		return sqlSession.selectOne(mapperNamespace  + AbleUtil.getCurrentMethodName(), adminId);
	}

	/** 등록 */
	public void insert(AdminMngActiveVO vo) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("vo", vo);
		sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 수정 */
	public int update(AdminMngActiveVO vo) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("vo", vo);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		return sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 비밀번호 초기화 */
	public int resetPassword(long adminSeq, String pswd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("adminSeq", adminSeq);
		param.put("pswd", pswd);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		return sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 삭제 */
	public int delete(long adminSeq) {
		return sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), adminSeq);
	}

}
