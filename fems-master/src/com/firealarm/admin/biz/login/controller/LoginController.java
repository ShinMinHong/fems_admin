package com.firealarm.admin.biz.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.firealarm.admin.appconfig.AppConst;
import com.firealarm.admin.common.support.ControllerSupport;
import com.firealarm.admin.security.exception.InitPasswordException;

/**
 * Login 컨트롤러
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
@Controller
@RequestMapping("/login")
public class LoginController extends ControllerSupport {

	@Autowired protected AuthenticationManager authenticationManager;

	@RequestMapping(value="", method = RequestMethod.GET)
	public ModelAndView loginform(HttpServletRequest request, Model model) {

		ModelAndView mv = new ModelAndView();

		HttpSession session = request.getSession(false);

		if (session != null && session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) != null ) {

			AuthenticationException ex = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
			String tryUserName = "";
			String errorMsg = ex.getMessage();

			if( ex instanceof InitPasswordException ) {
				mv.addObject("loginFailType", "InitPasswordException");
				tryUserName = (String)session.getAttribute(AppConst.AUTH_TEMPNAME_FOR_USERNAME);
			} else if( ex instanceof CredentialsExpiredException ) {
				mv.addObject("loginFailType", "CredentialsExpiredException");
				tryUserName = (String)session.getAttribute(AppConst.AUTH_TEMPNAME_FOR_USERNAME);
			}

			errorMsg = errorMsg.replace("\"", "").replace("'", "").replace("\r", "</br>").replace("\n", "</br>");

			mv.addObject("errorMsg", errorMsg);
			mv.addObject("tryUserName", tryUserName);

			//flashAttribute는 redirect만 지원함으로 에러메세지 생성 이후에 세션에서 에러 메세지 삭제 처리
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
			session.removeAttribute(AppConst.AUTH_TEMPNAME_FOR_USERNAME);
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();

		if(principal instanceof User) {
			return new ModelAndView("redirect:/");
		} else {
			mv.setViewName("login/loginform");
			return mv;
		}
	}

}