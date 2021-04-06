package com.firealarm.admin.common.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.firealarm.admin.common.support.DAOSupport;

import framework.util.AbleUtil;

@Repository
public class UserDAO extends DAOSupport {

	/** 사용자정보테이블에 최종접속일시 업데이트 */
	public void updateLastLoginDate(long adminSeq){
		sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), adminSeq);
	}

	/** 접속정보이력테이블에 로그인로그 등록 */
	public void insertLoginLog(long adminSeq) {
		Map<String, Object> param = new HashMap<>();
		param.put("adminSeq", adminSeq);
		sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 기존비밀번호 획득 */
	public String getOldPassword(String adminId){
		return sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName(), adminId);
	}

	/** 비밀번호변경 */
	public void updatePassword(String adminId, String adminPassword){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("adminId", adminId);
		param.put("adminPassword", adminPassword);
		sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 비밀번호변경이력 등록 */
	public void insertPwdChangeLog(long adminSeq, String adminId, String adminName, String dutyName) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("adminSeq", adminSeq);
		param.put("adminId", adminId);
		param.put("adminName", adminName);
		param.put("dutyName", dutyName);
		sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 로그인 실패 카운트 증가 처리 */
	public void setPasswordFailCo(String adminId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("adminId", adminId);
		sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}
}
