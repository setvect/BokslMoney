package com.setvect.bokslmoney.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Http 호출 시 전, 후 처리 로직
 */
@Component
public class HttpInterceptor extends HandlerInterceptorAdapter {
	/** logger */
	private static Logger logger = LoggerFactory.getLogger(HttpInterceptor.class);

	/**
	 * Application 시작과 동시에 최초 한번 실행.
	 */
	@PostConstruct
	public void init() {
	}

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws UnsupportedEncodingException, IOException {
		return true;
	}
}