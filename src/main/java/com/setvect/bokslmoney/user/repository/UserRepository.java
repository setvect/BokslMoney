package com.setvect.bokslmoney.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.setvect.bokslmoney.user.vo.UserVo;
import org.springframework.stereotype.Repository;

/**
 * 사용자 Repository
 */
@Repository
public interface UserRepository extends JpaRepository<UserVo, String> {
}