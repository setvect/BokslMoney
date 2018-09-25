package com.setvect.bokslmoney.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.setvect.bokslmoney.user.repository.UserRepository;
import com.setvect.bokslmoney.user.vo.UserVo;

/**
 * Spring security에 사용할 사용자 데이터를 조회 하는 서비스
 */
@Service
public class MyUserDetailsService implements UserDetailsService {
	/** */
	@Autowired
	private UserRepository userRepository;

	/** 사용자 데이터 조회용 */
	@Override
	public UserDetails loadUserByUsername(final String id) {
		UserVo user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(id));
		return user;
	}
}