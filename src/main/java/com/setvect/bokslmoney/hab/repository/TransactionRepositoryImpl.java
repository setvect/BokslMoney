package com.setvect.bokslmoney.hab.repository;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;

import com.setvect.bokslmoney.ApplicationUtil;
import com.setvect.bokslmoney.hab.controller.TransactionSearchParam;
import com.setvect.bokslmoney.hab.vo.TransactionVo;
import com.setvect.bokslmoney.util.PageResult;
import com.setvect.bokslmoney.util.jpa.PageQueryCondition;
import com.setvect.bokslmoney.util.jpa.PageUtil;

/**
 * 사용자 검색 조건 <br>
 */
public class TransactionRepositoryImpl implements TransactionRepositoryCustom {
	/** JPA DB 세션 */
	@PersistenceContext
	private EntityManager em;

	@Override
	public PageResult<TransactionVo> getPagingList(final TransactionSearchParam searchCondition) {
		Map<String, Object> bindParameter = new HashMap<>();
		String where = " WHERE 1 = 1 ";

		if (searchCondition.isRangeSearch()) {
			where += " AND m.transactionDate between :from and :to ";
			bindParameter.put("from", searchCondition.getFrom());
			bindParameter.put("to", searchCondition.getTo());
		}
		if (StringUtils.isNotEmpty(searchCondition.getNote())) {
			where += " AND note like :note ";
			bindParameter.put("note", ApplicationUtil.makeLikeString(searchCondition.getNote()));
		}
		if (searchCondition.getCategorySeq() != 0) {
			where += " AND m.category.parentSeq = :categorySeq ";
			bindParameter.put("categorySeq", searchCondition.getCategorySeq());
		}

		if (searchCondition.getKindTypeSet() != null && !searchCondition.getKindTypeSet().isEmpty()) {
			where += " AND kind in :kind ";
			bindParameter.put("kind", searchCondition.getKindTypeSet());
		}

		PageQueryCondition pageQuery = new PageQueryCondition(bindParameter, searchCondition);

		pageQuery.setCountQuery("select count(*) FROM TransactionVo m " + where);
		pageQuery.setSelectQuery("SELECT m FROM TransactionVo m " + where + " ORDER BY m.transactionDate, m.kind ");

		PageResult<TransactionVo> resultPage = PageUtil.excutePageQuery(em, pageQuery, TransactionVo.class);
		return resultPage;
	}

}