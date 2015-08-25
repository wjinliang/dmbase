package com.dm.platform.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "T_NOTICE")
public class NoticeEntity implements Serializable{

	/**
	 * @author CHENGJ
	 * 公告表
	 */
	private static final long serialVersionUID = 1203504659397413307L;
	
	private String noticeId;//id
	private String noticeTitle;//公告标题
	private String noticeContent;//公告内容
	private String noticeOriginal;//公告来源
	private String noticeDate;//公告日期
	private Integer isTop;//置顶标示符
	
	
	@Id
	@Column(name="NOTICE_ID",length=32)
	public String getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
	
	@Column(name="NOTICE_TITLE",length=255)
	public String getNoticeTitle() {
		return noticeTitle;
	}
	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}
	
	@Lob
	@Column(name="NOTICE_CONTENT")
	public String getNoticeContent() {
		return noticeContent;
	}
	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}
	
	@Column(name="NOTICE_ORIGINAL",length=255)
	public String getNoticeOriginal() {
		return noticeOriginal;
	}
	public void setNoticeOriginal(String noticeOriginal) {
		this.noticeOriginal = noticeOriginal;
	}
	
	@Column(name="NOTICE_DATE",length=20)
	public String getNoticeDate() {
		return noticeDate;
	}
	public void setNoticeDate(String noticeDate) {
		this.noticeDate = noticeDate;
	}
	@Column(name="NOTICE_IS_TOP",columnDefinition=" int(11) default 0")
	public Integer getIsTop() {
		return isTop;
	}
	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}
	
}
