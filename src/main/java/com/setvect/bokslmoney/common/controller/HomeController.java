package com.setvect.bokslmoney.common.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 공통페이지 컨트롤러
 */
@Controller
public class HomeController {
	/** 로깅 */
	private static Logger logger = LoggerFactory.getLogger(HomeController.class);

	// ============== 뷰 페이지 오픈 ==============

	/**
	 * @param request
	 *            servlet
	 * @return view 페이지
	 */
	@RequestMapping(value = "/")
	public String index(final HttpServletRequest request) {
		return "index";
	}

	/**
	 * 로그인 화면. SecurityConfig 설정과 연계
	 *
	 * @return view 페이지
	 */
	@RequestMapping("/login.do")
	public String login() {
		return "login";
	}

	/**
	 * 로그인 화면. SecurityConfig 설정과 연계
	 *
	 * @return view 페이지
	 */
	@RequestMapping("/home.do")
	public String manager() {
		return "manager";
	}

}
