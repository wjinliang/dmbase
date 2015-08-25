package com.dm.cms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CMS_TEMPLATE")
public class TemplateEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6263122329746147601L;
	
	@Id
	@Column(name="template_id",nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long templateId;
	@Column(name="template_path",length=200,nullable=false)
	private String templatePath;
	@Column(name="template_name",length=100,nullable=false)
	private String templateName;
	@Column(name="file_size")
	private Long fileSize;
	@Column(name="update_time",length=20)
	private String updateTime;
	
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	public String getTemplatePath() {
		return templatePath;
	}
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
