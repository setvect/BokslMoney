package com.setvect.bokslmoney.household.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.setvect.bokslmoney.household.vo.KindType;
import com.setvect.bokslmoney.util.SearchListParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransferSearchParam extends SearchListParam {
	/** 날짜 범위: 시작 */
	private Date from;

	/** 날짜 범위: 종료 */
	private Date to;

	/** 메모 검색 */
	private String note;

	/** 유형 검색 */
	private KindType kindType;

	/**
	 * @param startCursor
	 *            시작
	 * @param returnCount
	 *            반환
	 */
	@Autowired
	public TransferSearchParam(final int startCursor, final int returnCount) {
		super(startCursor, returnCount);
	}

	/**
	 * @return 날짜 범위 검색 여부 true, 아니면 false
	 */
	public boolean isRangeSearch() {
		return from != null && to != null;
	}
}
