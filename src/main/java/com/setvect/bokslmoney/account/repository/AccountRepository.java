package com.setvect.bokslmoney.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.setvect.bokslmoney.account.vo.AccountVo;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountVo, Integer> {

	@Query("select a from AccountVo a where deleteFlag = false order by name")
	public List<AccountVo> list();
}
