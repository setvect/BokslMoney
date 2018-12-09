package com.setvect.bokslmoney.hab.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 날짜별로, 유형별로 금액 합산
 */
@Getter
@ToString
@AllArgsConstructor
public class DateKindSumVo {
	/** 날짜 */
	private Date date;
	/** 유형 */
	private KindType kind;
	/** 합산 금액 */
	private int money;
	/** 그룹핑 건수 */
	private int count;
}
