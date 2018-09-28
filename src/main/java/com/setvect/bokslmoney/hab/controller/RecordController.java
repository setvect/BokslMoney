package com.setvect.bokslmoney.hab.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.setvect.bokslmoney.BokslMoneyConstant.AttributeName;
import com.setvect.bokslmoney.code.repository.CodeItemRepository;
import com.setvect.bokslmoney.code.repository.CodeMainRepository;
import com.setvect.bokslmoney.code.service.CodeService;
import com.setvect.bokslmoney.hab.repository.AccountRepository;
import com.setvect.bokslmoney.hab.repository.TransferRepository;
import com.setvect.bokslmoney.hab.vo.TransferVo;

/**
 * 달력 기반 가계부 입력
 */
@Controller
@RequestMapping(value = "/hab/record/")
public class RecordController {
	/** 로깅 */
	private static Logger logger = LoggerFactory.getLogger(RecordController.class);

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CodeItemRepository codeItemRepository;

	@Autowired
	private CodeMainRepository codeMainRepository;

	@Autowired
	private TransferRepository transferRepository;

	@Autowired
	private CodeService codeService;

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
	 * @param kindType
	 *            유형
	 * @param parent
	 *            부모 코드 번호
	 * @return 항목 목록
	 */
	@RequestMapping(value = "/list.json")
	@ResponseBody
	public List<TransferVo> list(final TransferSearchParam searchParam) {
		return null;
	}

	// ============== 등록 ==============
	@RequestMapping(value = "/add.do")
	@ResponseBody
	public void add(final TransferVo item) {
		transferRepository.save(item);
	}

	// ============== 수정 ==============
	@RequestMapping(value = "/edit.do")
	@ResponseBody
	public void edit(final TransferVo item) {
		transferRepository.save(item);
	}

	// ============== 삭제 ==============
	@RequestMapping(value = "/delete.do")
	@ResponseBody
	public void delete(@RequestParam("itemSeq") final int itemSeq) {
		TransferVo item = transferRepository.findById(itemSeq).get();
		transferRepository.delete(item);
	}
}
