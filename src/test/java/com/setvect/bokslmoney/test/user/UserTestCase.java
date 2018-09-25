package com.setvect.bokslmoney.test.user;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.setvect.bokslmoney.test.MainTestBase;
import com.setvect.bokslmoney.user.controller.UserSearchParam;
import com.setvect.bokslmoney.user.repository.UserRepository;
import com.setvect.bokslmoney.user.vo.UserVo;
import com.setvect.bokslmoney.util.PageResult;

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

	// @Test
	public void get() {
		insertInitValue();
		// UserVo b = userRepository.findOne("boksl");
		PageRequest page = PageRequest.of(0, 2);
		Page<UserVo> result = userRepository.findByName("사용자1", page);
		System.out.println(result);
		System.out.println();
	}

	// @Test
	public void getPage() {
		// insertInitValue();
		UserSearchParam searchCondition = new UserSearchParam();
		PageResult<UserVo> result = userRepository.getArticlePagingList(searchCondition);
		Assert.assertThat(result.getList().size(), CoreMatchers.is(6));

		searchCondition.setName("사용자");
		result = userRepository.getArticlePagingList(searchCondition);
		Assert.assertThat(result.getList().size(), CoreMatchers.is(5));

		System.out.println(result);
		System.out.println();
	}
}
