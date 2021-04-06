package com.firealarm.admin.biz.firedetector.lowbatterydetector.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.firealarm.admin.biz.firedetector.lowbatterydetector.dao.LowBatteryDetectorDAO;
import com.firealarm.admin.biz.firedetector.lowbatterydetector.vo.LowBatteryDetectorVO;
import com.firealarm.admin.common.support.ServiceSupport;

import framework.vo.SearchMap;


/**
 * 배터리부족 단말기 조회 Service
 * @author rodem4_pc1
 */
@Service
public class LowBatteryDetectorService extends ServiceSupport {

	@Autowired LowBatteryDetectorDAO lowBatteryDetectorDAO;


	/** 전체 목록 조회 */
	public List<LowBatteryDetectorVO> getListAll(Sort sort, SearchMap search) {
		return lowBatteryDetectorDAO.getListAll(sort, search);
	}

	/** 페이징 목록 조회 */
	public Page<LowBatteryDetectorVO> getList(Pageable pageable, SearchMap search) {
		return lowBatteryDetectorDAO.getList(pageable, search);
	}

}
