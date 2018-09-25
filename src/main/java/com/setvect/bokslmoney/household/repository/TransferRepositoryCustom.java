package com.setvect.bokslmoney.household.repository;

import com.setvect.bokslmoney.household.controller.TransferSearchParam;
import com.setvect.bokslmoney.household.vo.TransferVo;
import com.setvect.bokslmoney.util.PageResult;

public interface TransferRepositoryCustom {
	/**
	 * @param searchCondition
	 *            검색 조건
	 * @return 메모
	 */
	public PageResult<TransferVo> getPagingList(TransferSearchParam searchCondition);
}
