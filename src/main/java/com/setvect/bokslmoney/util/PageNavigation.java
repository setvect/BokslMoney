package com.setvect.bokslmoney.util;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.setvect.bokslmoney.BokslMoneyConstant;

/**
 * Page 네비게이션([1] [2] [3] [4] [5] ...) 정보를 출력하기 위한 정보<br>
 */
public abstract class PageNavigation {
	/** 시작 항목(0부터 시작) */
	private final int startCursor;
	/** 전체 항목 수 */
	private final int totalCount;
	/** 전체 페이지 수 */
	private final int totalPage;
	/** 현재 페이지 번호(1부터 시작) */
	private final int currentPage;
	/** 한 페이지당 반환할 항목 수 */
	private final int returnCount;

	/**
	 * 한 번에 보여질 최대 페이지 번호 갯수<br>
	 * [1] [2] [3] [4] ... 이런것
	 */
	private int visiblePage;

	/**
	 * @param startCursor
	 *            시작 항목(0부터 시작)
	 * @param totalCount
	 *            전체 항목 수
	 * @param returnCount
	 *            한 페이지당 반환할 항목 수
	 */
	public PageNavigation(final int startCursor, final int totalCount, final int returnCount) {
		this.startCursor = startCursor;
		this.totalCount = totalCount;
		this.returnCount = returnCount;
		int p1 = (int) Math.ceil((double) (startCursor + 1) / returnCount);
		int p2 = (int) Math.ceil((double) (totalCount + 1) / returnCount);
		this.currentPage = Math.min(p1, p2);
		this.totalPage = (int) Math.ceil((double) totalCount / returnCount);
		this.visiblePage = BokslMoneyConstant.Web.DEFAULT_VISIBLE_PAGE;
	}

	/**
	 * @return 시작 항목(0부터 시작)
	 */
	public int getStartCursor() {
		return startCursor;
	}

	/**
	 * @return 전체 항목 수
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * @return 전체 페이지 수
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * @return 현재 페이지 번호(1부터 시작)
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * 한 번에 보여질 최대 페이지 번호 갯수<br>
	 * [1] [2] [3] [4] ... 이런것
	 *
	 * @return 한 번에 보여질 최대 페이지 번호
	 */
	public int getVisiblePage() {
		return visiblePage;
	}

	/**
	 * 한 번에 보여질 최대 페이지 번호 갯수<br>
	 * [1] [2] [3] [4] ... 이런것
	 *
	 * @param visiblePage
	 *            한 번에 보여질 최대 페이지 번호
	 */
	public void setVisiblePage(final int visiblePage) {
		this.visiblePage = visiblePage;
	}

	/**
	 * @return 한 페이지당 반환할 항목 수
	 */
	public int getReturnCount() {
		return returnCount;
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