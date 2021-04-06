package com.firealarm.admin.biz.commonpopup.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.firealarm.admin.appconfig.CodeMap.APP_USER_GRADE;
import com.firealarm.admin.biz.commonpopup.service.PopupService;
import com.firealarm.admin.common.support.ApiControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.spring.web.rest.AbleResponseEntity;
import framework.spring.web.rest.AbleResponseEntityMap;

/**
 * 공통팝업 API Controller
 * @author ovcoimf
 *
 */
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/commonpopup/api")
public class PopupApiController extends ApiControllerSupport {

	@Autowired PopupService popupService;

	/** HQ관리자 관제지역 선택 */
	@PreAuthorize("hasAuthority('ROLE_SYSTEM_ADMIN')")
	@RequestMapping(value = "/setmngarea/{mngAreaSeq}", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> setMngArea(
			HttpServletRequest request,
			@PathVariable("mngAreaSeq") long mngAreaSeq,
			@AppLoginUser AppUserDetails user) {
		if(APP_USER_GRADE.HQ_ADMIN.equals(user.getRolegroupCode())) {
			user.setMngAreaSeq(mngAreaSeq);
		}

		return AbleResponseEntityMap.success(null);
	}





}
