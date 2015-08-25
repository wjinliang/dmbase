package com.dm.platform.service;

import java.util.List;
import java.util.Map;

import com.dm.platform.model.UserMenu;
import com.github.pagehelper.PageInfo;
public interface UserMenuService {
	public List<UserMenu> listUserMenu(); 
	public UserMenu findOne(Object id); 
	public void insertUserMenu(UserMenu entity);
	public void updateUserMenu(UserMenu entity);
	public void deleteUserMenu(UserMenu entity);
	public void refreshService();
	
	public List<Map> findAllMenuSimpleList();
	
}
