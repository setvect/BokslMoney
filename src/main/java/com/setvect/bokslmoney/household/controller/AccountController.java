package com.setvect.bokslmoney.household.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.setvect.bokslmoney.household.repository.AccountRepository;
import com.setvect.bokslmoney.household.vo.AccountVo;

@Controller
@RequestMapping(value = "/account")
public class AccountController {
	/** 로깅 */
	private static Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private AccountRepository accountRepository;

	// ============== 뷰==============
	/**
	 * @return view 페이지
	 */
	@RequestMapping(value = "/page.do")
	public String page() {
		return "/account/account";
	}

	// ============== 조회 ==============
	/**
	 * @return 계좌 목록
	 */
	@RequestMapping(value = "/list.json")
	@ResponseBody
	public List<AccountVo> list() {
		return accountRepository.list();
	}

	// ============== 등록 ==============
	@RequestMapping(value = "/add.do")
	@ResponseBody
	public void add(final AccountVo item) {
		accountRepository.save(item);
	}

	// ============== 수정 ==============
	@RequestMapping(value = "/edit.do")
	@ResponseBody
	public void edit(final AccountVo item) {
		accountRepository.save(item);
	}

	// ============== 삭제 ==============
	@RequestMapping(value = "/delete.do")
	@ResponseBody
	public void delete(@RequestParam("accountSeq") final int accountSeq) {
		AccountVo account = accountRepository.findById(accountSeq).get();
		account.setDeleteFlag(true);
		accountRepository.save(account);
	}
}
