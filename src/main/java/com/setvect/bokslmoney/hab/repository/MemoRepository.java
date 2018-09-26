package com.setvect.bokslmoney.hab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.setvect.bokslmoney.hab.vo.MemoVo;

public interface MemoRepository extends JpaRepository<MemoVo, Integer>, MemoRepositoryCustom {

}
