package com.dm.cms.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.dm.cms.model.ChannelEntity;
import com.dm.cms.model.SiteEntity;
import com.dm.cms.model.TemplateEntity;
import com.dm.cms.service.SiteService;
import com.dm.cms.service.TemplateService;
import com.dm.platform.dao.CommonDAO;
import com.dm.platform.util.ConfigUtil;
import com.dm.platform.util.FreeMarkertUtil;

@Service
public class SiteServiceImpl implements SiteService{

	@Resource
	CommonDAO commonDAO;
	@Resource
	TemplateService templateService;
	
	@Override
	public void insertItem(SiteEntity entity) {
		// TODO Auto-generated method stub
		commonDAO.save(entity);
	}

	@Override
	public void updateItem(SiteEntity entity) {
		// TODO Auto-generated method stub
		commonDAO.update(entity);
	}

	@Override
	public void deleteItemById(Object id) {
		// TODO Auto-generated method stub
		commonDAO.deleteById(SiteEntity.class, id);
	}

	@Override
	public SiteEntity getItemById(Object id) {
		// TODO Auto-generated method stub
		SiteEntity entity = commonDAO.findOne(SiteEntity.class, id);
		return entity;
	}

	@Override
	public Map<String, Object> getItemsMap(Map argsMap,String whereSql, List<Order> orders,
			Integer thispage, Integer pagesize) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String,Object>();
		Long totalcount = commonDAO.count("select count(*) from SiteEntity t where 1=1 "+whereSql, argsMap);
		map.put("totalcount", totalcount);
		List<SiteEntity> items = commonDAO.findByMapArg(SiteEntity.class, "", argsMap, orders, thispage, pagesize);
		map.put("items", items);
		return map;
	}

	@Override
	public List<SiteEntity> getAllItems(Map argsMap, List<Order> orders) {
		// TODO Auto-generated method stub
		List<SiteEntity> items = commonDAO.findByMapArg(SiteEntity.class, "", argsMap, orders);
		return items;
	}

	@Override
	public SiteEntity getItemByDomain(String domain) {
		// TODO Auto-generated method stub
		SiteEntity entity = null;
		if(commonDAO.findByPropertyName(SiteEntity.class, "domain", domain).size()>0)
			entity=commonDAO.findByPropertyName(SiteEntity.class, "domain", domain).get(0);
		return entity;
	}

	@Override
	public List<ChannelEntity> getAllChannelsByID(Object id) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("siteId", id);
		List<Order> orders = new ArrayList<Order>();
		List<ChannelEntity> list= commonDAO.findByMapArg(ChannelEntity.class, " and t.siteId = :siteId", map, orders);
		return list;
	}

	@Override
	public void generateHtml(HttpServletRequest request, Long siteId)
			throws Exception {
		// TODO Auto-generated method stub
		SiteEntity item = getItemById(siteId);
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("projectPath", request.getContextPath());
		root.put("own", siteId);
		String templatesPath =  ConfigUtil.getConfigContent("dm",
				"templateFolder");
		TemplateEntity e = null;
		if(item.getTemplateId()!=null){
				e = templateService.getItemById(item.getTemplateId());
		}
		if(e==null){
			e = new TemplateEntity();
		}
		String templateFile = e.getTemplateName()==null?"index_default.ftl":(e.getTemplatePath()+"/"+e.getTemplateName()+".ftl");
		
		String htmlFolder = ConfigUtil.getConfigContent("dm",
				"htmlDir")+"/"+item.getDomain();
		String htmlUrl = ConfigUtil.getConfigContent("dm",
				"htmlUrl")+"/"+item.getDomain();
		File folder = new File(htmlFolder);
		if(!folder.exists()){
			folder.mkdirs();
		}
		String htmlFile = htmlFolder+"/index.html";
		
		File file = new File(htmlFile);
		if(file.exists()){
			file.delete();
		}
		FreeMarkertUtil.analysisTemplate(templatesPath, templateFile, htmlFile,root,request);
		item.setIsStaticIndex("1");
		item.setStaticDir(htmlUrl+"/index.html");
		updateItem(item);
	}

}
