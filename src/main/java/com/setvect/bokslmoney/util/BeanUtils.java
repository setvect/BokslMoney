package com.setvect.bokslmoney.util;

import org.springframework.context.ApplicationContext;

import com.setvect.bokslmoney.ApplicationContextProvider;

/**
 * static한 메소드 등에서 spring bean 객체를 가져오기 위해 제공
 */
public abstract class BeanUtils {
	/**
	 * @param beanId
	 *            beanId
	 * @return Spring Bean
	 */
	public static Object getBean(final String beanId) {
		ApplicationContext applicationContext = getApplicationContext();
		return applicationContext.getBean(beanId);
	}

	/**
	 * @param classType
	 *            클래스 타입
	 * @param <T>
	 *            Bean 타입
	 * @return Spring Bean
	 */
	public static <T> T getBean(final Class<T> classType) {
		ApplicationContext applicationContext = getApplicationContext();
		return applicationContext.getBean(classType);
	}

	/**
	 * @return Application Context
	 */
	private static ApplicationContext getApplicationContext() {
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		if (applicationContext == null) {
			throw new NullPointerException("ApplicationContext not initialized.");
		}
		return applicationContext;
	}

}
