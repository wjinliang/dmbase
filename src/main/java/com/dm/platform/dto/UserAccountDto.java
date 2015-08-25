package com.dm.platform.dto;

import java.util.Set;

import com.dm.platform.model.UserAccount;
import com.dm.platform.model.UserRole;

public class UserAccountDto {
	public UserAccountDto(){
		
	}
	public UserAccountDto(UserAccount user){
		this.code = user.getCode();
		this.name = user.getName();
		this.loginName = user.getLoginname();
		this.enabled = user.isEnabled();
		this.nonLocked = user.isNonLocked();
		this.accountExpired = user.isAccountExpired();
		this.seq = user.getSeq();
		this.mobile = user.getMobile();
		this.email = user.getEmail();
		this.headpic = user.getHeadpic();
		this.orgId = user.getOrg()==null?null:user.getOrg().getId();
		this.orgName = user.getOrg()==null?"":user.getOrg().getName();
		String roleIdsStr = "";
		Set<UserRole> roles = user.getRoles();
		for (UserRole role : roles) {
			roleIdsStr+=role.getCode()+",";
		}
		if(roleIdsStr.endsWith(",")){
			roleIdsStr=roleIdsStr.substring(0, roleIdsStr.length()-1);
			this.roleIds = roleIdsStr;
		}
		this.lastLoginTime = user.getLastLoginTime();
		this.remoteIpAddr = user.getRemoteIpAddr();
		this.loginCount = user.getLoginCount();
	}
	
	private String code;
	private String name;
	private String loginName;
	private boolean enabled;
	private boolean nonLocked;
	private boolean accountExpired;
	private Long seq;
	private String mobile;
	private String email;
	private String headpic;
	private Long orgId;
	private String roleIds;
	private String orgName;
	private String lastLoginTime;
	private String remoteIpAddr;
	private Long loginCount;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isNonLocked() {
		return nonLocked;
	}
	public void setNonLocked(boolean nonLocked) {
		this.nonLocked = nonLocked;
	}
	public boolean isAccountExpired() {
		return accountExpired;
	}
	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}
	public Long getSeq() {
		return seq;
	}
	public void setSeq(Long seq) {
		this.seq = seq;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHeadpic() {
		return headpic;
	}
	public void setHeadpic(String headpic) {
		this.headpic = headpic;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getRemoteIpAddr() {
		return remoteIpAddr;
	}
	public void setRemoteIpAddr(String remoteIpAddr) {
		this.remoteIpAddr = remoteIpAddr;
	}
	public Long getLoginCount() {
		return loginCount;
	}
	public void setLoginCount(Long loginCount) {
		this.loginCount = loginCount;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
}
