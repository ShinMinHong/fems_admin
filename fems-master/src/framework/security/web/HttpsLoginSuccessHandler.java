package framework.security.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.firealarm.admin.common.service.UserService;
import com.firealarm.admin.security.vo.AppUserDetails;

public class HttpsLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired private UserService userService;

	protected Logger ableLogger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

		//로그 기록
		try {
			long adminSeq = ((AppUserDetails)authentication.getPrincipal()).getAdminSeq();
			userService.updateLoginLog(adminSeq);
		} catch (Exception ex) {
			ableLogger.warn("로그인 성공 로그 작성에 실패(HttpsLoginSuccessHandler > onAuthenticationSuccess)");
		}

		super.onAuthenticationSuccess(request, response, authentication);
	}
}