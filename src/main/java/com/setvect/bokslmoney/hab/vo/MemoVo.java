package com.setvect.bokslmoney.hab.vo;

import java.util.Date;

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
 * 메모
 */
@Entity
@Table(name = "BD_MEMO")
@Setter
@Getter
@ToString
public class MemoVo {
	/** 메모 일련번호 */
	@Id
	@Column(name = "MEMO_SEQ", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int memoSeq;

	/** 메모 내용 */
	@Column(name = "NOTE", length = 1000, nullable = false)
	private String note;

	/** 메모 일 */
	@Column(name = "NOTE_DATE", nullable = false)
	private Date memoDate;

	/** 삭제 여부 */
	@Column(name = "DELETE_FLAG", length = 1, nullable = false)
	@Type(type = "yes_no")
	private boolean deleteFlag;

}
