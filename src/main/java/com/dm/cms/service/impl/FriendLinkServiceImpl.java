package com.dm.cms.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.dm.cms.model.FriendLinkEntity;
import com.dm.cms.service.FriendLinkService;
import com.dm.platform.dao.CommonDAO;

@Service
public class FriendLinkServiceImpl implements FriendLinkService{
	/**
	 * @author ChenGJ 
	 * @version 创建时间：2015-4-15 下午4:19:58
	 * 类说明
	 */
	
	@Resource
	CommonDAO commonDAO;
	
	@Override
	public void insertItem(FriendLinkEntity entity) {
		// TODO Auto-generated method stub
		commonDAO.save(entity);
	}

	@Override
	public void updateItem(FriendLinkEntity entity) {
		// TODO Auto-generated method stub
		commonDAO.update(entity);
	}

	@Override
	public void deleteItemById(Object id) {
		// TODO Auto-generated method stub
		commonDAO.deleteById(FriendLinkEntity.class, id);
	}

	@Override
	public FriendLinkEntity getItemById(Object id) {
		// TODO Auto-generated method stub
		FriendLinkEntity entity = commonDAO.findOne(FriendLinkEntity.class, id);
		return entity;
	}

	@Override
	public Map<String, Object> getItemsMap(Map argsMap, String whereSql,
			List<Order> orders, Integer pageNum, Integer pageSize) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String,Object>();
		Long totalcount = commonDAO.count("select count(*) from FriendLinkEntity t where 1=1 "+whereSql, argsMap);
		map.put("total", totalcount);
		List<FriendLinkEntity> items = commonDAO.findByMapArg(FriendLinkEntity.class, whereSql, argsMap, orders, pageNum, pageSize);
		map.put("data", items);
		return map;
	}

	@Override
	public List<FriendLinkEntity> getItems(Map argsMap, String whereSql,
			List<Order> orders) {
		// TODO Auto-generated method stub
		List<FriendLinkEntity> items = commonDAO.findByMapArg(FriendLinkEntity.class, whereSql, argsMap, orders);
		return items;
	}
	
}
