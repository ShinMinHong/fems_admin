package com.firealarm.admin.security.exception;

import org.springframework.security.core.AuthenticationException;

public class InitPasswordException extends AuthenticationException {

	private static final long serialVersionUID = 2881451864356260020L;

	public InitPasswordException(String msg) {
		super(msg);
	}

	public InitPasswordException(String msg, Throwable t) {
		super(msg, t);
	}
}