package com.dm.cms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "CMS_CHANNEL")
public class ChannelEntity implements Serializable {

	/**
	 * @author CHENGJ
	 */
	private static final long serialVersionUID = -3834720115049877447L;

	private Long channelId;// 栏目id
	private String channelName;// 栏目名称
	private Long siteId;// 站点id
	private Long parentId;// 父栏目id
	private String channelPath;// 栏目页路径
	private String workflowId;//工作流Id;
	private Long seq;// 排列序号
	private String isDisplay;// 是否显示
	private String isBlank;// 是否在新窗口
	private String isStaticPage;// 是否静态化
	private String staticPageUrl;// 静态页路径
	private String staticPageDir;// 静态页目录
	private String description;// 栏目描述
	private Integer channelPageSize;//频道页显示条数
	private Integer indexPageSize;//首页显示条数
	private Long templateId;//模板id

	@Id
	@Column(name="channel_id",nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getChannelId() {
		return channelId;
	}
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	@Column(name="channel_name",length=100,nullable=false)
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	@Column(name="site_id")
	public Long getSiteId() {
		return siteId;
	}
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	@Column(name="parent_id")
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	@Column(name="channel_path",length=200)
	public String getChannelPath() {
		return channelPath;
	}
	public void setChannelPath(String channelPath) {
		this.channelPath = channelPath;
	}
	@Column(name="workflow_id",length=32)
	public String getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}
	@Column(name="seq")
	public Long getSeq() {
		return seq;
	}
	public void setSeq(Long seq) {
		this.seq = seq;
	}
	@Column(name="is_display",length=2)
	public String getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(String isDisplay) {
		this.isDisplay = isDisplay;
	}
	@Column(name="is_blank",length=2)
	public String getIsBlank() {
		return isBlank;
	}
	public void setIsBlank(String isBlank) {
		this.isBlank = isBlank;
	}
	@Column(name="is_static_page",length=2)
	public String getIsStaticPage() {
		return isStaticPage;
	}
	public void setIsStaticPage(String isStaticPage) {
		this.isStaticPage = isStaticPage;
	}
	@Column(name="description",length=400)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name="channel_page_size",columnDefinition=" int(11) default 10")
	public Integer getChannelPageSize() {
		return channelPageSize;
	}
	public void setChannelPageSize(Integer channelPageSize) {
		this.channelPageSize = channelPageSize;
	}
	@Column(name="index_page_size",columnDefinition=" int(11) default 5")
	public Integer getIndexPageSize() {
		return indexPageSize;
	}
	public void setIndexPageSize(Integer indexPageSize) {
		this.indexPageSize = indexPageSize;
	}
	@Column(name="template_id")
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	@Column(name="static_page_dir",length=200)
	public String getStaticPageDir() {
		return staticPageDir;
	}
	public void setStaticPageDir(String staticPageDir) {
		this.staticPageDir = staticPageDir;
	}
	@Column(name="static_page_url",length=200)
	public String getStaticPageUrl() {
		return staticPageUrl;
	}
	public void setStaticPageUrl(String staticPageUrl) {
		this.staticPageUrl = staticPageUrl;
	}
}
