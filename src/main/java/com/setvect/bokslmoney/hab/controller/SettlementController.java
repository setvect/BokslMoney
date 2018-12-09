package com.setvect.bokslmoney.hab.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.setvect.bokslmoney.BokslMoneyConstant.AttributeName;
import com.setvect.bokslmoney.hab.repository.AccountRepository;
import com.setvect.bokslmoney.hab.repository.CategoryRepository;
import com.setvect.bokslmoney.hab.repository.TransactionRepository;
import com.setvect.bokslmoney.hab.service.CategoryService;
import com.setvect.bokslmoney.hab.service.TransactionService;
import com.setvect.bokslmoney.hab.vo.AccountVo;
import com.setvect.bokslmoney.hab.vo.DateKindSumVo;
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

	@Autowired
	private AccountRepository accountRepository;

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
	 * 분류별 결산
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
	 * @return Key: 월(0 부터 시작), Value: (Key: 유형 , Value: 합산 금액)
	 */
	@RequestMapping(value = "/groupKindOfMonth.json")
	@ResponseBody
	public ResponseEntity<Map<Integer, Map<KindType, Integer>>> groupKindOfMonth(@RequestParam("year") final int year) {
		Map<Integer, Map<KindType, Integer>> groupKindOfMonth = transactionService.groupKindOfMonth(year);
		return new ResponseEntity<>(groupKindOfMonth, HttpStatus.OK);
	}

	/**
	 * 누적 자산
	 *
	 * @param from
	 *            시작 날짜
	 * @return 날짜별로, 유형별로 금액 합산
	 */
	@RequestMapping(value = "/statAssets.json")
	@ResponseBody
	public ResponseEntity<List<DateKindSumVo>> statAssets(@RequestParam("from") final Date from) {
		List<DateKindSumVo> groupKindOfMonth = transactionService.sumKindOfMonth(from);
		Map<Date, Integer> accumulateAsset = getAccumulateAsset(groupKindOfMonth);

		System.err.println(accumulateAsset);

		int currentAsset = getCurrentAsset();

		System.out.println(currentAsset);

		return new ResponseEntity<>(groupKindOfMonth, HttpStatus.OK);
	}

	/**
	 * @param groupKindOfMonth
	 *            날짜별로, 유형별로 금액 합산
	 *
	 * @return 누적 자산(Key: 날짜, Value: 수입 - 지출)
	 */
	private Map<Date, Integer> getAccumulateAsset(final List<DateKindSumVo> groupKindOfMonth) {
		Map<Date, Map<KindType, Integer>> group = groupKindOfMonth.stream().collect(Collectors
				.groupingBy(v -> v.getDate(), TreeMap::new, Collectors.toMap(v -> v.getKind(), v -> v.getMoney())));

		Map<Date, Integer> accumulateAsset = group.entrySet().stream().collect(Collectors.toMap(g -> g.getKey(), g -> {
			Map<KindType, Integer> value = g.getValue();
			Integer income = value.get(KindType.INCOME);
			Integer spending = value.get(KindType.SPENDING);
			income = income == null ? 0 : income;
			spending = spending == null ? 0 : spending;

			return income - spending;
		}, (k1, k2) -> k1, TreeMap::new));
		return accumulateAsset;
	}

	/**
	 * @return 현재 잔고
	 */
	private int getCurrentAsset() {
		List<AccountVo> list = accountRepository.list();
		// 현재 잔고
		int currentAsset = list.stream().mapToInt(a -> a.getBalance()).sum();
		return currentAsset;
	}
}
