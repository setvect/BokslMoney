package com.setvect.bokslmoney.hab.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.setvect.bokslmoney.hab.repository.CategoryRepository;
import com.setvect.bokslmoney.hab.vo.KindType;
import com.setvect.bokslmoney.hab.vo.CategoryVo;
import com.setvect.bokslmoney.util.TreeNode;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository itemRepository;

	private static int ROOT_ITEM_SEQ = 0;

	/**
	 * @return 항목 정보를 계층적으로 제공
	 */
	public Map<KindType, List<TreeNode<CategoryVo>>> listHierarchyAll() {
		Map<KindType, List<TreeNode<CategoryVo>>> r = Stream.of(KindType.values())
				.collect(Collectors.toMap(Function.identity(), k -> listHierarchy(k)));
		return r;
	}

	/**
	 * @param kindType
	 * @return
	 */
	private List<TreeNode<CategoryVo>> listHierarchy(final KindType kindType) {
		CategoryVo rootItem = new CategoryVo();
		rootItem.setCategorySeq(0);
		rootItem.setName("root");

		// TreeNode<ItemVo> rootTree = new TreeNode<ItemVo>(rootItem);

		List<CategoryVo> allItem = itemRepository.list(kindType, 0);

		List<TreeNode<CategoryVo>> result = allItem.stream().map(itemDepth1 -> {
			TreeNode<CategoryVo> depth1Tree = new TreeNode<CategoryVo>(itemDepth1);
			List<CategoryVo> itemList = itemRepository.list(kindType, itemDepth1.getCategorySeq());
			itemList.forEach(itemDepth2 -> {
				depth1Tree.addChild(itemDepth2);
			});
			return depth1Tree;
		}).collect(Collectors.toList());

		return result;
	}

}