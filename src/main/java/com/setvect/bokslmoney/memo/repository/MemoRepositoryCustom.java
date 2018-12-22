package com.setvect.bokslmoney.memo.repository;

import com.setvect.bokslmoney.memo.controller.MemoSearchParam;
import com.setvect.bokslmoney.memo.vo.MemoVo;
import com.setvect.bokslmoney.util.PageResult;

public interface MemoRepositoryCustom {
	/**
	 * @param searchCondition
	 *            검색 조건
	 * @return 메모
	 */
	public PageResult<MemoVo> getPagingList(MemoSearchParam searchCondition);
}
