package com.setvect.bokslmoney.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 날짜의 범위를 나타내줌 <br>
 * 날짜 범위 검색에 필요한 파라미터 역활을 할 수 있음
 */
public class DateRange implements Comparable<DateRange> {
	/** 시작 날짜 */
	private final Date start;

	/** 종료 날짜 */
	private final Date end;

	/** 기간 제한 없는 날짜 시작일 */
	public static final String UNLIMITE_DATE_START = "1990-01-01";

	/** 기간 제한 없는 날짜 종료일 */
	public static final String UNLIMITE_DATE_END = "2100-12-31";

	/**
	 * @return 1990-01-01 ~ 2100-12-31 날짜 범위 리턴
	 * @see #UNLIMITE_DATE_START
	 * @see #UNLIMITE_DATE_END
	 */
	public static DateRange getMaxRange() {
		return new DateRange(UNLIMITE_DATE_START, UNLIMITE_DATE_END);
	}

	/**
	 * 오늘 날짜를 기준으로 해서 차이 값을 생성 한다.
	 *
	 * @param diff
	 *            날짜 단위 차이
	 */
	public DateRange(final int diff) {
		String st;
		String ed;

		Calendar cal;

		// 양수
		if (diff > 0) {
			cal = Calendar.getInstance();
			st = DateUtil.getFormatString(cal);
			cal.add(Calendar.DAY_OF_YEAR, diff);
			ed = DateUtil.getFormatString(cal);
		} else {
			// 음수
			cal = Calendar.getInstance();
			ed = DateUtil.getFormatString(cal);
			cal.add(Calendar.DAY_OF_YEAR, diff);
			st = DateUtil.getFormatString(cal);
		}

		start = DateUtil.getDate(st, "yyyy-MM-dd");
		end = DateUtil.getDate(ed, "yyyy-MM-dd");

	}

	/**
	 * 날짜 범위를 해당 년도의 달에 1부터 그달의 마지막으로 한다.
	 *
	 * @param year
	 *            년도
	 * @param month
	 *            달 0: 1월달, 1: 2월달, ..., 11: 12월달
	 */
	public DateRange(final int year, final int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);

		// 해당 달의 맨 끝에 날짜로 가기위해서
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DATE, -1);
		end = cal.getTime();

		cal.set(Calendar.DATE, 1);
		start = cal.getTime();
	}

	/**
	 * 날짜영역 객체 생성. 기본 날짜 포맷 (yyyy-MM-dd)으로 날짜 변환
	 *
	 * @param st
	 *            시작날짜
	 * @param ed
	 *            종료날짜
	 */
	public DateRange(final String st, final String ed) {
		this(st, ed, "yyyy-MM-dd");
	}

	/**
	 * 날짜영역 객체 생성. 기본 날짜 포맷 (yyyy-MM-dd)으로 날짜 변환
	 *
	 * @param st
	 *            시작날짜
	 * @param ed
	 *            종료날짜
	 * @param format
	 *            날짜 패턴 "yyyy, MM, dd, HH, mm, ss and more"
	 */
	public DateRange(final String st, final String ed, final String format) {
		start = DateUtil.getDate(st, format);
		end = DateUtil.getDate(ed, format);
	}

	/**
	 * 날짜영역 객체 생성.
	 *
	 * @param st
	 *            시작일
	 * @param ed
	 *            종료일
	 */
	public DateRange(final Date st, final Date ed) {
		start = st;
		end = ed;
	}

	/**
	 * @return 종료날짜를 리턴합니다.
	 */
	public Date getEnd() {
		return end;
	}

	/**
	 * @return 시작날짜를 리턴합니다.
	 */
	public Date getStart() {
		return start;
	}

	/**
	 * @return 종료날짜를 "yyyy-MM-dd" 형태로 리턴합니다.
	 */
	public String getEndString() {
		return DateUtil.getFormatString(end);
	}

	/**
	 * @return 시작날짜를 "yyyy-MM-dd" 형태로 리턴합니다.
	 */
	public String getStartString() {
		return DateUtil.getFormatString(start);
	}

	/**
	 * @param format
	 *            날짜 패턴 "yyyy, MM, dd, HH, mm, ss and more"
	 * @return 종료날짜를 포맷 형태로 리턴합니다.
	 */
	public String getEndString(final String format) {
		return DateUtil.getFormatString(end, format);
	}

	/**
	 * @param format
	 *            날짜 패턴 "yyyy, MM, dd, HH, mm, ss and more"
	 * @return 종료날짜를 포맷 형태로 리턴합니다.
	 */
	public String getStartString(final String format) {
		return DateUtil.getFormatString(start, format);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DateRange)) {
			return false;
		}
		DateRange other = (DateRange) obj;
		if (end == null) {
			if (other.end != null) {
				return false;
			}
		} else if (!end.equals(other.end)) {
			return false;
		}
		if (start == null) {
			if (other.start != null) {
				return false;
			}
		} else if (!start.equals(other.start)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "DateRange [start=" + getStartString("yyyy-MM-dd HH:mm:ss") + ", end="
				+ getEndString("yyyy-MM-dd HH:mm:ss") + "]";
	}

	@Override
	public int compareTo(final DateRange o) {
		int c = this.start.compareTo(o.start);
		if (c != 0) {
			return c;
		}
		c = this.end.compareTo(o.end);
		return c;
	}
}