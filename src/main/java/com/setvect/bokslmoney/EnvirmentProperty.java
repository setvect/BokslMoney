package com.setvect.bokslmoney;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

/**
 * 프로젝트 설정 정보 제공.
 */
public final class EnvirmentProperty {
	/** */
	private static PropertiesConfiguration config;

	/**
	 */
	private EnvirmentProperty() {

	}

	/**
	 * @param propertise
	 *            propertise 파일
	 */
	public static void init(final File propertise) {
		try {
			PropertiesConfiguration conf = new PropertiesConfiguration(propertise);
			// 파일 수정 자동 감지
			conf.setReloadingStrategy(new FileChangedReloadingStrategy());
			config = conf;
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param propertise
	 *            propertise 파일
	 */
	public static void init(final URL propertise) {
		try {
			PropertiesConfiguration conf = new PropertiesConfiguration(propertise);
			// 파일 수정 자동 감지
			conf.setReloadingStrategy(new FileChangedReloadingStrategy());
			config = conf;
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	// Delegate
	/**
	 * @param paramString
	 *            property key
	 * @return property value
	 */
	public static boolean containsKey(final String paramString) {
		return config.containsKey(paramString);
	}

	/**
	 * @param paramString
	 *            property key
	 * @return property value
	 */
	@SuppressWarnings("rawtypes")
	public static Iterator getKeys(final String paramString) {
		return config.getKeys(paramString);
	}

	/**
	 * @return property keys
	 */
	@SuppressWarnings("rawtypes")
	public static Iterator getKeys() {
		return config.getKeys();
	}

	/**
	 * @param paramString
	 *            property key
	 * @return property value
	 */
	public static boolean getBoolean(final String paramString) {
		return config.getBoolean(paramString);
	}

	/**
	 * @param paramString
	 *            property key
	 * @param paramBoolean
	 *            기본 값
	 * @return property value
	 */
	public static boolean getBoolean(final String paramString, final boolean paramBoolean) {
		return config.getBoolean(paramString, paramBoolean);
	}

	/**
	 * @param paramString
	 *            property key
	 * @return property value
	 */
	public static byte getByte(final String paramString) {
		return config.getByte(paramString);
	}

	/**
	 * @param paramString
	 *            property key
	 * @param paramByte
	 *            기본 값
	 * @return property value
	 */
	public static byte getByte(final String paramString, final byte paramByte) {
		return config.getByte(paramString, paramByte);
	}

	/**
	 * @param paramString
	 *            property key
	 * @return property value
	 */
	public static double getDouble(final String paramString) {
		return config.getDouble(paramString);
	}

	/**
	 * @param paramString
	 *            property key
	 * @param paramDouble
	 *            기본 값
	 * @return property value
	 */
	public static double getDouble(final String paramString, final double paramDouble) {
		return config.getDouble(paramString, paramDouble);
	}

	/**
	 * @param paramString
	 *            property key
	 * @return property value
	 */
	public static float getFloat(final String paramString) {
		return config.getFloat(paramString);
	}

	/**
	 * @param paramString
	 *            property key
	 * @param paramFloat
	 *            기본 값
	 * @return property value
	 */
	public static float getFloat(final String paramString, final float paramFloat) {
		return config.getFloat(paramString, paramFloat);
	}

	/**
	 * @param paramString
	 *            property key
	 * @param paramFloat
	 *            기본 값
	 * @return property value
	 */
	public static Float getFloat(final String paramString, final Float paramFloat) {
		return config.getFloat(paramString, paramFloat);
	}

	/**
	 * @param paramString
	 *            property key
	 * @return property value
	 */
	public static int getInt(final String paramString) {
		return config.getInt(paramString);
	}

	/**
	 * @param paramString
	 *            property key
	 * @param paramInt
	 *            기본 값
	 * @return property value
	 */
	public static int getInt(final String paramString, final int paramInt) {
		return config.getInt(paramString, paramInt);
	}

	/**
	 * @param paramString
	 *            property key
	 * @return property value
	 */
	public static long getLong(final String paramString) {
		return config.getLong(paramString);
	}

	/**
	 * @param paramString
	 *            property key
	 * @param paramLong
	 *            기본 값
	 * @return property value
	 */
	public static long getLong(final String paramString, final long paramLong) {
		return config.getLong(paramString, paramLong);
	}

	/**
	 * @param paramString
	 *            property key
	 * @return property value
	 */
	public static short getShort(final String paramString) {
		return config.getShort(paramString);
	}

	/**
	 * @param paramString
	 *            property key
	 * @param paramShort
	 *            기본 값
	 * @return property value
	 */
	public static short getShort(final String paramString, final short paramShort) {
		return config.getShort(paramString, paramShort);
	}

	/**
	 * @param paramString
	 *            property key
	 * @return property value
	 */
	public static BigDecimal getBigDecimal(final String paramString) {
		return config.getBigDecimal(paramString);
	}

	/**
	 * @param paramString
	 *            property key
	 * @param paramBigDecimal
	 *            기본 값
	 * @return property value
	 */
	public static BigDecimal getBigDecimal(final String paramString, final BigDecimal paramBigDecimal) {
		return config.getBigDecimal(paramString, paramBigDecimal);
	}

	/**
	 * @param paramString
	 *            property key
	 * @return property value
	 */
	public static BigInteger getBigInteger(final String paramString) {
		return config.getBigInteger(paramString);
	}

	/**
	 * @param paramString
	 *            property key
	 * @param paramBigInteger
	 *            기본 값
	 * @return property value
	 */
	public static BigInteger getBigInteger(final String paramString, final BigInteger paramBigInteger) {
		return config.getBigInteger(paramString, paramBigInteger);
	}

	/**
	 * @param paramString
	 *            property key
	 * @return property value
	 */
	public static String getString(final String paramString) {
		return config.getString(paramString);
	}

	/**
	 * @param paramString
	 *            property key
	 * @param paramDefault
	 *            기본 값
	 * @return property value
	 */
	public static String getString(final String paramString, final String paramDefault) {
		return config.getString(paramString, paramDefault);
	}

	/**
	 * @param paramString
	 *            property key
	 * @return property value
	 */
	public static String[] getStringArray(final String paramString) {
		return config.getStringArray(paramString);
	}

	/**
	 * @param paramString
	 *            property key
	 * @return property value
	 */
	@SuppressWarnings("rawtypes")
	public static List getList(final String paramString) {
		return config.getList(paramString);
	}

	/**
	 * @param paramString
	 *            property key
	 * @param paramList
	 *            기본값
	 * @return property value
	 */
	@SuppressWarnings("rawtypes")
	public static List getList(final String paramString, final List paramList) {
		return config.getList(paramString, paramList);
	}

}
