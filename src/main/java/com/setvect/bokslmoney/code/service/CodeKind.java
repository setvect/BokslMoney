package com.setvect.bokslmoney.code.service;

/**
 * 코드 유형
 */
public enum CodeKind {
	/** 자산유형 */
	KIND_CODE("자산유형"),

	/** 지출속성 */
	ATTR_SPENDING("지출속성"),

	/** 이체속성 */
	ATTR_TRANSFER("이체속성"),

	/** 수입속성 */
	ATTR_INCOME("수입속성");

	/** 이름 */
	private String name;

	private CodeKind(final String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
