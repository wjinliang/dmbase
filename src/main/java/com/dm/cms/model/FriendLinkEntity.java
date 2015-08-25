package com.dm.cms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * @作者 ChenGJ
 * @创建日期 2015-4-15 上午11:38:58
 * @版本  
 * @描述
 */
@Entity
@Table(name = "CMS_FRIENDLINK")
public class FriendLinkEntity implements Serializable{

	private static final long serialVersionUID = -5573768858212486373L;
	
	private Long id;//友情链接id
	private String name;//友情链接名称
	private String type;//友情链接类型
	private String link;//友情链接地址
	private String email;//友情链接邮箱
	private String logo;//logo
	private String desc;//描述
	private Long seq;//排列序号
	private Long clickCount;//点击次数
	private String isShow;//是否显示
	
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "name", length = 100)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "type", length = 2)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column(name = "link", length = 200)
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	@Column(name = "email", length = 100)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Column(name = "logo", length = 200)
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	@Column(name = "_desc", length = 200)
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Column(name = "_seq")
	public Long getSeq() {
		return seq;
	}
	public void setSeq(Long seq) {
		this.seq = seq;
	}
	@Column(name = "click_count")
	public Long getClickCount() {
		return clickCount;
	}
	public void setClickCount(Long clickCount) {
		this.clickCount = clickCount;
	}
	@Column(name = "is_show", length = 2)
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

}
