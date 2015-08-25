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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.builder.HashCodeBuilder;


@Entity(name = "MenuGroup")
@Table(name = "T_MENU_GROUP")
public class MenuGroup implements Serializable{

	/**
	 * CHENGJ
	 */
	private static final long serialVersionUID = 8243631177329260740L;
	
	private Long id;
	
	private String name;
	
	private String icon;
	
	private Long seq;
	
	private Set<UserRole> roles;
	
	private Set<UserMenu> menus = new HashSet<UserMenu>();

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
	
	@Column(name = "ICON")
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@Column(name = "SEQ")
	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}
	
	@ManyToMany(mappedBy="menugroups",fetch = FetchType.EAGER)
	public Set<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}
	
	@ManyToMany(fetch = FetchType.EAGER,cascade = { CascadeType.REFRESH, CascadeType.MERGE,CascadeType.PERSIST })
	@JoinTable(name = "T_USERMENU_MENUGROUP", 
	inverseJoinColumns = @JoinColumn(name = "MENU_CODE", referencedColumnName = "ID"),
	joinColumns = @JoinColumn(name = "MENU_GROUP_ID", referencedColumnName = "ID"))
	@OrderBy("seq")
	public Set<UserMenu> getMenus() {
		return menus;
	}

	public void setMenus(Set<UserMenu> menus) {
		this.menus = menus;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof MenuGroup)){
			return false;
		}else{
			MenuGroup cast = (MenuGroup) other;
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

}
