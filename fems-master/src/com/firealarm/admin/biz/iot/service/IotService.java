package com.firealarm.admin.biz.iot.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firealarm.admin.appconfig.AppConfig;
import com.firealarm.admin.appconfig.AppProfiles;
import com.firealarm.admin.appconfig.CodeMap.DETECTOR_SIGNAL_TYPE;
import com.firealarm.admin.biz.iot.dao.IotDAO;
import com.firealarm.admin.biz.iot.vo.DetectorInstallInfoDT;
import com.firealarm.admin.common.service.AdminService;
import com.firealarm.admin.common.service.DemonLogService;
import com.firealarm.admin.common.service.F119SendLogService;
import com.firealarm.admin.common.service.FireDetectorLogService;
import com.firealarm.admin.common.service.FireDetectorNowStatusService;
import com.firealarm.admin.common.service.SmsAdminService;
import com.firealarm.admin.common.service.SmsSendLogService;
import com.firealarm.admin.common.service.SmsSendService;
import com.firealarm.admin.common.service.StoreNoAlarmService;
import com.firealarm.admin.common.service.StoreSmsUserService;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.common.vo.FireDetectorLogDT;
import com.firealarm.admin.common.vo.FireDetectorNowStatusDT;
import com.firealarm.admin.common.vo.IotDemonLogDT;
import com.firealarm.admin.common.vo.SmsAdminDT;
import com.firealarm.admin.common.vo.SmsRemainResultDT;
import com.firealarm.admin.common.vo.SmsSendDT;
import com.firealarm.admin.common.vo.SmsSendLogDT;
import com.firealarm.admin.common.vo.StoreNoAlarmDT;
import com.firealarm.admin.common.vo.StoreSmsUserDT;
import com.firealarm.admin.extlibrary.fire119.vo.Fire119SendVO;
import com.firealarm.admin.security.vo.AdminDT;

@Service
public class IotService extends ServiceSupport {

	@Autowired SmsSendService smsSendService;

	public static final String SMS_LINE_BREAK = System.lineSeparator();
	public static final String SMS_FIRE_TITLE = AppConfig.getInstance().getSmsSendTitle();
	public static final String F119_SEND_TYPE = AppConfig.getInstance().getF119SendType();
	public static final String SMS_LMS_REMAIN_WARN_TITLE = "지능형 화재감지기 LMS 부족 알림";

	@Autowired IotDAO iotDAO;
	@Autowired DemonLogService demonLogService;
	@Autowired StoreNoAlarmService storeNoAlarmService;
	@Autowired FireDetectorLogService fireDetectorLogService;
	@Autowired FireDetectorNowStatusService fireDetectorNowStatusService;
	@Autowired StoreSmsUserService storeSmsUserService;
	@Autowired SmsSendLogService smsSendLogService;
	@Autowired F119SendLogService f119SendLogService;
	@Autowired AdminService adminService;
	@Autowired SmsAdminService smsAdminService;

	public void processRecentNotConfirmIotLogs() {
		//미처리 로그 조회
		List<IotDemonLogDT> recentNotConfirmList = demonLogService.getRecentNotConfirmList(appConfig.getMaxLogcntPerProcess());

		//기존코드.
		//recentNotConfirmList.stream().collect(Collectors.groupingBy(IotDemonLogDT::getCtnNo)).forEach(this::processIotLog)

		if (!recentNotConfirmList.isEmpty()) {
			int sumSendSmsCount = 0;
			//효율성을 위해, Ctn으로 Group을 묶어 후속 업무 진행.
			Map<String, List<IotDemonLogDT>> demonLogGroupsByCtnNo = recentNotConfirmList.stream().collect(Collectors.groupingBy(IotDemonLogDT::getCtnNo));

			//통합관리자에 대한 SMS 발송 대상 데이터 조회
			List<AdminDT> smsRecieveHqAdmins = adminService.getSmsRecieveHqAdmin();
			List<String> tmpHqPhoneNos = smsRecieveHqAdmins.stream().map(c->c.getPhoneNo().replaceAll("[^0-9]", "")).collect(Collectors.toList());

			//통합SMS관리자에 대한 SMS 발송 대상 데이터 조회
			List<SmsAdminDT> smsRecieveHqSmsAdmins = smsAdminService.getSmsRecieveHqSmsAdmin();
			List<String> tmpHqSmsPhoneNos = smsRecieveHqSmsAdmins.stream().map(c->c.getPhoneNo().replaceAll("[^0-9]", "")).collect(Collectors.toList());

			for (Map.Entry<String, List<IotDemonLogDT>> entry : demonLogGroupsByCtnNo.entrySet()) {
				int sendSmsCount = processIotLog(entry.getKey(), entry.getValue(), smsRecieveHqAdmins, tmpHqPhoneNos, smsRecieveHqSmsAdmins, tmpHqSmsPhoneNos);
				sumSendSmsCount = sumSendSmsCount + sendSmsCount;
			}

			//SMS 발송 한 경우, 잔여 SMS 조회 API 호출 및 관리자에게 SMS 발송
			if(sumSendSmsCount > 0) {
				SmsRemainResultDT smsRemainResult = smsSendService.checkRemainSmsCnt();
				if(smsRemainResult.getLmsCnt() < 100) {
			    	SmsSendDT smsSendInfo = SmsSendDT.builder()
			    			.receiver(appConfig.getSmsPhonenoLmscounterror())
			    			.title(SMS_LMS_REMAIN_WARN_TITLE)
			    			.msg("LMS 잔여수량이 부족합니다. 잔여수량:"+smsRemainResult.getLmsCnt())
			    			.testmodeYn("N")
			    			.build();
			    	smsSendService.sendSms(smsSendInfo);
				}
			}
		} else {
			logger.info("::::::::::::::::::::::::::: DemonLog 없음.");
		}
	}

	/**
	 * 각각의 단말기 별(DeviceEntityId로 구분), 화재수신기 상태 업무 수행
	 * @param ctnNo 단말기 Ctn번호
	 * @param iotDemonLogList 데몬 신호 목록
	 * @param smsRecieveHqAdmins SMS수신 통합관리자 목록
	 * @param tmpHqPhoneNos SMS수신 통합관리자 전화번호 목록
	 * @param smsRecieveHqSmsAdmins SMS수신 통합 SMS 관리자 목록
	 * @param tmpHqSmsPhoneNos SMS수신 통합 SMS 관리자 전화번호 목록
	 * @return SMS 발송 개수
	 */
	private int processIotLog(String ctnNo, List<IotDemonLogDT> iotDemonLogList
			, List<AdminDT> smsRecieveHqAdmins, List<String> tmpHqPhoneNos
			, List<SmsAdminDT> smsRecieveHqSmsAdmins, List<String> tmpHqSmsPhoneNos) {
		int sendSmsCnt = 0;
		try {
			Collections.sort(iotDemonLogList);

			//1. 최종 상태값 저장
			IotDemonLogDT lastIotDemonLog = iotDemonLogList.get(iotDemonLogList.size()-1);
			FireDetectorLogDT firstFireEventLog = null;
			FireDetectorLogDT firstFire119EventLog = null;
			FireDetectorLogDT firstFireCancelEventLog = null;

			//2. 해당 Detector의 기본정보 식별(화재감지기, 설치정보) 수급
			DetectorInstallInfoDT detectorInstallInfo = iotDAO.getDetectorAndInstallInfoByCtnNo(ctnNo);
			if(detectorInstallInfo == null) {
				logger.warn("데몬 수신 로그 처리 중, 화재감지기 및 설치정보 조회 실패. ctnNo: {}", ctnNo);
				return 0;
			}
			long storeSeq = detectorInstallInfo.getStoreSeq();

			//3. 해당 Store의 비화재보 정보 수급
			StoreNoAlarmDT storeNoAlarmInfo = storeNoAlarmService.getByStoreSeq(storeSeq);

			//4. 순환. 데몬로그를 관재시스템 화재감지기 로그 형태로 변환하고, 비화재보 감지하고, 화재 이벤트 전송여부 판단.
			List<FireDetectorLogDT> insertLogList = new ArrayList<>();
			boolean hasFireEvent = false; //화재가 발생하였는지 기록
			boolean hasFire119Event = false; //119전송대상 화재가 발생하였는지 기록
			boolean hasFireCancelEvent = false;
			for(IotDemonLogDT iotDemonLog : iotDemonLogList) {
				//4-1. 비화재보 설정. 아래에서 화재이고, 비화재보 설정시간이면 true로 변경.
				boolean notFireYn = false;
				FireDetectorLogDT tmpFireDetectorLog = FireDetectorLogDT.fromIotDemonLogDT(iotDemonLog, detectorInstallInfo, notFireYn);
				if(iotDemonLog.isFireEvent()) {
					notFireYn = storeNoAlarmInfo.isNoAlarmEvent(iotDemonLog.getDemonRegDate());

					//4-1-1. 화재(isFireEvent) 이고 최초화재(!hasFireEvent) 이고 비화재보가 아니면(!notFireYn)
					if(!hasFireEvent && !notFireYn) {
						firstFireEventLog = tmpFireDetectorLog;
						hasFireEvent = true;
					}

					//4-1-2. 119전송 화재 판단 로직
					//	화재(isFireEvent) 이고 최초119화재(!hasFire119Event) 이고 비화재보가 아니면(!notFireYn)
					//	이고 (연기화재도 전송 || (연기화재는 전송안함 이고 연기이외 화재))
					if(!hasFire119Event && !notFireYn && (detectorInstallInfo.isSmokeAlarmYn() || (!detectorInstallInfo.isSmokeAlarmYn() && !iotDemonLog.isFireEventByOtherThanSmoke()))) {
						firstFire119EventLog = tmpFireDetectorLog;
						hasFire119Event = true;
					}
				}

				//4-2. 통합관리자, 통합화재 SMS 수신자에게 전송하기 위한 화재 알림 해제 이벤트 식별.
				if(!hasFireCancelEvent && DETECTOR_SIGNAL_TYPE.CANCEL.equals(tmpFireDetectorLog.getSignalType())) {
					firstFireCancelEventLog = tmpFireDetectorLog;
					hasFireCancelEvent = true;
				}

				//4-3. 전문 변환
				insertLogList.add(tmpFireDetectorLog);
			}

			//5. 로그 저장.
			fireDetectorLogService.insertAll(insertLogList);

			//6. 최종 상태 저장
			boolean lastStatusNotFireYn = storeNoAlarmInfo.isNoAlarmEvent(lastIotDemonLog.getDemonRegDate());
			FireDetectorNowStatusDT lastDetectorNowStatus = FireDetectorNowStatusDT.fromIotDemonLogDT(lastIotDemonLog, detectorInstallInfo, lastStatusNotFireYn);
			fireDetectorNowStatusService.update(lastDetectorNowStatus);

			//7. 화재 발생이 감지되었고 해당 상점이 SMS 문자 알림 여부를 설정해 둔 경우, SMS 화재 알림 전송.
			if(hasFireEvent && firstFireEventLog!=null && detectorInstallInfo.isSmsAlarmYn()) {
				//관제지역 설정의 중복 방지 시간내에 발송한 SMS가 있는지 체크
				boolean hasSmsInDuration = smsSendLogService.hasSmsInDuration(storeSeq, detectorInstallInfo.getNoAlarmTime());

				if(!hasSmsInDuration) {
					//화재 알림 문자 생성
			    	StringBuilder sb = new StringBuilder();
			    	sb.append("["+SMS_FIRE_TITLE+"]").append(SMS_LINE_BREAK);
			    	sb.append(" ▶ 화재 종류 : "+firstFireEventLog.getFireEventString()).append(SMS_LINE_BREAK);
			    	sb.append(" ▶ 주   소: ("+detectorInstallInfo.getZipCode()+") "+detectorInstallInfo.getRoadAddress()).append(SMS_LINE_BREAK);
			    	sb.append(" ▶ 대표자 : "+detectorInstallInfo.getManagerName()).append(SMS_LINE_BREAK);
			    	sb.append(" ▶ 점포명 : "+detectorInstallInfo.getStoreName()+"("+detectorInstallInfo.getInstallPlace()+")").append(SMS_LINE_BREAK);
			    	sb.append(" ▶ 연락처 : "+detectorInstallInfo.getPhoneNo()).append(SMS_LINE_BREAK);
			    	sb.append(" ▶ 감지기CTN : "+detectorInstallInfo.getCtnNo()).append(SMS_LINE_BREAK);
			    	if(StringUtils.isNotBlank(detectorInstallInfo.getSmsAddMessage())) {
				    	sb.append(" ▶ 추가사항").append(SMS_LINE_BREAK);
				    	sb.append(detectorInstallInfo.getSmsAddMessage());
			    	}

					List<SmsSendLogDT> smsSendLogList = new ArrayList<>();
					List<String> smsSendPhoneNos = new ArrayList<>();
					String smsMessage = sb.toString();

					if(detectorInstallInfo.isAlarmStore()) {
						List<StoreSmsUserDT> receiveAcceptUsers = storeSmsUserService.getReceiveAcceptListByStoreSeq(storeSeq);
						List<String> tmpPhoneNos = receiveAcceptUsers.stream().map(c->c.getPhoneNo().replaceAll("[^0-9]", "")).collect(Collectors.toList());
						smsSendPhoneNos.addAll(tmpPhoneNos);
						List<SmsSendLogDT> tmpSmsSendLogs = receiveAcceptUsers.stream().map(c->SmsSendLogDT.fromStoreSmsUserDTAndInstallInfo(c, detectorInstallInfo, SMS_FIRE_TITLE, smsMessage)).collect(Collectors.toList());
						smsSendLogList.addAll(tmpSmsSendLogs);
					}

					if(detectorInstallInfo.isAlarmMarket()) {
						List<AdminDT> smsRecieveMarketAdmins = adminService.getSmsRecieveMarketAdminByMngAreaSeq(detectorInstallInfo.getMarketSeq());
						List<String> tmpPhoneNos = smsRecieveMarketAdmins.stream().map(c->c.getPhoneNo().replaceAll("[^0-9]", "")).collect(Collectors.toList());
						smsSendPhoneNos.addAll(tmpPhoneNos);
						List<SmsSendLogDT> tmpSmsSendLogs = smsRecieveMarketAdmins.stream().map(c->SmsSendLogDT.fromAdminDTAndInstallInfo(c, detectorInstallInfo, SMS_FIRE_TITLE, smsMessage)).collect(Collectors.toList());
						smsSendLogList.addAll(tmpSmsSendLogs);
					}

					if(detectorInstallInfo.isAlarmArea()) {
						List<AdminDT> smsRecieveMngAreaAdmins = adminService.getSmsRecieveMngAreaAdminByMngAreaSeq(detectorInstallInfo.getMngAreaSeq());
						List<String> tmpPhoneNos = smsRecieveMngAreaAdmins.stream().map(c->c.getPhoneNo().replaceAll("[^0-9]", "")).collect(Collectors.toList());
						smsSendPhoneNos.addAll(tmpPhoneNos);
						List<SmsSendLogDT> tmpSmsSendLogs = smsRecieveMngAreaAdmins.stream().map(c->SmsSendLogDT.fromAdminDTAndInstallInfo(c, detectorInstallInfo, SMS_FIRE_TITLE, smsMessage)).collect(Collectors.toList());
						smsSendLogList.addAll(tmpSmsSendLogs);
					}

					//통합관리자 SMS 발송여부
					if (CollectionUtils.isNotEmpty(tmpHqPhoneNos)) {
						smsSendPhoneNos.addAll(tmpHqPhoneNos);
						List<SmsSendLogDT> tmpSmsSendLogs = smsRecieveHqAdmins.stream().map(c -> SmsSendLogDT
								.fromAdminDTAndInstallInfo(c, detectorInstallInfo, SMS_FIRE_TITLE, smsMessage))
								.collect(Collectors.toList());
						smsSendLogList.addAll(tmpSmsSendLogs);
					}

					//통합SMS관리자 SMS 발송여부
					if (CollectionUtils.isNotEmpty(tmpHqSmsPhoneNos)) {
						smsSendPhoneNos.addAll(tmpHqSmsPhoneNos);
						List<SmsSendLogDT> tmpSmsSendLogs = smsRecieveHqSmsAdmins.stream().map(c -> SmsSendLogDT
								.fromSmsAdminDTAndInstallInfo(c, detectorInstallInfo, SMS_FIRE_TITLE, smsMessage))
								.collect(Collectors.toList());
						smsSendLogList.addAll(tmpSmsSendLogs);
					}

					if(CollectionUtils.isNotEmpty(smsSendPhoneNos)) {
						//발송 로그 기록
						logger.debug("Insert Sms Log. data:{}", smsSendLogList);
						smsSendLogService.insertAll(smsSendLogList);

						//SMS 발송
						String phoneNosAsString = String.join(",", smsSendPhoneNos);
				    	SmsSendDT smsSendInfo = SmsSendDT.builder()
				    			.receiver(phoneNosAsString)
				    			.title(SMS_FIRE_TITLE)
				    			.msg(smsMessage)
				    			.testmodeYn("N")
				    			.build();
				    	smsSendService.sendSms(smsSendInfo);
				    	sendSmsCnt += smsSendPhoneNos.size();
					}
				}
			}

			//7-1. 화재 알림 해제가 된 경우에는 통합관리자, 통합SMS관리자에게 문자 전송
			if(hasFireCancelEvent && firstFireCancelEventLog!=null && detectorInstallInfo.isSmsAlarmYn()) {
				//화재 알림 문자 생성
		    	StringBuilder sb = new StringBuilder();
		    	sb.append("[화재 경보해지 알람]").append(SMS_LINE_BREAK);
		    	sb.append(" ▶ 주   소: ("+detectorInstallInfo.getZipCode()+") "+detectorInstallInfo.getRoadAddress()).append(SMS_LINE_BREAK);
		    	sb.append(" ▶ 대표자 : "+detectorInstallInfo.getManagerName()).append(SMS_LINE_BREAK);
		    	sb.append(" ▶ 점포명 : "+detectorInstallInfo.getStoreName()+"("+detectorInstallInfo.getInstallPlace()+")").append(SMS_LINE_BREAK);
		    	sb.append(" ▶ 연락처 : "+detectorInstallInfo.getPhoneNo()).append(SMS_LINE_BREAK);
		    	sb.append(" ▶ 감지기CTN : "+detectorInstallInfo.getCtnNo()).append(SMS_LINE_BREAK);

				List<String> smsSendPhoneNos = new ArrayList<>();
				String smsMessage = sb.toString();

				//통합관리자 SMS 발송여부
				if (CollectionUtils.isNotEmpty(tmpHqPhoneNos)) {
					smsSendPhoneNos.addAll(tmpHqPhoneNos);
				}

				//통합SMS관리자 SMS 발송여부
				if (CollectionUtils.isNotEmpty(tmpHqSmsPhoneNos)) {
					smsSendPhoneNos.addAll(tmpHqSmsPhoneNos);
				}

				if(CollectionUtils.isNotEmpty(smsSendPhoneNos)) {
					//SMS 발송
					String phoneNosAsString = String.join(",", smsSendPhoneNos);
			    	SmsSendDT smsSendInfo = SmsSendDT.builder()
			    			.receiver(phoneNosAsString)
			    			.title("화재 경보해지 알람")
			    			.msg(smsMessage)
			    			.testmodeYn("N")
			    			.build();
			    	smsSendService.sendSms(smsSendInfo);

			    	sendSmsCnt += smsSendPhoneNos.size();
				}
			}

			// 2021.01.25 SHH
			// 119다매체 연동 조건 추가 : 기존 (운영서버인 경우만), 변경 (운영서버 또는 개발서버인 경우)
			//8. 화재 발생이 감지되었고, 해당 상점이 119다매체 전달설정 true 경우, 운영기인 경우 메시지 연동.
			if(hasFire119Event && firstFire119EventLog !=null
					&& detectorInstallInfo.isFirestationAlarmYn()
					&& AppProfiles.getInstance().isRuntimeProduction()
					) {
				Fire119SendVO sendData = new Fire119SendVO();
				if (F119_SEND_TYPE.equals("T")) {
					sendData.setSttemntCn("신고 테스트입니다." + detectorInstallInfo.getRoadAddress());
					//sendData.setSttemntCn("IOT신고 " + detectorInstallInfo.getRoadAddress()
					//			+ ". 화재유형:" + firstFireEventLog.getFireEventString()
					//			+ ". 점포명:" + detectorInstallInfo.getStoreName()
					//			+ ". 단말기CTN:" + detectorInstallInfo.getCtnNo());
					sendData.setCallName("퀘스타정보");
					sendData.setCallTel(StringUtils.remove(appConfig.getSmsSendNo(), '-'));
					sendData.setAplcntNm("퀘스타정보");
					sendData.setAplcntTelno(StringUtils.remove(appConfig.getSmsSendNo(), '-'));
					sendData.setCtrdCode(detectorInstallInfo.getCtrdCode());
					sendData.setSignguCode(detectorInstallInfo.getSignguCode());
					sendData.setDongCode(detectorInstallInfo.getDongCode());
					sendData.setLiCode(detectorInstallInfo.getLiCode());
					sendData.setLocation(detectorInstallInfo.getCtrdCode()+"000");
				} else {
					sendData.setSttemntCn("IOT신고 " + detectorInstallInfo.getRoadAddress()
					+ ". 화재유형:" + firstFireEventLog.getFireEventString()
					+ ". 점포명:" + detectorInstallInfo.getStoreName()
					+ ". 단말기CTN:" + detectorInstallInfo.getCtnNo());
					sendData.setCallName("퀘스타정보");
					sendData.setCallTel(StringUtils.remove(appConfig.getSmsSendNo(), '-'));
					sendData.setAplcntNm("퀘스타정보");
					sendData.setAplcntTelno(StringUtils.remove(appConfig.getSmsSendNo(), '-'));
					sendData.setCtrdCode(detectorInstallInfo.getCtrdCode());
					sendData.setSignguCode(detectorInstallInfo.getSignguCode());
					sendData.setDongCode(detectorInstallInfo.getDongCode());
					sendData.setLiCode(detectorInstallInfo.getLiCode());
					sendData.setLocation(detectorInstallInfo.getCtrdCode()+"000");
				}

				try {
					if(sendData.hasFire119AreaCode()) {
						sendXmlDataToFire119(sendData, detectorInstallInfo);
					} else {
						logger.warn("119 다매체 전송 시. Market에 주소코드 기본정보 없음. data:{}", sendData);
					}
				} catch(Exception ex) {
					logger.warn("119 다매체 전송 모듈(sendXmlDataToFire119) 호출 시 오류. sendData:{}", ex);
				}
			}
		} catch (Exception e) {
			logger.warn("Ctn번호별로 processIotLog 진행시에 오류 발생: {} - {}", e.getClass().getName(), e.getMessage(), e);
		}
		return sendSmsCnt;
	}

	private static final String NFA_URL = "http://125.60.28.118:80/wds/web.service";
	public void sendXmlDataToFire119(Fire119SendVO sendData, DetectorInstallInfoDT detectorInstallInfo) throws IOException {

		logger.debug("119 다매체 전송 시도. sendData:{}", sendData);
		long insertedF119SendLogSeq = 0;
		try {
			insertedF119SendLogSeq = f119SendLogService.insert(sendData, detectorInstallInfo);
		} catch (Exception e) {
			logger.warn("119 다매체 전송 Log Insert 에러. sendData:{}", sendData, e);
		}

		StringBuilder sbResult = new StringBuilder();

		//전송 XML 데이터 생성.
		String sendDataString = null;
		try {
			sendDataString = sendData.getEncodedXmlString();
		} catch (Exception e) {
			logger.warn("119 다매체 전송 XML 문자열 생성 에러. sendData:{}", sendData, e);
			return;
		}

		logger.debug("119 다매체 전송 문자열: {}", sendDataString);

		//XML 전송
		URL url = new URL(AppProfiles.getInstance().isRuntimeProduction()?NFA_URL:"");
		HttpURLConnection huc = (HttpURLConnection) url.openConnection();
		huc.setRequestMethod("POST");
		huc.setDoOutput(true);
		huc.setRequestProperty("Content-Type", "application/xml");

		try(
			DataOutputStream dos = new DataOutputStream(huc.getOutputStream());
		) {
			dos.write(sendDataString.getBytes());
		} catch (Exception e) {
			logger.warn("119 다매체 전송 Request 오류. sendData:{}", sendData, e);
			return;
		}

		//수신 데이터 확인
		try(
			BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
		) {
			String str = "";
			while ((str = br.readLine()) != null) {
				sbResult.append(str);
			}

			String resultString = sbResult.toString();
			logger.debug("119 다매체 전송 결과. receiveData:{}", resultString);

			if(insertedF119SendLogSeq > 0) {
				f119SendLogService.updateResult(insertedF119SendLogSeq, resultString);
			}
		} catch (Exception ex) {
			logger.warn("119 다매체 전송 데이터 파싱 및 Log DB Update 오류. sendData:{}", sendData, ex);
		}

		huc.disconnect();
	}

	public void deleteIotLogs() {
		return;
	}
}
