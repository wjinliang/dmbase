package com.dm.cms.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.annotation.RequestParam;

import com.dm.cms.model.ChannelEntity;
import com.dm.cms.model.SiteEntity;

public interface SiteService {
	public void insertItem(SiteEntity entity);
	public void updateItem(SiteEntity entity);
	public void deleteItemById(Object id);
	public SiteEntity getItemById(Object id);
	public SiteEntity getItemByDomain(String domain);
	
	public Map<String,Object> getItemsMap(Map argsMap,String whereSql,List<Order> orders,Integer thispage,Integer pagesize);
	public List<SiteEntity> getAllItems(Map argsMap,List<Order> orders);
	/**
	 * 获取站点下所有频道
	 * @param id 站点id
	 * @return List<ChannelEntity>
	 */
	public List<ChannelEntity> getAllChannelsByID(Object id);
	
	public void generateHtml(HttpServletRequest request,Long siteId) throws Exception;
}
