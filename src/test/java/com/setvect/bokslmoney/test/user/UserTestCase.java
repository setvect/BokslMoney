package com.setvect.bokslmoney.test.user;

import org.springframework.beans.factory.annotation.Autowired;

import com.setvect.bokslmoney.test.MainTestBase;
import com.setvect.bokslmoney.user.repository.UserRepository;
import com.setvect.bokslmoney.user.vo.UserVo;

public class UserTestCase extends MainTestBase {

	@Autowired
	private UserRepository userRepository;

	// @Test
	public void test() {

		UserVo e = new UserVo();
		e.setUserId("boksl");
		e.setName("복슬이");
		e.setPassword("1234");
		e.setDeleteFlag(false);

		userRepository.saveAndFlush(e);

		System.out.println(userRepository);
		System.out.println("###########");
	}
}
