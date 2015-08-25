package com.dm.cms.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort.Order;

import com.dm.cms.model.TemplateEntity;

public interface TemplateService {
	public void insertItem(TemplateEntity entity);
	public void updateItem(TemplateEntity entity);
	public void deleteItemById(Object id);
	public TemplateEntity getItemByPathAndName(String templatePath,String templateName);
	public TemplateEntity getItemById(Object id);
	
	public Map<String,Object> getItemsMap(Map argsMap,String whereSql,List<Order> orders,Integer thispage,Integer pagesize);
	public List<TemplateEntity> getAllItems(Map argsMap,List<Order> orders);
	
	public Map<String,Object> getContents(Map argsMap,String whereSql,List<Order> orders,Integer thispage,Integer pagesize);
	
}
