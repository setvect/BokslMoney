package com.setvect.bokslmoney.transaction.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.setvect.bokslmoney.BokslMoneyConstant.AttributeName;
import com.setvect.bokslmoney.transaction.repository.CategoryRepository;
import com.setvect.bokslmoney.transaction.service.CategoryService;
import com.setvect.bokslmoney.transaction.service.TransactionService;
import com.setvect.bokslmoney.transaction.vo.CategoryVo;
import com.setvect.bokslmoney.transaction.vo.KindType;
import com.setvect.bokslmoney.util.TreeNode;

/**
 * 항목관리 컨트롤러
 */
@Controller
@RequestMapping(value = "/hab/category")
public class CategoryController {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private TransactionService transactionService;
	// ============== 뷰==============

	/**
	 * @param request
	 *            servlet
	 * @return view 페이지
	 */
	@RequestMapping(value = "/page.do")
	public String page(final HttpServletRequest request) {
		request.setAttribute(AttributeName.LOAD_PAGE, "/WEB-INF/views/hab/category/category.jsp");
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
	public ResponseEntity<List<CategoryVo>> list(@RequestParam("kind") final KindType kindType,
			@RequestParam("parent") final int parent) {
		List<CategoryVo> list = categoryRepository.list(kindType, parent);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	/**
	 * @param kindType
	 *            유형
	 * @return 항목 목록
	 */
	@RequestMapping(value = "/listAll.json")
	@ResponseBody
	public ResponseEntity<Map<KindType, List<TreeNode<CategoryVo>>>> listAll() {
		Map<KindType, List<TreeNode<CategoryVo>>> listHierarchyAll = categoryService.listHierarchyAll();
		return new ResponseEntity<>(listHierarchyAll, HttpStatus.OK);
	}

	/**
	 * 지난 거래 내역을 바탕으로 관련 있는 카테고리 추천
	 *
	 * @param note
	 *            거래 내역 메모
	 * @param kind
	 *            유형
	 * @return 카테고리 목록
	 */
	@RequestMapping(value = "/listRecommend.json")
	@ResponseBody
	public ResponseEntity<List<CategoryVo>> listRecommend(@RequestParam("note") final String note,
			@RequestParam("kind") final KindType kind) {
		List<CategoryVo> sorted = categoryService.listRecommend(note, kind);

		return new ResponseEntity<>(sorted, HttpStatus.OK);
	}

	// ============== 등록 ==============
	@RequestMapping(value = "/add.do")
	@ResponseBody
	public void add(final CategoryVo category) {
		categoryRepository.save(category);
	}

	// ============== 수정 ==============
	@RequestMapping(value = "/edit.do")
	@ResponseBody
	public void edit(final CategoryVo category) {
		CategoryVo org = categoryRepository.findById(category.getCategorySeq()).get();
		org.setName(category.getName());
		categoryRepository.save(org);
	}

	/**
	 * 정렬 순서 변경
	 *
	 * @param downCategorySeq
	 *            내려갈 항목
	 * @param upCategorySeq
	 *            올라갈 항목
	 */
	@RequestMapping(value = "/changeOrder.do")
	@ResponseBody
	public void changeOrder(@RequestParam("downCategorySeq") final int downCategorySeq,
			@RequestParam("upCategorySeq") final int upCategorySeq) {
		CategoryVo downCategory = categoryRepository.findById(downCategorySeq).get();
		CategoryVo upCategory = categoryRepository.findById(upCategorySeq).get();
		int downOrder = downCategory.getOrderNo();
		int upOrder = upCategory.getOrderNo();

		downCategory.setOrderNo(upOrder);
		upCategory.setOrderNo(downOrder);

		categoryRepository.save(downCategory);
		categoryRepository.save(upCategory);
	}

	// ============== 삭제 ==============
	@RequestMapping(value = "/delete.do")
	@ResponseBody
	public void delete(@RequestParam("categorySeq") final int categorySeq) {
		CategoryVo category = categoryRepository.findById(categorySeq).get();
		category.setDeleteFlag(true);
		categoryRepository.save(category);
	}
}
