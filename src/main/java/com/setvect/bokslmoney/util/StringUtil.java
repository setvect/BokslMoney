package com.setvect.bokslmoney.util;

import org.apache.commons.lang.StringUtils;

/**
 * 문자열 처리 관련 유틸 클래스
 */
public abstract class StringUtil {

	/**
	 * 기본 생성자
	 */
	private StringUtil() {
	}

	/**
	 * @param str
	 *            처리할 문자열
	 * @return 특수문자 제거된 문자열
	 */
	public static String specialRemove(final String str) {
		if (StringUtils.isNotEmpty(str)) {
			String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
			return str.replaceAll(match, " ");
		} else {
			return "";
		}
	}

	/**
	 * @param str
	 *            처리할 문자열
	 * @return 스페이스가 제거된 문자열
	 */
	public static String spaceRemove(final String str) {
		if (StringUtils.isNotEmpty(str)) {
			String target = str;
			String match2 = "\\s{2,}";
			target = target.replaceAll(match2, "");
			target = target.replaceAll("\\p{Z}", "");
			target.trim();
			return target;
		} else {
			return "";
		}

	}

	/**
	 * @param s
	 *            확인할 문자열
	 * @return 숫자인지 판단
	 */
	public static boolean isNumeric(final String s) {
		return s.matches("^[0-9]+$");
	}

	/**
	 * @param bytes
	 *            바이트 크기
	 * @param si
	 *            단위 종류 기본 false
	 * @return 변환된 값
	 */
	public static String readableByteCount(final long bytes, final boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit) {
			return bytes + " B";
		}
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(-1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
}