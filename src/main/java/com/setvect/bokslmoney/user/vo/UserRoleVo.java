package com.setvect.bokslmoney.user.vo;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 권한
 */
@Entity
@Table(name = "AB_ROLE")
@Setter
@Getter
@ToString
public class UserRoleVo {

	/** 일련번호 */
	@Id
	@Column(name = "ROLE_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int roleSeq;

	/** 사용자 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "username", nullable = false)
	@JsonIgnore
	private UserVo user;

	/** 권한 이름 */
	@Column(name = "ROLE_NAME", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private RoleName roleName;

}