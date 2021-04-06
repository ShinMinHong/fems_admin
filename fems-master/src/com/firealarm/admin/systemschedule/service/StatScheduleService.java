package com.firealarm.admin.systemschedule.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.systemschedule.dao.StatScheduleDAO;

import framework.util.HostUtil;

@Service
public class StatScheduleService extends ServiceSupport {

	private static final List<String> SERVER_IP_LIST = HostUtil.getServerIpListInMultiNetworkCard();

	@Autowired StatScheduleDAO statScheduleDAO;

	@Scheduled(cron = "${cron.firedetector.event.stats}")	//0 0 2 * * *
	public void scheduleFireDetectorEventStats() {
		//Schedule 실행 IP조회 및 검사
		// 2020.12.09 rodem4
		// 최초 제공받았던 172.20.0.233가 포함인 경우만 스케줄러 기동에서
		// WEB서버를 1대만 사용하여 1대의 WEB서버가 배치를 돌리도록 제한 풀었음
		// 향후 web서버를 이중화 할 경우 다시 재한해야 함 주의 필요
		//if(SERVER_IP_LIST.contains(appConfig.getServeripIotLogs())) {
			logger.info("### SERVER_IP_LIST = {}", SERVER_IP_LIST);
			logger.info("### 화재감지기 이벤트 통계 생성 스케쥴 실행 Start");
			String result = statScheduleDAO.execFireDetectorEventStatsFunc();
			logger.info("### 화재감지기 이벤트 통계 생성 스케쥴 실행 End. Result:{}", result);
		//}
	}
}
