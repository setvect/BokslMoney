package com.setvect.bokslmoney.hab.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 수입, 지출, 이체 항목
 */
@Entity
@Table(name = "BB_CATEGORY")
@Setter
@Getter
@ToString
@EqualsAndHashCode(of = { "categorySeq" })
public class CategoryVo {
	/** 아이템 일련번호 */
	@Id
	@Column(name = "CATEGORY_SEQ", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int categorySeq;

	/** 유형 */
	@Column(name = "KIND", length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	private KindType kind;

	/** 항목이름 */
	@Column(name = "NAME", length = 100, nullable = false)
	private String name;

	/** 부모항목 번호 */
	@Column(name = "PARENT_SEQ", nullable = true)
	private int parentSeq;

	/** 항목내 정렬 순서 */
	@Column(name = "ORDER_NO", nullable = false)
	private int orderNo;

	/** 삭제 여부 */
	@Column(name = "DELETE_FLAG", length = 1, nullable = false)
	@Type(type = "yes_no")
	private boolean deleteFlag;

	/** 부모 카테고리 */
	@Transient
	private CategoryVo parentCategory;

	public boolean isRoot() {
		return parentSeq == 0;
	}

}
