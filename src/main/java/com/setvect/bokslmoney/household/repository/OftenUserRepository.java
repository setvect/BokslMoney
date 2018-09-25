package com.setvect.bokslmoney.household.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.setvect.bokslmoney.household.vo.OftenUserVo;

public interface OftenUserRepository extends JpaRepository<OftenUserVo, Integer> {
}
