package com.firealarm.admin.common.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firealarm.admin.common.service.ManagerAuditService;
import com.firealarm.admin.common.vo.ManagerAuditDT;
import com.firealarm.admin.security.exception.MngAreaAccessDeniedException;
import com.firealarm.admin.security.util.UserSecurityUtil;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.exception.AbleRuntimeException;

/**
 * Commponent 지원 클래스
 *
 * @author ByeongDon
 */
public class ComponentSupport {

	@Autowired protected ObjectMapper objectMapper;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = false) protected ManagerAuditService managerAuditService;

	/** 관리자 감사로그 추가 */
	public void insertManagerAudit(String menuName, String actionName, String actionDetails) {
		try{
			ManagerAuditDT managerAuditDT = new ManagerAuditDT();
			managerAuditDT.setMenuName(menuName);
			managerAuditDT.setActionName(actionName);
			managerAuditDT.setActionDetail(actionDetails);

			logger.info("::::::::::::::::::::::::: managerAuditDT = {}", managerAuditDT);
			managerAuditService.insert(managerAuditDT);

		} catch (Exception e) {
			//감사로그 저장 중 에러발생 시 오류 메시지 출력 및 사용자에게 오류 관련 메시지 보이지 않도록 exception return 발생 X
			e.getMessage(); //시큐어코딩 가이드
		}
	}

//	/** 관리자 감사로그 추가 */
//	public void insertAdminAudit(AdminAuditDT dt) {
//		adminAuditService.insertAdminAudit(dt);
//	}

	/**
	 * 주어진 객체를 JSON문자열로 Serialize (Jackson2 ObjectMapper 사용)
	 * @param object 변환할 객체
	 * @return JSON 문자열
	 *
	 * @see http://wiki.fasterxml.com/JacksonJsonViews
	 * @see http://www.jroller.com/RickHigh/entry/working_with_jackson_json_views
	 */
	public String toJson(Object object) {
		return toJson(object, null);
	}

	/**
	 * 주어진 객체를 JSON문자열로 Serialize (Jackson2 ObjectMapper 사용)
	 * @param object 변환할 객체
	 * @param serializationView Serialization에 사용할 View Class (<code>@JsonView</code> 참고)
	 * @return JSON 문자열
	 *
	 * @see http://wiki.fasterxml.com/JacksonJsonViews
	 * @see http://www.jroller.com/RickHigh/entry/working_with_jackson_json_views
	 */
	public String toJson(Object object, Class<?> serializationView) {
		String json = null;
		try {
			if(serializationView != null) {
				json = objectMapper.writerWithView(serializationView).writeValueAsString(object);
			} else {
				json = (object == null) ? null : objectMapper.writeValueAsString(object);
			}
		} catch (JsonProcessingException e) {
			throw new AbleRuntimeException("Failed to write as json", e);
		}
		return StringUtils.replace(json, "/", "\\/"); //StringEscapeUtils.escapeJson
	}

	public boolean hasMngArea() {
		AppUserDetails user = UserSecurityUtil.getCurrentUserDetails();
		return user.hasMngArea();
	}

	public void validateMngArea() {
		AppUserDetails user = UserSecurityUtil.getCurrentUserDetails();

		if(!user.hasMngArea()) {
			throw new MngAreaAccessDeniedException();
		}
	}
}