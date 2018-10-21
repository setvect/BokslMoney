package com.setvect.bokslmoney.hab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.setvect.bokslmoney.hab.vo.KindType;
import com.setvect.bokslmoney.hab.vo.OftenUsedVo;

public interface OftenUsedRepository extends JpaRepository<OftenUsedVo, Integer> {

	@Query("select o from OftenUsedVo o where deleteFlag = false and o.kind = :kind order by orderNo")
	public List<OftenUsedVo> list(@Param("kind") KindType kind);

}
