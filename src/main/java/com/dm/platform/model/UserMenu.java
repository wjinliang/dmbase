package com.dm.platform.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity(name = "UserMenu")
@Table(name = "T_USER_MENU")
public class UserMenu implements Serializable{

	/**
	 * CHENGUOJUN
	 */
	private static final long serialVersionUID = -3489291121819692381L;

	private Long id;

	private String name;

	private String url;
	//是否隐藏菜单
	private String isShow;
	//是否公用方法
	private String isCommon;
	
	private String icon;
	
	private String detail;
	
	private Long seq;

	private UserMenu puserMenu;
	
	private Set<UserMenu> children = new HashSet<UserMenu>();
	
	private Boolean checked;

	private Boolean open;

	private Set<UserRole> roles;
	
	private Set<MenuGroup> menugroups;
	
	@Id
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "NAME", nullable = false, length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "MENUURL")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Column(name = "SEQ")
	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}
	
	@ManyToMany(mappedBy="menus",fetch = FetchType.EAGER)
	@OrderBy("seq asc")
	public Set<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}
	
	@Column(name = "ICON")
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@Column(name = "CHECKED")
	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	
	@Column(name = "OPEN")
	public Boolean getOpen() {
		return open;
	}
	public void setOpen(Boolean open) {
		this.open = open;
	}
	
	@Column(name = "ISSHOW")
	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	
	@Column(name = "ISCOMMON",length = 10)
	public String getIsCommon() {
		return isCommon;
	}

	public void setIsCommon(String isCommon) {
		this.isCommon = isCommon;
	}
	
	@ManyToOne(fetch = FetchType.EAGER,cascade = { CascadeType.REFRESH, CascadeType.MERGE,CascadeType.PERSIST})
	@JoinColumn(name="PID")
	public UserMenu getPuserMenu() {
		return puserMenu;
	}
	
	public void setPuserMenu(UserMenu puserMenu) {
		this.puserMenu = puserMenu;
	}
	
	@OneToMany(fetch = FetchType.EAGER,cascade = { CascadeType.REFRESH, CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REMOVE})
	@JoinColumn(name="PID")
	@OrderBy("seq asc")
	public Set<UserMenu> getChildren() {
		return children;
	}
	public void setChildren(Set<UserMenu> children) {
		this.children = children;
	}
	
	@ManyToMany(mappedBy="menus",fetch = FetchType.EAGER)
	@OrderBy("seq")
	public Set<MenuGroup> getMenugroups() {
		return menugroups;
	}
	public void setMenugroups(Set<MenuGroup> menugroups) {
		this.menugroups = menugroups;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.getId()).toHashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof UserMenu)){
			return false;
		}else{
			UserMenu cast = (UserMenu) other;
			if(this.getId()!=null){
			if(cast.getId().longValue()==this.getId().longValue()){
				return true;
			}else{
				return false;
			}
			}else{
				return false;
			}
		}
	}
	@Column(name = "DETAIL")
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
