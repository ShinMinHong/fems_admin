package com.firealarm.admin.security.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.firealarm.admin.biz.areasystem.marketmng.service.MarketMngService;
import com.firealarm.admin.biz.areasystem.marketmng.vo.MarketMngVO;
import com.firealarm.admin.biz.system.mngareamng.service.MngAreaMngService;
import com.firealarm.admin.biz.system.mngareamng.vo.MngAreaMngVO;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.security.common.AppGrantedAuthority;
import com.firealarm.admin.security.dao.AppUserDAO;
import com.firealarm.admin.security.vo.AdminDT;
import com.firealarm.admin.security.vo.AppUserDetails;

/**
 * 스프링 시큐리티 UserDetailsService
 * @author Min ByeongDon <deepfree@gmail.com>
 *
 */
@Service
public class AppUserDetailsService extends ServiceSupport implements UserDetailsService  {

	@Autowired AppUserDAO adminDAO;

	@Autowired MngAreaMngService mngAreaMngService;
	@Autowired MarketMngService marketMngService;

	@Override
	public UserDetails loadUserByUsername(String adminId) {
		AdminDT user = adminDAO.findAdminById(adminId);
		if(user == null) {
			throw new UsernameNotFoundException("adminId '"+adminId+"' cannot found.");
		}

		//관리자권한 목록
		List<String> roles = adminDAO.findAdminRoleCodesById(adminId);
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (String role : roles) {
			AppGrantedAuthority appAuthority = new AppGrantedAuthority(role);
			if(!StringUtils.isEmpty(appAuthority.getAuthority())) {
				authorities.add(appAuthority);
			}
		}

		//계정잠금관련
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;

		String mngAreaName = "";
		String marketName = "";

		if(user.getMngAreaSeq() != null) {
			MngAreaMngVO mngAreaInfo = mngAreaMngService.getByMngAreaSeq(user.getMngAreaSeq());
			mngAreaName = mngAreaInfo.getMngAreaName();
		}

		if(user.getMarketSeq() != null) {
			MarketMngVO marketMngActiveVO = marketMngService.getByMarketSeq(user.getMarketSeq());
			marketName = marketMngActiveVO.getMarketName();
		}

		//로그인 객체
		return new AppUserDetails(user, mngAreaName, marketName, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

}
