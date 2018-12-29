package com.setvect.bokslmoney.user.service;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.setvect.bokslmoney.BokslMoneyConstant;
import com.setvect.bokslmoney.user.repository.UserRepository;
import com.setvect.bokslmoney.user.vo.RoleName;
import com.setvect.bokslmoney.user.vo.UserRoleVo;
import com.setvect.bokslmoney.user.vo.UserVo;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	/** 암호화 인코더. */
	@Autowired
	private PasswordEncoder passwordEncoder;

	public void init() {
		UserVo user = userRepository.findById(BokslMoneyConstant.Login.ID).orElse(null);
		if (user != null) {
			return;
		}
		// 계정이 없으면, 기본 계정 생성
		user = new UserVo();
		user.setName("복슬이");
		user.setDeleteFlag(false);
		user.setUserId(BokslMoneyConstant.Login.ID);
		user.setPassword(passwordEncoder.encode("boksl"));

		UserRoleVo role = new UserRoleVo();
		role.setRoleName(RoleName.ROLE_ADMIN);
		role.setUser(user);
		user.setUserRole(new HashSet<>(Arrays.asList(role)));
		userRepository.save(user);
	}
}
