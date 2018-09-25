package com.setvect.bokslmoney.household.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.setvect.bokslmoney.household.vo.AccountVo;

public interface AccountRepository extends JpaRepository<AccountVo, Integer> {
}
