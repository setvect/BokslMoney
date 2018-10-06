package com.setvect.bokslmoney.hab.vo;

import java.util.Date;

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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 지출, 수입, 이체내역
 */
@Entity
@Table(name = "BE_TRANSFER")
@Setter
@Getter
@ToString
public class TransferVo {
	/** 내역 일련번호 */
	@Id
	@Column(name = "TRANSFER_SEQ", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int transferSeq;

	/** 메인코드 종속 일련번호 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ITEM_SEQ", nullable = false)
	private ItemVo item;

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
	@Column(name = "TRANSFER_DATE", nullable = false)
	private Date transferDate;

	/** 메모 내용 */
	@Column(name = "NOTE", length = 100, nullable = false)
	private String note;

	/** 수수료 */
	@Column(name = "FEE", nullable = true)
	private int fee;

}
