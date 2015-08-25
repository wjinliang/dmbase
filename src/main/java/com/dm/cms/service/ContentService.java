package com.dm.cms.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.annotation.RequestParam;

import com.dm.cms.model.ContentEntity;

public interface ContentService {
	public void insertItem(ContentEntity entity);
	public void updateItem(ContentEntity entity);
	public void deleteItemById(Object id);
	public ContentEntity getItemById(Object id);
	
	public Map<String,Object> getItemsMap(Map argsMap,String whereSql,List<Order> orders,Integer thispage,Integer pagesize);
	public List<ContentEntity> getAllItems(Map argsMap,List<Order> orders);
	
	public void generateHtml(HttpServletRequest request,Long contentId) throws Exception;
	
}
