package com.setvect.bokslmoney.hab.repository;

import com.setvect.bokslmoney.hab.controller.TransactionSearchParam;
import com.setvect.bokslmoney.hab.vo.TransactionVo;
import com.setvect.bokslmoney.util.PageResult;

public interface TransactionRepositoryCustom {
	/**
	 * @param searchCondition
	 *            검색 조건
	 * @return 메모
	 */
	public PageResult<TransactionVo> getPagingList(TransactionSearchParam searchCondition);
}
