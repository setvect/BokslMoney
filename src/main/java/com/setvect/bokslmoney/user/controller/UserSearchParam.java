package com.setvect.bokslmoney.user.controller;

import com.setvect.bokslmoney.BokslMoneyConstant;
import com.setvect.bokslmoney.util.SearchListParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 사용자 검색 조건
 */
@Getter
@Setter
@ToString
public class UserSearchParam extends SearchListParam {
	/** 사용자 이름 */
	private String name;

	/**
	 *
	 */
	public UserSearchParam() {
		super(0, BokslMoneyConstant.Web.DEFAULT_PAGE_SIZE);
	}

	/**
	 * @param startCursor
	 *            시작 지점 (0부터 시작)
	 * @param returnCount
	 *            가져올 항목 갯수
	 */
	public UserSearchParam(final int startCursor, final int returnCount) {
		super(startCursor, returnCount);
	}

}
