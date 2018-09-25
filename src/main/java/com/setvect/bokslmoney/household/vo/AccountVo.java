package com.setvect.bokslmoney.household.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 계좌정보
 */
@Entity
@Table(name = "BA_ACCOUNT")
@Setter
@Getter
@ToString
public class AccountVo {

	/** 계좌_일련번호 */
	@Id
	@Column(name = "ACCOUNT_SEQ", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int accountSeq;

	/** 이름 */
	@Column(name = "NAME", length = 100, nullable = false)
	private String name;

	/** 계좌번호 */
	@Column(name = "ACCOUNT_NUMBER", length = 100, nullable = true)
	private String accountNumber;

	/**
	 * 자산종류<br>
	 * 코드 값: KIND_CODE<br>
	 * 신용카드, 통장, 지갑 등
	 */
	@Column(name = "KIND_CODE", nullable = false)
	private int kindCode;

	/** 잔고 */
	@Column(name = "BALANCE", nullable = false)
	private int balance;

	/** 이율 */
	@Column(name = "INTEREST_RATE", length = 100, nullable = true)
	private String interestRate;

	/** 계약기간 */
	@Column(name = "TERM", length = 100, nullable = true)
	private String term;

	/** 만기일 */
	@Column(name = "EXP_DATE", length = 100, nullable = true)
	private String expDate;

	/** 월 납입액 */
	@Column(name = "MONTHLY_PAY", length = 100, nullable = true)
	private String monthlyPay;

	/** 이체일 */
	@Column(name = "TRANSFER_DATE", length = 100, nullable = true)
	private String transferDate;

	/** 메모 내용 */
	@Column(name = "NOTE", length = 100, nullable = true)
	private String note;

	/** 삭제 여부 */
	@Column(name = "DELETE_FLAG", length = 1, nullable = false)
	@Type(type = "yes_no")
	private boolean deleteFlag;

}
