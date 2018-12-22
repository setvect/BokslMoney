package com.setvect.bokslmoney.stat.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.setvect.bokslmoney.BokslMoneyConstant.AttributeName;

/**
 * 통계
 */
@Controller
@RequestMapping(value = "/stat")
public class StatController {

	// ============== 뷰==============

	/**
	 * @param request
	 *            servlet
	 * @return view 페이지
	 */
	@RequestMapping(value = "/stat.do")
	public String page(final HttpServletRequest request) {
		request.setAttribute(AttributeName.LOAD_PAGE, "/WEB-INF/views/app/stat/stat.jsp");
		return "template";

		// ============== 조회 ==============
	}
}
