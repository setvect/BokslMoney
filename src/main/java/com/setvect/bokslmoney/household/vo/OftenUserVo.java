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
 * 자주 쓰는 항목
 */
@Entity
@Table(name = "BC_OFTEN_USED")
@Setter
@Getter
@ToString
public class OftenUserVo {
	/** 자주 쓰는 항목 일련번호 */
	@Id
	@Column(name = "OFTEN_USER_SEQ", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int oftenUsedSeq;

	/** 항목 일련번호 */
	@Column(name = "ITEM_SEQ", nullable = false)
	private int itemSeq;

	/** 출금계좌 */
	@Column(name = "PAY_ACCOUNT", nullable = true)
	private int payAccount;

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
