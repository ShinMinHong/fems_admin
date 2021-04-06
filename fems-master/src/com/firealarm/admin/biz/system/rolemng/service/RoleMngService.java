package com.firealarm.admin.biz.system.rolemng.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.biz.system.rolegroupmng.vo.RoleGroupMngVO;
import com.firealarm.admin.biz.system.rolemng.dao.RoleMngDAO;
import com.firealarm.admin.biz.system.rolemng.vo.RoleMngActiveVO;
import com.firealarm.admin.biz.system.rolemng.vo.RoleMngDetailsVO;
import com.firealarm.admin.biz.system.rolemng.vo.RoleMngRoleDetailsVO;
import com.firealarm.admin.biz.system.rolemng.vo.RoleMngVO;

import framework.vo.SearchMap;

/**
 * 권한코드관리 Service
 * @author JKS
 *
 */
@Service
public class RoleMngService extends ServiceSupport {

	@Autowired RoleMngDAO roleMngDAO;

	/** 권한 그룹 리스트 */
	public List<RoleGroupMngVO> getRoleGroupList() {
		return roleMngDAO.getRoleGroupList();
	}

	/** 권한코드관리 목록 조회 */
	public List<RoleMngVO> getListAll(Sort sort, SearchMap search) {
		return roleMngDAO.getListAll(sort, search);
	}

	/** 권한코드관리 페이징 조회 */
	public Page<RoleMngVO> getList(Pageable pageable, SearchMap search) {
		return roleMngDAO.getList(pageable, search);
	}

	/** 권한 정보획득 */
	public RoleMngVO getByRole(String authorCode){
		return roleMngDAO.getByRole(authorCode);
	}

	/** 상세 */
	public RoleMngDetailsVO getDetailsPageByRoleCode(String authorCode) {
		return roleMngDAO.getDetailsPageByRoleCode(authorCode);
	}

	/** 등록 */
	@Transactional
	public void add(RoleMngActiveVO actionVO, String register) {
		roleMngDAO.insertRole(actionVO, register);
		if ( !actionVO.getRoleDetailsList().isEmpty() ){
			List<RoleMngRoleDetailsVO> roleDetailsList = new ArrayList<RoleMngRoleDetailsVO>();
			roleDetailsList = actionVO.getRoleDetailsList();
			for(RoleMngRoleDetailsVO vo : roleDetailsList ) {
				if(vo.getChecked()) {
					vo.setRoleCode(actionVO.getRoleCode());
					roleMngDAO.insertDataRoleRoleGroup(vo,register);
				}
			}
		}
	}

	/** 수정 */
	@Transactional
	public void update(RoleMngActiveVO actionVO, String preRoleCode, String register ) {
		roleMngDAO.updateRole(actionVO, preRoleCode , register );
		roleMngDAO.deleteRolegroupRole(actionVO.getRoleCode());
		if ( !actionVO.getRoleDetailsList().isEmpty() ){
			List<RoleMngRoleDetailsVO> roleDetailsList = new ArrayList<RoleMngRoleDetailsVO>();
			roleDetailsList = actionVO.getRoleDetailsList();
			for(RoleMngRoleDetailsVO vo : roleDetailsList ) {
				if(vo.getChecked()) {
					vo.setRoleCode(actionVO.getRoleCode());
					roleMngDAO.insertDataRoleRoleGroup(vo,register);
				}
			}
		}
	}

	/** 삭제  */
	@Transactional
	public void delete(String roleCode) {
		roleMngDAO.deleteRolegroupRole(roleCode);
		roleMngDAO.deleteRole(roleCode);
	}
}