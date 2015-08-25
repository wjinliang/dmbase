package com.dm.cms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "CMS_SITE")
public class SiteEntity implements Serializable{

	/**
	 * CHENGJ
	 */
	private static final long serialVersionUID = -1206758517139908841L;
	
	@Id
	@Column(name="site_id",nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long siteId;//站点id
	
	@Column(name="site_name",length=100,nullable=false)
	private String siteName;//站点名称
	
	@Column(name="domain",length=50,unique=true,nullable=false)
	private String domain;//域名
	
	@Column(name="site_path",length=50)
	private String sitePath;//站点路径
	
	@Column(name="is_static_index",length=2)
	private String isStaticIndex;//首页是否静态化 0：否 1：是
	
	@Column(name="static_dir",length=50)
	private String staticDir;//静态页存放目录
	
	@Column(name="index_tpl",length=100)
	private String index_tpl;//首页模板
	
	@Column(name="template_id")
	private Long templateId;//首页模板id
	
	@Column(name="description",length=255)
	private String description;//站点描述
	
	@Column(name="create_user_id",length=32)
	private String createUserId;//建站用户id
	
	@Column(name="create_date",length=32)
	private String createDate;//建站日期
	
	@Column(name="sort_seq")
	private Integer sortSeq;//序号

	

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getSitePath() {
		return sitePath;
	}

	public void setSitePath(String sitePath) {
		this.sitePath = sitePath;
	}

	public String getStaticDir() {
		return staticDir;
	}

	public void setStaticDir(String staticDir) {
		this.staticDir = staticDir;
	}

	public String getIndex_tpl() {
		return index_tpl;
	}

	public void setIndex_tpl(String index_tpl) {
		this.index_tpl = index_tpl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsStaticIndex() {
		return isStaticIndex;
	}

	public void setIsStaticIndex(String isStaticIndex) {
		this.isStaticIndex = isStaticIndex;
	}
	public Integer getSortSeq() {
		return sortSeq;
	}

	public void setSortSeq(Integer sortSeq) {
		this.sortSeq = sortSeq;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
}
