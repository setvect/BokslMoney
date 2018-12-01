package com.setvect.bokslmoney.hab.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.setvect.bokslmoney.BokslMoneyConstant.AttributeName;
import com.setvect.bokslmoney.hab.repository.CategoryRepository;
import com.setvect.bokslmoney.hab.repository.TransactionRepository;
import com.setvect.bokslmoney.hab.service.CategoryService;
import com.setvect.bokslmoney.hab.service.TransactionService;
import com.setvect.bokslmoney.hab.vo.KindType;

/**
 * 결산
 */
@Controller
@RequestMapping(value = "/hab/settlement")
public class SettlementController {
	@Autowired
	private TransactionService transactionService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CategoryRepository categoryRepository;

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

	// ============== 조회 ==============
	/**
	 * 유형별 결산
	 *
	 * @param year
	 *            년도
	 * @param kind
	 *            조회 항목 유형
	 * @return Key: 월(0 부터 시작), Value: (Key: 대분류 아이디, Value: 합산 금액)
	 */
	@RequestMapping(value = "/groupOfMonth.json")
	@ResponseBody
	public ResponseEntity<Map<Integer, Map<Integer, Integer>>> groupOfMonth(@RequestParam("year") final int year,
			@RequestParam("kind") final KindType kind) {
		Map<Integer, Map<Integer, Integer>> groupOfMonth = transactionService.groupCategoryOfMonth(year, kind);
		return new ResponseEntity<>(groupOfMonth, HttpStatus.OK);
	}

	/**
	 * 유형별 결산
	 *
	 * @param year
	 *            년도
	 * @return Key: 월(0 부터 시작), Value: (Key: 대분류 아이디, Value: 합산 금액)
	 */
	@RequestMapping(value = "/groupKindOfMonth.json")
	@ResponseBody
	public ResponseEntity<Map<Integer, Map<KindType, Integer>>> groupKindOfMonth(@RequestParam("year") final int year) {
		Map<Integer, Map<KindType, Integer>> groupKindOfMonth = transactionService.groupKindOfMonth(year);
		return new ResponseEntity<>(groupKindOfMonth, HttpStatus.OK);
	}
}
