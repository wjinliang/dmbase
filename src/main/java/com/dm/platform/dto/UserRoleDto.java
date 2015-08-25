package com.dm.platform.dto;

import com.dm.platform.model.MenuGroup;
import com.dm.platform.model.UserMenu;
import com.dm.platform.model.UserRole;

public class UserRoleDto {
	public UserRoleDto(){
		
	}
	public UserRoleDto(UserRole role){
		this.code = role.getCode();
		this.name = role.getName();
		this.homePage = role.getHomePage();
		this.detail = role.getDetail();
		String menuIdsStr = "";
		if(role.getMenus().size()>0){
			for (UserMenu menu : role.getMenus()) {
				menuIdsStr+=menu.getId()+",";
			}
		}
		if(menuIdsStr.endsWith(",")){
			menuIdsStr=menuIdsStr.substring(0, menuIdsStr.length()-1);
			this.menuIds = menuIdsStr;
		}
		String menuGroupIdsStr = "";
		if(role.getMenugroups().size()>0){
			for (MenuGroup mg : role.getMenugroups()) {
				menuGroupIdsStr+=mg.getId()+",";
			}
		}
		if(menuGroupIdsStr.endsWith(",")){
			menuGroupIdsStr=menuGroupIdsStr.substring(0, menuGroupIdsStr.length()-1);
			this.menuGroupIds = menuGroupIdsStr;
		}
	}
	private String code;
	private String name;
	private String homePage;
	private String detail;
	private String menuIds;
	private String menuGroupIds;
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
	public String getHomePage() {
		return homePage;
	}
	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
	public String getMenuGroupIds() {
		return menuGroupIds;
	}
	public void setMenuGroupIds(String menuGroupIds) {
		this.menuGroupIds = menuGroupIds;
	}
}
