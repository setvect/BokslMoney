package com.setvect.bokslmoney.hab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.setvect.bokslmoney.hab.vo.TransactionVo;

public interface TransactionRepository extends JpaRepository<TransactionVo, Integer>, TransactionRepositoryCustom {
}
