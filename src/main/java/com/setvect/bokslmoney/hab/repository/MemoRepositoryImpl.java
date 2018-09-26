package com.setvect.bokslmoney.hab.repository;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.setvect.bokslmoney.hab.controller.MemoSearchParam;
import com.setvect.bokslmoney.hab.vo.MemoVo;
import com.setvect.bokslmoney.util.PageResult;
import com.setvect.bokslmoney.util.jpa.PageQueryCondition;
import com.setvect.bokslmoney.util.jpa.PageUtil;

/**
 * 사용자 검색 조건 <br>
 */
public class MemoRepositoryImpl implements MemoRepositoryCustom {
	/** JPA DB 세션 */
	@PersistenceContext
	private EntityManager em;

	@Override
	public PageResult<MemoVo> getPagingList(final MemoSearchParam searchCondition) {
		Map<String, Object> bindParameter = new HashMap<>();
		String where = " WHERE m.deleteFlag = 'N' ";

		if (searchCondition.isRangeSearch()) {
			where += " AND m.memoDate between :from and :to ";
			bindParameter.put("from", searchCondition.getFrom());
			bindParameter.put("to", searchCondition.getTo());
		}
		PageQueryCondition pageQuery = new PageQueryCondition(bindParameter, searchCondition);

		pageQuery.setCountQuery("select count(*) FROM MemoVo m " + where);
		pageQuery.setSelectQuery("SELECT m FROM MemoVo m " + where + " ORDER BY m.memoSeq DESC");

		PageResult<MemoVo> resultPage = PageUtil.excutePageQuery(em, pageQuery, MemoVo.class);
		return resultPage;
	}

}