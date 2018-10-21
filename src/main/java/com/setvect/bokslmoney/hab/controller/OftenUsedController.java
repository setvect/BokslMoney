package com.setvect.bokslmoney.hab.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.setvect.bokslmoney.hab.repository.CategoryRepository;
import com.setvect.bokslmoney.hab.repository.OftenUsedRepository;
import com.setvect.bokslmoney.hab.vo.KindType;
import com.setvect.bokslmoney.hab.vo.OftenUsedVo;

@Controller
@RequestMapping(value = "/hab/oftenUsed")
public class OftenUsedController {
	/** 로깅 */
	private static Logger logger = LoggerFactory.getLogger(OftenUsedController.class);

	@Autowired
	private OftenUsedRepository oftenUsedRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	// ============== 뷰==============

	// ============== 조회 ==============
	/**
	 * @return 계좌 목록
	 */
	@RequestMapping(value = "/list.json")
	@ResponseBody
	public List<OftenUsedVo> list(@RequestParam(name = "kind") KindType kind) {
		List<OftenUsedVo> list = oftenUsedRepository.list(kind);
		return list;
	}

	// ============== 등록 ==============
	@RequestMapping(value = "/add.do")
	@ResponseBody
	public void add(final OftenUsedVo item, @RequestParam("categorySeq") final int categorySeq) {
		item.setCategory(categoryRepository.findById(categorySeq).get());
		oftenUsedRepository.save(item);
		// 정렬 값은 가장 나중에 입력된 순으로 함.
		// 별도의 다음 OrderNo를 계산하지 않고 중복 되지 않는 Key 값을 사용
		item.setOrderNo(item.getOftenUsedSeq());
	}

	// ============== 수정 ==============
	@RequestMapping(value = "/edit.do")
	@ResponseBody
	public void edit(final OftenUsedVo item, @RequestParam("categorySeq") final int categorySeq) {
		item.setCategory(categoryRepository.findById(categorySeq).get());
		oftenUsedRepository.save(item);
	}

	// ============== 삭제 ==============
	@RequestMapping(value = "/delete.do")
	@ResponseBody
	public void delete(@RequestParam("oftenUsedSeq") final int oftenUsedSeq) {
		OftenUsedVo often = oftenUsedRepository.findById(oftenUsedSeq).get();
		often.setDeleteFlag(true);
		oftenUsedRepository.save(often);
	}
}
