package com.setvect.bokslmoney.code.vo;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 코드 항목값
 */
@Entity
@Table(name = "CB_CODE_ITEM")
@Setter
@Getter
@ToString
public class CodeItemVo {
	/** 복합키 */
	@EmbeddedId
	private CodeItemKey codeItemKey;

	/** 코드 이름 */
	@Column(name = "NAME", nullable = false, length = 100)
	private String name;

	/** 항목내 정렬 순서 */
	@Column(name = "ORDER_NO", nullable = false)
	private int orderNo;

	/** 삭제 여부 */
	@Column(name = "DELETE_FLAG", length = 1, nullable = false)
	@Type(type = "yes_no")
	private boolean deleteFlag;
}
