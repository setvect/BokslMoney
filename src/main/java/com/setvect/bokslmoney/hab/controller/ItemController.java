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
import com.setvect.bokslmoney.hab.repository.ItemRepository;
import com.setvect.bokslmoney.hab.service.ItemService;
import com.setvect.bokslmoney.hab.vo.ItemVo;
import com.setvect.bokslmoney.hab.vo.KindType;
import com.setvect.bokslmoney.util.TreeNode;

/**
 * 항목관리 컨트롤러
 */
@Controller
@RequestMapping(value = "/hab/item")
public class ItemController {
	/** 로깅 */
	private static Logger logger = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ItemService itemServcie;

	private static int ROOT_ITEM_SEQ = 0;

	// ============== 뷰==============

	/**
	 * @param request
	 *            servlet
	 * @return view 페이지
	 */
	@RequestMapping(value = "/page.do")
	public String page(final HttpServletRequest request) {
		request.setAttribute(AttributeName.LOAD_PAGE, "/WEB-INF/views/hab/item/item.jsp");
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
	public List<ItemVo> list(@RequestParam("kind") final KindType kindType, @RequestParam("parent") final int parent) {
		return itemRepository.list(kindType, parent);
	}

	/**
	 * @param kindType
	 *            유형
	 * @return 항목 목록
	 */
	@RequestMapping(value = "/listAll.json")
	@ResponseBody
	public Map<KindType, List<TreeNode<ItemVo>>> listAll() {
		return itemServcie.listHierarchyAll();
	}

	// ============== 등록 ==============
	@RequestMapping(value = "/add.do")
	@ResponseBody
	public void add(final ItemVo item) {
		itemRepository.save(item);
	}

	// ============== 수정 ==============
	@RequestMapping(value = "/edit.do")
	@ResponseBody
	public void edit(final ItemVo item) {
		ItemVo org = itemRepository.findById(item.getItemSeq()).get();
		org.setName(item.getName());
		itemRepository.save(org);
	}

	/**
	 * 정렬 순서 변경
	 *
	 * @param downItemSeq
	 *            내려갈 항목
	 * @param upItemSeq
	 *            올라갈 항목
	 */
	@RequestMapping(value = "/changeOrder.do")
	@ResponseBody
	public void changeOrder(@RequestParam("downItemSeq") final int downItemSeq,
			@RequestParam("upItemSeq") final int upItemSeq) {
		ItemVo downItem = itemRepository.findById(downItemSeq).get();
		ItemVo upItem = itemRepository.findById(upItemSeq).get();
		int downOrder = downItem.getOrderNo();
		int upOrder = upItem.getOrderNo();

		downItem.setOrderNo(upOrder);
		upItem.setOrderNo(downOrder);

		itemRepository.save(downItem);
		itemRepository.save(upItem);
	}

	// ============== 삭제 ==============
	@RequestMapping(value = "/delete.do")
	@ResponseBody
	public void delete(@RequestParam("itemSeq") final int itemSeq) {
		ItemVo item = itemRepository.findById(itemSeq).get();
		item.setDeleteFlag(true);
		itemRepository.save(item);
	}
}
