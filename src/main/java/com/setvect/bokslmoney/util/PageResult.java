package com.setvect.bokslmoney.util;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Page에서 검색된 객체들의 타입을 지정함
 *
 * @param <T>
 *            아이템 객체
 */
public class PageResult<T> extends PageNavigation {
	/** 목록 객체 */
	private final List<T> list;

	/**
	 * @param list
	 *            리스트
	 * @param startCursor
	 *            시작 항목(0부터 시작)
	 * @param totalCount
	 *            전체 항목 수
	 * @param returnCount
	 *            한 페이지당 반환할 항목 수
	 */
	public PageResult(final List<T> list, final int startCursor, final int totalCount, final int returnCount) {
		super(startCursor, totalCount, returnCount);
		this.list = list;
	}

	/**
	 * @return 목록 값
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * @param <T>
	 *            반환 데이터
	 * @return 빈 데이터
	 */
	public static <T> PageResult<T> empty() {
		return new PageResult<>(Collections.emptyList(), 0, 0, 0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}