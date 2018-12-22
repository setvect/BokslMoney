package com.setvect.bokslmoney.transaction.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 자주 쓰는 항목
 */
@Entity
@Table(name = "BC_OFTEN_USED")
@Setter
@Getter
@ToString
public class OftenUsedVo {
	/** 자주 쓰는 항목 일련번호 */
	@Id
	@Column(name = "OFTEN_USED_SEQ", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int oftenUsedSeq;

	/** 거래 분류 정보 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CATEGORY_SEQ", nullable = false)
	private CategoryVo category;

	/** 부모 분류 정보*/
	@Transient
	private CategoryVo parentCategory;

	/** 유형(지출, 수입, 이체) */
	@Column(name = "KIND", length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	private KindType kind;

	/** 출금계좌 */
	@Column(name = "PAY_ACCOUNT", nullable = true)
	private int payAccount;

	/** 거래제목 */
	@Column(name = "TITLE", nullable = false, length = 200)
	private String title;

	/** 입금계좌 */
	@Column(name = "RECEIVE_ACCOUNT", nullable = true)
	private int receiveAccount;

	/** 금액 */
	@Column(name = "MONEY", nullable = true)
	private int money;

	/** 항목 설명 */
	@Column(name = "NOTE", nullable = true)
	private String note;

	/** 속성 */
	@Column(name = "ATTRIBUTE", nullable = true)
	private int attribute;

	/** 항목내 정렬 순서 */
	@Column(name = "ORDER_NO", nullable = false)
	private int orderNo;

	/** 삭제 여부 */
	@Column(name = "DELETE_FLAG", length = 1, nullable = false)
	@Type(type = "yes_no")
	private boolean deleteFlag;

}
