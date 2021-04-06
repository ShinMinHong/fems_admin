package com.firealarm.admin.systemschedule.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.firealarm.admin.biz.iot.service.IotService;
import com.firealarm.admin.common.support.ServiceSupport;

import framework.util.HostUtil;

@Service
public class IotScheduleService extends ServiceSupport {

	private static final List<String> SERVER_IP_LIST = HostUtil.getServerIpListInMultiNetworkCard();

	@Autowired IotService iotServcie;

	@Scheduled(fixedDelayString = "${fixeddelay.iot.logs}", initialDelayString = "${initialdelay.iot.logs}")
	public void scheduleIotGetLogs() {
		//Schedule 실행 IP조회 및 검사
		// 2020.12.09 rodem4
		// 최초 제공받았던 172.20.0.233가 포함인 경우만 스케줄러 기동에서
		// WEB서버를 1대만 사용하여 1대의 WEB서버가 배치를 돌리도록 제한 풀었음
		// 향후 web서버를 이중화 할 경우 다시 재한해야 함 주의 필요
		//if(SERVER_IP_LIST.contains(appConfig.getServeripIotLogs())) {
			logger.info("### SERVER_IP_LIST = {}", SERVER_IP_LIST);
			logger.info("### iot 이벤트 수집 스케쥴 실행 Start");
			iotServcie.processRecentNotConfirmIotLogs();
		//}
	}

//TODO : 프로젝트 완료 시점에 기기 로그 삭제로직 검토.
//	@Scheduled(cron = "${cron.iot.delete.logs}")	//0 1 * * * *
//	public void scheduleIotDeleteLogs() {
//		//Schedule 실행 IP조회 및 검사
//		if(SERVER_IP_LIST.contains(appConfig.getServeripIotLogs())) {
//			iotMngServcie.deleteIotLogs();
//		}
//	}
}
