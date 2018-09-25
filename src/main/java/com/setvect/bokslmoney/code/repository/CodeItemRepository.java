package com.setvect.bokslmoney.code.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.setvect.bokslmoney.code.vo.CodeItemKey;
import com.setvect.bokslmoney.code.vo.CodeItemVo;

public interface CodeItemRepository extends JpaRepository<CodeItemVo, CodeItemKey> {

	@Query("select c from CodeItemVo c where deleteFlag = false and c.codeItemKey.codeMain.codeMainId = :codeMainId order by c.orderNo")
	public List<CodeItemVo> list(@Param("codeMainId") String codeMainId);

	@Query("select ifnull(max(c.codeItemKey.codeItemSeq), 0) + 1 from CodeItemVo c where c.codeItemKey.codeMain.codeMainId = :codeMainId")
	public int getNextId(@Param("codeMainId") String codeMainId);

}
