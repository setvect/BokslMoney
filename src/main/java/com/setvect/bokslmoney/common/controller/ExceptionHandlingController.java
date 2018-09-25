package com.setvect.bokslmoney.common.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 전역 예외 처리.
 */
@ControllerAdvice
@RestController
public class ExceptionHandlingController {
	/** logger */
	private static Logger logger = LoggerFactory.getLogger(ExceptionHandlingController.class);

	/**
	 * 공통 예외 처리.
	 *
	 * @param req
	 *            servletRequest
	 * @param res
	 *            servletResponse
	 * @param exception
	 *            전달 받은 예외 객체
	 * @return 예외 처리 페이지
	 * @throws IOException
	 *             .
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView handleError(final HttpServletRequest req, final HttpServletResponse res,
			final Exception exception) throws IOException {
		ModelAndView mav = new ModelAndView();
		logger.error(exception.getMessage(), exception);
		res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
		return mav;
	}
}
