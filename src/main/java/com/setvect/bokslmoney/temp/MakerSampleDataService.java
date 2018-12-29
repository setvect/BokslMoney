package com.setvect.bokslmoney.temp;

import com.setvect.bokslmoney.code.repository.CodeItemRepository;
import com.setvect.bokslmoney.code.repository.CodeMainRepository;
import com.setvect.bokslmoney.code.service.CodeKind;
import com.setvect.bokslmoney.code.vo.CodeItemKey;
import com.setvect.bokslmoney.code.vo.CodeItemVo;
import com.setvect.bokslmoney.code.vo.CodeMainVo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
@Service
public class MakerSampleDataService {
	@Autowired
	private CodeItemRepository codeItemRepository;

	@Autowired
	private CodeMainRepository codeMainRepository;

	/**
	 * 임시로 사용할 데이터 생성
	 */
	public void makeSampleData() {
		makeCode();
	}

	/**
	 * 코드 생성
	 */
	private void makeCode() {
		codeItemRepository.deleteAll();
		List<CodeValue> datas = new ArrayList<>();
		datas.add(new CodeValue("지갑", CodeKind.KIND_CODE));
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
		datas.add(new CodeValue("전세보증금", CodeKind.KIND_CODE));
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
