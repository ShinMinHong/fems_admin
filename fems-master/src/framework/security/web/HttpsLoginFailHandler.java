package framework.security.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.firealarm.admin.appconfig.AppConst;

public class HttpsLoginFailHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException ex) throws IOException, ServletException {

		request.getSession().setAttribute(AppConst.AUTH_TEMPNAME_FOR_USERNAME, request.getParameter("username"));
        super.onAuthenticationFailure(request, response, ex);
	}


}