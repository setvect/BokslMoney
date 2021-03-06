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

	/** 웹 포트 */
	public static final int WEB_PORT = EnvirmentProperty.getInt("server.port");

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
		/** 로그인 아이디(고정) */
		public static final String ID = "boksl";
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

	/** 시작 후 웹페이지 열기 */
	public static final boolean OPEN_WEB = EnvirmentProperty.getBoolean("com.setvect.bokslmoney.openweb");

	/**
	 * 거래 내역중 메모를 바탕으로 카테고리 추천
	 */
	public static class CategoryRecommend {
		/** 현재를 기준으로 과거 거래 내역을 어디까지 조회 할 지 일(day)로 범위 지정 */
		public static final int DAYS = 100;
		/** 최대 조회 건수 */
		public static final int MAX_ITEM_COUNT = 1000;
		/** 후보 추천 갯수 */
		public static final int CANDIDATE_COUNT = 10;
	}

}
