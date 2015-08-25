package com.dm.platform.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_APPROVE")
public class ApproveEntity implements  Serializable{

	/**
	 * CHENGJ
	 * 审批结果实体
	 */
	private static final long serialVersionUID = -1813206882814176839L;
	
	private String approveId;
	private String applyId;
	private String approveStatus;
	private String approveComment;
	private String approveUserId;
	private String approveDate;
	
	@Id
	@Column(name="APPROVE_ID",length=32)
	public String getApproveId() {
		return approveId;
	}
	public void setApproveId(String approveId) {
		this.approveId = approveId;
	}
	@Column(name="APPLY_ID",length=32)
	public String getApplyId() {
		return applyId;
	}
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	@Column(name="APPROVE_STATUS",length=5)
	public String getApproveStatus() {
		return approveStatus;
	}
	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}
	@Column(name="APPROVE_COMMENT",length=400)
	public String getApproveComment() {
		return approveComment;
	}
	public void setApproveComment(String approveComment) {
		this.approveComment = approveComment;
	}
	@Column(name="APPROVE_USERID",length=400)
	public String getApproveUserId() {
		return approveUserId;
	}
	public void setApproveUserId(String approveUserId) {
		this.approveUserId = approveUserId;
	}
	@Column(name="APPROVE_DATE",length=400)
	public String getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(String approveDate) {
		this.approveDate = approveDate;
	}

}
