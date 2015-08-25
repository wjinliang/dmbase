package com.dm.platform.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_APPLY")
public class ApplyEntity implements  Serializable{

	/**
	 * CHENGJ
	 * 审批实体
	 */
	private static final long serialVersionUID = -14276760480160886L;
	
	private String applyId;
	private String applyUserId;
	private String applyContent;
	private String applyDate;
	private String applyObjType;
	private String applyObjId;
	private String applyStatus;
	private String approvedDate;
	
	@Id
	@Column(name="APPLY_ID",length=32)
	public String getApplyId() {
		return applyId;
	}
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	@Column(name="APPLY_USER_ID",length=32)
	public String getApplyUserId() {
		return applyUserId;
	}
	public void setApplyUserId(String applyUserId) {
		this.applyUserId = applyUserId;
	}
	@Column(name="APPLY_CONTENT",length=400)
	public String getApplyContent() {
		return applyContent;
	}
	public void setApplyContent(String applyContent) {
		this.applyContent = applyContent;
	}
	@Column(name="APPLY_DATE",length=20)
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	@Column(name="APPLY_OBJ_TYPE",length=10)
	public String getApplyObjType() {
		return applyObjType;
	}
	public void setApplyObjType(String applyObjType) {
		this.applyObjType = applyObjType;
	}
	@Column(name="APPLY_OBJ_ID",length=32)
	public String getApplyObjId() {
		return applyObjId;
	}
	public void setApplyObjId(String applyObjId) {
		this.applyObjId = applyObjId;
	}
	@Column(name="APPLY_STATUS",length=10)
	public String getApplyStatus() {
		return applyStatus;
	}
	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}
	@Column(name="APPROVEDDATE",length=20)
	public String getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(String approvedDate) {
		this.approvedDate = approvedDate;
	}
	
}
