package com.setvect.bokslmoney.hab.vo;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 결산 항목
 */
@Setter
@Getter
@ToString
public class SettlementVo {
	/**
	 * 대분류 기준 지출 합산<br>
	 * Key: 대분류 번호, Value: 금액
	 */
	private Map<Integer, Integer> spending;

	/** 수입 */
	private int income;

	/** 이체 */
	private int transfer;
}
