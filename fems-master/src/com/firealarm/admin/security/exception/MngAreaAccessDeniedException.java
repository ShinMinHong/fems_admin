package com.firealarm.admin.security.exception;

import org.springframework.security.access.AccessDeniedException;

public class MngAreaAccessDeniedException extends AccessDeniedException {

	private static final long serialVersionUID = 1850791673522145498L;

	public MngAreaAccessDeniedException(String msg, Throwable t) {
		super(msg, t);
	}

	public MngAreaAccessDeniedException(String msg) {
		super(msg);
	}

	public MngAreaAccessDeniedException() {
		super("AREA MANAGE FORBIDDEN");
	}
}
