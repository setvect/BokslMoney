package com.setvect.bokslmoney.hab.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.setvect.bokslmoney.BokslMoneyConstant.AttributeName;
import com.setvect.bokslmoney.hab.repository.TransactionRepository;

/**
 * 결산
 */
@Controller
@RequestMapping(value = "/hab/")
public class SettlementController {
	/** 거래 내역 */
	@Autowired
	private TransactionRepository transactionRepository;

	// ============== 뷰==============

	/**
	 * @param request
	 *            servlet
	 * @return view 페이지
	 */
	@RequestMapping(value = "/settlement.do")
	public String page(final HttpServletRequest request) {
		request.setAttribute(AttributeName.LOAD_PAGE, "/WEB-INF/views/hab/settlement/settlement.jsp");
		return "template";
	}
}
