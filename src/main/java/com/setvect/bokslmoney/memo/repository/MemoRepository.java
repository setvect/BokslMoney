package com.setvect.bokslmoney.memo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.setvect.bokslmoney.memo.vo.MemoVo;

public interface MemoRepository extends JpaRepository<MemoVo, Integer>, MemoRepositoryCustom {

}
