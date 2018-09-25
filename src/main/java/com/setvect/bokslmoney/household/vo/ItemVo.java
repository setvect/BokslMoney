package com.setvect.bokslmoney.household.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 수입, 지출, 이체 항목
 */
@Entity
@Table(name = "BB_ITEM")
@Setter
@Getter
@ToString
public class ItemVo {
	/** 아이템 일련번호 */
	@Id
	@Column(name = "ITEM_SEQ", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int itemSeq;

	/** 유형 */
	@Column(name = "KIND", length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	private KindType kind;

	/** 항목이름 */
	@Column(name = "NAME", length = 100, nullable = false)
	private String name;

	/** 부모항목 번호 */
	@Column(name = "PARENT_ITEM_SEQ", nullable = true)
	private int parentItemSeq;

	/** 항목내 정렬 순서 */
	@Column(name = "ORDER_NO", nullable = false)
	private int orderNo;

	/** 삭제 여부 */
	@Column(name = "DELETE_FLAG", length = 1, nullable = false)
	@Type(type = "yes_no")
	private boolean deleteFlag;

}
