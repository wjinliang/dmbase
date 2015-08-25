package com.dm.platform.service;


import java.util.List;
import java.util.Map;

import com.dm.platform.model.MenuGroup;
import com.github.pagehelper.PageInfo;

public interface MenuGroupService {
	public List<MenuGroup> listMenuGroup(int thispage,int pagesize); 
	public void insertMenuGroup(MenuGroup entity);
	public void updateMenuGroup(MenuGroup entity);
	public void deleteMenuGroup(MenuGroup entity);
	public Long countMenuGrou();
	public MenuGroup findOne(Long id);
	public void refreshService();
	
	/**
	 * ibatics查询菜单组list
	 * @param pageNum 当前页码
	 * @param pageSize 一页条数
	 * @param argMap 查询参数集合
	 * @return
	 */
	public PageInfo<Map> findMgList(Integer pageNum,Integer pageSize,Map argMap);
	public List<Map> findAllMgList();
}
