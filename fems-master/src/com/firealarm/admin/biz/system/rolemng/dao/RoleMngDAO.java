package com.firealarm.admin.biz.system.rolemng.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.biz.system.rolegroupmng.vo.RoleGroupMngVO;
import com.firealarm.admin.biz.system.rolemng.vo.RoleMngActiveVO;
import com.firealarm.admin.biz.system.rolemng.vo.RoleMngDetailsVO;
import com.firealarm.admin.biz.system.rolemng.vo.RoleMngRoleDetailsVO;
import com.firealarm.admin.biz.system.rolemng.vo.RoleMngVO;

import framework.spring.web.rest.jsonview.AblePageImpl;
import framework.util.AbleUtil;
import framework.vo.SearchMap;

/**
 * 권한코드관리 DAO
 * @role JKS
 *
 */
@Repository
public class RoleMngDAO extends DAOSupport {

	/** 목록 조회 */
	public List<RoleMngVO> getListAll(Sort sort, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sort", prepareSortParameter(sort, RoleMngVO.class));
		param.put("search", search);
		List<RoleMngVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		return list;
	}

	/** 페이지 조회 */
	public Page<RoleMngVO> getList(Pageable pageable, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("search", search);
		int total = sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName() + "Count", param);
		Pageable pageableToApply = ensureValidPageable(pageable, total);
		param.put("page", pageableToApply);
		param.put("sort", prepareSortParameter(pageableToApply.getSort(), RoleMngVO.class));
		List<RoleMngVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName() + "Page", param);
		Page<RoleMngVO> page = new AblePageImpl<>(list, pageableToApply, total);
		return page;
	}

	/** SEQ로 권한 정보획득 */
	public RoleMngVO getByRole(String roleCode){
		return sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName(), roleCode);
	}

	/** 상세 */
	public RoleMngDetailsVO getDetailsPageByRoleCode(String roleCode) {
		return sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName(), roleCode);
	}

	/** 권한 수정 */
	public int updateRole(RoleMngActiveVO actionVO, String preRoleCode, String regAdminId) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("roleCode", actionVO.getRoleCode());
		param.put("roleName", actionVO.getRoleName());
		param.put("regAdminId", regAdminId);
		param.put("preRoleCode", preRoleCode);
		return sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 권한 삭제 */
	public int deleteRole(String roleCode) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("roleCode", roleCode);
		return sqlSession.delete(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 권한-권한테이블 삭제 */
	public int deleteRolegroupRole(String roleCode) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("roleCode", roleCode);
		return sqlSession.delete(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 권한 등록 */
	public void insertRole(RoleMngActiveVO actionVO , String regAdminId) {
		 Map<String,Object> param = new HashMap<String,Object>();
		 param.put("roleCode", actionVO.getRoleCode() );
		 param.put("roleName", actionVO.getRoleName());
		 param.put("regAdminId", regAdminId );
		 sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/**  권한 - 권한그룹 단건 등록  */
	public void insertDataRoleRoleGroup(RoleMngRoleDetailsVO actionVO, String regAdminId) {
		 Map<String,Object> param = new HashMap<String,Object>();
		 param.put("rolegroupCode", actionVO.getRolegroupCode() );
		 param.put("roleCode", actionVO.getRoleCode());
		 param.put("regAdminId", regAdminId );
		 sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 권한 그룹 조회 */
	public List<RoleGroupMngVO> getRoleGroupList() {
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName());
	}
}