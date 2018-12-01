package com.setvect.bokslmoney.hab.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.setvect.bokslmoney.BokslMoneyConstant;
import com.setvect.bokslmoney.hab.vo.KindType;
import com.setvect.bokslmoney.util.SearchListParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransactionSearchParam extends SearchListParam {
	/** 날짜 범위: 시작 */
	private Date from;

	/** 날짜 범위: 종료 */
	private Date to;

	/** 메모 검색 */
	private String note;

	/** 유형 검색 */
	private Set<KindType> kindTypeSet;

	/**
	 */
	public TransactionSearchParam() {
		super(0, BokslMoneyConstant.Web.DEFAULT_PAGE_SIZE);
	}

	/**
	 * @param startCursor
	 *            시작
	 * @param returnCount
	 *            반환
	 */
	public TransactionSearchParam(final int startCursor, final int returnCount) {
		super(startCursor, returnCount);
	}

	/**
	 * @return 날짜 범위 검색 여부 true, 아니면 false
	 */
	public boolean isRangeSearch() {
		return from != null && to != null;
	}

	/**
	 * @param kindType
	 *            .
	 */
	public void setKindType(final KindType kindType) {
		kindTypeSet = new HashSet<>(Arrays.asList(kindType));

	}
}
