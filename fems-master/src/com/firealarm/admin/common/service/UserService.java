package com.firealarm.admin.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firealarm.admin.biz.home.vo.PasswordChangeVO;
import com.firealarm.admin.common.dao.UserDAO;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.security.dao.AppUserDAO;
import com.firealarm.admin.security.vo.AdminDT;

import framework.exception.AbleRuntimeException;
import framework.security.provider.AppSHAPasswordEncoder;

@Service
public class UserService extends ServiceSupport{

	@Autowired UserDAO userDAO;
	@Autowired AppUserDAO appUserDAO;

	/** 로그인 로그 기록
	 * @param loginType */
	public void updateLoginLog(long adminSeq){
		//사용자정보테이블에 최종접속일시 업데이트
		userDAO.updateLastLoginDate(adminSeq);
		//로그인로그정보테이블에 로그인 로그 등록
		userDAO.insertLoginLog(adminSeq);
	}

	/** 비밀번호변경 */
	public void updatePassword(PasswordChangeVO vo){
		AppSHAPasswordEncoder passwordEncoder = new AppSHAPasswordEncoder();
		String adminId = vo.getLoginId();
		String originalRawPassword = vo.getOriginalPassword();
		String newRawPassword = vo.getNewPassword();

		AdminDT adminInfo = appUserDAO.findAdminById(adminId);
		String oldPassword = adminInfo.getAdminPassword();

		if ( !passwordEncoder.matches(originalRawPassword, oldPassword) ){
			throw new AbleRuntimeException("현재 비밀번호가 올바르지 않습니다.");
		}

		String encodeNewPassword = passwordEncoder.encode(newRawPassword);
		//비밀번호변경
		userDAO.updatePassword(adminId, encodeNewPassword);
		//비밀번호변경이력 등록
		userDAO.insertPwdChangeLog(adminInfo.getAdminSeq(), adminInfo.getAdminId()
									, adminInfo.getAdminName(), adminInfo.getDutyName());
	}

}
