package com.firealarm.admin.biz.integration.totcontrolmap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.firealarm.admin.biz.home.vo.FireDetectorNowStatusVO;
import com.firealarm.admin.biz.integration.totcontrolmap.service.TotControlMapService;
import com.firealarm.admin.common.support.ApiControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.spring.web.rest.AbleResponseEntity;
import framework.spring.web.rest.AbleResponseEntityBuilder;

@Controller
@PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_ADMIN')")
@RequestMapping(value = "/integration/totcontrolmap/api")
public class TotControlMapApiController extends ApiControllerSupport {

	@Autowired TotControlMapService totControlMapService;

	/** 이벤트 상태의 목록 조회 */
	@RequestMapping(value = "/getstatuslistinevent", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> getStatusListInEvent(
			@AppLoginUser AppUserDetails user, Model model) {

		List<FireDetectorNowStatusVO> list = totControlMapService.getAllStatusListInEvent();

		return AbleResponseEntityBuilder.success(list);
	}
}
