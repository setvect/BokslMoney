package com.setvect.bokslmoney.user.vo;

import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.setvect.bokslmoney.ApplicationUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 사용자
 */
@Entity
@Table(name = "AA_USER")
@Setter
@Getter
@ToString(exclude = { "password", "userRole" })
public class UserVo implements UserDetails {

	/** */
	private static final long serialVersionUID = 1344718828637691374L;

	/** 사용자 아이디 */
	@Id
	@Column(name = "USER_ID", unique = true, nullable = false, length = 20)
	private String userId;

	/** 이름 */
	@Column(name = "NAME", nullable = false, length = 50)
	private String name;

	/** 비밀번호 */
	@Column(name = "PASSWD", nullable = false, length = 60)
	@JsonIgnore
	private String password;

	/** 삭제 여부 */
	@Column(name = "DELETE_FLAG", nullable = false, length = 1)
	@Type(type = "yes_no")
	private boolean deleteFlag;

	/** 보유 권한 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	private Set<UserRoleVo> userRole;

	/**
	 * @return 비밀번호
	 */
	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * @param role
	 *            비교할 role
	 * @return 사용자가 해당 role를 가지고 있는지 판단
	 */
	public boolean hasRole(final RoleName role) {
		long count = getUserRole().stream().filter(r -> r.getRoleName() == role).count();
		return count != 0;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return ApplicationUtil.buildUserAuthority(getUserRole());
	}

	@Override
	public String getUsername() {
		return userId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}