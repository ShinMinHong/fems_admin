package com.firealarm.admin.security.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import framework.util.AbleUtil;
import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.security.vo.AdminDT;

/**
 * Spring Security DAO
 *
 */
@Repository
public class AppUserDAO extends DAOSupport {

	/**
	 * adminId로 관리자 조회
	 * @param userId
	 */
	public AdminDT findAdminById(String adminId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("adminId", adminId);
		AdminDT dt = sqlSession.selectOne(mapperNamespace+AbleUtil.getCurrentMethodName(), param);
		return dt;
	}

	/**
	 * 관리자ID로 관리자권한코드목록 조회
	 * @param adminId
	 */
	public List<String> findAdminRoleCodesById(String adminId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("adminId", adminId);
		return sqlSession.selectList(mapperNamespace+AbleUtil.getCurrentMethodName(), param);
	}

//	/**
//	 * MngAreaSeq를 기반으로 관제지역 정보 조회
//	 * @param adminId
//	 */
//	public List<String> findAdminRoleCodesById(long mngAreaSeq) {
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("adminId", adminId);
//		return sqlSession.selectList(mapperNamespace+AbleUtil.getCurrentMethodName(), param);
//	}
//
//	/**
//	 * 관리자ID로 관리자권한코드목록 조회
//	 * @param adminId
//	 */
//	public List<String> findAdminRoleCodesById(String adminId) {
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("adminId", adminId);
//		return sqlSession.selectList(mapperNamespace+AbleUtil.getCurrentMethodName(), param);
//	}
}
