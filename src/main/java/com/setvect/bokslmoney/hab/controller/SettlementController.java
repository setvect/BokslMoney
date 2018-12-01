package com.setvect.bokslmoney.hab.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.setvect.bokslmoney.hab.vo.TransactionVo;
import com.setvect.bokslmoney.util.DateUtil;

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
	 * 지출 결산
	 *
	 * @param year
	 *            년도
	 * @return Key: 월, Value: (Key: 대분류 아이디, Value: 합산 금액)
	 */
	@RequestMapping(value = "/statSpending.json")
	@ResponseBody
	public ResponseEntity<Map<Integer, Map<Integer, Integer>>> listByRange(@RequestParam("year") final int year) {
		TransactionSearchParam searchCondition = new TransactionSearchParam();

		LocalDate from = LocalDate.of(year, 1, 1);
		LocalDate to = from.plusYears(1).minusDays(1);

		searchCondition.setFrom(DateUtil.toDate(from));
		searchCondition.setTo(DateUtil.toDate(to));
		searchCondition.setReturnCount(Integer.MAX_VALUE);
		searchCondition.setKindType(KindType.SPENDING);
		List<TransactionVo> transactionList = transactionService.list(searchCondition);

		Map<Integer, Integer> mapping = categoryService.getChildCategoryParentMapping(KindType.SPENDING);

		// 1차 완성
		// Map<Integer, Collection<TransactionVo>> tranOfMonth =
		// transactionList.stream()
		// .collect(Collectors.groupingBy(tran -> tran.getMonth(),
		// Collectors.mapping(Function.identity(),
		// Collectors.toCollection(HashSet::new))));
		// Map<Integer, Map<Integer, Integer>> result = new HashMap<>();
		// Map<Integer, Map<Integer, List<TransactionVo>>>
		// Key: 월, Value: (Key: 대분류 아이디, Value: 합산 금액)
		Map<Integer, Map<Integer, Integer>> tranOfMonth = transactionList.stream()
				.collect(Collectors.groupingBy(tran -> tran.getMonth(),
						Collectors.groupingBy(tran -> tran.getParentCategory().getCategorySeq(),
								Collectors.summingInt(tran -> tran.getMoney()))));

		System.out.println(tranOfMonth);

		// return ApplicationUtil.toJson(result,
		// "**,item[-handler,-hibernateLazyInitializer]");
		return null;
	}

}
