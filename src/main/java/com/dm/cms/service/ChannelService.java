package com.dm.cms.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.annotation.RequestParam;

import com.dm.cms.model.ChannelEntity;
import com.dm.cms.model.ContentEntity;

public interface ChannelService {
	public void insertItem(ChannelEntity entity);
	public void updateItem(ChannelEntity entity);
	public void deleteItemById(Object id);
	public ChannelEntity getItemById(Object id);
	
	public Long getTotalContentByChannelId(Object id);
	
	public Map<String,Object> getItemsMap(Map argsMap,String whereSql,List<Order> orders,Integer thispage,Integer pagesize);
	public List<ChannelEntity> getAllItems(Map argsMap,List<Order> orders);
	
	public Map<String,Object> getContents(Map argsMap,String whereSql,List<Order> orders,Integer thispage,Integer pagesize);
	/**
	 * 通过频道id
	 * @param id
	 * @return
	 */
	public List<ContentEntity> getContentsById(Object id);
	
	public void generateHtml(HttpServletRequest request,Long channelId) throws Exception;
}
