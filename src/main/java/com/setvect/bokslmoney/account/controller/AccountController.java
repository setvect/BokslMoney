package com.setvect.bokslmoney.account.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.setvect.bokslmoney.BokslMoneyConstant.AttributeName;
import com.setvect.bokslmoney.account.repository.AccountRepository;
import com.setvect.bokslmoney.account.vo.AccountVo;
import com.setvect.bokslmoney.code.service.CodeKind;
import com.setvect.bokslmoney.code.service.CodeService;

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
	public String page(final HttpServletRequest request) {
		request.setAttribute(AttributeName.LOAD_PAGE, "/WEB-INF/views/app/account/account.jsp");
		return "template";
	}

	// ============== 조회 ==============
	/**
	 * @return 계좌 목록
	 */
	@RequestMapping(value = "/list.json")
	@ResponseBody
	public ResponseEntity<List<AccountVo>> list() {
		Map<Integer, String> mapping = codeService.getMappingKindCode(CodeKind.KIND_CODE);

		List<AccountVo> list = accountRepository.list();
		list.forEach(a -> {
			applyKindName(a, mapping);
		});

		return new ResponseEntity<List<AccountVo>>(list, HttpStatus.OK);
	}

	/**
	 * 계좌 일련번호에 대한 계좌 이름 맵핑 정보
	 *
	 * @return Key:일련번호, Value: 계좌 이름
	 */
	@RequestMapping(value = "/mapName.json")
	@ResponseBody
	public ResponseEntity<Map<Integer, String>> mapName() {
		List<AccountVo> list = accountRepository.list();
		Map<Integer, String> accMap = list.stream().collect(Collectors.toMap(a -> a.getAccountSeq(), a -> a.getName()));
		return new ResponseEntity<>(accMap, HttpStatus.OK);
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
