package com.setvect.bokslmoney.config;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

/**
 * 추가 함수 정의
 */
public class CustomH2Dialect extends H2Dialect {
	/**
	 *
	 */
	public CustomH2Dialect() {
		super();
		registerFunction("dateformat", new StandardSQLFunction("formatdatetime", StandardBasicTypes.STRING));
	}
}
