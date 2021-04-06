package com.firealarm.admin.security.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.firealarm.admin.appconfig.CodeMap.APP_USER_GRADE;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.security.util.UserSecurityUtil;
import com.firealarm.admin.security.vo.AppUserDetails;

/**
 * @author ovcoi
 *
 */
@Service
@Component("fireAlarmCustomSecurityService")
public class FireAlarmCustomSecurityService extends ServiceSupport {
	public boolean hasMngArea() {
		AppUserDetails user = UserSecurityUtil.getCurrentUserDetails();
		return user.hasMngArea();
	}

	/*마켓 관리자인지 체크*/
	public boolean IsMarketAdmin() {
		AppUserDetails user = UserSecurityUtil.getCurrentUserDetails();
		if (user == null) {
			return false;
		}
		return APP_USER_GRADE.MARKET_ADMIN.equals(user.getRolegroupCode());
	}

}
