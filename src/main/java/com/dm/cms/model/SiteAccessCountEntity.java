package com.dm.cms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class SiteAccessCountEntity implements Serializable{

	/**
	 * @param serialVersionUID
	 */
	private static final long serialVersionUID = 5111262593643424138L;
	
	@Id
	@Column(name="access_count_id",nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long accessCountId;
	
	@Column(name="page_count",nullable=false,columnDefinition=" default '0'")
	private Long pageCount;
	
	@Column(name="visitors",nullable=false,columnDefinition=" default '0'")
	private Long visitors;
	
	@Column(name="statistic_date",length=20,nullable=false)
	private String statisticDate;
	
	@Column(name="site_id",nullable=false)
	private Long siteId;
	
	public Long getAccessCountId() {
		return accessCountId;
	}
	public void setAccessCountId(Long accessCountId) {
		this.accessCountId = accessCountId;
	}
	public Long getPageCount() {
		return pageCount;
	}
	public void setPageCount(Long pageCount) {
		this.pageCount = pageCount;
	}
	public Long getVisitors() {
		return visitors;
	}
	public void setVisitors(Long visitors) {
		this.visitors = visitors;
	}
	public String getStatisticDate() {
		return statisticDate;
	}
	public void setStatisticDate(String statisticDate) {
		this.statisticDate = statisticDate;
	}
	public Long getSiteId() {
		return siteId;
	}
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	
}
