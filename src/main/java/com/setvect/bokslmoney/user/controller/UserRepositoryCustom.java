package com.setvect.bokslmoney.user.controller;

import com.setvect.bokslmoney.user.vo.UserVo;
import com.setvect.bokslmoney.util.PageResult;

/**
 * 사용자 목록
 */
public interface UserRepositoryCustom {

	/**
	 * @param searchCondition
	 *            검색 조건
	 * @return 사용자 목록
	 */
	public PageResult<UserVo> getArticlePagingList(UserSearchParam searchCondition);
}
