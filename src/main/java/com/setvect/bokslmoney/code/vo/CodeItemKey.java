package com.setvect.bokslmoney.code.vo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 코드 항목 복합키 Key
 */
@Setter
@Getter
@ToString
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CodeItemKey implements Serializable {
	/** */
	private static final long serialVersionUID = 7327316880932658454L;

	/** 매인 코드 값 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codeMainId", nullable = false)
	private CodeMainVo codeMain;

	/** 메인코드 종속 일련번호 */
	@Column(name = "CODE_ITEM_SEQ", nullable = true)
	private int codeItemSeq;
}