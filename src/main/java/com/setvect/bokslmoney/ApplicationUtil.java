package com.setvect.bokslmoney;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.setvect.bokslmoney.user.vo.UserRoleVo;

/**
 * 어플리케이션 전반에 사용되는 공통 함수 제공.
 */
public abstract class ApplicationUtil {

	/** 로깅 */
	private static Logger logger = LoggerFactory.getLogger(ApplicationUtil.class);

	/**
	 * @param userRoles
	 *            Role 정보
	 * @return UserRoleVo를 GrantedAuthority로 변환
	 */
	public static List<GrantedAuthority> buildUserAuthority(final Set<UserRoleVo> userRoles) {
		List<GrantedAuthority> authList = userRoles.stream()
				.map(x -> new SimpleGrantedAuthority(x.getRoleName().name())).collect(Collectors.toList());
		return authList;
	}

	/**
	 * filePath에서 basePath 경로를 제외.<br>
	 * 예)<br>
	 * basePath = /home/user/<br>
	 * filePath = /home/user/temp/readme.txt<br>
	 * 리턴값: temp/read.txt
	 *
	 * @param basePath
	 *            기준 경로(OS Full Path)
	 * @param filePath
	 *            파일 경로(OS Full Path)
	 * @return filePath에서 basePath 경로를 제외된 값
	 */
	public static String getRelativePath(final File basePath, final File filePath) {
		String dir = basePath.toURI().relativize(filePath.toURI()).getPath();
		return dir;
	}

	/**
	 * @param keyword
	 *            검색어
	 * @return 주어진 검색어에 like 검색을 할 수 있도록 양쪽에 % 넣기
	 */
	public static String makeLikeString(final String keyword) {
		return "%" + keyword + "%";
	}

	/**
	 * 파일 다운로드
	 *
	 * @param response
	 *            .
	 * @param targetFile
	 *            다운로드 대상 파일
	 * @param downloadFileName
	 *            다운로드 파일 이름
	 * @throws IOException
	 *             .
	 */
	public static void downloadFile(final HttpServletResponse response, final File targetFile,
			final String downloadFileName) throws IOException {
		String fileName = URLEncoder.encode(downloadFileName.replace(" ", "_"), "UTF-8");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		try (FileInputStream fis = new FileInputStream(targetFile); OutputStream os = response.getOutputStream();) {
			FileCopyUtils.copy(fis, os);
		}
	}

	/**
	 * 파일 다운로드
	 *
	 * @param response
	 *            .
	 * @param is
	 *            입력 스트림
	 * @param downloadFileName
	 *            다운로드 파일 이름
	 * @throws IOException
	 *             .
	 */
	public static void downloadFile(final HttpServletResponse response, final InputStream is,
			final String downloadFileName) throws IOException {
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName);
		try (OutputStream os = response.getOutputStream();) {
			FileCopyUtils.copy(is, os);
		}
	}

	/**
	 * val 객체를 json 문자열로 변환 한다.
	 *
	 * @param val
	 *            대상 객체
	 * @param filter
	 *            변환 필터링 조건
	 * @return json
	 */
	public static String toJson(final Object val, final String filter) {
		if (val == null) {
			return null;
		}
		ObjectMapper objectMapper = Squiggly.init(new ObjectMapper(), filter);
		return SquigglyUtils.stringify(objectMapper, val);
	}

	/**
	 * 파일 확장자를 구별해 2007이전, 이후 나눠 Workbook 반환
	 *
	 * @param file
	 *            엑셀 파일
	 * @return 파일 확장자를 구별해 2007이전, 이후 나눠 Workbook 반환
	 * @throws IOException
	 *             파일 처리 오류
	 */
	public static Workbook getWorkbook(final MultipartFile file) throws IOException {
		if (file.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
			return new XSSFWorkbook(file.getInputStream());
		}
		return new HSSFWorkbook(file.getInputStream());
	}
}
