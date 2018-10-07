package com.setvect.bokslmoney.hab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.setvect.bokslmoney.hab.repository.ItemRepository;
import com.setvect.bokslmoney.hab.repository.TransferRepository;
import com.setvect.bokslmoney.hab.vo.ItemVo;
import com.setvect.bokslmoney.hab.vo.TransferVo;

@Service
public class TransferService {

	@Autowired
	private TransferRepository transferRepository;

	@Autowired
	private ItemRepository itemRepository;

	/**
	 * 부모 수입, 지출, 이체 항목
	 *
	 * @param list
	 *            .
	 */
	public void mappingParentItem(final List<TransferVo> list) {
		list.stream().forEach(t -> {
			ItemVo parent = itemRepository.findById(t.getItem().getParentItemSeq()).get();
			t.setParentItem(parent);
		});
	}
}
