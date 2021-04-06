package com.firealarm.admin.biz.home.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.firealarm.admin.appconfig.CodeMap.APP_USER_GRADE;
import com.firealarm.admin.biz.areasystem.adminmng.service.AdminMngService;
import com.firealarm.admin.biz.areasystem.adminmng.vo.AdminMngActiveVO;
import com.firealarm.admin.biz.areasystem.adminmng.vo.AdminMngVO;
import com.firealarm.admin.biz.home.service.HomeService;
import com.firealarm.admin.biz.home.vo.FireDetectorNowStatusVO;
import com.firealarm.admin.biz.home.vo.MapFireDetectorDetailVO;
import com.firealarm.admin.common.support.ApiControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.exception.MngAreaAccessDeniedException;
import com.firealarm.admin.security.util.UserSecurityUtil;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.exception.AbleRuntimeException;
import framework.exception.AbleValidationException;
import framework.spring.web.rest.AbleResponseEntity;
import framework.spring.web.rest.AbleResponseEntityBuilder;

@Controller
@RequestMapping(value = "/home/api")
@PreAuthorize("isAuthenticated()")
public class HomeApiController extends ApiControllerSupport {

	@Autowired HomeService homeService;
	@Autowired AdminMngService adminMngService;

	/** 이벤트 상태의 목록 조회 */
	@PreAuthorize("hasAuthority('ROLE_CONTROLMAP') AND @fireAlarmCustomSecurityService.hasMngArea()")
	@RequestMapping(value = "/getstatuslistinevent/{mngAreaSeq}", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> getStatusListInEvent(
			@PathVariable("mngAreaSeq") long mngAreaSeq,
			@AppLoginUser AppUserDetails user, Model model) {

		if(mngAreaSeq != user.getMngAreaSeq().longValue()) {
			throw new MngAreaAccessDeniedException();
		}

		boolean marketAdmin = APP_USER_GRADE.MARKET_ADMIN.equals(user.getRolegroupCode());
		List<FireDetectorNowStatusVO> list = homeService.getStatusListInEvent(user.getMngAreaSeq(),
				(marketAdmin ? user.getMarketSeq() : null));

		return AbleResponseEntityBuilder.success(list);
	}

	/** 화재감지기 상세정보 조회 */
	@PreAuthorize("hasAuthority('ROLE_SYSTEM_ADMIN') OR (hasAuthority('ROLE_CONTROLMAP') AND @fireAlarmCustomSecurityService.hasMngArea())")
	@RequestMapping(value = "/details/{fireDetectorSeq}", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> details(
			@PathVariable("fireDetectorSeq") long fireDetectorSeq,
			@AppLoginUser AppUserDetails user, Model model) {

		MapFireDetectorDetailVO ro = homeService.getMapDetailBySeq(fireDetectorSeq);
		if(ro == null) {
			throw new AbleRuntimeException("화재감지기 상세정보를 찾을 수 없습니다.");
		}
		return AbleResponseEntityBuilder.success(ro);
	}

	/** 회원정보 변경 [상세] */
	@RequestMapping(value = "/getuserinfo/{adminSeq}", method = RequestMethod.GET)
	public @ResponseBody AbleResponseEntity<?> details(
			@PathVariable("adminSeq") long adminSeq,
			Model model) {
		AdminMngVO ro = adminMngService.getUserDetailsByAdminSeq(adminSeq);
		return AbleResponseEntityBuilder.success(ro);
	}

	/** 회원정보 변경 [수정] */
	@RequestMapping(value = "/changeuserinfo/update/{adminSeq}", method = RequestMethod.POST)
	public @ResponseBody AbleResponseEntity<?> update(
			HttpServletRequest request,
			@PathVariable("adminSeq") long adminSeq,
			@RequestBody @Validated({AdminMngActiveVO.UpdateAction.class}) AdminMngActiveVO uploadActionVO, BindingResult error) {
		logger.debug("adminSeq: {}, vo: {}", adminSeq, uploadActionVO);
		if(error.hasErrors()) {
			logger.trace("error: {}", error);
			throw new AbleValidationException(error);
		}
		//회원이 존재하는지 체크
		AdminMngVO adminInfo = adminMngService.getUserDetailsByAdminSeq(adminSeq);
		AppUserDetails me = UserSecurityUtil.getCurrentUserDetails();
		if ( adminInfo == null || adminSeq != me.getAdminSeq().longValue() ){
			throw new AbleRuntimeException("수정하려는 회원이 존재하지 않습니다.");
		}

		adminMngService.update(uploadActionVO, adminInfo);
		return AbleResponseEntityBuilder.success(null);
	}
}
