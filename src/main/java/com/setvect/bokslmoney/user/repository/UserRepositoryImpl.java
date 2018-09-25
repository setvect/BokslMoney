package com.setvect.bokslmoney.user.repository;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;

import com.setvect.bokslmoney.user.controller.UserSearchParam;
import com.setvect.bokslmoney.user.vo.UserVo;
import com.setvect.bokslmoney.util.PageResult;
import com.setvect.bokslmoney.util.jpa.PageQueryCondition;
import com.setvect.bokslmoney.util.jpa.PageUtil;

/**
 * 사용자 검색 조건 <br>
 */
public class UserRepositoryImpl implements UserRepositoryCustom {
	/** JPA DB 세션 */
	@PersistenceContext
	private EntityManager em;

	@Override
	public PageResult<UserVo> getArticlePagingList(final UserSearchParam searchCondition) {
		Map<String, Object> bindParameter = new HashMap<>();
		String where = " WHERE p.deleteF = 'N' ";
		if (StringUtils.isNotEmpty(searchCondition.getName())) {
			where += " AND p.name like :name";
			bindParameter.put("name", "%" + searchCondition.getName() + "%");
		}
		PageQueryCondition pageQuery = new PageQueryCondition(bindParameter, searchCondition);

		pageQuery.setCountQuery("select count(*) FROM UserVo p " + where);
		pageQuery.setSelectQuery("SELECT p FROM UserVo p " + where + " ORDER BY p.userId DESC");

		PageResult<UserVo> resultPage = PageUtil.excutePageQuery(em, pageQuery, UserVo.class);
		return resultPage;
	}

}