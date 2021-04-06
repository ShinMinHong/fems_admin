package framework.security.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import com.firealarm.admin.common.dao.UserDAO;
import com.firealarm.admin.security.exception.InitPasswordException;
import com.firealarm.admin.security.vo.AppUserDetails;

/**
 *
 * DAO를 통한 인증 프로바이더
 *
 * org.springframework.security.authentication.dao.DaoAuthenticationProvider을 수정
 *
 *  - deprecated된 org.springframework.security.authentication.encoding.PasswordEncoder를
 *    org.springframework.security.crypto.password.PasswordEncoder 계열로 변경
 *    - 기본 인코더를 PlaintextPasswordEncoder에서 NoOpPasswordEncoder로 변경
 *    - 불필요 Salt 관리 제거
 *
 * 필요시 additionalAuthenticationChecks에 추가로직 구현 원하는 추가 예외를 Throw 처리 가능
 *
 * An {@link AuthenticationProvider} implementation that retrieves user details from a
 * {@link UserDetailsService}.
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 * @author Ben Alex
 * @author Rob Winch
 */
public class AbleDaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Autowired UserDAO userDAO;

	// ~ Static fields/initializers
	// =====================================================================================
	private static final Logger logger = LoggerFactory.getLogger(AbleDaoAuthenticationProvider.class);
	/**
	 * The plaintext password used to perform
	 * {@link PasswordEncoder#isPasswordValid(String, String, Object)} on when
	 * the user is not found to avoid SEC-2056.
	 */
	private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

	// ~ Instance fields
	// ================================================================================================

	private AppSHAPasswordEncoder passwordEncoder;

	/**
	 * The password used to perform
	 * {@link PasswordEncoder#isPasswordValid(String, String, Object)} on when
	 * the user is not found to avoid SEC-2056. This is necessary, because some
	 * {@link PasswordEncoder} implementations will short circuit if the
	 * password is not in a valid format.
	 */
	private String userNotFoundEncodedPassword;

	//private SaltSource saltSource;

	private UserDetailsService userDetailsService;

	public AbleDaoAuthenticationProvider() {
		super();
		AppSHAPasswordEncoder innerEncoder = new AppSHAPasswordEncoder();
		setPasswordEncoder(innerEncoder);
	}

	// ~ Methods
	// ========================================================================================================

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		if (authentication.getCredentials() == null) {
			logger.debug("Authentication failed: no credentials provided");
			throw new BadCredentialsException( "Authentication failed: no credentials provided" );
		}

		AppUserDetails appUserDetails = (AppUserDetails)userDetails;
		if(appUserDetails.getPasswordFailCo() >= 5) {
			throw new LockedException ("비밀번호가 5회이상 실패하여 서비스 이용이 불가능 합니다. 관리자에게 문의바랍니다.");
		}

		String presentedPassword = authentication.getCredentials().toString();

		if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
			logger.debug("Authentication failed: password does not match stored value");
			//비밀번호 실패 카운트 증가
			userDAO.setPasswordFailCo(userDetails.getUsername());
			throw new BadCredentialsException( "아이디 또는 패스워드가 잘못되었습니다." );
		}

		if(appUserDetails.getLastPwChangeDt() == null) {
			throw new InitPasswordException ("최초 비밀번호 발급 이후 비밀번호를 변경해야 서비스 이용이 가능합니다.");
		} else if(appUserDetails.isPswdExpired()) {
			throw new CredentialsExpiredException ("비밀번호 변경 주기가 초과되어 비밀번호를 변경해야 서비스 이용이 가능합니다.");
		}
	}

	@Override
	protected void doAfterPropertiesSet() {
		Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
	}

	@Override
	protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		UserDetails loadedUser;
		try {
			loadedUser = this.getUserDetailsService().loadUserByUsername(username);
		} catch (UsernameNotFoundException ex) {
			if(logger.isTraceEnabled()) {
				logger.trace("{} - {}", ex.getClass().getName(), ex.getMessage(), ex);
			}
			if (authentication.getCredentials() != null) {
				String presentedPassword = authentication.getCredentials().toString();
				//passwordEncoder.isPasswordValid(userNotFoundEncodedPassword, presentedPassword, null);
				passwordEncoder.matches(presentedPassword, userNotFoundEncodedPassword);
			}
			throw new BadCredentialsException( "아이디 또는 패스워드가 잘못되었습니다." );
		} catch (Exception repositoryProblem) {
			throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
		}

		if (loadedUser == null) {
			throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
		}

		return loadedUser;
	}

	/**
	 * Sets the PasswordEncoder instance to be used to encode and validate
	 * passwords. If not set, the password will be compared as plain text.
	 * <p>
	 * For systems which are already using salted password which are encoded
	 * with a previous release, the encoder should be of type
	 * {@code org.springframework.security.authentication.encoding.PasswordEncoder}
	 * . Otherwise, the recommended approach is to use
	 * {@code org.springframework.security.crypto.password.PasswordEncoder}.
	 *
	 * @param passwordEncoder
	 *            must be an instance of one of the {@code PasswordEncoder}
	 *            types.
	 */
	public void setPasswordEncoder(Object passwordEncoder) {
		Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");

		if (passwordEncoder instanceof PasswordEncoder) {
			setPasswordEncoder((PasswordEncoder) passwordEncoder);
			return;
		}

		/* deepfree del - 이제는 org.springframework.security.crypto.password.PasswordEncoder를 기본 사용
		if (passwordEncoder instanceof org.springframework.security.crypto.password.PasswordEncoder) {
			final org.springframework.security.crypto.password.PasswordEncoder delegate = (org.springframework.security.crypto.password.PasswordEncoder) passwordEncoder;
			setPasswordEncoder(new PasswordEncoder() {
				public String encodePassword(String rawPass, Object salt) {
					checkSalt(salt);
					return delegate.encode(rawPass);
				}

				public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
					checkSalt(salt);
					return delegate.matches(rawPass, encPass);
				}

				private void checkSalt(Object salt) {
					Assert.isNull(salt, "Salt value must be null when used with crypto module PasswordEncoder");
				}
			});

			return;
		}
		*/

		throw new IllegalArgumentException("passwordEncoder must be a PasswordEncoder instance");
	}

	private void setPasswordEncoder(AppSHAPasswordEncoder passwordEncoder) {
		Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");

		//this.userNotFoundEncodedPassword = passwordEncoder.encodePassword(USER_NOT_FOUND_PASSWORD, null);
		this.userNotFoundEncodedPassword = passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
		this.passwordEncoder = passwordEncoder;
	}

	protected AppSHAPasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

//	/**
//	 * The source of salts to use when decoding passwords. <code>null</code> is
//	 * a valid value, meaning the <code>DaoAuthenticationProvider</code> will
//	 * present <code>null</code> to the relevant <code>PasswordEncoder</code>.
//	 * <p>
//	 * Instead, it is recommended that you use an encoder which uses a random
//	 * salt and combines it with the password field. This is the default
//	 * approach taken in the
//	 * {@code org.springframework.security.crypto.password} package.
//	 *
//	 * @param saltSource
//	 *            to use when attempting to decode passwords via the
//	 *            <code>PasswordEncoder</code>
//	 */
//	public void setSaltSource(SaltSource saltSource) {
//		this.saltSource = saltSource;
//	}
//
//	protected SaltSource getSaltSource() {
//		return saltSource;
//	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	protected UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}
}

