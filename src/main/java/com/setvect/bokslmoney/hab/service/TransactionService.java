package com.setvect.bokslmoney.hab.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.setvect.bokslmoney.hab.controller.TransactionSearchParam;
import com.setvect.bokslmoney.hab.repository.CategoryRepository;
import com.setvect.bokslmoney.hab.repository.TransactionRepository;
import com.setvect.bokslmoney.hab.vo.CategoryVo;
import com.setvect.bokslmoney.hab.vo.DateKindSumVo;
import com.setvect.bokslmoney.hab.vo.KindType;
import com.setvect.bokslmoney.hab.vo.TransactionVo;
import com.setvect.bokslmoney.util.DateUtil;
import com.setvect.bokslmoney.util.PageResult;

@Service
public class TransactionService {
	/** 거래 내역 */
	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	/**
	 * @param searchCondition
	 *            조회 조건
	 * @return 거래 내역
	 */
	public List<TransactionVo> list(final TransactionSearchParam searchCondition) {
		PageResult<TransactionVo> page = transactionRepository.getPagingList(searchCondition);
		List<TransactionVo> list = page.getList();
		mappingParentItem(list);
		return list;
	}

	/**
	 * 분류 항목(이체, 수입, 지출)의 부모 분류 넣기
	 *
	 * @param list
	 *            .
	 */
	private void mappingParentItem(final List<TransactionVo> list) {
		list.stream().forEach(t -> {
			int parentSeq = t.getCategory().getParentSeq();
			CategoryVo parent = categoryRepository.findById(parentSeq).get();
			t.setParentCategory(parent);
		});
	}

	/**
	 * @param year
	 *            년도
	 * @param kind
	 *            조회 항목 유형
	 * @return Key: 월(0 부터 시작), Value: (Key: 대분류 아이디, Value: 합산 금액)
	 */
	public Map<Integer, Map<Integer, Integer>> groupCategoryOfMonth(final int year, final KindType kind) {
		TransactionSearchParam searchCondition = new TransactionSearchParam();

		LocalDate from = LocalDate.of(year, 1, 1);
		LocalDate to = from.plusYears(1).minusDays(1);

		searchCondition.setFrom(DateUtil.toDate(from));
		searchCondition.setTo(DateUtil.toDate(to));
		searchCondition.setReturnCount(Integer.MAX_VALUE);
		searchCondition.setKindType(kind);
		List<TransactionVo> transactionList = list(searchCondition);

		// Key: 월, Value: (Key: 대분류 아이디, Value: 합산 금액)
		Map<Integer, Map<Integer, Integer>> groupOfMonth = transactionList.stream()
				.collect(Collectors.groupingBy(tran -> tran.getMonth(),
						Collectors.groupingBy(tran -> tran.getParentCategory().getCategorySeq(),
								Collectors.summingInt(tran -> tran.getMoney()))));
		return groupOfMonth;
	}

	/**
	 * 유형별 금액을 합산함.
	 *
	 * @param year
	 *            년도
	 * @return Key: 월(0 부터 시작), Value: (Key: 유형 항목, Value: 합산 금액)
	 */
	public Map<Integer, Map<KindType, Integer>> groupKindOfMonth(final int year) {
		TransactionSearchParam searchCondition = new TransactionSearchParam();

		LocalDate from = LocalDate.of(year, 1, 1);
		LocalDate to = from.plusYears(1).minusDays(1);

		searchCondition.setFrom(DateUtil.toDate(from));
		searchCondition.setTo(DateUtil.toDate(to));
		searchCondition.setReturnCount(Integer.MAX_VALUE);
		List<TransactionVo> transactionList = list(searchCondition);

		// Key: 월, Value: (Key: 대분류 아이디, Value: 합산 금액)
		Map<Integer, Map<KindType, Integer>> groupCategoryOfMonth = transactionList.stream()
				.collect(Collectors.groupingBy(tran -> tran.getMonth(),
						Collectors.groupingBy(tran -> tran.getKind(), Collectors.summingInt(tran -> tran.getMoney()))));
		return groupCategoryOfMonth;
	}

	/**
	 * 날짜별로, 유형별로 금액 합산
	 *
	 * @param from
	 *            시작 날짜
	 * @return 날짜별로, 유형별로 금액 합산
	 */
	public List<DateKindSumVo> sumKindOfMonth(final Date from) {
		List<Object[]> list = transactionRepository.sumKindOfMonth(from);
		List<DateKindSumVo> result = list.stream().map(objs -> {
			Date date = DateUtil.getDate((String) objs[0], "yyyy-MM");
			KindType kind = (KindType) objs[1];
			long money = (long) objs[2];
			long count = (long) objs[3];

			DateKindSumVo dateKindSum = new DateKindSumVo(date, kind, (int) money, (int) count);
			return dateKindSum;
		}).collect(Collectors.toList());

		return result;
	}
}
