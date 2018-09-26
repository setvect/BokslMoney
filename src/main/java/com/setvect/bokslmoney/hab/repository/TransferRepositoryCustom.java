package com.setvect.bokslmoney.hab.repository;

import com.setvect.bokslmoney.hab.controller.TransferSearchParam;
import com.setvect.bokslmoney.hab.vo.TransferVo;
import com.setvect.bokslmoney.util.PageResult;

public interface TransferRepositoryCustom {
	/**
	 * @param searchCondition
	 *            검색 조건
	 * @return 메모
	 */
	public PageResult<TransferVo> getPagingList(TransferSearchParam searchCondition);
}
