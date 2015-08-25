package com.dm.platform.dto;

import com.dm.platform.model.MenuGroup;
import com.dm.platform.model.UserMenu;
import com.dm.platform.model.UserRole;

public class MenuGroupDto {
	public MenuGroupDto() {

	}

	public MenuGroupDto(MenuGroup mg) {
		this.id = mg.getId();
		this.name = mg.getName();
		this.seq = mg.getSeq();
		String menuIdsStr = "";
		if (mg.getMenus().size() > 0) {
			for (UserMenu menu : mg.getMenus()) {
				menuIdsStr += menu.getId() + ",";
			}
		}
		if (menuIdsStr.endsWith(",")) {
			menuIdsStr = menuIdsStr.substring(0, menuIdsStr.length() - 1);
			this.menuIds = menuIdsStr;
		}

		String roleIdsStr = "";
		if (mg.getRoles().size() > 0) {
			for (UserRole role : mg.getRoles()) {
				roleIdsStr += role.getCode() + ",";
			}
		}
		if (roleIdsStr.endsWith(",")) {
			roleIdsStr = roleIdsStr.substring(0, roleIdsStr.length() - 1);
			this.roleIds = menuIdsStr;
		}
	}

	private Long id;
	private String name;
	private Long seq;
	private String menuIds;
	private String roleIds;

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

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public String getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

}
