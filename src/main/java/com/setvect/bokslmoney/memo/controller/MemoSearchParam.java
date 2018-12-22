package com.setvect.bokslmoney.memo.controller;

import java.util.Date;

import com.setvect.bokslmoney.util.SearchListParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemoSearchParam extends SearchListParam {
	/** 날짜 범위: 시작 */
	private Date from;

	/** 날짜 범위: 종료 */
	private Date to;

	public MemoSearchParam(final int startCursor, final int returnCount) {
		super(startCursor, returnCount);
	}

	/**
	 * @return 날짜 범위 검색 여부 true, 아니면 false
	 */
	public boolean isRangeSearch() {
		return from != null && to != null;
	}
}
