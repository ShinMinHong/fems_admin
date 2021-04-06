package spring;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

import com.mashape.unirest.http.Unirest;

import framework.unirest.UnirestObjectMapper;
import com.firealarm.admin.appconfig.AppConst;
import com.firealarm.admin.appconfig.AppProfiles;

@Configuration
public class GlobalConfig {
	private Logger logger = LoggerFactory.getLogger(GlobalConfig.class);
	AppProfiles appProfiles;

	public GlobalConfig(@Autowired AppProfiles appProfiles) {
		this.appProfiles = appProfiles;
		configUnirestGlobal();
	}

	public void configUnirestGlobal() {
		//Unirest 전역설정
		//필요시 동시성 설정: Unirest.setConcurrency(200,20);
		//필요시 타임아웃 설정: Unirest.setTimeouts(10000, 60000);
		UnirestObjectMapper unirestObjectMapper = new UnirestObjectMapper();
		Unirest.setObjectMapper(unirestObjectMapper);

		logger.info("### GlobalConfig Profile: {}", appProfiles.getActiveRuntimeProfile());

		//개발용 Proxy 설정
		if(appProfiles.isRuntimeLocal()) {
			HttpHost proxyHost = new HttpHost(AppConst.LOCALHOST_IP, 8888);
			logger.info("### Unirest.setProxy: {}", proxyHost);
			Unirest.setProxy(proxyHost);
		}
	}

	@EventListener({ContextClosedEvent.class})
    void contextRefreshedEvent() throws IOException {
		try {
			// Unirest starts a background event loop
			// and your Java application won't be able to exit
			// until you manually shutdown all the threads by invoking:
			Unirest.shutdown();
		} catch (IOException e) {
			logger.debug("{} - {}", e.getClass().getName(), e.getMessage(), logger.isTraceEnabled() ? e : null);
			throw e;
		}
    }

}
