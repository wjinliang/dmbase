package com.dm.platform.dto;

import com.dm.platform.model.Org;
import com.dm.platform.model.UserAccount;

public class OrgDto {
	public OrgDto(){
		
	}
	public OrgDto(Org org){
		this.id = org.getId();
		this.name = org.getName();
		this.code = org.getCode();
		this.seq = org.getSeq();
		if(org.getParent()!=null){
			this.pId = org.getParent().getId();
		}
		if(org.getUser().size()>0){
			String userIdsStr = "";
			for (UserAccount user : org.getUser()) {
				userIdsStr+=user.getCode()+",";
			}
			if(userIdsStr.endsWith(",")){
				userIdsStr = userIdsStr.substring(0,userIdsStr.length()-1);
				this.userIds = userIdsStr;
			}
		}
	}
	private Long id;

	private String name;
	
	private String code;
	
	private Long seq;
	
	private Long pId; 
	
	private String userIds;
	
	private String childrenIds;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Long getSeq() {
		return seq;
	}
	public void setSeq(Long seq) {
		this.seq = seq;
	}
	public Long getpId() {
		return pId;
	}
	public void setpId(Long pId) {
		this.pId = pId;
	}
	public String getUserIds() {
		return userIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	public String getChildrenIds() {
		return childrenIds;
	}
	public void setChildrenIds(String childrenIds) {
		this.childrenIds = childrenIds;
	}
	
}
