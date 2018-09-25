package com.setvect.bokslmoney;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 프로그램에서 명시적으로 발생하는 예외
 */
public class ApplicationRuntimeException extends RuntimeException {

	/** */
	private static final long serialVersionUID = 2094101589503179374L;

	/**
	 * 예외 발생
	 */
	public ApplicationRuntimeException() {
		super();
	}

	/**
	 * @param message
	 *            메시지
	 */
	@Autowired
	public ApplicationRuntimeException(final String message) {
		super(message);
	}

}
