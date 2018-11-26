package com.setvect.bokslmoney.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * 날짜, 시간관련된 클래
 */
public abstract class DateUtil {
	/**
	 * Don't let anyone instantiate this class
	 */
	private DateUtil() {
	}

	/**
	 * check date string validation with the default format "yyyy-MM-dd".
	 *
	 * @param s
	 *            date string you want to check with default format "yyyy-MM-dd".
	 * @throws ParseException
	 *             파싱 예외
	 */
	public static void check(final String s) throws ParseException {
		DateUtil.check(s, "yyyy-MM-dd");
	}

	/**
	 * check date string validation with an user defined format.
	 *
	 * @param s
	 *            date string you want to check.
	 * @param format
	 *            string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 * @throws ParseException
	 *             파싱 예외
	 */
	public static void check(final String s, final String format) throws ParseException {
		if (s == null) {
			throw new NullPointerException("date string to check is null");
		}
		if (format == null) {
			throw new NullPointerException("format string to check date is null");
		}

		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);
		java.util.Date date = null;
		try {
			date = formatter.parse(s);
		} catch (java.text.ParseException e) {
			throw new java.text.ParseException(e.getMessage() + " with format \"" + format + "\"", e.getErrorOffset());
		}

		if (!formatter.format(date).equals(s)) {
			throw new java.text.ParseException("Out of bound date:\"" + s + "\" with format \"" + format + "\"", 0);
		}
	}

	/**
	 * 날짜 패턴을 검사함
	 *
	 * @param s
	 *            날짜 문자열
	 * @param format
	 *            검사 패턴 형식 <br>
	 *            예: "yyyy-MM-dd"
	 * @return 패턴에 적합하면 true
	 */
	public static boolean isDatePatten(final String s, final String format) {
		if (s == null || format == null) {
			return false;
		}

		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = formatter.parse(s);
		} catch (java.text.ParseException e) {
			return false;
		}

		if (!formatter.format(date).equals(s)) {
			return false;
		}
		return true;
	}

	/**
	 * @return DD 형식의 날짜
	 */
	public static int getDay() {
		return getNumberByPattern("dd");
	}

	/**
	 * For example, String time = DateTime.getFormatString("yyyy-MM-dd HH:mm:ss");
	 *
	 * @param pattern
	 *            "yyyy, MM, dd, HH, mm, ss and more"
	 * @return formatted string representation of current day and time with your
	 *         pattern.
	 */
	public static String getFormatString(final String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String dateString = formatter.format(new java.util.Date());
		return dateString;
	}

	/**
	 * 날짜를 원하는 패턴으로 리턴
	 *
	 * @param dd
	 *            날짜값
	 * @param pattern
	 *            "yyyy, MM, dd, HH, mm, ss and more"
	 *
	 * @return 변환된 날짜 스트링
	 */
	public static String getFormatString(final Date dd, final String pattern) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(pattern);
		String dateString = formatter.format(dd);
		return dateString;
	}

	/**
	 * 날짜를 원하는 패턴으로 리턴
	 *
	 * @param dd
	 *            날짜값
	 * @param pattern
	 *            "yyyy, MM, dd, HH, mm, ss and more"
	 *
	 * @return 변환된 날짜 스트링
	 */
	public static String getFormatString(final LocalDate dd, final String pattern) {
		Date date = Date.from(dd.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return getFormatString(date, pattern);
	}

	/**
	 * 날짜를 기본 패턴 (yyyy-MM-dd)로 리턴
	 *
	 * @param dd
	 *            날짜값
	 * @return 변환된 날짜 스트링
	 */
	public static String getFormatString(final Date dd) {
		return getFormatString(dd, "yyyy-MM-dd");
	}

	/**
	 * 날짜를 기본 패턴 (yyyy-MM-dd HH:mm:ss)로 리턴
	 *
	 * @param dd
	 *            날짜값
	 * @return 변환된 날짜 스트링
	 */
	public static String getFormatStringDateTime(final Date dd) {
		return getFormatString(dd, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * @return 현제 달(Month)
	 */

	public static int getMonth() {
		return getNumberByPattern("MM");
	}

	/**
	 * 현재 시간을 지정한 패턴으로 변환 <br>
	 * For example, String time = DateTime.getFormatString("yyyy-MM-dd HH:mm:ss");
	 *
	 * @param pattern
	 *            "yyyy, MM, dd, HH, mm, ss and more"
	 * @return formatted string representation of current day and time with your
	 *         pattern.
	 */
	public static int getNumberByPattern(final String pattern) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(pattern);
		String dateString = formatter.format(new java.util.Date());
		return Integer.parseInt(dateString);
	}

	/**
	 * @return formatted string representation of current day with "yyyyMMdd".
	 */
	public static String getShortDateString() {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.KOREA);
		return formatter.format(new java.util.Date());
	}

	/**
	 * @return formatted string representation of current time with "HHmmss".
	 */
	public static String getShortTimeString() {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("HHmmss", java.util.Locale.KOREA);
		return formatter.format(new java.util.Date());
	}

	/**
	 * @return formatted string representation of current time with
	 *         "yyyy-MM-dd-HH:mm:ss".
	 */
	public static String getTimeStampString() {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS",
				java.util.Locale.KOREA);
		return formatter.format(new java.util.Date());
	}

	/**
	 * @return formatted string representation of current time with "HH:mm:ss".
	 */
	public static String getTimeString() {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.KOREA);
		return formatter.format(new java.util.Date());
	}

	/**
	 * @return 현재 년도
	 */
	public static int getYear() {
		return getNumberByPattern("yyyy");
	}

	/**
	 * @return 현재 시간
	 */
	public static int getHour() {
		return getNumberByPattern("HH");
	}

	/**
	 * @return 현재 분
	 */
	public static int getMinute() {
		return getNumberByPattern("mm");
	}

	/**
	 * @return 현재 초
	 */
	public static int getSecond() {
		return getNumberByPattern("ss");
	}

	/**
	 * @return 현재시간을 YYYY-MM-DD으로 전달
	 */
	public static String getSysDate() {
		// Date클래스에서 현재의 날짜 시간을 가져옴
		java.util.Date datetime = new java.util.Date();
		java.text.SimpleDateFormat formattime = new SimpleDateFormat("yyyy-MM-dd");
		String texttime = formattime.format(datetime);

		return texttime;
	}

	/**
	 * 현재시간을 주어진 날자 형식으로 변환
	 *
	 * @param strFormat
	 *            주어진 날자형식
	 * @return 날짜 스트링
	 */
	public static String getSysDate(final String strFormat) {
		// Date클래스에서 현재의 날짜 시간을 가져옴
		java.util.Date datetime = new java.util.Date();
		java.text.SimpleDateFormat formattime = new SimpleDateFormat(strFormat);
		String texttime = formattime.format(datetime);

		return texttime;
	}

	/**
	 * @return 현지시간을 'yyyy-MM-dd HH:mm:ss' 이런 식으로 리턴
	 */
	public static String getSysDateTime() {
		// Date클래스에서 현재의 날짜 시간을 가져옴
		java.util.Date datetime = new java.util.Date();
		java.text.SimpleDateFormat formattime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String texttime = formattime.format(datetime);

		return texttime;
	}

	/**
	 * @param t
	 *            타임 스템프 형식의 시간
	 * @param pattern
	 *            표현할 패턴
	 * @return 변환된 패턴 스트링
	 */
	public static String getFormatString(final long t, final String pattern) {
		// Date클래스에서 현재의 날짜 시간을 가져옴
		java.util.Date datetime = new java.util.Date(t);
		java.text.SimpleDateFormat formattime = new SimpleDateFormat(pattern);
		String texttime = formattime.format(datetime);

		return texttime;
	}

	/**
	 * @param cal
	 *            시간값
	 * @return YYYY-MM-DD으로 전달
	 */
	public static String getFormatString(final Calendar cal) {
		java.text.SimpleDateFormat formattime = new SimpleDateFormat("yyyy-MM-dd");
		return formattime.format(cal.getTime());
	}

	/**
	 * @param cal
	 *            시간값
	 * @param pattern
	 *            표현할 패턴
	 * @return 패턴대로 리턴
	 */
	public static String getFormatString(final Calendar cal, final String pattern) {
		java.text.SimpleDateFormat formattime = new SimpleDateFormat(pattern);
		return formattime.format(cal.getTime());
	}

	/**
	 * 날짜를 기본 패턴 (yyyy-MM-dd HH:mm:ss)로 리턴
	 *
	 * @param cal
	 *            날짜값
	 * @return 변환된 날짜 스트링
	 */
	public static String getFormatStringDateTime(final Calendar cal) {
		return getFormatString(cal, "yyyy-MM-dd HH:mm:ssa");
	}

	/**
	 * 메소드 설명을 삽입하십시오.
	 *
	 * @param lDate
	 *            timestamp 형태
	 * @return 'yyyy-MM-dd' 형식으로 날짜 스트링
	 */
	public static String getDate(final long lDate) {
		// Date클래스에서 현재의 날짜 시간을 가져옴
		java.util.Date datetime = new java.util.Date(lDate);
		java.text.SimpleDateFormat formattime = new SimpleDateFormat("yyyy-MM-dd");
		String texttime = formattime.format(datetime);

		return texttime;
	}

	/**
	 * 시작일과 종요일 사이에 cod가 있으면 true 아니면 false 날짜 형식은 yyyy-MM-dd로 한다.
	 *
	 * @param cod
	 *            비교
	 * @param start
	 *            시작일
	 * @param end
	 *            종료일
	 * @return 날짜사이에 cond디가 있으면 true 앖으면 false
	 * @throws ParseException
	 *             파싱 예외
	 */
	public static boolean isBetween(final String cod, final String start, final String end) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		Calendar c = Calendar.getInstance();
		Calendar s = Calendar.getInstance();
		Calendar e = Calendar.getInstance();
		c.setTime(df.parse(cod));
		s.setTime(df.parse(start));
		e.setTime(df.parse(end));

		boolean between = (c.after(s) || c.equals(s)) && (c.before(e) || c.equals(e));
		return between;
	}

	/**
	 * 두날짜 차이 구하기
	 *
	 * @param startDate
	 *            날짜 시작
	 * @param format
	 *            날짜 종료
	 * @return 두 날짜의 차이 (타임스템프)
	 * @throws Exception
	 *             파싱 예외
	 */
	public static long dayDiff(final String startDate, final String format) throws Exception {
		return dayDiff(startDate, getFormatString("yyyy-MM-dd HH:mm:ss"), format);
	}

	/**
	 * 두 날짜 사이의 차이
	 *
	 * @param startDate
	 *            시작 날짜
	 * @param endDate
	 *            종료 날짜
	 * @param format
	 *            날짜 형식 기본값: yyyy-MM-dd HH:mm:ss.SSS
	 * @return long 날짜 차이 초단위
	 * @throws Exception
	 *             파싱 예외
	 */
	public static long dayDiff(final String startDate, final String endDate, final String format) throws Exception {
		String formatApply = format;
		if (formatApply == null) {
			formatApply = "yyyy-MM-dd HH:mm:ss.SSS";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(formatApply);
		Date sDate;
		Date eDate;
		long day2day = 0;
		try {
			sDate = sdf.parse(startDate);
			eDate = sdf.parse(endDate);
			day2day = (eDate.getTime() - sDate.getTime()) / 1000;
		} catch (Exception e) {
			throw new Exception("wrong format string");
		}

		return day2day;
	}

	/**
	 * @param startDate
	 *            시작날짜
	 * @param endDate
	 *            종료 날짜
	 * @param format
	 *            날짜 포맷 (ex: yyyy-MM-dd HH:mm:ss.SSS)
	 * @return 두 날짜에 차이 일수
	 */
	public static int dayDiffDate(final String startDate, final String endDate, final String format) {
		return dayDiffDate(getDate(startDate, format), getDate(endDate, format));
	}

	/**
	 * @param startDate
	 *            시작날짜
	 * @param endDate
	 *            종료 날자
	 * @return 두 날짜에 차이 일수
	 */
	public static int dayDiffDate(final Date startDate, final Date endDate) {
		long diff = (endDate.getTime() - startDate.getTime()) / 1000;
		return (int) (diff / (60 * 60 * 24));
	}

	/**
	 * 문자열의 날짜를 Date 타입으로 변환 기본 포맷 형식 "yyyy-MM-dd"
	 *
	 * @param dateString
	 *            문자열 날짜
	 * @return Date
	 */
	public static Date getDate(final String dateString) {
		return getDate(dateString, "yyyy-MM-dd");
	}

	/**
	 * 문자열의 날짜를 Date 타입으로 변환 기본 포맷 형식 "yyyy-MM-dd HH:mm:ss"
	 *
	 * @param dateString
	 *            문자열 날짜
	 * @return Date
	 */
	public static Date getDateTime(final String dateString) {
		return getDate(dateString, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 문자열의 시간를 Date 타입으로 변환 기본 포맷 형식 "HH:mm:ss"
	 *
	 * @param dateString
	 *            문자열 시간
	 * @return Date
	 */
	public static Date getTime(final String dateString) {
		return getDate(dateString, "HH:mm:ss");
	}

	/**
	 * 문자열의 날짜를 Date 타입으로 변환
	 *
	 * @param dateString
	 *            문자형 날짜
	 * @param dateFormat
	 *            데이터 포맷
	 * @return Date
	 */
	public static Date getDate(final String dateString, final String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date rtnValue = null;
		try {
			rtnValue = sdf.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return rtnValue;
	}

	/**
	 * 문자열의 날짜를 Calendar 타입으로 변환
	 *
	 * @param dateString
	 *            문자형 날짜
	 * @param dateFormat
	 *            데이터 포맷
	 * @return Date
	 */
	public static Calendar getCalendar(final String dateString, final String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date rtnValue = null;
		try {
			rtnValue = sdf.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		long l = rtnValue.getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(l);
		return cal;
	}

	/**
	 * 문자열로된 날짜를 특정 포맷에 맞게 스트링 형태로 변환후 리턴
	 *
	 * @param dateString
	 *            날짜로된 만자열
	 * @param paramF
	 *            전달될는 문자열 포맷
	 * @param retF
	 *            리턴될 포맷
	 * @return 리턴될 포맷
	 */
	public static String getDatePart(final String dateString, final String paramF, final String retF) {
		Date d = getDate(dateString, paramF);
		return getFormatString(d, retF);
	}

	/**
	 * 현재 일에서 diff를 더한 값을 리턴
	 *
	 * @param diff
	 *            날짜 차이값(일단위)
	 * @return 현재 일에서 diff를 더한 값을 리턴
	 */
	public static String getDiffDay(final int diff) {
		Calendar cal;
		cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, diff);

		return getFormatString(cal);
	}

	/**
	 * 현재 일에서 diff를 더한 값을 리턴
	 *
	 * @param diff
	 *            날짜 차이값(일단위)
	 * @return 현재 일에서 diff를 더한 값을 리턴
	 */
	public static String getDiffMonth(final int diff) {
		Calendar cal;
		cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, diff);

		return getFormatString(cal);
	}

	/**
	 * 해당 날짜가 포함된 주의 첫날로 이동.<br>
	 * 예) <br>
	 * 2011-01-01(토요일), 주 시작: 일요일 = > 2010-12-26<br>
	 * 2012-02-08(수요일), 주 시작: 월요일 = > 2012-02-06<br>
	 *
	 * @param cal
	 *            날짜
	 * @param fristWeek
	 *            주(week)에 시작 요일. Calendar 클래스 상수값 이용
	 * @return 주의 첫날로 이동된 날짜. 시간데이터는 변경되지 않음.
	 */
	public static Calendar getFirstWeekDate(final Calendar cal, final int fristWeek) {
		Calendar rtnValue = (Calendar) cal.clone();
		rtnValue.setFirstDayOfWeek(fristWeek);
		rtnValue.set(Calendar.DAY_OF_WEEK, fristWeek);
		return rtnValue;
	}

	/**
	 * @param localDate
	 *            .
	 * @return 날짜로 변환
	 */
	public static Date toDate(final LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * @param date
	 *            날짜
	 * @return LocalDate로 변환
	 */
	public static LocalDate toLocalDate(final Date date) {
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return localDate;
	}
}