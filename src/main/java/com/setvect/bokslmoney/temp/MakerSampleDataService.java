package com.setvect.bokslmoney.temp;

import com.setvect.bokslmoney.account.repository.AccountRepository;
import com.setvect.bokslmoney.account.vo.AccountVo;
import com.setvect.bokslmoney.code.repository.CodeItemRepository;
import com.setvect.bokslmoney.code.repository.CodeMainRepository;
import com.setvect.bokslmoney.code.service.CodeKind;
import com.setvect.bokslmoney.code.vo.CodeItemKey;
import com.setvect.bokslmoney.code.vo.CodeItemVo;
import com.setvect.bokslmoney.code.vo.CodeMainVo;
import lombok.AllArgsConstructor;
import org.apache.commons.configuration.FileConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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

	/**
	 * 임시로 사용할 데이터 생성
	 */
	public void makeSampleData() {
		makeCode();
		makeAccount();
	}

	/**
	 * 계좌 만들기
	 */
	private void makeAccount() {
		acccuntRepository.deleteAll();

		AccountVo account = new AccountVo();
		account.setName("월세보증금");
		account.setKindCode(findKind("월세보증금"));
		account.setBalance(30_000_000);
		account.setTerm("2019-04-03 ~ 2021-04-02");
		account.setMonthlyPay("50만원");
		account.setTransferDate("매월 10일");
		account.setNote("집주인: 복슬이(011-486-0000");
		acccuntRepository.saveAndFlush(account);

		account = new AccountVo();
		account.setName("지갑");
		account.setKindCode(findKind("현금"));
		account.setBalance(150_000);
		acccuntRepository.saveAndFlush(account);

		account = new AccountVo();
		account.setName("월급통장");
		account.setKindCode(findKind("은행통장"));
		account.setAccountNumber("112-1112-555-7777");
		account.setBalance(2_501_251);
		account.setInterestRate("0.1%");
		acccuntRepository.saveAndFlush(account);

		account = new AccountVo();
		account.setName("복슬은행 적금");
		account.setKindCode(findKind("정기적금"));
		account.setAccountNumber("111-222333-44555");
		account.setBalance(15_000_000);
		account.setExpDate("2025-02-01");
		account.setMonthlyPay("100만원");
		account.setTransferDate("매월 10일");
		account.setInterestRate("8%");
		acccuntRepository.saveAndFlush(account);

		account = new AccountVo();
		account.setName("복슬은행 예금");
		account.setKindCode(findKind("정기예금"));
		account.setAccountNumber("111-222333-87877");
		account.setBalance(25_000_000);
		account.setExpDate("2023-10-01");
		account.setInterestRate("6.5%");
		acccuntRepository.saveAndFlush(account);

		account = new AccountVo();
		account.setName("복슬전자");
		account.setKindCode(findKind("주식"));
		account.setBalance(13_500_000);
		account.setNote("2020-01-25 현재 +30%");
		acccuntRepository.saveAndFlush(account);

		account = new AccountVo();
		account.setName("복슬닷컴");
		account.setKindCode(findKind("주식"));
		account.setBalance(3_500_000);
		account.setNote("2020-01-25 현재 -5%");
		acccuntRepository.saveAndFlush(account);

		account = new AccountVo();
		account.setName("복슬 인덱스 펀드(적립형)");
		account.setKindCode(findKind("펀드"));
		account.setAccountNumber("110-00-2222-111");
		account.setBalance(10_000_000);
		account.setMonthlyPay("20만원");
		acccuntRepository.saveAndFlush(account);

		account = new AccountVo();
		account.setName("복슬 카드");
		account.setKindCode(findKind("신용카드"));
		account.setAccountNumber("0100-0000-1111-5555");
		account.setBalance(-1_580_510);
		account.setTransferDate("매월 10일 결제");
		acccuntRepository.saveAndFlush(account);

		account = new AccountVo();
		account.setName("내 땅");
		account.setKindCode(findKind("부동산"));
		account.setBalance(175_000_000);
		account.setNote("바다 보이는 넓은 언덕");
		acccuntRepository.saveAndFlush(account);

		account = new AccountVo();
		account.setName("복슬은행 대출금");
		account.setKindCode(findKind("빌린돈"));
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
	private int findKind(String name) {
		List<CodeItemVo> codeList = codeItemRepository.findAll();
		Optional<CodeItemVo> code = codeList.stream().filter(c -> c.getName().equals(name)).findFirst();
		return code.get().getCodeItemKey().getCodeItemSeq();
	}

	/**
	 * 코드 생성
	 */
	private void makeCode() {
		codeItemRepository.deleteAll();
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
