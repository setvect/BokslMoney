package com.setvect.bokslmoney.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 페이지 정보를 계산하기위해 사용
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class SearchListParam {

	/** 시작 커서 위치 (0부터 시작) */
	private int startCursor;

	/** 가져올 항목 갯수 */
	private int returnCount;

}