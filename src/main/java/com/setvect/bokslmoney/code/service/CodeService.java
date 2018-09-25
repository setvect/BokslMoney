package com.setvect.bokslmoney.code.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.setvect.bokslmoney.code.repository.CodeMainRepository;
import com.setvect.bokslmoney.code.vo.CodeMainVo;

@Service
public class CodeService {
	@Autowired
	private CodeMainRepository codeMainRepository;

	/**
	 * 대분류 코드 값을 넣음
	 */
	public void init() {
		List<CodeMainVo> codes = Stream.of(CodeKind.values()).map((code) -> {
			CodeMainVo other = new CodeMainVo();
			other.setCodeMainId(code.name());
			other.setName(code.getName());
			CodeMainVo c = codeMainRepository.findById(code.name()).orElse(other);
			c.setDeleteFlag(false);
			return c;
		}).collect(Collectors.toList());

		codeMainRepository.saveAll(codes);
	}
}
