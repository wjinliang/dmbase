package com.dm.cms.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort.Order;

import com.dm.cms.model.FriendLinkEntity;

public interface FriendLinkService {
	/**
	 * @author ChenGJ 
	 * @version 创建时间：2015-4-15 下午4:07:48
	 * 类说明 友情链接service
	 */
	
	
	/**
	 * 插入友情链接
	 * @param entity
	 */
	public void insertItem(FriendLinkEntity entity);
	
	/**
	 * 更新友情链接
	 * @param entity
	 */
	public void updateItem(FriendLinkEntity entity);
	/**
	 * 通过id删除友情链接
	 * @param id
	 */
	public void deleteItemById(Object id);
	
	/**
	 * 通过id获取友情链接实体
	 * @param id
	 * @return
	 */
	
	public FriendLinkEntity getItemById(Object id);
	
	/**
	 * 获取分页map 包含当页数据（data）和总数（total）
	 * @param argsMap 注入参数
	 * @param whereSql 附加sql
	 * @param orders 排序参数
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Map<String,Object> getItemsMap(Map argsMap,String whereSql,List<Order> orders,Integer pageNum,Integer pageSize);
	
	/**
	 * 根据条件获取友情链接list
	 * @param argsMap
	 * @param orders
	 * @return
	 */
	public List<FriendLinkEntity> getItems(Map argsMap,String whereSql,List<Order> orders);
	
}
