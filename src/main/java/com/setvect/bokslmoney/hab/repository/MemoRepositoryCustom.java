package com.setvect.bokslmoney.hab.repository;

import com.setvect.bokslmoney.hab.controller.MemoSearchParam;
import com.setvect.bokslmoney.hab.vo.MemoVo;
import com.setvect.bokslmoney.util.PageResult;

public interface MemoRepositoryCustom {
	/**
	 * @param searchCondition
	 *            검색 조건
	 * @return 메모
	 */
	public PageResult<MemoVo> getPagingList(MemoSearchParam searchCondition);
}
