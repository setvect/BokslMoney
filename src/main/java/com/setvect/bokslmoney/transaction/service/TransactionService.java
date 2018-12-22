package com.setvect.bokslmoney.transaction.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.setvect.bokslmoney.account.repository.AccountRepository;
import com.setvect.bokslmoney.account.vo.AccountVo;
import com.setvect.bokslmoney.stat.vo.DateKindSumVo;
import com.setvect.bokslmoney.transaction.controller.TransactionSearchParam;
import com.setvect.bokslmoney.transaction.repository.CategoryRepository;
import com.setvect.bokslmoney.transaction.repository.TransactionRepository;
import com.setvect.bokslmoney.transaction.vo.CategoryVo;
import com.setvect.bokslmoney.transaction.vo.KindType;
import com.setvect.bokslmoney.transaction.vo.TransactionVo;
import com.setvect.bokslmoney.util.DateUtil;
import com.setvect.bokslmoney.util.PageResult;

@Service
public class TransactionService {
	/** 거래 내역 */
	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private AccountRepository accountRepository;

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
	 * @param from
	 *            시작 날짜
	 * @return Key: 날짜(타임스탬프), Value: 누적 자산
	 */
	public Map<Long, Integer> accumulateOfMonth(final Date from) {
		List<DateKindSumVo> groupKindOfMonth = sumKindOfMonth(from);
		Map<Date, Integer> accumulateAsset = getAccumulateAsset(groupKindOfMonth);
		Map<Long, Integer> accumulateOfMonth = accumulateAssetOfMonth(accumulateAsset);
		return accumulateOfMonth;
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

	/**
	 * 월별 누적 자산
	 *
	 * @param accumulateAsset
	 *            .
	 * @return Key: 날짜(타임스탬프), Value: 누적 자산
	 */
	private Map<Long, Integer> accumulateAssetOfMonth(final Map<Date, Integer> accumulateAsset) {
		int accumulateTotal = accumulateAsset.entrySet().stream().mapToInt(e -> e.getValue()).sum();
		int currentAsset = getCurrentAsset();
		int baseAsset = currentAsset - accumulateTotal;

		Map<Long, Integer> accumulateOfMonth = new TreeMap<>();
		for (Entry<Date, Integer> entry : accumulateAsset.entrySet()) {
			baseAsset += entry.getValue();
			accumulateOfMonth.put(entry.getKey().getTime(), baseAsset);
		}
		return accumulateOfMonth;
	}

	/**
	 * @param groupKindOfMonth
	 *            날짜별로, 유형별로 금액 합산
	 *
	 * @return 누적 자산(Key: 날짜, Value: 수입 - 지출)
	 */
	private Map<Date, Integer> getAccumulateAsset(final List<DateKindSumVo> groupKindOfMonth) {
		Map<Date, Map<KindType, Integer>> group = groupKindOfMonth.stream().collect(Collectors
				.groupingBy(v -> v.getDate(), TreeMap::new, Collectors.toMap(v -> v.getKind(), v -> v.getMoney())));

		Map<Date, Integer> accumulateAsset = group.entrySet().stream().collect(Collectors.toMap(g -> g.getKey(), g -> {
			Map<KindType, Integer> value = g.getValue();
			Integer income = value.get(KindType.INCOME);
			Integer spending = value.get(KindType.SPENDING);
			income = income == null ? 0 : income;
			spending = spending == null ? 0 : spending;

			return income - spending;
		}, (k1, k2) -> k1, TreeMap::new));
		return accumulateAsset;
	}

	/**
	 * @return 현재 잔고
	 */
	private int getCurrentAsset() {
		List<AccountVo> list = accountRepository.list();
		// 현재 잔고
		int currentAsset = list.stream().mapToInt(a -> a.getBalance()).sum();
		return currentAsset;
	}
}
