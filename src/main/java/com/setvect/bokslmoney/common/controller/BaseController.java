package com.setvect.bokslmoney.common.controller;

import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import com.setvect.bokslmoney.common.DatePropertyEditor;

/**
 * 특정 데이터 타입에 대한 전역 바인드 처리
 */
@ControllerAdvice
public class BaseController {
	/**
	 * http로 넘온값을 Date 타입 속성을 바인드
	 *
	 * @param binder
	 *            request binder
	 */
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new DatePropertyEditor());
	}
}
