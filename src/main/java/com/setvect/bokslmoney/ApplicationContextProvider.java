package com.setvect.bokslmoney;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring bean 객체를 가져옴
 */
public class ApplicationContextProvider implements ApplicationContextAware {

	/** Spring application context */
	private static ApplicationContext ctx = null;

	/**
	 * @return Spring application context
	 */
	public static ApplicationContext getApplicationContext() {
		return ctx;
	}

	@Override
	public void setApplicationContext(final ApplicationContext ctx) throws BeansException {
		ApplicationContextProvider.ctx = ctx;
	}
}
