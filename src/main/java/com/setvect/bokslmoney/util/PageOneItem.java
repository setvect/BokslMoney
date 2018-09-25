package com.setvect.bokslmoney.util;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 리스트 타입으로 표현하지 못하는 항목를 페이징 처리 할 때 사용
 *
 * @param <T>
 *            아이템 객체
 */
public class PageOneItem<T> extends PageNavigation {
	/** 페이징 항목 */
	private final T item;

	/**
	 * @param item
	 *            항목
	 * @param startCursor
	 *            시작 항목(0부터 시작)
	 * @param totalCount
	 *            전체 항목 수
	 * @param returnCount
	 *            한 페이지당 반환할 항목 수
	 */
	public PageOneItem(final T item, final int startCursor, final int totalCount, final int returnCount) {
		super(startCursor, totalCount, returnCount);
		this.item = item;
	}

	/**
	 * @return 페이징 항목
	 */
	public T getItem() {
		return item;
	}

	/**
	 * @param <T>
	 *            반환 데이터
	 * @return 빈 데이터
	 */
	public static <T> PageOneItem<T> empty() {
		return new PageOneItem<>(null, 0, 0, 0);
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