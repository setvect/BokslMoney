package com.setvect.bokslmoney.util;

import java.io.PrintStream;
import java.text.DecimalFormat;

/**
 * 시간을 체크 한다.<br>
 * 프로그램 코드 진행 시간을 체크 할때 사용한다.
 *
 * @version $Id: LapTimeChecker.java,v 1.1 2013/07/08 02:23:24 jhjang Exp $
 */
public class LapTimeChecker {

	/** 1초를 밀리세컨드로 */
	private static final int SEC_FOR_MS = 1000;
	/** 1분을 밀리세컨드로 */
	private static final int MIN_FOR_MS = SEC_FOR_MS * 60;
	/** 1시간을 밀리세컨드로 */
	private static final int HOUR_FOR_MS = MIN_FOR_MS * 60;
	/** 하루를 밀리세컨드로 */
	private static final int DAY_FOR_MS = HOUR_FOR_MS * 24;

	private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("0000");
	private static final DecimalFormat NUMBER_COMMA_FORMAT = new DecimalFormat(",###");
	private static final DecimalFormat NUMBER_TIME_FORMAT = new DecimalFormat("00");

	/** 체크 시작 시간 */
	private long startTime;

	/** 출력 스트림 */
	private PrintStream out;

	/** 체크 카운트 */
	private int checkCount;

	/** 이름 */
	private String name;

	/** 전체 카운트 */
	private int totalCount;
	/** 처리된 건수 */
	private int processCount;

	/**
	 * 기본 생성자<br>
	 * 메세지 출력은 System.out 에서 함
	 *
	 * @param name
	 *            구분 이름
	 */
	public LapTimeChecker(final String name) {
		this(System.out, name);
	}

	/**
	 * @param out
	 *            출력 스트림
	 * @param name
	 *            구분 이름
	 */
	public LapTimeChecker(final PrintStream out, final String name) {
		this.name = name;
		this.out = out;
		startTime = System.currentTimeMillis();
		this.out.println(name + " start [" + startTime + "]");
	}

	/**
	 * 시간 체크
	 */
	public void check() {
		check("");
	}

	/**
	 * 시간 체크
	 *
	 * @param message
	 *            출력 메시지
	 */
	public void check(final String message) {
		out.println(getCheckMessage(message));
	}

	/**
	 * @param message
	 *            출력 메시지
	 * @return 시간 체크 출력 메세지를 문자열
	 */
	public String getCheckMessage(final String message) {
		checkCount++;
		long cur = System.currentTimeMillis();
		String msg = name + ", " + NUMBER_FORMAT.format(checkCount) + ", " + NUMBER_COMMA_FORMAT.format(cur - startTime)
				+ "ms, " + message;
		return msg;
	}

	/**
	 * @param processCount
	 *            처리된 건수
	 * @return 예상 남은 시간을 ms 단위로 리턴
	 */
	public long getRemainTime(final int processCount) {
		long running = System.currentTimeMillis() - startTime;
		if (processCount == 0) {
			return 0;
		}
		return (running * totalCount / processCount) - running;
	}

	/**
	 * @return 예상 남은 시간을 정해진 포맷에 의해 문자열로 제공
	 */
	public String getRemainTimeFormat() {
		return getRemainTimeFormat(this.processCount);
	}

	/**
	 * @param processCount
	 *            처리된 건수
	 * @return 예상 남은 시간을 정해진 포맷에 의해 문자열로 제공
	 */
	public String getRemainTimeFormat(final int processCount) {
		long time = getRemainTime(processCount);
		return getTimeToDayFormat(time);
	}

	/**
	 * @return 수행 시간을 날짜/시간 포맷으로
	 */
	public String getRunningTimeFormat() {
		long runningTime = System.currentTimeMillis() - startTime;
		return getTimeToDayFormat(runningTime);
	}

	/**
	 * @param t
	 *            진행 시간
	 * @return 진행 시간를 포맷팅해 표현
	 */
	private String getTimeToDayFormat(final long t) {
		long time = t;
		long day = time / DAY_FOR_MS;
		time = time % DAY_FOR_MS;

		long hour = time / HOUR_FOR_MS;
		time = time % HOUR_FOR_MS;

		long min = time / MIN_FOR_MS;
		time = time % MIN_FOR_MS;

		long sec = time / SEC_FOR_MS;

		StringBuffer rtnValue = new StringBuffer();
		if (day != 0) {
			rtnValue.append(NUMBER_TIME_FORMAT.format(day) + "day(s) ");
		}
		rtnValue.append(NUMBER_TIME_FORMAT.format(hour) + ":");
		rtnValue.append(NUMBER_TIME_FORMAT.format(min) + ":");
		rtnValue.append(NUMBER_TIME_FORMAT.format(sec));
		return rtnValue.toString();
	}

	/**
	 * 현재 진행 시간 대비 전체 카운트를 계산하여 총 예상 시간을 구함
	 *
	 * @param totalCount
	 *            총 작업 건수
	 */
	public void setTotalCount(final int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 총 작업 건수
	 *
	 * @return the totalCount
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * 처리된 건수
	 *
	 * @return the processCount
	 */
	public int getProcessCount() {
		return processCount;
	}

	/**
	 * 처리된 건수
	 *
	 * @param processCount
	 *            the processCount to set
	 */
	public void setProcessCount(final int processCount) {
		this.processCount = processCount;
	}

}