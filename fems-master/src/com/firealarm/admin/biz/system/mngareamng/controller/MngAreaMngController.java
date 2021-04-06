package com.firealarm.admin.biz.system.mngareamng.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.firealarm.admin.biz.system.mngareamng.service.MngAreaMngService;
import com.firealarm.admin.common.support.ControllerSupport;
import com.firealarm.admin.security.annotation.AppLoginUser;
import com.firealarm.admin.security.vo.AppUserDetails;

/**
 * 관제지역관리 Controller
 * @author ovcoimf
 *
 */
@Controller
@RequestMapping(value = "/system/mngareamng")
@PreAuthorize("hasAuthority('ROLE_SYSTEM_ADMIN')")
public class MngAreaMngController extends ControllerSupport {

	@Autowired MngAreaMngService mngAreaMngService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model, @AppLoginUser AppUserDetails user) {
		return "system/mngareamng/mngareamng";
	}

	/** HQ관리자 관제지역 선택 */
	@PreAuthorize("hasAuthority('ROLE_SYSTEM_ADMIN')")
	@RequestMapping(value = "/clearmngarea", method = RequestMethod.GET)
	public ModelAndView clearMngArea(Model model, @AppLoginUser AppUserDetails user) {
		user.setMngAreaSeq(null);
		user.setMngAreaName(null);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/system/mngareamng");
		return mv;
	}
}