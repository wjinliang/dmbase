package com.dm.platform.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
@Entity
@Table(name = "T_INBOX")
public class InboxEntity implements Serializable{
	private static final long serialVersionUID = -812878346189387409L;
	
	@Id
	@Column(name="ib_id",length=32)
	private String inboxId;
	@Column(name="ib_subject",length=255)
	private String subject;
	@Lob
	@Column(name="ib_content")
	private String content;
	@Column(name="ib_has_attachment",length=2)
	private String hasAttachment;
	@Column(name="ib_from_user",length=255)
	private String formUserStr;
	@Lob
	@Column(name="ib_to_users")
	private String toUserStr;
	@Column(name="ib_create_user_id",length=32)
	private String creatUserId;
	@Column(name="ib_create_time",length=32)
	private String creatTime;
	
	public String getInboxId() {
		return inboxId;
	}
	public void setInboxId(String inboxId) {
		this.inboxId = inboxId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getHasAttachment() {
		return hasAttachment;
	}
	public void setHasAttachment(String hasAttachment) {
		this.hasAttachment = hasAttachment;
	}
	public String getFormUserStr() {
		return formUserStr;
	}
	public void setFormUserStr(String formUserStr) {
		this.formUserStr = formUserStr;
	}
	public String getToUserStr() {
		return toUserStr;
	}
	public void setToUserStr(String toUserStr) {
		this.toUserStr = toUserStr;
	}
	public String getCreatUserId() {
		return creatUserId;
	}
	public void setCreatUserId(String creatUserId) {
		this.creatUserId = creatUserId;
	}
	public String getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}
	
}
