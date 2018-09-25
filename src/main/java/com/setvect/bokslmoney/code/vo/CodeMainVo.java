package com.setvect.bokslmoney.code.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 메인코드
 */
@Entity
@Table(name = "CA_CODE_MAIN")
@Setter
@Getter
@ToString
public class CodeMainVo {
	/** 메인코드 */
	@Id
	@Column(name = "CODE_MAIN_ID", unique = true, nullable = false, length = 20)
	private String codeMainId;

	/** 코드 이름 */
	@Column(name = "NAME", nullable = false, length = 100)
	private String name;

	/** 삭제 여부 */
	@Column(name = "DELETE_FLAG", length = 1, nullable = false)
	@Type(type = "yes_no")
	private boolean deleteFlag;
}
