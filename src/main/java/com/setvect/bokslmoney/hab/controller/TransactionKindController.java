package com.setvect.bokslmoney.hab.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.setvect.bokslmoney.BokslMoneyConstant.AttributeName;
import com.setvect.bokslmoney.hab.repository.TransactionKindRepository;
import com.setvect.bokslmoney.hab.service.TransactionKindService;
import com.setvect.bokslmoney.hab.vo.KindType;
import com.setvect.bokslmoney.hab.vo.TransactionKindVo;
import com.setvect.bokslmoney.util.TreeNode;

/**
 * 항목관리 컨트롤러
 */
@Controller
@RequestMapping(value = "/hab/transactionKind")
public class TransactionKindController {
	/** 로깅 */
	private static Logger logger = LoggerFactory.getLogger(TransactionKindController.class);

	@Autowired
	private TransactionKindRepository transactionKindRepository;

	@Autowired
	private TransactionKindService transactionKindServcie;

	// ============== 뷰==============

	/**
	 * @param request
	 *            servlet
	 * @return view 페이지
	 */
	@RequestMapping(value = "/page.do")
	public String page(final HttpServletRequest request) {
		request.setAttribute(AttributeName.LOAD_PAGE, "/WEB-INF/views/hab/transaction_kind/transaction_kind.jsp");
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
	public List<TransactionKindVo> list(@RequestParam("kind") final KindType kindType,
			@RequestParam("parent") final int parent) {
		return transactionKindRepository.list(kindType, parent);
	}

	/**
	 * @param kindType
	 *            유형
	 * @return 항목 목록
	 */
	@RequestMapping(value = "/listAll.json")
	@ResponseBody
	public Map<KindType, List<TreeNode<TransactionKindVo>>> listAll() {
		return transactionKindServcie.listHierarchyAll();
	}

	// ============== 등록 ==============
	@RequestMapping(value = "/add.do")
	@ResponseBody
	public void add(final TransactionKindVo transactionKind) {
		transactionKindRepository.save(transactionKind);
	}

	// ============== 수정 ==============
	@RequestMapping(value = "/edit.do")
	@ResponseBody
	public void edit(final TransactionKindVo transactionKind) {
		TransactionKindVo org = transactionKindRepository.findById(transactionKind.getTransactionKindSeq()).get();
		org.setName(transactionKind.getName());
		transactionKindRepository.save(org);
	}

	/**
	 * 정렬 순서 변경
	 *
	 * @param downtransactionKindSeq
	 *            내려갈 항목
	 * @param uptransactionKindSeq
	 *            올라갈 항목
	 */
	@RequestMapping(value = "/changeOrder.do")
	@ResponseBody
	public void changeOrder(@RequestParam("downtransactionKindSeq") final int downtransactionKindSeq,
			@RequestParam("uptransactionKindSeq") final int uptransactionKindSeq) {
		TransactionKindVo downTransactionKind = transactionKindRepository.findById(downtransactionKindSeq).get();
		TransactionKindVo upTransactionKind = transactionKindRepository.findById(uptransactionKindSeq).get();
		int downOrder = downTransactionKind.getOrderNo();
		int upOrder = upTransactionKind.getOrderNo();

		downTransactionKind.setOrderNo(upOrder);
		upTransactionKind.setOrderNo(downOrder);

		transactionKindRepository.save(downTransactionKind);
		transactionKindRepository.save(upTransactionKind);
	}

	// ============== 삭제 ==============
	@RequestMapping(value = "/delete.do")
	@ResponseBody
	public void delete(@RequestParam("transactionKindSeq") final int transactionKindSeq) {
		TransactionKindVo transactionKind = transactionKindRepository.findById(transactionKindSeq).get();
		transactionKind.setDeleteFlag(true);
		transactionKindRepository.save(transactionKind);
	}
}
