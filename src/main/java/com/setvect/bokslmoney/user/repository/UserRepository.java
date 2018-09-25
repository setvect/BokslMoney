package com.setvect.bokslmoney.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.setvect.bokslmoney.user.vo.UserVo;

/**
 * 사용자 Repository
 */
public interface UserRepository extends JpaRepository<UserVo, String>, UserRepositoryCustom {
	Page<UserVo> findByName(String name, Pageable pageable);
}