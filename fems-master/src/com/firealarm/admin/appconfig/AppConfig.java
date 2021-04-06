package com.firealarm.admin.appconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import lombok.Data;

/**
 * 서버 Config - Properties에 대한 쉬운 Access 제공
 *
 * @author ovcoimf
 */
@Data
public class AppConfig implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

	private static AppConfig instance;
	private static void setInstance(AppConfig instance) { AppConfig.instance = instance; }
	public static AppConfig getInstance() {
		return instance;
	}

	@Value("${jdbc.driverClass}")
	private String jdbcDriverClass;
	@Value("${jdbc.url}")
	private String jdbcUrl;
	@Value("${jdbc.username}")
	private String jdbcUsername;
	@Value("${jdbc.password}")
	private String jdbcPassword;

	/** NAS경로. /nas경로 (Url공개) */
	@Value("${url.nasPath}")
	private String nasPath;
	/** NAS경로. /nas경로 (Url비공개) */
	@Value("${url.nasPrivatePath}")
	private String nasPrivatePath;
	/** NASURL. http://도메인/nas경로 */
	@Value("${url.nasUrl}")
	private String nasUrl;

	/** Schedule서비스 설정 */
	@Value("${fixeddelay.iot.logs}")
	private String fixeddelayIotLogs;
	@Value("${initialdelay.iot.logs}")
	private String initialdelayIotLogs;
	@Value("${cron.iot.delete.logs}")
	private String cronIotDeleteLogs;
	@Value("${serverip.iot.logs}")
	private String serveripIotLogs;

    /** 로그 목록 요청시 최대 응답 갯수(최근순) */
    @Value("${max.logcnt.per.process}")
    private int maxLogcntPerProcess;


    /** Aligo SMS 발송 정보 */
	@Value("${sms.apiKey}")
	private String smsApiKey;
	@Value("${sms.userId}")
	private String smsUserId;
	@Value("${sms.sendNo}")
	private String smsSendNo;
	@Value("${sms.sendTitle}")
	private String smsSendTitle;
	@Value("${sms.phoneno.lmscounterror}")
	private String smsPhonenoLmscounterror;

	@Override
	public void afterPropertiesSet() {
		//Holder설정 (static에서 접근시 사용)
		AppConfig.setInstance(this);

		//로그출력
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n===============================================");
		sb.append("\r\n- jdbc.driverClass: " + jdbcDriverClass);
		sb.append("\r\n- jdbc.url: " + jdbcUrl);
		sb.append("\r\n- jdbc.username: " + jdbcUsername);
		sb.append("\r\n");
		sb.append("\r\n- url.nasPath: " + nasPath);
		sb.append("\r\n- url.nasPrivatePath: " + nasPrivatePath);
		sb.append("\r\n- url.nasUrl: " + nasUrl);
		sb.append("\r\n");
		sb.append("\r\n- fixeddelay.iot.logs: " + fixeddelayIotLogs);
		sb.append("\r\n- initialdelay.iot.logs: " + initialdelayIotLogs);
		sb.append("\r\n- cron.iot.delete.logs: " + cronIotDeleteLogs);
		sb.append("\r\n- serverip.iot.logs: " + serveripIotLogs);
		sb.append("\r\n");
		sb.append("\r\n- max.logcnt.per.process: " + maxLogcntPerProcess);
		sb.append("\r\n");
		sb.append("\r\n- smsApiKey: " + smsApiKey);
		sb.append("\r\n- smsUserId: " + smsUserId);
		sb.append("\r\n- smsSendNo: " + smsSendNo);
		sb.append("\r\n- smsSendTitle: " + smsSendTitle);
		sb.append("\r\n- smsPhonenoLmscounterror: " + smsPhonenoLmscounterror);
		sb.append("\r\n===============================================");
		logger.info(sb.toString());
	}

}
