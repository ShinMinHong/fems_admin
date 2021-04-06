package com.firealarm.admin.systemschedule.dao;

import org.springframework.stereotype.Repository;

import com.firealarm.admin.common.support.DAOSupport;

import framework.util.AbleUtil;

@Repository
public class StatScheduleDAO extends DAOSupport {

	/**
	 * 일별 화재감지기 이벤트 통계 함수 실행
	 * @return
	 */
	public String execFireDetectorEventStatsFunc() {
		return sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName());
	}
}
