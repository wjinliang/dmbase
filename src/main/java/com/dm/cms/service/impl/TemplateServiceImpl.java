package com.dm.cms.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.dm.cms.model.ContentEntity;
import com.dm.cms.model.TemplateEntity;
import com.dm.cms.service.TemplateService;
import com.dm.platform.dao.CommonDAO;
@Service
public class TemplateServiceImpl implements TemplateService{

	@Resource
	CommonDAO commonDAO;
	
	@Override
	public void insertItem(TemplateEntity entity) {
		// TODO Auto-generated method stub
		commonDAO.save(entity);
	}

	@Override
	public void updateItem(TemplateEntity entity) {
		// TODO Auto-generated method stub
		commonDAO.update(entity);
	}

	@Override
	public void deleteItemById(Object id) {
		// TODO Auto-generated method stub
		commonDAO.deleteById(TemplateEntity.class, id);
	}

	@Override
	public TemplateEntity getItemById(Object id) {
		// TODO Auto-generated method stub
		TemplateEntity entity = commonDAO.findOne(TemplateEntity.class, id);
		return entity;
	}

	@Override
	public Map<String, Object> getItemsMap(Map argsMap,String whereSql, List<Order> orders,
			Integer thispage, Integer pagesize) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String,Object>();
		Long totalcount = commonDAO.count("select count(*) from TemplateEntity t where 1=1 "+whereSql, argsMap);
		map.put("totalcount", totalcount);
		List<TemplateEntity> items = commonDAO.findByMapArg(TemplateEntity.class, whereSql, argsMap, orders, thispage, pagesize);
		map.put("items", items);
		return map;
	}

	@Override
	public List<TemplateEntity> getAllItems(Map argsMap, List<Order> orders) {
		// TODO Auto-generated method stub
		List<TemplateEntity> items = commonDAO.findByMapArg(TemplateEntity.class, "", argsMap, orders);
		return items;
	}

	@Override
	public Map<String, Object> getContents(Map argsMap,String whereSql, List<Order> orders,
			Integer thispage, Integer pagesize) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String,Object>();
		Long totalcount = commonDAO.count("select count(*) from ContentEntity t where 1=1 "+whereSql, argsMap);
		map.put("totalcount", totalcount);
		List<ContentEntity> items = commonDAO.findByMapArg(ContentEntity.class, whereSql, argsMap, orders, thispage, pagesize);
		map.put("items", items);
		return map;
	}

	@Override
	public TemplateEntity getItemByPathAndName(String templatePath, String templateName) {
		// TODO Auto-generated method stub
		List<TemplateEntity> list = commonDAO.findByPropertyName(TemplateEntity.class, new String[]{"templatePath","templateName"}, new Object[]{templatePath,templateName});
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

}
