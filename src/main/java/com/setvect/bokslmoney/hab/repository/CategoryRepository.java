package com.setvect.bokslmoney.hab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.setvect.bokslmoney.hab.vo.CategoryVo;
import com.setvect.bokslmoney.hab.vo.KindType;

/**
 * 수입, 지출, 이체 항목
 */
public interface CategoryRepository extends JpaRepository<CategoryVo, Integer> {
	/**
	 * @param kindType
	 *            유형
	 * @param parent
	 *            부모 코드 번호
	 * @return 항목 목록
	 */
	@Query("select i from CategoryVo i where deleteFlag = false and i.kind = :kind and i.parentSeq = :parent order by i.orderNo")
	public List<CategoryVo> list(@Param("kind") KindType kindType, @Param("parent") int parent);

}
