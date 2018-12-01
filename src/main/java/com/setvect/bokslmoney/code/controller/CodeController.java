package com.setvect.bokslmoney.code.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.setvect.bokslmoney.ApplicationUtil;
import com.setvect.bokslmoney.BokslMoneyConstant.AttributeName;
import com.setvect.bokslmoney.code.repository.CodeItemRepository;
import com.setvect.bokslmoney.code.repository.CodeMainRepository;
import com.setvect.bokslmoney.code.vo.CodeItemKey;
import com.setvect.bokslmoney.code.vo.CodeItemVo;
import com.setvect.bokslmoney.code.vo.CodeMainVo;
import com.setvect.bokslmoney.hab.repository.CategoryRepository;

@Controller
@RequestMapping(value = "/code")
public class CodeController {

	@Autowired
	private CodeMainRepository codeMainRepository;

	@Autowired
	private CodeItemRepository codeItemRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	// ============== 뷰==============

	/**
	 * @param request
	 *            servlet
	 * @return view 페이지
	 */
	@RequestMapping(value = "/page.do")
	public String page(final HttpServletRequest request) {
		request.setAttribute(AttributeName.LOAD_PAGE, "/WEB-INF/views/code/code.jsp");
		return "template";
	}

	// ============== 조회 ==============
	@RequestMapping(value = "/list.json")
	@ResponseBody
	public String list(@RequestParam("codeMainId") final String codeMainId) {
		List<CodeItemVo> list = codeItemRepository.list(codeMainId);
		return ApplicationUtil.toJson(list, "**,codeItemKey[**,codeMain[-handler,-hibernateLazyInitializer]]");
	}

	@RequestMapping(value = "/getCodeMain.json")
	@ResponseBody
	public CodeMainVo getCodeMain(@RequestParam("codeMainId") final String codeMainId) {
		return codeMainRepository.findById(codeMainId).get();
	}

	// ============== 등록 ==============
	@RequestMapping(value = "/add.do")
	@ResponseBody
	public void add(final CodeItemVo code, @RequestParam("codeMainId") final String codeMainId) {
		int nextId = codeItemRepository.getNextId(codeMainId);
		CodeMainVo codeMain = codeMainRepository.findById(codeMainId).get();
		CodeItemKey codeItemKey = new CodeItemKey(codeMain, nextId);
		code.setCodeItemKey(codeItemKey);
		codeItemRepository.save(code);
	}

	// ============== 수정 ==============
	@RequestMapping(value = "/edit.do")
	@ResponseBody
	public void edit(final CodeItemVo codeItemVo, @RequestParam("codeMainId") final String codeMainId,
			@RequestParam("codeItemSeq") final int codeItemSeq) {
		CodeMainVo codeMain = codeMainRepository.findById(codeMainId).get();
		CodeItemKey codeItemKey = new CodeItemKey(codeMain, codeItemSeq);

		CodeItemVo org = codeItemRepository.findById(codeItemKey).get();
		org.setName(codeItemVo.getName());
		codeItemRepository.save(org);
	}

	/**
	 * 정렬 순서 변경
	 *
	 * @param mainCodeId
	 *            메인 코드
	 *
	 * @param downCodeItemSeq
	 *            내려갈 항목
	 * @param upCodeItemSeq
	 *            올라갈 항목
	 */
	@RequestMapping(value = "/changeOrder.do")
	@ResponseBody
	public void changeOrder(@RequestParam("codeMainId") final String mainCodeId,
			@RequestParam("downCodeItemSeq") final int downCodeItemSeq,
			@RequestParam("upCodeItemSeq") final int upCodeItemSeq) {

		CodeMainVo mainCode = codeMainRepository.findById(mainCodeId).get();

		CodeItemVo downCode = codeItemRepository.findById(new CodeItemKey(mainCode, downCodeItemSeq)).get();
		CodeItemVo upCode = codeItemRepository.findById(new CodeItemKey(mainCode, upCodeItemSeq)).get();

		int downOrder = downCode.getOrderNo();
		int upOrder = upCode.getOrderNo();

		downCode.setOrderNo(upOrder);
		upCode.setOrderNo(downOrder);

		codeItemRepository.save(downCode);
		codeItemRepository.save(upCode);
	}

	// ============== 삭제 ==============
	@RequestMapping(value = "/delete.do")
	@ResponseBody
	public void delete(@RequestParam("codeMainId") final String codeMainId,
			@RequestParam("codeItemSeq") final int codeItemSeq) {
		CodeMainVo mainCode = codeMainRepository.findById(codeMainId).get();

		CodeItemVo code = codeItemRepository.findById(new CodeItemKey(mainCode, codeItemSeq)).get();
		code.setDeleteFlag(true);
		codeItemRepository.save(code);
	}

}
