package com.firealarm.admin.biz.system.rolegroupmng.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.biz.system.rolegroupmng.vo.RoleGroupMngActiveVO;
import com.firealarm.admin.biz.system.rolegroupmng.vo.RoleGroupMngDetailsVO;
import com.firealarm.admin.biz.system.rolegroupmng.vo.RoleGroupMngRoleDetailsVO;
import com.firealarm.admin.biz.system.rolegroupmng.vo.RoleGroupMngVO;
import com.firealarm.admin.biz.system.rolemng.vo.RoleMngVO;

import framework.spring.web.rest.jsonview.AblePageImpl;
import framework.util.AbleUtil;
import framework.vo.SearchMap;

@Repository
public class RoleGroupMngDAO extends DAOSupport {

	public List<RoleMngVO> getRoleList() {
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName());
	}

	/** 권한그룹관리 목록 조회 */
	public List<RoleGroupMngVO> getListAll(Sort sort, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sort", prepareSortParameter(sort, RoleGroupMngVO.class));
		param.put("search", search);
		List<RoleGroupMngVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		return list;
	}

	/** 권한그룹관리 페이징 조회 */
	public Page<RoleGroupMngVO> getList(Pageable pageable, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("search", search);
		int total = sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName() + "Count", param);
		Pageable pageableToApply = ensureValidPageable(pageable, total);
		param.put("page", pageableToApply);
		param.put("sort", prepareSortParameter(pageableToApply.getSort(), RoleGroupMngVO.class));
		List<RoleGroupMngVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName() + "Page", param);
		Page<RoleGroupMngVO> page = new AblePageImpl<>(list, pageableToApply, total);
		return page;
	}

	/** 상세 정보 */
	public RoleGroupMngDetailsVO getDetailsPageByRolegroupCode(String rolegroupCode) {
		return sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName(), rolegroupCode);
	}

	/** 권한그룹 정보획득 */
	public RoleGroupMngVO getByRolegroup(String rolegroupCode) {
		return sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName(), rolegroupCode);
	}

	/** 권한그룹관리 등록 - RFFMS_ROLEGROUP_ROLE 테이블 INSERT */
	public int insertRolegroupRole(RoleGroupMngActiveVO actionVO) {
		return sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), actionVO);
	}

	/** 권한그룹관리 등록 - RMS_ROLEGROUP 테이블 INSERT */
	public int insertRolegroup(RoleGroupMngActiveVO actionVO, String register) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("register", register);
		param.put("rolegroupCode", actionVO.getRolegroupCode());
		param.put("rolegroupName", actionVO.getRolegroupName());
		return sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 권한코드관리 삭제 - RMS_ROLEGROUP 테이블 DELETE */
	public int deleteRolegroup(String rolegroupCode) {
		return sqlSession.delete(mapperNamespace + AbleUtil.getCurrentMethodName(), rolegroupCode);
	}

	/** 권한코드관리 삭제 - RMS_ROLEGROUP_ROLE 테이블 연관 코드 삭제 */
	public int deleteRolegroupRole(String rolegroupCode) {
		return sqlSession.delete(mapperNamespace + AbleUtil.getCurrentMethodName(), rolegroupCode);
	}

	public void insertDataRoleRoleGroup(RoleGroupMngRoleDetailsVO detailsVO) {
		sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), detailsVO);
	}

	/** 권한 그룹 수정 */
	public void updateRolegroup(RoleGroupMngActiveVO actionVO, String prevRolegroupCode, String register) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("prevRolegroupCode", prevRolegroupCode);
		param.put("rolegroupName", actionVO.getRolegroupName());
		param.put("rolegroupCode", actionVO.getRolegroupCode());
		param.put("updusr", register);
		sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

}
