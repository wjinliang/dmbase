package com.dm.platform.dto;

import java.io.Serializable;

import com.dm.platform.model.InboxEntity;
import com.dm.platform.util.DmDateUtil;

public class InboxDto extends InboxEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5917831767161558258L;
	
	private Integer tId;
	private String inboxType;
	private String isRead;
	private String isMark;
	private String isTrash;
	private String isDelete;
	private String inboxDate;
	public Integer gettId() {
		return tId;
	}
	public void settId(Integer tId) {
		this.tId = tId;
	}
	public String getInboxType() {
		return inboxType;
	}
	public void setInboxType(String inboxType) {
		this.inboxType = inboxType;
	}
	public String getIsRead() {
		return isRead;
	}
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
	public String getIsMark() {
		return isMark;
	}
	public void setIsMark(String isMark) {
		this.isMark = isMark;
	}
	public String getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}
	public String getIsTrash() {
		return isTrash;
	}
	public void setIsTrash(String isTrash) {
		this.isTrash = isTrash;
	}
	public String getInboxDate() {
		return inboxDate;
	}
	public void setInboxDate(String inboxDate) {
		this.inboxDate = inboxDate;
	}
	public String getFormatTime() {
		String time = DmDateUtil.formatDateTime(getInboxDate());
		return time;
	}

}
