package com.setvect.bokslmoney.hab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.setvect.bokslmoney.hab.repository.CategoryRepository;
import com.setvect.bokslmoney.hab.repository.TransactionRepository;
import com.setvect.bokslmoney.hab.vo.CategoryVo;
import com.setvect.bokslmoney.hab.vo.TransactionVo;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transferRepository;

	@Autowired
	private CategoryRepository itemRepository;

	/**
	 * 부모 수입, 지출, 이체 항목
	 *
	 * @param list
	 *            .
	 */
	public void mappingParentItem(final List<TransactionVo> list) {
		list.stream().forEach(t -> {
			int parentSeq = t.getCategory().getParentSeq();
			CategoryVo parent = itemRepository.findById(parentSeq).orElse(null);
			t.setParentCategory(parent);
		});
	}
}
