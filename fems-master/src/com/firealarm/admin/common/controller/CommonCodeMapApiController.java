package com.firealarm.admin.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.firealarm.admin.common.service.CommonCodeMapService;
import com.firealarm.admin.common.support.ApiControllerSupport;

@Controller
@RequestMapping(value = "/commoncodemap/api")
public class CommonCodeMapApiController extends ApiControllerSupport {

	@Autowired CommonCodeMapService commonCodeMapService;

}
