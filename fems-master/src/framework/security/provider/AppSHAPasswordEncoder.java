package framework.security.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AppSHAPasswordEncoder implements PasswordEncoder {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private ShaPasswordEncoder shaPasswordEncoder;
	private int bit = 256;
	private Object salt = "8zff4fgflgfd93f";

	public AppSHAPasswordEncoder() {
		super();
		shaPasswordEncoder = new ShaPasswordEncoder(bit);
	}

	public void setEncodeHashAsBase64(boolean encodeHashAsBase64) {
		shaPasswordEncoder.setEncodeHashAsBase64(encodeHashAsBase64);
	}

	public void setSalt(int salt) {
		this.salt = salt;
	}

	@Override
	public String encode(CharSequence rawPassword) {
		return shaPasswordEncoder.encodePassword(rawPassword.toString(), this.salt);
	}

	@Override public boolean matches(CharSequence rawPassword, String encodedPassword) {
		logger.info(" ::: salt ::: {}", salt);
		logger.info(" ::: password ::: {}", this.encode(rawPassword));
		logger.info(" ::: password ::: {}", encodedPassword);
		return shaPasswordEncoder.isPasswordValid(encodedPassword, rawPassword.toString(), salt); }
	}

