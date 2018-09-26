package com.setvect.bokslmoney.hab.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.setvect.bokslmoney.code.service.CodeKind;
import com.setvect.bokslmoney.code.service.CodeService;
import com.setvect.bokslmoney.hab.repository.AccountRepository;
import com.setvect.bokslmoney.hab.vo.AccountVo;

@Controller
@RequestMapping(value = "/account")
public class AccountController {
	/** 로깅 */
	private static Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CodeService codeService;

	// ============== 뷰==============
	/**
	 * @return view 페이지
	 */
	@RequestMapping(value = "/page.do")
	public String page() {
		return "/hab/account/account";
	}

	// ============== 조회 ==============
	/**
	 * @return 계좌 목록
	 */
	@RequestMapping(value = "/list.json")
	@ResponseBody
	public List<AccountVo> list() {
		Map<Integer, String> mapping = codeService.getMappingKindCode(CodeKind.KIND_CODE);

		List<AccountVo> list = accountRepository.list();
		list.forEach(a -> {
			applyKindName(a, mapping);
		});

		return list;
	}

	private void applyKindName(AccountVo account, Map<Integer, String> mapping) {
		account.setKindName(mapping.get(account.getKindCode()));
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
