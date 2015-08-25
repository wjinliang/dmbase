package com.dm.platform.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.builder.HashCodeBuilder;


@Entity
@Table(name = "T_ORG")
public class Org implements Serializable {
	private static final long serialVersionUID = -7414938571167391613L;

	private Long id;

	private String name;
	
	private String code;
	
	private Long seq;
	
	private Org parent;

	private List<Org> children = new ArrayList<Org>();
	
	private Set<UserAccount> user = new HashSet<UserAccount>();
	
	@Id
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="NAME",nullable = false, length = 32)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne(fetch = FetchType.EAGER,cascade = { CascadeType.REFRESH, CascadeType.MERGE,CascadeType.PERSIST })
	@JoinColumn(name = "parent_id", insertable = true, updatable = true)
	public Org getParent() {
		return parent;
	}

	public void setParent(Org parent) {
		this.parent = parent;
	}

	@OrderBy("name ASC")
	@OneToMany(fetch = FetchType.EAGER,cascade = { CascadeType.REFRESH, CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REMOVE })
	@JoinColumn(name = "parent_id")
	public List<Org> getChildren() {
		return children;
	}

	public void setChildren(List<Org> children) {
		this.children = children;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "org")
	public Set<UserAccount> getUser() {
		return user;
	}

	public void setUser(Set<UserAccount> user) {
		this.user = user;
	}

	@Column(name="CODE",nullable = false, length = 32)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name="SEQ",nullable = false)
	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}


	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Org)){
			return false;
		}else{
			Org cast = (Org) other;
			if(cast.getId().longValue()==this.getId().longValue()){
				return true;
			}else{
				return false;
			}
		}
	}


}
