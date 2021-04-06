package spring;

import java.util.List;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * Spring Security 메소드 보안설정
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, mode = AdviceMode.PROXY, proxyTargetClass = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

	/**
	 * 기본 AffirmativeBased에서 UnanimousBased로 수정
	 * 반대가 1건도 있으면 안되게 처리. Url접근제어, Annotation접근제어 모두 ALLOW 필요
	 * @see org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration#accessDecisionManager()
	 */
	@Override
	protected AccessDecisionManager accessDecisionManager() {
		//
		AffirmativeBased accessDecisionManager = (AffirmativeBased)super.accessDecisionManager();
		List<AccessDecisionVoter<? extends Object>> decisionVoters = accessDecisionManager.getDecisionVoters();
		return new UnanimousBased(decisionVoters);
	}
}