package com.setvect.bokslmoney.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

/**
 * 파일 다운로드시 한글 관련 처리를 위한 클래스
 */
public abstract class FileDownUtil {

	/**
	 * 기본 생성자
	 */
	private FileDownUtil() {
	}

	/**
	 * 파일 다운로드 헤더 설정 브라우져의 종류에 따라 설정한다
	 *
	 * @param request
	 *            리퀘스트
	 * @param fileName
	 *            파일명
	 * @return 헤더 정보
	 * @throws UnsupportedEncodingException
	 *             문자열 변환 에러
	 */
	public static String getFileDownHeader(final HttpServletRequest request, final String fileName)
			throws UnsupportedEncodingException {

		String header = request.getHeader("User-Agent");
		String filename = fileName;

		String result = "";
		if (header.contains("MSIE") || header.contains("Trident")) { // IE 11버전부터 Trident로 변경되었기때문에 추가해준다.
			filename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
			result = "attachment;filename=" + filename + ";";
		} else if (header.contains("Chrome")) {
			filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
			result = "attachment; filename=\"" + filename + "\"";
		} else if (header.contains("Opera")) {
			filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
			result = "attachment; filename=\"" + filename + "\"";
		} else if (header.contains("Firefox")) {
			filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
			result = "attachment; filename=\"" + filename + "\"";
		}

		return result;
	}

}