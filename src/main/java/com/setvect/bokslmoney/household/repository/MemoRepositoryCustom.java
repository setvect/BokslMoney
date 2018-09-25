package com.setvect.bokslmoney.household.repository;

import com.setvect.bokslmoney.household.controller.MemoSearchParam;
import com.setvect.bokslmoney.household.vo.MemoVo;
import com.setvect.bokslmoney.util.PageResult;

public interface MemoRepositoryCustom {
	/**
	 * @param searchCondition
	 *            검색 조건
	 * @return 메모
	 */
	public PageResult<MemoVo> getPagingList(MemoSearchParam searchCondition);
}
