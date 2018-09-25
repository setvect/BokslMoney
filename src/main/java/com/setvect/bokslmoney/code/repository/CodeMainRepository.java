package com.setvect.bokslmoney.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.setvect.bokslmoney.code.vo.CodeMainVo;

public interface CodeMainRepository extends JpaRepository<CodeMainVo, String> {
}
