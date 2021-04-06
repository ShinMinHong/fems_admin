package com.firealarm.admin.biz.areasystem.adminmng.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.firealarm.admin.biz.areasystem.adminmng.dao.AdminMngDAO;
import com.firealarm.admin.biz.areasystem.adminmng.vo.AdminMngActiveVO;
import com.firealarm.admin.biz.areasystem.adminmng.vo.AdminMngVO;
import com.firealarm.admin.common.dao.UserDAO;
import com.firealarm.admin.common.support.ServiceSupport;

import framework.exception.AbleRuntimeException;
import framework.security.provider.AppSHAPasswordEncoder;
import framework.vo.SearchMap;

/**
 * 관리자 관리 Service
 * @author JKS
 */
@Service
public class AdminMngService extends ServiceSupport {

	@Autowired AdminMngDAO adminMngDAO;
	@Autowired UserDAO userDAO;

	private AppSHAPasswordEncoder appSHAPasswordEncoder = new AppSHAPasswordEncoder();

	/** 전체 목록 조회 */
	public List<AdminMngVO> getListAll(Sort sort, SearchMap search) {
		return adminMngDAO.getListAll(sort, search);
	}

	/** 페이징 목록 조회 */
	public Page<AdminMngVO> getList(Pageable pageable, SearchMap search) {
		return adminMngDAO.getList(pageable, search);
	}

	/** 상세 */
	public AdminMngVO getUserDetailsByAdminSeq(long adminSeq) {
		return adminMngDAO.getUserDetailsByAdminSeq(adminSeq);
	}

	/** 아이디 중복체크 */
	public boolean checkDuplicateId(String adminId){
		if ( StringUtils.isBlank(adminId) ){
			return false;
		}
		if ( adminMngDAO.getSameIdCnt(adminId) > 0 ){
			return false;
		}
		return true;
	}

	/** 등록 */
	public void insert(AdminMngActiveVO vo) {
		vo.setAdminPassword((appSHAPasswordEncoder.encode(vo.getAdminPassword())));
		this.checkDuplicateId(vo.getAdminId());

		adminMngDAO.insert(vo);
	}

	/** 수정 */
	public void update(AdminMngActiveVO newUploadVo, AdminMngVO oldAdminInfo) {
		if (newUploadVo.isCheckChangePassword()){
			//기존 비밀번호 검사
			AppSHAPasswordEncoder passwordEncoder = new AppSHAPasswordEncoder();
			String oldRawPassword = newUploadVo.getOldPassword();
			String oldPassword = oldAdminInfo.getAdminPassword();

			if ( !passwordEncoder.matches(oldRawPassword, oldPassword) ){
				throw new AbleRuntimeException("기존 비밀번호가 올바르지 않습니다.");
			}

			newUploadVo.setAdminPassword(appSHAPasswordEncoder.encode(newUploadVo.getAdminPassword()));
		}

		adminMngDAO.update(newUploadVo);

		if (newUploadVo.isCheckChangePassword()){
			//비밀번호변경이력 등록
			userDAO.insertPwdChangeLog(oldAdminInfo.getAdminSeq(), oldAdminInfo.getAdminId()
					, newUploadVo.getAdminName(), newUploadVo.getDutyName());
		}
	}

	/** 관리자에 의한 수정 */
	public void updateByAdmin(AdminMngActiveVO newUploadVo, AdminMngVO oldAdminInfo) {
		if (newUploadVo.isCheckChangePassword()){
			newUploadVo.setAdminPassword(appSHAPasswordEncoder.encode(newUploadVo.getAdminPassword()));
		}

		adminMngDAO.update(newUploadVo);

		if (newUploadVo.isCheckChangePassword()){
			//비밀번호변경이력 등록
			userDAO.insertPwdChangeLog(oldAdminInfo.getAdminSeq(), oldAdminInfo.getAdminId()
					, newUploadVo.getAdminName(), newUploadVo.getDutyName());
		}
	}

	/** 비밀번호 초기화 */
	public void resetPassword(AdminMngVO adminInfo) {
		String pswd = appSHAPasswordEncoder.encode("firealarm*119");
		adminMngDAO.resetPassword(adminInfo.getAdminSeq(), pswd);

		//비밀번호변경이력 등록
		userDAO.insertPwdChangeLog(adminInfo.getAdminSeq(), adminInfo.getAdminId()
				, adminInfo.getAdminName(), adminInfo.getDutyName());
	}

	/** 삭제 */
	public void delete(long adminSeq) {
		adminMngDAO.delete(adminSeq);
	}
}
