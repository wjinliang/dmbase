package com.dm.platform.dto;

import com.dm.platform.model.UserMenu;

public class UserMenuDto {
	public UserMenuDto(){
		
	}
	public UserMenuDto(UserMenu menu){
		this.id = menu.getId();
		this.name = menu.getName();
		this.url = menu.getUrl();
		this.icon = menu.getIcon();
		this.seq = menu.getSeq();
		if(menu.getPuserMenu()!=null){
			this.pId = menu.getPuserMenu().getId();
		}
		this.isShow = menu.getIsShow();
	}
	private Long id;
	private String name;
	private String url;
	private String icon;
	private Long seq;
	private Long pId;
	private String isShow;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
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
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
}
