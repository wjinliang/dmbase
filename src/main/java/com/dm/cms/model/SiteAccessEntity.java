package com.dm.cms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class SiteAccessEntity implements Serializable{

	/**
	 * @param serialVersionUID
	 */
	private static final long serialVersionUID = 4325635183005755718L;
	
	@Id
	@Column(name="access_id",nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long accessId;
	
	@Column(name="site_id",nullable=false)
	private Long siteId;
	
	@Column(name="ip",length=50,nullable=false)
	private String ip;
	
	@Column(name="browser",length=255)
	private String browser;
	
	@Column(name="access_time",length=20,nullable=false)
	private String accessTime;
	
	public Long getAccessId() {
		return accessId;
	}
	public void setAccessId(Long accessId) {
		this.accessId = accessId;
	}
	public Long getSiteId() {
		return siteId;
	}
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public String getAccessTime() {
		return accessTime;
	}
	public void setAccessTime(String accessTime) {
		this.accessTime = accessTime;
	}
	
}
