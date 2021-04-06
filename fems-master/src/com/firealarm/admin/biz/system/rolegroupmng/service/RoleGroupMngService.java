package com.firealarm.admin.biz.system.rolegroupmng.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.firealarm.admin.biz.system.rolegroupmng.dao.RoleGroupMngDAO;
import com.firealarm.admin.biz.system.rolegroupmng.vo.RoleGroupMngActiveVO;
import com.firealarm.admin.biz.system.rolegroupmng.vo.RoleGroupMngDetailsVO;
import com.firealarm.admin.biz.system.rolegroupmng.vo.RoleGroupMngRoleDetailsVO;
import com.firealarm.admin.biz.system.rolegroupmng.vo.RoleGroupMngVO;
import com.firealarm.admin.biz.system.rolemng.vo.RoleMngVO;
import com.firealarm.admin.common.support.ServiceSupport;

import framework.vo.SearchMap;

@Service
public class RoleGroupMngService extends ServiceSupport {
	@Autowired RoleGroupMngDAO roleGroupMngDAO;

	/** ROLE 테이블 (ROLE_CODE, ROLE_NAME) <= controller (details 페이지 권한 명칭 표현을 위해 추출)*/
	public List<RoleMngVO> getRoleList() {
		return roleGroupMngDAO.getRoleList();
	}

	/** 권한그룹관리 목록 조회 */
	public List<RoleGroupMngVO> getListAll(Sort sort, SearchMap search) {
		return roleGroupMngDAO.getListAll(sort, search);
	}

	/** 권한그룹관리 페이징 조회 */
	public Page<RoleGroupMngVO> getList(Pageable pageable, SearchMap search) {
		return roleGroupMngDAO.getList(pageable, search);
	}

	/** 상세 */
	public RoleGroupMngDetailsVO getDetailsPageByRolegroupCode(String rolegroupCode) {
		return roleGroupMngDAO.getDetailsPageByRolegroupCode(rolegroupCode);
	}

	/** 권한그룹 정보획득 */
	public RoleGroupMngVO getByRolegroup(String rolegroupCode){
		return roleGroupMngDAO.getByRolegroup(rolegroupCode);
	}

	/** 권한그룹관리 등록 - RFFMS_ROLEGROUP 테이블 INSERT */
	@Transactional
	public void add(RoleGroupMngActiveVO actionVO, String rolegroupCode, String register ) {
		/* 1.권한 그룹 등록 */
		roleGroupMngDAO.insertRolegroup(actionVO, register );

		/* 권한과 연결 */
		List<RoleGroupMngRoleDetailsVO> roleDetailsList = new ArrayList<RoleGroupMngRoleDetailsVO>() ;
		roleDetailsList = actionVO.getRoleDetailsList();
		if(!roleDetailsList.isEmpty()) {
			for(RoleGroupMngRoleDetailsVO vo : roleDetailsList){
				vo.setRegAdminId(register);
				vo.setRolegroupCode(rolegroupCode);
				if(vo.getChecked()) {
					roleGroupMngDAO.insertDataRoleRoleGroup(vo);
				}
			}
		}
	}

	/** 권한그룹관리 수정 - ROLEGROUP 테이블 UPDATE */
	@Transactional
	public void update(RoleGroupMngActiveVO actionVO, String prevRolegroupCode,  String register) {
		roleGroupMngDAO.updateRolegroup(actionVO, prevRolegroupCode, register);
		roleGroupMngDAO.deleteRolegroupRole(actionVO.getRolegroupCode());
		List<RoleGroupMngRoleDetailsVO> roleDetailsList = new ArrayList<RoleGroupMngRoleDetailsVO>() ;
		roleDetailsList = actionVO.getRoleDetailsList();
		if (! actionVO.getRoleDetailsList().isEmpty()){
			for(RoleGroupMngRoleDetailsVO vo : roleDetailsList){
				vo.setRegAdminId(register);
				vo.setRolegroupCode(actionVO.getRolegroupCode());
				if(vo.getChecked()) {
					roleGroupMngDAO.insertDataRoleRoleGroup(vo);
				}
			}
		}
	}

	/** 권한그룹관리 삭제 - ROLEGROUP 테이블 DELETE */
	@Transactional
	public void delete(String rolegroupCode) {
		/* 아래부분은 RMS_ROLEGROUP 테이블을 DELETE하면서 ORACLE CASECADE를 통해서 삭제됨. 실제로 DAO를 통해서 삭제되진 않음 */
		roleGroupMngDAO.deleteRolegroupRole(rolegroupCode);
		roleGroupMngDAO.deleteRolegroup(rolegroupCode);
	}
}
