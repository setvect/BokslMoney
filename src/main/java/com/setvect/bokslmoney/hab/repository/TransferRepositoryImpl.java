package com.setvect.bokslmoney.hab.repository;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;

import com.setvect.bokslmoney.ApplicationUtil;
import com.setvect.bokslmoney.hab.controller.TransferSearchParam;
import com.setvect.bokslmoney.hab.vo.TransferVo;
import com.setvect.bokslmoney.util.PageResult;
import com.setvect.bokslmoney.util.jpa.PageQueryCondition;
import com.setvect.bokslmoney.util.jpa.PageUtil;

/**
 * 사용자 검색 조건 <br>
 */
public class TransferRepositoryImpl implements TransferRepositoryCustom {
	/** JPA DB 세션 */
	@PersistenceContext
	private EntityManager em;

	@Override
	public PageResult<TransferVo> getPagingList(final TransferSearchParam searchCondition) {
		Map<String, Object> bindParameter = new HashMap<>();
		String where = " WHERE 1 = 1 ";

		if (searchCondition.isRangeSearch()) {
			where += " AND m.transferDate between :from and :to ";
			bindParameter.put("from", searchCondition.getFrom());
			bindParameter.put("to", searchCondition.getTo());
		}
		if (StringUtils.isNotEmpty(searchCondition.getNote())) {
			where += " AND note like :note ";
			bindParameter.put("note", ApplicationUtil.makeLikeString(searchCondition.getNote()));
		}
		if (searchCondition.getKindType() != null) {
			where += " AND kind = :kind ";
			bindParameter.put("kind", ApplicationUtil.makeLikeString(searchCondition.getNote()));
		}

		PageQueryCondition pageQuery = new PageQueryCondition(bindParameter, searchCondition);

		pageQuery.setCountQuery("select count(*) FROM TransferVo m " + where);
		pageQuery.setSelectQuery("SELECT m FROM TransferVo m " + where + " ORDER BY m.transferDate ");

		PageResult<TransferVo> resultPage = PageUtil.excutePageQuery(em, pageQuery, TransferVo.class);
		return resultPage;
	}

}