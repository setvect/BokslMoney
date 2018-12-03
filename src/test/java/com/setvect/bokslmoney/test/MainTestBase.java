package com.setvect.bokslmoney.test;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.setvect.bokslmoney.BokslMoneyApplication;
import com.setvect.bokslmoney.BokslMoneyConstant;

/**
 * spring 테스트를 위한 설정<br>
 * spring과 연관된 테스트는 해당 클래스를 상속 한다.
 */
@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(classes = { BokslMoneyApplication.class })
@TestPropertySource(locations = "classpath:application.properties")
@Rollback(false)
public class MainTestBase {
	static {
		System.setProperty(BokslMoneyConstant.TEST_CHECK_PROPERTY_NAME, "true");
	}
}
