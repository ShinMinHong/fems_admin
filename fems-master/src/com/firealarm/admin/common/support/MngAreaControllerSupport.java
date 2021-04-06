package com.firealarm.admin.common.support;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.firealarm.admin.appconfig.CodeMap.APP_USER_GRADE;
import com.firealarm.admin.security.exception.MngAreaAccessDeniedException;
import com.firealarm.admin.security.util.UserSecurityUtil;
import com.firealarm.admin.security.vo.AppUserDetails;

/**
 * 관제지역 하위에서 관리하는 페이지의 Controller
 *
 * 컨트롤러의 기본 로직을 포함
 *
 * @author ovcoimf
 */
public class MngAreaControllerSupport extends ControllerSupport {
	/**
	 * {@link MngAreaAccessDeniedException} handler
	 */
	@ExceptionHandler({ MngAreaAccessDeniedException.class })
    protected ModelAndView handleMngAreaAccessDeniedException(MngAreaAccessDeniedException e, WebRequest request) {
		String redirectUrl = "/logout";
		try {
			AppUserDetails user = UserSecurityUtil.getCurrentUserDetails();
			if( APP_USER_GRADE.HQ_ADMIN.equals(user.getRolegroupCode()) ) {
				redirectUrl = "redirect:/system/mngareamng";
			}
		} catch (Exception innerEx) {
			redirectUrl = "/logout";
		}
		return new ModelAndView(redirectUrl);
    }
}
