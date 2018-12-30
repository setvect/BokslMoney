package com.setvect.bokslmoney.temp;

import com.setvect.bokslmoney.account.repository.AccountRepository;
import com.setvect.bokslmoney.account.vo.AccountVo;
import com.setvect.bokslmoney.code.repository.CodeItemRepository;
import com.setvect.bokslmoney.code.repository.CodeMainRepository;
import com.setvect.bokslmoney.code.service.CodeKind;
import com.setvect.bokslmoney.code.vo.CodeItemKey;
import com.setvect.bokslmoney.code.vo.CodeItemVo;
import com.setvect.bokslmoney.code.vo.CodeMainVo;
import com.setvect.bokslmoney.transaction.repository.CategoryRepository;
import com.setvect.bokslmoney.transaction.repository.TransactionRepository;
import com.setvect.bokslmoney.transaction.vo.CategoryVo;
import com.setvect.bokslmoney.transaction.vo.KindType;
import com.setvect.bokslmoney.transaction.vo.TransactionVo;
import com.setvect.bokslmoney.util.DateUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 샘플 데이터를 만드는 모듈<br>
 * 임시로 쓸 거라 날코딩 요소가 많음.
 */
@Service
public class MakerSampleDataService {
	@Autowired
	private CodeItemRepository codeItemRepository;

	@Autowired
	private CodeMainRepository codeMainRepository;

	@Autowired
	private AccountRepository acccuntRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private TransactionRepository transactionRepository;
	private List<CategoryVo> childCategoryList;
	private List<AccountVo> accountList;
	private List<CodeItemVo> codeList;


	/**
	 * 임시로 사용할 데이터 생성
	 */
	public void makeSampleData() {
		transactionRepository.deleteAll();
		acccuntRepository.deleteAll();
		categoryRepository.deleteAll();
		codeItemRepository.deleteAll();

		makeCode();
		makeCategory();
		makeAccount();
		makeTransaction();
	}

	/**
	 * 거래 정보 등록
	 */
	private void makeTransaction() {

		List<Consumer<LocalDate>> inserter = makeTransactionInserts();

		LocalDate currentDate = LocalDate.of(2014, 8, 1);
		LocalDate endDate = LocalDate.of(2019, 5, 10);
		long endStamp = DateUtil.toDate(endDate).getTime();

		while (true) {
			for (Consumer<LocalDate> insert : inserter) {
				insert.accept(currentDate);
			}

			currentDate = currentDate.plusDays(1);
			Date tranDate = DateUtil.toDate(currentDate);
			long cur = tranDate.getTime();
			if (cur > endStamp) {
				break;
			}
		}
	}

	private List<Consumer<LocalDate>> makeTransactionInserts() {
		List<Consumer<LocalDate>> inserter = new ArrayList<>();

		// ======================================
		// 수입
		// ======================================

		// 매달 5일 월급
		inserter.add(date -> {
			doTransction(() -> date.getDayOfMonth() == 5, () -> 3_185_950, date, KindType.INCOME, "급여", "복슬은행 월급통장", "단순 수입", "월급");
		});

		// 매년 12월 30일
		inserter.add(date -> {
			if (date.getMonthValue() != 12 || date.getDayOfMonth() != 30) {
				return;
			}

			TransactionVo t = new TransactionVo();
			t.setCategory(findCategory("성과급"));
			t.setKind(KindType.INCOME);
			t.setPayAccount(findAccount("복슬은행 월급통장"));
			t.setAttribute(findCode("단순 수입"));
			t.setMoney(1_500_000);
			Date tranDate = DateUtil.toDate(date);
			t.setTransactionDate(tranDate);
			t.setNote("연말 성과급");
			transactionRepository.save(t);
		});

		// 이자, 확률:1%, 1000 ~ 2000원
		inserter.add(date -> {
			double r = Math.random();
			if (r > 0.01) {
				return;
			}

			TransactionVo t = new TransactionVo();
			t.setCategory(findCategory("이자소득"));
			t.setKind(KindType.INCOME);
			t.setPayAccount(findAccount("복슬은행 월급통장"));
			t.setAttribute(findCode("투자 수입"));
			int money = RandomUtils.nextInt(1000, 2000);
			t.setMoney(money);
			Date tranDate = DateUtil.toDate(date);
			t.setTransactionDate(tranDate);
			t.setNote("이자");
			transactionRepository.save(t);
		});

		// 배당금, 매년 12월 10일, 10만 ~ 30만
		inserter.add(date -> {
			if (date.getMonthValue() != 12 || date.getDayOfMonth() != 10) {
				return;
			}

			TransactionVo t = new TransactionVo();
			t.setCategory(findCategory("배당금"));
			t.setKind(KindType.INCOME);
			t.setPayAccount(findAccount("복슬증권 주식계좌"));
			t.setAttribute(findCode("투자 수입"));
			int money = RandomUtils.nextInt(100_000, 300_000);
			t.setMoney(money);
			Date tranDate = DateUtil.toDate(date);
			t.setTransactionDate(tranDate);
			t.setNote("주식 배당금");
			transactionRepository.save(t);
		});

		// ======================================
		// 지출
		// ======================================

		// 월세 입력. 매달 10일에 등록
		inserter.add(date -> {
			int day = date.getDayOfMonth();
			if (day != 10) {
				return;
			}
			TransactionVo t = new TransactionVo();
			t.setCategory(findCategory("월세"));
			t.setKind(KindType.SPENDING);
			t.setPayAccount(findAccount("복슬은행 월급통장"));
			t.setAttribute(findCode("고정 지출"));
			t.setMoney(500_000);
			Date tranDate = DateUtil.toDate(date);
			t.setTransactionDate(tranDate);
			t.setNote("월세");
			transactionRepository.save(t);
		});

		// 전철비
		inserter.add(date -> {
			double r = Math.random();
			if (r > 0.9) {
				return;
			}

			TransactionVo t = new TransactionVo();
			CategoryVo category = findCategory("대중교통비");
			t.setCategory(category);
			t.setKind(KindType.SPENDING);
			t.setPayAccount(findAccount("복슬 카드"));
			t.setAttribute(findCode("단순 지출"));

			int money = RandomUtils.nextInt(29, 100) * 50;
			t.setMoney(money);

			Date tranDate = DateUtil.toDate(date);
			t.setTransactionDate(tranDate);
			t.setNote("전철요금");
			transactionRepository.save(t);
		});

		// 버스요금
		inserter.add(date -> {
			double r = Math.random();
			if (r > 0.4) {
				return;
			}

			TransactionVo t = new TransactionVo();
			t.setCategory(findCategory("대중교통비"));
			t.setKind(KindType.SPENDING);
			t.setPayAccount(findAccount("복슬 카드"));
			t.setAttribute(findCode("단순 지출"));

			int money = RandomUtils.nextInt(28, 60) * 50;
			t.setMoney(money);

			Date tranDate = DateUtil.toDate(date);
			t.setTransactionDate(tranDate);
			t.setNote("버스용금");
			transactionRepository.save(t);
		});

		// 택시
		inserter.add(date -> {
			double r = Math.random();
			if (r > 0.05) {
				return;
			}

			TransactionVo t = new TransactionVo();
			t.setCategory(findCategory("대중교통비"));
			t.setKind(KindType.SPENDING);
			t.setPayAccount(findAccount("복슬 카드"));
			t.setAttribute(findCode("단순 지출"));

			int money = RandomUtils.nextInt(30, 200) * 1000;
			t.setMoney(money);

			Date tranDate = DateUtil.toDate(date);
			t.setTransactionDate(tranDate);
			t.setNote("택시비");
			transactionRepository.save(t);
		});

		// 기차
		inserter.add(date -> {
			double r = Math.random();
			if (r > 0.03) {
				return;
			}

			TransactionVo t = new TransactionVo();
			t.setCategory(findCategory("대중교통비"));
			t.setKind(KindType.SPENDING);
			t.setPayAccount(findAccount("복슬 카드"));
			t.setAttribute(findCode("단순 지출"));

			int money = RandomUtils.nextInt(5, 100) * 1000;
			t.setMoney(money);

			Date tranDate = DateUtil.toDate(date);
			t.setTransactionDate(tranDate);
			t.setNote("기차요금");
			transactionRepository.save(t);
		});


		// 주유
		inserter.add(date -> {
			double r = Math.random();
			if (r > 0.03) {
				return;
			}

			TransactionVo t = new TransactionVo();
			t.setCategory(findCategory("주유비"));
			t.setKind(KindType.SPENDING);
			t.setPayAccount(findAccount("복슬 카드"));
			t.setAttribute(findCode("단순 지출"));

			int money = RandomUtils.nextInt(2, 10) * 10_000;
			t.setMoney(money);

			Date tranDate = DateUtil.toDate(date);
			t.setTransactionDate(tranDate);
			t.setNote("기름값");
			transactionRepository.save(t);
		});
		// 아침
		inserter.add(date -> {
			double r = Math.random();
			if (r > 0.01) {
				return;
			}

			TransactionVo t = new TransactionVo();
			t.setCategory(findCategory("아침"));
			t.setKind(KindType.SPENDING);
			t.setPayAccount(findAccount("복슬 카드"));
			t.setAttribute(findCode("단순 지출"));

			int money = RandomUtils.nextInt(4, 10) * 1000;
			t.setMoney(money);

			Date tranDate = DateUtil.toDate(date);
			t.setTransactionDate(tranDate);
			t.setNote("술");
			transactionRepository.save(t);
		});

		// 점심
		inserter.add(date -> {
			double r = Math.random();
			if (r > 0.8) {
				return;
			}

			TransactionVo t = new TransactionVo();
			t.setCategory(findCategory("주식/부식"));
			t.setKind(KindType.SPENDING);
			t.setPayAccount(findAccount("복슬 카드"));
			t.setAttribute(findCode("단순 지출"));

			int money = RandomUtils.nextInt(10, 24) * 500;
			t.setMoney(money);

			Date tranDate = DateUtil.toDate(date);
			t.setTransactionDate(tranDate);
			t.setNote("점심");
			transactionRepository.save(t);
		});

		// 술
		inserter.add(date -> {
			double r = Math.random();
			if (r > 0.1) {
				return;
			}

			TransactionVo t = new TransactionVo();
			t.setCategory(findCategory("담배/술"));
			t.setKind(KindType.SPENDING);
			t.setPayAccount(findAccount("복슬 카드"));
			t.setAttribute(findCode("단순 지출"));

			int money = RandomUtils.nextInt(20, 100) * 1000;
			t.setMoney(money);

			Date tranDate = DateUtil.toDate(date);
			t.setTransactionDate(tranDate);
			t.setNote("술");
			transactionRepository.save(t);
		});

		// 영화
		inserter.add(date -> {
			double r = Math.random();
			if (r > 0.15) {
				return;
			}

			TransactionVo t = new TransactionVo();
			t.setCategory(findCategory("영화공연관람"));
			t.setKind(KindType.SPENDING);
			t.setPayAccount(findAccount("복슬 카드"));
			t.setAttribute(findCode("단순 지출"));

			int money = RandomUtils.nextInt(6, 24) * 1000;
			t.setMoney(money);

			Date tranDate = DateUtil.toDate(date);
			t.setTransactionDate(tranDate);
			t.setNote("영화관람");
			transactionRepository.save(t);
		});


		// 당구
		inserter.add(date -> {
			double r = Math.random();
			if (r > 0.03) {
				return;
			}

			TransactionVo t = new TransactionVo();
			t.setCategory(findCategory("기타문화생활비"));
			t.setKind(KindType.SPENDING);
			t.setPayAccount(findAccount("지갑"));
			t.setAttribute(findCode("단순 지출"));

			int money = RandomUtils.nextInt(10, 45) * 500;
			t.setMoney(money);

			Date tranDate = DateUtil.toDate(date);
			t.setTransactionDate(tranDate);
			t.setNote("술");
			transactionRepository.save(t);
		});

		// 입장료
		inserter.add(date -> {
			double r = Math.random();
			if (r > 0.15) {
				return;
			}

			TransactionVo t = new TransactionVo();
			t.setCategory(findCategory("여행"));
			t.setKind(KindType.SPENDING);
			t.setPayAccount(findAccount("복슬 카드"));
			t.setAttribute(findCode("단순 지출"));

			int money = RandomUtils.nextInt(6, 20) * 500;
			t.setMoney(money);

			Date tranDate = DateUtil.toDate(date);
			t.setTransactionDate(tranDate);
			t.setNote("입장료");
			transactionRepository.save(t);
		});

		// 콘서트
		inserter.add(date -> {
			double r = Math.random();
			if (r > 0.005) {
				return;
			}

			TransactionVo t = new TransactionVo();
			t.setCategory(findCategory("기타문화생활비"));
			t.setKind(KindType.SPENDING);
			t.setPayAccount(findAccount("복슬 카드"));
			t.setAttribute(findCode("단순 지출"));

			int money = RandomUtils.nextInt(3, 10) * 10_000;
			t.setMoney(money);

			Date tranDate = DateUtil.toDate(date);
			t.setTransactionDate(tranDate);
			t.setNote("복슬걸즈 콘서트 관람");
			transactionRepository.save(t);
		});


		// 노트북 구매
		inserter.add(date -> {
			double r = Math.random();
			if (r > 0.002) {
				return;
			}

			TransactionVo t = new TransactionVo();
			t.setCategory(findCategory("여행"));
			t.setKind(KindType.SPENDING);
			t.setPayAccount(findAccount("복슬 카드"));
			t.setAttribute(findCode("단순 지출"));

			int money = RandomUtils.nextInt(20, 50) * 50_000;
			t.setMoney(money);

			Date tranDate = DateUtil.toDate(date);
			t.setTransactionDate(tranDate);
			t.setNote("노트북");
			transactionRepository.save(t);
		});


		// ======================================
		// 이체
		// ======================================


		return inserter;
	}

	/**
	 * @param decision      입력 여부
	 * @param money         금액
	 * @param date          날짜
	 * @param kind          거래 유형
	 * @param categoryName  카테고리
	 * @param accountName   계좌 이름
	 * @param attributeName 거래 속성
	 * @param note          메모
	 */
	private void doTransction(BooleanSupplier decision, Supplier<Integer> money,
							  LocalDate date, KindType kind, String categoryName, String accountName, String attributeName, String note) {
		if (!decision.getAsBoolean()) {
			return;
		}

		TransactionVo t = new TransactionVo();
		t.setCategory(findCategory(categoryName));
		t.setKind(kind);
		t.setPayAccount(findAccount(accountName));
		t.setAttribute(findCode(attributeName));
		t.setMoney(money.get());
		Date tranDate = DateUtil.toDate(date);
		t.setTransactionDate(tranDate);
		t.setNote(note);
		transactionRepository.save(t);
	}


	/**
	 * 분류 정보 등록
	 */
	private void makeCategory() {
		String result;
		try {
			URL categoryUrl = MakerSampleDataService.class.getResource("/sample/category.txt");
			result = IOUtils.toString(categoryUrl.openStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Map<Integer, CategoryVo> categoryMapping = new HashMap<>();

		String[] lines = result.split("\n");
		Stream.of(lines).forEach((line) -> {
			String[] token = line.trim().split("\t");
			CategoryVo category = new CategoryVo();
			category.setKind(KindType.valueOf(token[1]));
			category.setName(token[2]);
			category.setOrderNo(Integer.parseInt(token[3]));
			int parentSeq = Integer.parseInt(token[4]);
			CategoryVo parent = categoryMapping.get(parentSeq);
			if (parent != null) {
				category.setParentSeq(parent.getCategorySeq());
			}
			categoryRepository.saveAndFlush(category);

			int categorySeq = Integer.parseInt(token[0]);
			categoryMapping.put(categorySeq, category);
		});
	}

	/**
	 * 계좌 만들기
	 */
	private void makeAccount() {
		AccountVo account = new AccountVo();
		account.setName("월세보증금");
		account.setKindCode(findCode("월세보증금"));
		account.setBalance(30_000_000);
		account.setTerm("2019-04-03 ~ 2021-04-02");
		account.setMonthlyPay("50만원");
		account.setTransferDate("매월 10일");
		account.setNote("집주인: 복슬이(011-486-0000");
		acccuntRepository.saveAndFlush(account);

		account = new AccountVo();
		account.setName("지갑");
		account.setKindCode(findCode("현금"));
		account.setBalance(150_000);
		acccuntRepository.saveAndFlush(account);

		account = new AccountVo();
		account.setName("복슬은행 월급통장");
		account.setKindCode(findCode("은행통장"));
		account.setAccountNumber("112-1112-555-7777");
		account.setBalance(2_501_251);
		account.setInterestRate("0.1%");
		acccuntRepository.saveAndFlush(account);

		account = new AccountVo();
		account.setName("복슬증권 주식계좌");
		account.setKindCode(findCode("은행통장"));
		account.setAccountNumber("555-2222-5588");
		account.setBalance(3_580_500);
		account.setInterestRate("0%");
		acccuntRepository.saveAndFlush(account);


		account = new AccountVo();
		account.setName("복슬은행 적금");
		account.setKindCode(findCode("정기적금"));
		account.setAccountNumber("111-222333-44555");
		account.setBalance(15_000_000);
		account.setExpDate("2025-02-01");
		account.setMonthlyPay("100만원");
		account.setTransferDate("매월 10일");
		account.setInterestRate("8%");
		acccuntRepository.saveAndFlush(account);


		account = new AccountVo();
		account.setName("복슬은행 정기예금");
		account.setKindCode(findCode("정기예금"));
		account.setAccountNumber("111-222333-87877");
		account.setBalance(25_000_000);
		account.setExpDate("2023-10-01");
		account.setInterestRate("6.5%");
		acccuntRepository.saveAndFlush(account);

		account = new AccountVo();
		account.setName("복슬전자");
		account.setKindCode(findCode("주식"));
		account.setBalance(13_500_000);
		account.setNote("2020-01-25 현재 +30%");
		acccuntRepository.saveAndFlush(account);

		account = new AccountVo();
		account.setName("복슬닷컴");
		account.setKindCode(findCode("주식"));
		account.setBalance(3_500_000);
		account.setNote("2020-01-25 현재 -5%");
		acccuntRepository.saveAndFlush(account);

		account = new AccountVo();
		account.setName("복슬 인덱스 펀드(적립형)");
		account.setKindCode(findCode("펀드"));
		account.setAccountNumber("110-00-2222-111");
		account.setBalance(10_000_000);
		account.setMonthlyPay("20만원");
		acccuntRepository.saveAndFlush(account);

		account = new AccountVo();
		account.setName("복슬 카드");
		account.setKindCode(findCode("신용카드"));
		account.setAccountNumber("0100-0000-1111-5555");
		account.setBalance(-1_580_510);
		account.setTransferDate("매월 10일 결제");
		acccuntRepository.saveAndFlush(account);

		account = new AccountVo();
		account.setName("내 땅");
		account.setKindCode(findCode("부동산"));
		account.setBalance(175_000_000);
		account.setNote("바다 보이는 넓은 언덕");
		acccuntRepository.saveAndFlush(account);

		account = new AccountVo();
		account.setName("복슬은행 대출금");
		account.setKindCode(findCode("빌린돈"));
		account.setBalance(-10_000_000);
		account.setExpDate("2020-05-22");
		account.setInterestRate("5.28%");
		acccuntRepository.saveAndFlush(account);


	}


	/**
	 * 이름으로 코드를 찾고,
	 *
	 * @param name
	 * @return
	 */
	private int findCode(String name) {
		if (codeList == null) {
			codeList = codeItemRepository.findAll();
		}
		Optional<CodeItemVo> code = codeList.stream().filter(c -> c.getName().equals(name)).findFirst();
		return code.get().getCodeItemKey().getCodeItemSeq();
	}

	/**
	 * @param name
	 * @return 카테고리 정보 반환
	 */
	private CategoryVo findCategory(String name) {
		List<CategoryVo> categoryList = categoryRepository.findAll();
		if (childCategoryList == null) {
			childCategoryList = categoryList.stream().filter(c -> c.getParentSeq() != 0).collect(Collectors.toList());
		}
		Optional<CategoryVo> category = childCategoryList.stream().filter(c -> c.getName().equals(name)).findFirst();
		return category.get();
	}

	/**
	 * 계좌 찾기
	 *
	 * @param name
	 * @return
	 */
	private int findAccount(String name) {
		if (accountList == null) {
			accountList = acccuntRepository.findAll();
		}

		Optional<AccountVo> account = accountList.stream().filter(c -> c.getName().equals(name)).findFirst();
		return account.get().getAccountSeq();
	}


	/**
	 * 코드 생성
	 */
	private void makeCode() {

		List<CodeValue> datas = new ArrayList<>();
		datas.add(new CodeValue("현금", CodeKind.KIND_CODE));
		datas.add(new CodeValue("신용카드", CodeKind.KIND_CODE));
		datas.add(new CodeValue("체크카드", CodeKind.KIND_CODE));
		datas.add(new CodeValue("은행통장", CodeKind.KIND_CODE));
		datas.add(new CodeValue("정기예금", CodeKind.KIND_CODE));
		datas.add(new CodeValue("정기적금", CodeKind.KIND_CODE));
		datas.add(new CodeValue("주식", CodeKind.KIND_CODE));
		datas.add(new CodeValue("펀드", CodeKind.KIND_CODE));
		datas.add(new CodeValue("받을돈(미수금)", CodeKind.KIND_CODE));
		datas.add(new CodeValue("빌린돈", CodeKind.KIND_CODE));
		datas.add(new CodeValue("보험", CodeKind.KIND_CODE));
		datas.add(new CodeValue("월세보증금", CodeKind.KIND_CODE));
		datas.add(new CodeValue("전세보증금", CodeKind.KIND_CODE));
		datas.add(new CodeValue("부동산", CodeKind.KIND_CODE));
		datas.add(new CodeValue("단순 이체", CodeKind.ATTR_TRANSFER));
		datas.add(new CodeValue("투자 이체", CodeKind.ATTR_TRANSFER));
		datas.add(new CodeValue("부채 이체", CodeKind.ATTR_TRANSFER));
		datas.add(new CodeValue("단순 지출", CodeKind.ATTR_SPENDING));
		datas.add(new CodeValue("고정 지출", CodeKind.ATTR_SPENDING));
		datas.add(new CodeValue("단순 수입", CodeKind.ATTR_INCOME));
		datas.add(new CodeValue("투자 수입", CodeKind.ATTR_INCOME));

		CodeKind currentKind = null;
		AtomicInteger inc = null;
		for (CodeValue v : datas) {
			if (currentKind != v.kind) {
				inc = new AtomicInteger(0);
				currentKind = v.kind;
			}

			CodeItemKey key = new CodeItemKey();
			key.setCodeItemSeq(inc.incrementAndGet());
			CodeMainVo mainCode = codeMainRepository.getOne(currentKind.name());
			key.setCodeMain(mainCode);
			CodeItemVo code = new CodeItemVo();
			code.setCodeItemKey(key);
			code.setName(v.name);
			code.setOrderNo(key.getCodeItemSeq());
			codeItemRepository.saveAndFlush(code);
		}
	}

	@AllArgsConstructor
	class CodeValue {
		private String name;
		private CodeKind kind;
	}
}
