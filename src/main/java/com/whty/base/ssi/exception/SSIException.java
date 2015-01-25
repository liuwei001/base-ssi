package com.whty.base.ssi.exception;

import java.lang.Exception;

public class SSIException extends Exception {
	private Long code = 0l;
	private Exception exception;

	public SSIException(Long code, Exception exception) {
		this.code = code;
		this.exception = exception;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

}
