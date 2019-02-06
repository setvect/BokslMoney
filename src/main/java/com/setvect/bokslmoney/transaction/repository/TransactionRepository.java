package com.setvect.bokslmoney.transaction.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.setvect.bokslmoney.transaction.vo.TransactionVo;
import org.springframework.stereotype.Repository;

/**
 * 거래 내역
 */
@Repository
public interface TransactionRepository extends JpaRepository<TransactionVo, Integer>, TransactionRepositoryCustom {
	/**
	 * 날짜별로, 유형별로 금액 합산
	 *
	 * @param from
	 *            시작 날짜
	 * @return 날짜별로, 유형별로 금액 합산
	 */
	@Query(" SELECT dateformat( t.transactionDate,  'yyyy-MM') as date, t.kind, SUM(t.money) as money, COUNT(*) as count "
			+ //
			" FROM TransactionVo t  " + //
			" WHERE " + //
			" t.transactionDate >= :from " + //
			" GROUP BY dateformat( t.transactionDate,  'yyyy-MM'), t.kind " + //
			" ORDER BY 1, 2")
	public List<Object[]> sumKindOfMonth(@Param("from") final Date from);
}
