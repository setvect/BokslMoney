package com.setvect.bokslmoney.hab.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.setvect.bokslmoney.hab.repository.TransactionKindRepository;
import com.setvect.bokslmoney.hab.vo.KindType;
import com.setvect.bokslmoney.hab.vo.TransactionKindVo;
import com.setvect.bokslmoney.util.TreeNode;

@Service
public class TransactionKindService {

	@Autowired
	private TransactionKindRepository itemRepository;

	private static int ROOT_ITEM_SEQ = 0;

	/**
	 * @return 항목 정보를 계층적으로 제공
	 */
	public Map<KindType, List<TreeNode<TransactionKindVo>>> listHierarchyAll() {
		Map<KindType, List<TreeNode<TransactionKindVo>>> r = Stream.of(KindType.values())
				.collect(Collectors.toMap(Function.identity(), k -> listHierarchy(k)));
		return r;
	}

	/**
	 * @param kindType
	 * @return
	 */
	private List<TreeNode<TransactionKindVo>> listHierarchy(final KindType kindType) {
		TransactionKindVo rootItem = new TransactionKindVo();
		rootItem.setTransactionKindSeq(0);
		rootItem.setName("root");

		// TreeNode<ItemVo> rootTree = new TreeNode<ItemVo>(rootItem);

		List<TransactionKindVo> allItem = itemRepository.list(kindType, 0);

		List<TreeNode<TransactionKindVo>> result = allItem.stream().map(itemDepth1 -> {
			TreeNode<TransactionKindVo> depth1Tree = new TreeNode<TransactionKindVo>(itemDepth1);
			List<TransactionKindVo> itemList = itemRepository.list(kindType, itemDepth1.getTransactionKindSeq());
			itemList.forEach(itemDepth2 -> {
				depth1Tree.addChild(itemDepth2);
			});
			return depth1Tree;
		}).collect(Collectors.toList());

		return result;
	}

}
