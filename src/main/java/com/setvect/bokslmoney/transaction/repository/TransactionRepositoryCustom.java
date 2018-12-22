package com.setvect.bokslmoney.transaction.repository;

import com.setvect.bokslmoney.transaction.controller.TransactionSearchParam;
import com.setvect.bokslmoney.transaction.vo.TransactionVo;
import com.setvect.bokslmoney.util.PageResult;

public interface TransactionRepositoryCustom {
	/**
	 * @param searchCondition
	 *            검색 조건
	 * @return 메모
	 */
	public PageResult<TransactionVo> getPagingList(TransactionSearchParam searchCondition);
}
