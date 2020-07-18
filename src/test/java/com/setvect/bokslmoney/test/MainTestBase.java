package com.setvect.bokslmoney.test;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.setvect.bokslmoney.BokslMoneyApplication;
import com.setvect.bokslmoney.BokslMoneyConstant;

/**
 * spring 테스트를 위한 설정<br>
 * spring과 연관된 테스트는 해당 클래스를 상속 한다.
 */
@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(classes = { BokslMoneyApplication.class })
@TestPropertySource(locations = "classpath:test.properties")
@Rollback(false)
public class MainTestBase {
	static {
		System.setProperty(BokslMoneyConstant.TEST_CHECK_PROPERTY_NAME, "true");
	}

	/** 초기 데이터 입력 */
	private static final String INIT_INSERT_SQL = "/init.sql";

	/** 엔티티의 refrash, merge 등을 관리하기 위해 */
	@Autowired
	private EntityManager entityManager;

	/**
	 * DB 초기 값 등록.
	 */
	protected void insertInitValue() {

	}

	/**
	 * 텍스트에서 쿼리문을 문장 단위로 분리
	 *
	 * @param text
	 *            sql문을 담고 있는 텍스트
	 * @return 문장 단위 쿼리문
	 */
	private List<String> extractQeruy(final String text) {
		List<String> result = new ArrayList<>();
		StringBuffer r = new StringBuffer();
		String[] sqlInsertQueries = text.split("\n");

		for (String s : sqlInsertQueries) {
			if (StringUtils.isBlank(s)) {
				continue;
			}
			if (s.startsWith("--")) {
				continue;
			}
			r.append(s);
			if (s.trim().endsWith(";")) {
				result.add(r.toString());
				r.setLength(0);
			}
		}
		return result;
	}

}
