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
			doTransction(() -> date.getDayOfMonth() == 5, () -> 3_185_950,
					date, KindType.INCOME, "급여", null, "복슬은행 월급통장", "단순 수입", "월급");
		});


		// 성과급
		inserter.add(date -> {
			doTransction(() -> date.getMonthValue() == 12 && date.getDayOfMonth() == 30, () -> 1_500_000,
					date, KindType.INCOME, "성과급", null, "복슬은행 월급통장", "단순 수입", "연말 성과급");
		});

		// 이자, 확률:1%, 1000 ~ 2000원
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.01, () -> RandomUtils.nextInt(1000, 2000),
					date, KindType.INCOME, "이자소득", null, "복슬은행 월급통장", "투자 수입", "예금 이자");
		});


		// 배당금, 매년 12월 10일, 10만 ~ 30만
		inserter.add(date -> {
			doTransction(() -> date.getMonthValue() == 12 && date.getDayOfMonth() == 10, () -> RandomUtils.nextInt(100_000, 300_000),
					date, KindType.INCOME, "배당금", null, "복슬증권 주식계좌", "투자 수입", "주식 배당금");
		});

		// 용돈 받음
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.01, () -> RandomUtils.nextInt(5, 11) * 10_000,
					date, KindType.INCOME, "기타소득", null, "지갑", "단순 수입", "용돈 받음");
		});


		// ======================================
		// 지출
		// ======================================
		// 월세
		inserter.add(date -> {
			doTransction(() -> date.getDayOfMonth() == 20, () -> 500_000,
					date, KindType.SPENDING, "월세", "복슬은행 월급통장", null, "단순 지출", "월세");
		});

		// 전철비
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.9, () -> RandomUtils.nextInt(29, 100) * 50,
					date, KindType.SPENDING, "대중교통비", "복슬 카드", null, "단순 지출", "전철요금");
		});

		// 버스요금
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.4, () -> RandomUtils.nextInt(28, 60) * 50,
					date, KindType.SPENDING, "대중교통비", "복슬 카드", null, "단순 지출", "버스요금");
		});

		// 택시
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.05, () -> RandomUtils.nextInt(30, 200) * 1000,
					date, KindType.SPENDING, "대중교통비", "복슬 카드", null, "단순 지출", "택시비");
		});

		// 기차
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.03, () -> RandomUtils.nextInt(5, 100) * 1000,
					date, KindType.SPENDING, "대중교통비", "복슬 카드", null, "단순 지출", "기차요금");
		});


		// 주유
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.03, () -> RandomUtils.nextInt(2, 10) * 10_000,
					date, KindType.SPENDING, "주유비", "복슬 카드", null, "단순 지출", "기름값");
		});

		// 아침
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.01, () -> RandomUtils.nextInt(4, 10) * 1000,
					date, KindType.SPENDING, "아침", "복슬 카드", null, "단순 지출", "술");
		});


		// 점심
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.8, () -> RandomUtils.nextInt(10, 24) * 500,
					date, KindType.SPENDING, "주식/부식", "복슬 카드", null, "단순 지출", "점심");
		});

		// 술
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.1, () -> RandomUtils.nextInt(20, 100) * 1000,
					date, KindType.SPENDING, "담배/술", "복슬 카드", null, "단순 지출", "술");
		});

		// 영화
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.15, () -> RandomUtils.nextInt(6, 24) * 1000,
					date, KindType.SPENDING, "영화공연관람", "복슬 카드", null, "단순 지출", "영화관람");
		});

		// 당구
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.03, () -> RandomUtils.nextInt(10, 45) * 500,
					date, KindType.SPENDING, "기타문화생활비", "지갑", null, "단순 지출", "당구");
		});

		// 입장료
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.02, () -> RandomUtils.nextInt(6, 20) * 500,
					date, KindType.SPENDING, "여행", "복슬 카드", null, "단순 지출", "입장료");
		});

		// 콘서트
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.005, () -> RandomUtils.nextInt(3, 10) * 10_000,
					date, KindType.SPENDING, "영화공연관람", "복슬 카드", null, "단순 지출", "복슬걸즈 콘서트 관람");
		});

		// 노트북 구매
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.002, () -> RandomUtils.nextInt(20, 50) * 50_000,
					date, KindType.SPENDING, "컴퓨터 용품", "복슬 카드", null, "단순 지출", "노트북");
		});

		// 병원비
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.003, () -> RandomUtils.nextInt(50, 1_000) * 500,
					date, KindType.SPENDING, "병원비", "복슬 카드", null, "단순 지출", "병원비");
		});

		// 약값
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.008, () -> RandomUtils.nextInt(5, 50) * 500,
					date, KindType.SPENDING, "약값", "복슬 카드", null, "단순 지출", "약값");
		});

		// 학원비
		inserter.add(date -> {
			doTransction(() -> date.getDayOfMonth() == 15, () -> 180_000,
					date, KindType.SPENDING, "학원비", "복슬은행 월급통장", null, "단순 지출", "피아노 학원비");
		});

		// 보혐료
		inserter.add(date -> {
			doTransction(() -> date.getDayOfMonth() == 20, () -> 48_000,
					date, KindType.SPENDING, "생명보험료", "복슬은행 월급통장", null, "고정 지출", "보험료");
		});

		// 핸드폰
		inserter.add(date -> {
			doTransction(() -> date.getDayOfMonth() == 15, () -> 59_500,
					date, KindType.SPENDING, "이동전화", "복슬 카드", null, "고정 지출", "핸드폰 요금");
		});

		// 전기요금
		inserter.add(date -> {
			doTransction(() -> date.getDayOfMonth() == 10, () -> RandomUtils.nextInt(5, 50) * 500,
					date, KindType.SPENDING, "전기료", "복슬 카드", null, "고정 지출", "전기료");
		});

		// 가스요금
		inserter.add(date -> {
			doTransction(() -> date.getDayOfMonth() == 10, () -> RandomUtils.nextInt(5, 200) * 1000,
					date, KindType.SPENDING, "전기료", "복슬 카드", null, "고정 지출", "가스요금");
		});

		// 수도요금
		inserter.add(date -> {
			doTransction(() -> date.getDayOfMonth() == 10, () -> RandomUtils.nextInt(5, 20) * 1000,
					date, KindType.SPENDING, "수도료", "복슬은행 월급통장", null, "고정 지출", "수도요금");
		});

		// 관리비
		inserter.add(date -> {
			doTransction(() -> date.getDayOfMonth() == 10, () -> RandomUtils.nextInt(400, 520) * 100,
					date, KindType.SPENDING, "관리비", "복슬은행 월급통장", null, "고정 지출", "아파트 관리비");
		});


		// 옷
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.02, () -> RandomUtils.nextInt(50, 500) * 1_000,
					date, KindType.SPENDING, "의류", "복슬 카드", null, "단순 지출", "옷");
		});


		// 축의금
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.008, () -> RandomUtils.nextInt(5, 20) * 10_000,
					date, KindType.SPENDING, "축의금", "복슬은행 월급통장", null, "단순 지출", "축의금");
		});

		// 부의금
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.007, () -> RandomUtils.nextInt(5, 10) * 10_000,
					date, KindType.SPENDING, "부의금", "지갑", null, "단순 지출", "조의금");
		});


		// 세금
		inserter.add(date -> {
			doTransction(() -> date.getMonthValue() == 10 && date.getDayOfMonth() == 30, () -> 12_000,
					date, KindType.SPENDING, "기타세금", "복슬은행 월급통장", null, "단순 지출", "주민세");
		});

		// 그릇
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.007, () -> RandomUtils.nextInt(5, 10) * 1_000,
					date, KindType.SPENDING, "주방용품", "복슬 카드", null, "단순 지출", "그릇");
		});

		// 건전지
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.007, () -> RandomUtils.nextInt(2, 5) * 1_000,
					date, KindType.SPENDING, "잡화/소모품", "복슬 카드", null, "단순 지출", "건전지");
		});

		// ======================================
		// 이체
		// ======================================
		// 카드 값 결제
		inserter.add(date -> {
			doTransction(() -> date.getDayOfMonth() == 10, () -> RandomUtils.nextInt(40_000, 250_000) * 10,
					date, KindType.TRANSFER, "카드결제", "복슬은행 월급통장", "복슬 카드", "부채 이체", "카드값 결제");
		});

		// 펀드 불입
		inserter.add(date -> {
			doTransction(() -> date.getDayOfMonth() == 15, () -> 200_000,
					date, KindType.TRANSFER, "펀드불입", "복슬은행 월급통장", "복슬 인덱스 펀드(적립형)", "투자 이체", "복슬 펀드");
		});


		// 적금 불입
		inserter.add(date -> {
			doTransction(() -> date.getDayOfMonth() == 15, () -> 1_000_000,
					date, KindType.TRANSFER, "적금불입", "복슬은행 월급통장", "복슬은행 적금", "투자 이체", "복슬 적금");
		});


		// 돈 찾기
		inserter.add(date -> {
			doTransction(() -> Math.random() < 0.02, () -> RandomUtils.nextInt(5, 16) * 10_000,
					date, KindType.TRANSFER, "카드결제", "복슬은행 월급통장", "지갑", "단순 이체", "돈 찾기");
		});

		return inserter;
	}

	/**
	 * @param decision           입력 여부
	 * @param money              금액
	 * @param date               날짜
	 * @param kind               거래 유형
	 * @param categoryName       카테고리
	 * @param payAccountName     지출 계좌 이름
	 * @param receiveAccountName 수입 계좌 이름
	 * @param attributeName      거래 속성
	 * @param note               메모
	 */
	private void doTransction(BooleanSupplier decision, Supplier<Integer> money,
							  LocalDate date, KindType kind, String categoryName, String payAccountName, String receiveAccountName, String attributeName, String note) {
		if (!decision.getAsBoolean()) {
			return;
		}

		TransactionVo t = new TransactionVo();
		t.setCategory(findCategory(categoryName));
		t.setKind(kind);
		if (payAccountName != null) {
			t.setPayAccount(findAccount(payAccountName));
		}
		if (receiveAccountName != null) {
			t.setReceiveAccount(findAccount(receiveAccountName));
		}

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
