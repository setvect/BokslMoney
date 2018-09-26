package com.setvect.bokslmoney.hab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.setvect.bokslmoney.hab.vo.AccountVo;

public interface AccountRepository extends JpaRepository<AccountVo, Integer> {

	@Query("select a from AccountVo a where deleteFlag = false order by accountSeq")
	public List<AccountVo> list();
}
