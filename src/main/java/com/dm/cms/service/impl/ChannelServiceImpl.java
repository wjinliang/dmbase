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
import com.dm.cms.model.ContentEntity;
import com.dm.cms.model.TemplateEntity;
import com.dm.cms.service.ChannelService;
import com.dm.cms.service.TemplateService;
import com.dm.platform.dao.CommonDAO;
import com.dm.platform.util.ConfigUtil;
import com.dm.platform.util.FreeMarkertUtil;
@Service
public class ChannelServiceImpl implements ChannelService{

	@Resource
	CommonDAO commonDAO;
	@Resource
	TemplateService templateService;
	
	@Override
	public void insertItem(ChannelEntity entity) {
		// TODO Auto-generated method stub
		commonDAO.save(entity);
	}

	@Override
	public void updateItem(ChannelEntity entity) {
		// TODO Auto-generated method stub
		commonDAO.update(entity);
	}

	@Override
	public void deleteItemById(Object id) {
		// TODO Auto-generated method stub
		commonDAO.deleteById(ChannelEntity.class, id);
	}

	@Override
	public ChannelEntity getItemById(Object id) {
		// TODO Auto-generated method stub
		ChannelEntity entity = commonDAO.findOne(ChannelEntity.class, id);
		return entity;
	}

	@Override
	public Map<String, Object> getItemsMap(Map argsMap,String whereSql, List<Order> orders,
			Integer thispage, Integer pagesize) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String,Object>();
		Long totalcount = commonDAO.count("select count(*) from ChannelEntity t where 1=1 "+whereSql, argsMap);
		map.put("totalcount", totalcount);
		List<ChannelEntity> items = commonDAO.findByMapArg(ChannelEntity.class, whereSql, argsMap, orders, thispage, pagesize);
		map.put("items", items);
		return map;
	}

	@Override
	public List<ChannelEntity> getAllItems(Map argsMap, List<Order> orders) {
		// TODO Auto-generated method stub
		List<ChannelEntity> items = commonDAO.findByMapArg(ChannelEntity.class, "", argsMap, orders);
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
	public Long getTotalContentByChannelId(Object id) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("channelId", id);
		Long totalcount = commonDAO.count("select count(*) from ContentEntity t where 1=1 and t.channelId = :channelId", map);
		return totalcount;
	}

	@Override
	public List<ContentEntity> getContentsById(Object id) {
		// TODO Auto-generated method stub
		List<Order> orders = new ArrayList<Order>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("channelId", id);
		List<ContentEntity> items = commonDAO.findByMapArg(ContentEntity.class, " and t.channelId = :channelId", map, orders);
		return items;
	}

	@Override
	public void generateHtml(HttpServletRequest request,Long channelId) throws Exception {
		// TODO Auto-generated method stub
		ChannelEntity channel = getItemById(channelId);
		String templatesPath =  ConfigUtil.getConfigContent("dm",
				"templateFolder");
		TemplateEntity template = new TemplateEntity();
		if(channel.getTemplateId()!=null){
				template = templateService.getItemById(channel.getTemplateId());
		}
		String templateFile = template.getTemplateName()==null?"/channel_default.ftl":(template.getTemplatePath()+"/"+template.getTemplateName()+".ftl");
		
		String htmlFolder = ConfigUtil.getConfigContent("dm",
				"htmlDir")+"/"+channel.getChannelId();
		String htmlUrl = ConfigUtil.getConfigContent("dm",
				"htmlUrl")+"/"+channel.getChannelId();
		
		channel.setIsStaticPage("1");
		channel.setStaticPageDir(htmlUrl);
		channel.setStaticPageUrl(htmlUrl+"/"+channelId+".html");
		updateItem(channel);
		
		File folder = new File(htmlFolder);
		if(!folder.exists()){
			folder.mkdirs();
		}
		Long total = getTotalContentByChannelId(channelId);
		Integer totalpage = (int) (total / channel.getChannelPageSize());
		if (total % channel.getChannelPageSize() != 0) {
			totalpage++;
		}
		for(int i = 1;i<=totalpage;i++){
			Map<String, Object> root = new HashMap<String, Object>();
			root.put("projectPath", request.getContextPath());
			root.put("own", channelId);
			root.put("pageNum", i);
			String htmlFile = "";
			if(i==1){
				htmlFile = htmlFolder+"/"+channelId+".html";
			}else{
				htmlFile = htmlFolder+"/"+channelId+"_"+i+".html";
			}
			File file = new File(htmlFile);
			if(file.exists()){
				file.delete();
			}
			FreeMarkertUtil.analysisTemplate(templatesPath, templateFile, htmlFile,root,request);
		}
	}

}
