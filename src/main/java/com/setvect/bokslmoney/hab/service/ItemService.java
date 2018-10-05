package com.setvect.bokslmoney.hab.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.setvect.bokslmoney.hab.repository.ItemRepository;
import com.setvect.bokslmoney.hab.vo.ItemVo;
import com.setvect.bokslmoney.hab.vo.KindType;
import com.setvect.bokslmoney.util.TreeNode;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;

	private static int ROOT_ITEM_SEQ = 0;

	/**
	 * @return 항목 정보를 계층적으로 제공
	 */
	public Map<KindType, List<TreeNode<ItemVo>>> listHierarchyAll() {
		Map<KindType, List<TreeNode<ItemVo>>> r = Stream.of(KindType.values())
				.collect(Collectors.toMap(Function.identity(), k -> listHierarchy(k)));
		return r;
	}

	/**
	 * @param kindType
	 * @return
	 */
	private List<TreeNode<ItemVo>> listHierarchy(final KindType kindType) {
		ItemVo rootItem = new ItemVo();
		rootItem.setItemSeq(0);
		rootItem.setName("root");

		// TreeNode<ItemVo> rootTree = new TreeNode<ItemVo>(rootItem);

		List<ItemVo> allItem = itemRepository.list(kindType, 0);

		List<TreeNode<ItemVo>> result = allItem.stream().map(itemDepth1 -> {
			TreeNode<ItemVo> depth1Tree = new TreeNode<ItemVo>(itemDepth1);
			List<ItemVo> itemList = itemRepository.list(kindType, itemDepth1.getItemSeq());
			itemList.forEach(itemDepth2 -> {
				depth1Tree.addChild(itemDepth2);
			});
			return depth1Tree;
		}).collect(Collectors.toList());

		return result;
	}

}
