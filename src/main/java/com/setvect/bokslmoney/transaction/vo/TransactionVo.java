package com.setvect.bokslmoney.transaction.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.setvect.bokslmoney.util.DateUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 지출, 수입, 이체내역
 */
@Entity
@Table(name = "BE_TRANSACTION", indexes = {
		@Index(name = "IDX_BE_TRANSACTION_DATE", columnList = "TRANSACTION_DATE", unique = false) })
@Setter
@Getter
@ToString
public class TransactionVo {
	/** 내역 일련번호 */
	@Id
	@Column(name = "TRANSACTION_SEQ", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int transactionSeq;

	/** 거래 분류 정보 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CATEGORY_SEQ", nullable = false)
	private CategoryVo category;

	/** 부모 분류 정보 */
	@Transient
	private CategoryVo parentCategory;

	/** 유형(지출, 수입, 이체) */
	@Column(name = "KIND", length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	private KindType kind;

	/** 출금계좌 */
	@Column(name = "PAY_ACCOUNT", nullable = true)
	private int payAccount;

	/** 입금계좌 */
	@Column(name = "RECEIVE_ACCOUNT", nullable = true)
	private int receiveAccount;

	/** 속성 */
	@Column(name = "ATTRIBUTE", nullable = true)
	private int attribute;

	/** 금액 */
	@Column(name = "MONEY", nullable = false)
	private int money;

	/** 사용일 */
	@Column(name = "TRANSACTION_DATE", nullable = false)
	private Date transactionDate;

	/** 메모 내용 */
	@Column(name = "NOTE", length = 100, nullable = false)
	private String note;

	/** 수수료 */
	@Column(name = "FEE", nullable = true)
	private int fee;

	/**
	 * @return 사용 월. 0(1월)부터 11까지
	 */
	public int getMonth() {
		return DateUtil.toLocalDate(transactionDate).getMonthValue() - 1;
	}

}
