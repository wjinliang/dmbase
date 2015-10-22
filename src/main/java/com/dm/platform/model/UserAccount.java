package com.dm.platform.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.Pattern;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity(name = "UserAccount")
@Table(name = "T_USER_ACCOUNT")
public class UserAccount implements Serializable, UserDetails {
	private static final long serialVersionUID = 1724450140216701197L;

	@Id
	@Pattern(regex = "[A-Za-z0-9_\\-]+")
	@Column(name = "CODE", nullable = false, length = 50)
	private String code;

	@Column(name = "LOGINNAME", nullable = false, length = 50)
	private String loginname;

	@Column(name = "NAME", nullable = false, length = 50)
	private String name;

	@Column(name = "PASSWORD", nullable = false, length = 64)
	private String password;
	//是否失效
	@Column(name = "ENABLED", nullable = false)
	private boolean enabled;
	//是否锁定
	@Column(name = "NONLOCKED")
	private boolean nonLocked;
	//账号是否过期
	@Column(name = "ACCOUNT_EXPIRED")
	private boolean accountExpired;
	//密码是否过期
	@Column(name = "PASSWORD_EXPIRED")
	private boolean passwordExpired;
	//排序号
	@Column(name = "SEQ")
	private Long seq;

	/*
	 * 个人基本资料
	 * */
	@Column(name = "MOBILE", length = 50)
	private String mobile;

	@Column(name = "EMAIL", length = 50)
	private String email;

	@Column(name = "HEADPIC")
	private String headpic;

	@OneToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE,
			CascadeType.PERSIST })
	@JoinColumn(name = "headphoto_id")
	private FileEntity headphoto;

	//角色
	@ManyToMany(cascade = { CascadeType.REFRESH, CascadeType.MERGE,
			CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JoinTable(name = "T_USER_ACCOUNT_ROLE", joinColumns = @JoinColumn(name = "USER_CODE", referencedColumnName = "CODE"), inverseJoinColumns = @JoinColumn(name = "ROLE_CODE", referencedColumnName = "CODE"))
	@OrderBy("seq asc")
	private Set<UserRole> roles=new HashSet<UserRole>();
	//组织
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH,
			CascadeType.MERGE, CascadeType.PERSIST })
	@JoinColumn(name = "org_id")
	@OrderBy("seq asc")
	private Org org = new Org();

	/*
	 * 登录日志基本信息
	 * */
	@Column(name = "LASTLOGINTIME", length = 50)
	private String lastLoginTime;

	@Column(name = "REMOTEIPADDR", length = 50)
	private String remoteIpAddr;

	@Column(name = "LOGINCOUNT",columnDefinition="int default 0")
	private Long loginCount;


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public boolean isNonLocked() {
		return nonLocked;
	}

	public void setNonLocked(boolean nonLocked) {
		this.nonLocked = nonLocked;
	}


	public boolean isAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public boolean isPasswordExpired() {
		return passwordExpired;
	}

	public void setPasswordExpired(boolean passwordExpired) {
		this.passwordExpired = passwordExpired;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}


	public String getRemoteIpAddr() {
		return remoteIpAddr;
	}

	public void setRemoteIpAddr(String remoteIpAddr) {
		this.remoteIpAddr = remoteIpAddr;
	}


	public Long getLoginCount() {
		return loginCount==null?0:loginCount;
	}

	public void setLoginCount(Long loginCount) {
		this.loginCount = loginCount;
	}

	public String getHeadpic() {
		return headpic;
	}

	public void setHeadpic(String headpic) {
		this.headpic = headpic;
	}


	public FileEntity getHeadphoto() {
		return headphoto;
	}

	public void setHeadphoto(FileEntity headphoto) {
		this.headphoto = headphoto;
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}


	public Set<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}

	public void addRole(UserRole role){
		Set<UserRole> roleset = getRoles();
		roleset.add(role);
		setRoles(roleset);
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	// ----------------------------------------------------------------------------------------
	@Override
	@Transient
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		Collection<GrantedAuthority> authCol = new ArrayList<GrantedAuthority>();
		Set<UserRole> userRoleSet = this.getRoles();
		for (UserRole ur : userRoleSet) {
			GrantedAuthority ga = new SimpleGrantedAuthority(ur.getCode());
			authCol.add(ga);
		}
		return authCol;
	}

	@Override
	@Transient
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.loginname;
	}

	@Override
	@Transient
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		//账号过期
		return !isAccountExpired();
	}

	@Override
	@Transient
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		//账号锁定
		return isNonLocked();
	}

	@Override
	@Transient
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		//密码失效
		return !isPasswordExpired();
	}

	@Override
	public boolean equals(Object rhs) {
		if (rhs instanceof UserAccount) {
			return getLoginname().equals(((UserAccount) rhs).getLoginname());
		}
		return false;
	}

	/**
	 * Returns the hashcode of the {@code username}.
	 */
	@Override
	public int hashCode() {
		if (getLoginname() != null) {
			return getLoginname().hashCode();
		} else {
			return 0;
		}
	}


}
