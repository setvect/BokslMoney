package com.setvect.bokslmoney.temp;

import com.setvect.bokslmoney.code.repository.CodeItemRepository;
import com.setvect.bokslmoney.code.repository.CodeMainRepository;
import com.setvect.bokslmoney.code.service.CodeKind;
import com.setvect.bokslmoney.code.vo.CodeItemKey;
import com.setvect.bokslmoney.code.vo.CodeItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public void makeSampleData(){
		CodeItemKey key = new CodeItemKey();
		key.setCodeItemSeq(1);
		key.setCodeMain(codeMainRepository.getOne(CodeKind.KIND_CODE.name()));
		CodeItemVo code = new CodeItemVo();
		code.setCodeItemKey(key);
		code.setName("지갑");
		code.setOrderNo(1);
		codeItemRepository.save(code);
	}
}
