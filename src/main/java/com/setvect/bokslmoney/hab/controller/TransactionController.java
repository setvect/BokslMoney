package com.setvect.bokslmoney.hab.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.setvect.bokslmoney.ApplicationUtil;
import com.setvect.bokslmoney.BokslMoneyConstant.AttributeName;
import com.setvect.bokslmoney.code.repository.CodeItemRepository;
import com.setvect.bokslmoney.code.repository.CodeMainRepository;
import com.setvect.bokslmoney.code.service.CodeService;
import com.setvect.bokslmoney.hab.repository.AccountRepository;
import com.setvect.bokslmoney.hab.repository.CategoryRepository;
import com.setvect.bokslmoney.hab.repository.TransactionRepository;
import com.setvect.bokslmoney.hab.service.TransactionService;
import com.setvect.bokslmoney.hab.vo.TransactionVo;
import com.setvect.bokslmoney.util.DateUtil;
import com.setvect.bokslmoney.util.PageResult;

/**
 * 달력 기반 가계부 입력
 */
@Controller
@RequestMapping(value = "/hab/transaction/")
public class TransactionController {
	/** 로깅 */
	private static Logger logger = LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CodeItemRepository codeItemRepository;

	@Autowired
	private CodeMainRepository codeMainRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private CodeService codeService;

	@Autowired
	private TransactionService transactionService;

	// ============== 뷰==============

	/**
	 * @param request
	 *            servlet
	 * @return view 페이지
	 */
	@RequestMapping(value = "/calendar.do")
	public String page(final HttpServletRequest request) {
		request.setAttribute(AttributeName.LOAD_PAGE, "/WEB-INF/views/hab/record/calendar/calendar.jsp");
		return "template";
	}

	// ============== 조회 ==============
	/**
	 * @param searchParam
	 * @return 지출, 수입, 이체내역
	 */
	@RequestMapping(value = "/list.json")
	@ResponseBody
	public List<TransactionVo> list(final TransactionSearchParam searchParam) {
		return null;
	}

	/**
	 * @param year
	 *            년
	 * @param month
	 *            월
	 * @return 해당 월에 등록된 지출, 수입, 이체 내역
	 */
	@RequestMapping(value = "/listByMonth.json")
	@ResponseBody
	public String listByMonth(final int year, final int month) {
		TransactionSearchParam searchCondition = new TransactionSearchParam(0, Integer.MAX_VALUE);
		LocalDate fromDate = LocalDate.of(year, month, 1);
		LocalDate toDate = fromDate.plusMonths(1).minusDays(1);

		searchCondition.setFrom(DateUtil.toDate(fromDate));
		searchCondition.setTo(DateUtil.toDate(toDate));

		PageResult<TransactionVo> page = transactionRepository.getPagingList(searchCondition);
		List<TransactionVo> list = page.getList();
		transactionService.mappingParentItem(list);
		return ApplicationUtil.toJson(list, "**,item[-handler,-hibernateLazyInitializer]");
	}

	// ============== 등록 ==============
	@RequestMapping(value = "/add.do")
	@ResponseBody
	public void add(final TransactionVo item, @RequestParam("categorySeq") final int categorySeq) {
		item.setCategory(categoryRepository.findById(categorySeq).get());
		transactionRepository.save(item);
	}

	// ============== 수정 ==============
	@RequestMapping(value = "/edit.do")
	@ResponseBody
	public void edit(final TransactionVo transaction, @RequestParam("categorySeq") final int categorySeq) {
		transaction.setCategory(categoryRepository.findById(categorySeq).get());
		transactionRepository.save(transaction);
	}

	// ============== 삭제 ==============
	@RequestMapping(value = "/delete.do")
	@ResponseBody
	public void delete(@RequestParam("itemSeq") final int itemSeq) {
		TransactionVo item = transactionRepository.findById(itemSeq).get();
		transactionRepository.delete(item);
	}
}