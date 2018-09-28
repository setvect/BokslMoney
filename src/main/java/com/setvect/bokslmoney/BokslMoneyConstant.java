package com.setvect.bokslmoney;

/**
 * 상수 정의.
 */
public final class BokslMoneyConstant {
	/**
	 * not instance.
	 */
	private BokslMoneyConstant() {
	}

	/**
	 * js, css 등 웹 자원 파일 캐싱을 하지위한 값<br>
	 * src="${request.contextPath}/boksl.js?<%=com.setvect.bokslmoney.BokslMoneyConstant.CACHE_VER%>"
	 */
	public static final long CACHE_VER = System.currentTimeMillis();

	/**
	 * 웹 관련 상수
	 */
	public static class Web {
		/** 한페이지에 불러올 항목 수 */
		public static final int DEFAULT_PAGE_SIZE = 10;
		/**
		 * 한 번에 보여질 최대 페이지 번호 갯수<br>
		 * [1] [2] [3] [4]
		 */
		public static final int DEFAULT_VISIBLE_PAGE = 10;
	}

	/**
	 * request.setAttribute()에 사용할 이름
	 */
	public static class AttributeName {
		/** 템플릿에서 불러올 페이지 */
		public static final String LOAD_PAGE = "LOAD_PAGE";
	}

	/**
	 * 로그인 관련 상수.
	 */
	public static class Login {
		/** remember 관련. */
		public static final String REMEMBER_ME_KEY = "bokslLoginKey";
		/** remember 관련. */
		public static final String REMEMBER_COOKIE_NAME = "bokslCookie";

		/** 강제 로그인 여부 */
		public static final boolean CONSTRAINT_LOGIN = EnvirmentProperty
				.getBoolean("com.setvect.bokslmoney.constraint-login");
	}

	/** 테스트 실행 여부을 알수 있는 System property 이름 */
	public static final String TEST_CHECK_PROPERTY_NAME = "test_run";

}
