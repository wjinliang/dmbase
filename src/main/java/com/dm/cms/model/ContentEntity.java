package com.dm.cms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "CMS_CONTENT")
public class ContentEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2294452452048466995L;

	private Long contentId;// 内容id
	private Long channelId;// 栏目id
	private String contentType;// 内容类型
	private String title;// 标题
	private String digest;// 摘要
	private String titleColor;// 标题颜色
	private String titleBlod;// 标题加粗
	private String author;// 作者
	private String origin;// 来源
	private String originUrl;// 来源url
	private String publishTime;// 发布时间
	private String attachmentId;//附件id
	private String content;// 内容
	private String status;// 状态
	private String isStaticPage;// 是否静态化
	private String staticPageUrl;// 静态化路径
	private Long templateId;//模板id

	@Id
	@Column(name = "content_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	@Column(name = "channel_id", nullable = false)
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	@Column(name = "content_type", length = 2)
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Column(name = "title", length = 200)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "digest", length = 200)
	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	@Column(name = "title_color", length = 7)
	public String getTitleColor() {
		return titleColor;
	}

	public void setTitleColor(String titleColor) {
		this.titleColor = titleColor;
	}

	@Column(name = "title_blod", length = 2)
	public String getTitleBlod() {
		return titleBlod;
	}

	public void setTitleBlod(String titleBlod) {
		this.titleBlod = titleBlod;
	}

	@Column(name = "author", length = 30)
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Column(name = "origin", length = 100)
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	@Column(name = "origin_url", length = 200)
	public String getOriginUrl() {
		return originUrl;
	}

	public void setOriginUrl(String originUrl) {
		this.originUrl = originUrl;
	}

	@Column(name = "publish_time", length = 20)
	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	@Lob
	@Column(name = "CONTENT")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "status", length = 2)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "is_static_page", length = 2)
	public String getIsStaticPage() {
		return isStaticPage;
	}

	public void setIsStaticPage(String isStaticPage) {
		this.isStaticPage = isStaticPage;
	}
	@Column(name = "template_id")
	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	
	@Column(name = "static_page_url", length = 200)
	public String getStaticPageUrl() {
		return staticPageUrl;
	}

	public void setStaticPageUrl(String staticPageUrl) {
		this.staticPageUrl = staticPageUrl;
	}
	@Column(name = "attachement_id", length = 200)
	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
}
