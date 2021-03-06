package com.setvect.bokslmoney.transaction.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.setvect.bokslmoney.ApplicationUtil;
import com.setvect.bokslmoney.BokslMoneyConstant.AttributeName;
import com.setvect.bokslmoney.account.repository.AccountRepository;
import com.setvect.bokslmoney.account.vo.AccountVo;
import com.setvect.bokslmoney.transaction.repository.CategoryRepository;
import com.setvect.bokslmoney.transaction.repository.TransactionRepository;
import com.setvect.bokslmoney.transaction.service.TransactionService;
import com.setvect.bokslmoney.transaction.vo.KindType;
import com.setvect.bokslmoney.transaction.vo.TransactionVo;
import com.setvect.bokslmoney.util.DateUtil;

/**
 * 달력 기반 가계부 입력
 */
@Controller
@RequestMapping(value = "/transaction/")
public class TransactionController {

	@Autowired
	private CategoryRepository categoryRepository;

	/** 거래 내역 */
	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountRepository accountRepository;

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
		request.setAttribute(AttributeName.LOAD_PAGE, "/WEB-INF/views/app/transaction/calendar.jsp");
		return "template";
	}

	/**
	 * @param request
	 *            servlet
	 * @return view 페이지
	 */
	@RequestMapping(value = "/grid.do")
	public String grid(final HttpServletRequest request) {
		request.setAttribute(AttributeName.LOAD_PAGE, "/WEB-INF/views/app/transaction/grid.jsp");
		return "template";
	}

	// ============== 조회 ==============

	/**
	 * @param year
	 *            년
	 * @param month
	 *            월
	 * @return 해당 월에 등록된 지출, 수입, 이체 내역
	 */
	@RequestMapping(value = "/listByMonth.json")
	@ResponseBody
	public ResponseEntity<String> listByMonth(final int year, final int month) {
		TransactionSearchParam searchCondition = new TransactionSearchParam(0, Integer.MAX_VALUE);
		LocalDate fromDate = LocalDate.of(year, month, 1);
		LocalDate toDate = fromDate.plusMonths(1).minusDays(1);

		searchCondition.setFrom(DateUtil.toDate(fromDate));
		searchCondition.setTo(DateUtil.toDate(toDate));

		return listByRange(searchCondition);
	}

	/**
	 * @param searchCondition
	 *            검색 조건
	 * @return 검위내 등록된 지출, 수입, 이체 내역
	 */
	@RequestMapping(value = "/listByRange.json")
	@ResponseBody
	public ResponseEntity<String> listByRange(final TransactionSearchParam searchCondition) {
		searchCondition.setReturnCount(Integer.MAX_VALUE);
		List<TransactionVo> list = transactionService.list(searchCondition);
		String json = ApplicationUtil.toJson(list, "**,item[-handler,-hibernateLazyInitializer]");
		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	// ============== 등록 ==============
	@RequestMapping(value = "/add.do")
	@ResponseBody
	public void add(final TransactionVo item, @RequestParam("categorySeq") final int categorySeq) {
		item.setCategory(categoryRepository.findById(categorySeq).get());
		transactionRepository.save(item);

		applyAccount(item);
	}

	// ============== 수정 ==============
	@RequestMapping(value = "/edit.do")
	@ResponseBody
	public void edit(final TransactionVo item, @RequestParam("categorySeq") final int categorySeq) {
		TransactionVo beforeTrans = transactionRepository.findById(item.getTransactionSeq()).get();
		revertAccount(beforeTrans);

		item.setCategory(categoryRepository.findById(categorySeq).get());
		transactionRepository.save(item);
		applyAccount(item);
	}

	// ============== 삭제 ==============
	@RequestMapping(value = "/delete.do")
	@ResponseBody
	public void delete(@RequestParam("itemSeq") final int itemSeq) {
		TransactionVo item = transactionRepository.findById(itemSeq).get();

		revertAccount(item);

		transactionRepository.delete(item);
	}

	// ============== 공통 ==============

	/**
	 * 거래 내용 계좌에 반영
	 *
	 * @param trans
	 *            거래정보
	 */
	private void applyAccount(final TransactionVo trans) {
		int money = trans.getMoney();
		if (trans.getKind() == KindType.INCOME) {
			addAcount(trans.getReceiveAccount(), money);
		} else if (trans.getKind() == KindType.SPENDING) {
			subAcount(trans.getPayAccount(), money);
		} else if (trans.getKind() == KindType.TRANSFER) {
			subAcount(trans.getPayAccount(), money + trans.getFee());
			addAcount(trans.getReceiveAccount(), money);
		}
	}

	/**
	 * 거래 이전 내용으로 계좌 반영
	 *
	 * @param trans
	 *            거래정보
	 */
	private void revertAccount(final TransactionVo trans) {
		int money = trans.getMoney();
		if (trans.getKind() == KindType.INCOME) {
			subAcount(trans.getReceiveAccount(), money);
		} else if (trans.getKind() == KindType.SPENDING) {
			addAcount(trans.getPayAccount(), money);
		} else if (trans.getKind() == KindType.TRANSFER) {
			addAcount(trans.getPayAccount(), money + trans.getFee());
			subAcount(trans.getReceiveAccount(), money);
		}
	}

	/**
	 * 특정 계좌에 돈을 뺀다
	 *
	 * @param accountSeq
	 *            계좌정보보 일련번호
	 * @param money
	 *            금액
	 */
	private void subAcount(final int accountSeq, final int money) {
		AccountVo receiveAcount = accountRepository.findById(accountSeq).get();
		receiveAcount.setBalance(receiveAcount.getBalance() - money);
		accountRepository.saveAndFlush(receiveAcount);
	}

	/**
	 * 특정 계좌에 돈을 더한다
	 *
	 * @param accountSeq
	 *            계좌정보보 일련번호
	 * @param money
	 *            금액
	 */
	private void addAcount(final int accountSeq, final int money) {
		AccountVo receiveAcount = accountRepository.findById(accountSeq).get();
		receiveAcount.setBalance(receiveAcount.getBalance() + money);
		accountRepository.saveAndFlush(receiveAcount);
	}
}
