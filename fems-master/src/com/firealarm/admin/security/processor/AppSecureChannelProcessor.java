package com.firealarm.admin.security.processor;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.channel.SecureChannelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * WAS에서 프로토콜로 SSL구분이 불가능한 경우
 * x-forwarded-proto 헤더정보로 SSL 구분 처리를 위한 Custom ChannelProcessor
 * @author impjs <impjs@ablecoms.com>
 */
@Component
public class AppSecureChannelProcessor extends SecureChannelProcessor {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	protected final static String X_FORWARDED_PROTO = "x-forwarded-proto";

	@Override
	public void decide(FilterInvocation invocation, Collection<ConfigAttribute> config) throws IOException, ServletException {
		logger.debug("***** AppSecureChannelProcessor decide  ==> invocation : {}, config : {}", invocation, config);

		if((null == invocation) || (null == config)) {
			throw new IllegalArgumentException("Nulls cannot be provided");
		}

		String forwardedProtocol = getXForwardedForHeaderValue(invocation.getRequest());
		logger.debug("***** AppSecureChannelProcessor decide  ==> forwardedProtocol : {}", forwardedProtocol);

		for(ConfigAttribute attribute : config) {
			logger.debug("***** AppSecureChannelProcessor decide  ==> attribute : {}", attribute);
			if(supports(attribute)) {
				logger.debug("***** AppSecureChannelProcessor decide supports ==> attribute : {}", attribute);
				if( null != forwardedProtocol ) {
					if(!"https".equals(forwardedProtocol)) {
						logger.debug("***** SECURE 1 ==> {} : {}", X_FORWARDED_PROTO, forwardedProtocol);
						getEntryPoint().commence(invocation.getRequest(), invocation.getResponse());
					}
				} else {
					if(!invocation.getHttpRequest().isSecure()) {
						logger.debug("***** SECURE 2 ==> {} : {}", X_FORWARDED_PROTO, forwardedProtocol);
						getEntryPoint().commence(invocation.getRequest(), invocation.getResponse());
					}
				}
			}
		}
	}

	/** 헤더값 획득 없으면 null */
	private String getXForwardedForHeaderValue(HttpServletRequest httpRequest) {
		return CollectionUtils.contains(httpRequest.getHeaderNames(), X_FORWARDED_PROTO)
				? httpRequest.getHeader(X_FORWARDED_PROTO) : null;
	}
}