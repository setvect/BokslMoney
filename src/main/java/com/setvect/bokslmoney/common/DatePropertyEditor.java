package com.setvect.bokslmoney.common;

import java.beans.PropertyEditorSupport;
import java.util.Date;

import com.setvect.bokslmoney.util.DateUtil;

/**
 * Date 필드를 Request Bind 하기 위한 처리
 */
public class DatePropertyEditor extends PropertyEditorSupport {

	/**
	 * 입력값에 맞게 데이트 필드를 마인딩 함.
	 */
	@Override
	public void setAsText(final String value) throws IllegalArgumentException {
		if (DateUtil.isDatePatten(value, "yyyy-MM-dd")) {
			setValue(DateUtil.getDate(value, "yyyy-MM-dd"));
			return;
		}

		if (DateUtil.isDatePatten(value, "yyyyMMdd")) {
			setValue(DateUtil.getDate(value, "yyyyMMdd"));
			return;
		}

		try {
			long date = Long.parseLong(value);
			Date time = new Date(date);
			setValue(time);
		} catch (NumberFormatException e) {
			// ignore
		}
	}
}