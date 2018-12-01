package com.setvect.bokslmoney.hab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.setvect.bokslmoney.hab.controller.TransactionSearchParam;
import com.setvect.bokslmoney.hab.repository.CategoryRepository;
import com.setvect.bokslmoney.hab.repository.TransactionRepository;
import com.setvect.bokslmoney.hab.vo.CategoryVo;
import com.setvect.bokslmoney.hab.vo.TransactionVo;
import com.setvect.bokslmoney.util.PageResult;

@Service
public class TransactionService {
	/** 거래 내역 */
	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	/**
	 * @param searchCondition
	 *            조회 조건
	 * @return 거래 내역
	 */
	public List<TransactionVo> list(final TransactionSearchParam searchCondition) {
		PageResult<TransactionVo> page = transactionRepository.getPagingList(searchCondition);
		List<TransactionVo> list = page.getList();
		mappingParentItem(list);
		return list;
	}

	/**
	 * 분류 항목(이체, 수입, 지출)의 부모 분류 넣기
	 *
	 * @param list
	 *            .
	 */
	private void mappingParentItem(final List<TransactionVo> list) {
		list.stream().forEach(t -> {
			int parentSeq = t.getCategory().getParentSeq();
			CategoryVo parent = categoryRepository.findById(parentSeq).get();
			t.setParentCategory(parent);
		});
	}
}
