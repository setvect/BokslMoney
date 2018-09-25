package com.setvect.bokslmoney.household.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.setvect.bokslmoney.household.vo.MemoVo;

public interface MemoRepository extends JpaRepository<MemoVo, Integer>, MemoRepositoryCustom {

}
