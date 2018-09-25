package com.setvect.bokslmoney.household.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.setvect.bokslmoney.household.vo.TransferVo;

public interface TransferRepository extends JpaRepository<TransferVo, Integer>, TransferRepositoryCustom {
}
