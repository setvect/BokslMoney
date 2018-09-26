package com.setvect.bokslmoney.hab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.setvect.bokslmoney.hab.vo.TransferVo;

public interface TransferRepository extends JpaRepository<TransferVo, Integer>, TransferRepositoryCustom {
}
