package com.firealarm.admin.common.support;

import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.firealarm.admin.appconfig.AppConfig;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

import framework.util.AbleUtil;

/**
 * Controller Support
 *
 * 컨트롤러의 기본 로직을 포함
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class ControllerSupport extends ComponentSupport {

	/** 서버 Properties 설정 접근자 */
	@Autowired protected AppConfig appConfig;

	/** MessageSource 접근자 */
	@Autowired protected MessageSource messageSource;

//	/** 로그인 되어 있는가? */
//	protected Boolean isAuthenticated() {
//		return UserSecurityUtil.isAuthenticated();
//	}
//
//	protected AppUserDetails getMe(){
//		return UserSecurityUtil.getCurrentUserDetails();
//	}

	/**
	 * 모든 Controller의 처리시 Model에 로그인 사용자 정보 추가
	 * @param me 로그인 사용자 정보
	 */
	@ModelAttribute()
	public void addAttribute(Model model, @AppLoginUser AppUserDetails me) {
		if(me != null) {
			model.addAttribute("me", me);
		} else {
			model.addAttribute("me", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		}
	}

	/**
	 * 에러 메시지 획득. 구분자:"\r\n"
	 * @param errors 에러정보 객체 (BindingResult등...)
	 * @param seperator 에러간 구분자
	 * @return 에러 메시지
	 */
	protected String buildErrorMessage(Errors errors) {
		return buildErrorMessage(errors, "\r\n");
	}

	/**
	 * 에러 메시지 획득. 구분자:"\\r\\n"
	 * @param errors 에러정보 객체 (BindingResult등...)
	 * @return 에러 메시지
	 */
	protected String buildJSErrorMessage(Errors errors) {
		return buildErrorMessage(errors, "\\n");
	}

	/**
	 * 에러 메시지 획득
	 */
	private String buildErrorMessage(Errors errors, String seperator) {
		String errorMessage = AbleUtil.buildErrorMessage(errors, messageSource, LocaleContextHolder.getLocale(), seperator);
		logger.debug("errorMessage: {}", errorMessage);
		return errorMessage;
	}

	/**
	 * 모델의 내용을 복사한다.
	 */
	protected void copyModel(Model srcModel, Model destModel) {
		destModel.addAllAttributes(srcModel.asMap().entrySet());
	}

	/**
	 * 모델의 내용을 Flash속성으로 복사한다.
	 */
	protected void copyModelToFlashAttributes(Model srcModel, RedirectAttributes destModel) {
		Set<Entry<String, Object>> entrySet = srcModel.asMap().entrySet();
		for (Entry<String, Object> entry : entrySet) {
			destModel.addFlashAttribute(entry.getKey(), entry.getValue());
		}
	}
}
