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

import com.dm.cms.model.ContentEntity;
import com.dm.cms.model.TemplateEntity;
import com.dm.cms.service.ContentService;
import com.dm.cms.service.TemplateService;
import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.FileEntity;
import com.dm.platform.service.FileService;
import com.dm.platform.util.ConfigUtil;
import com.dm.platform.util.FreeMarkertUtil;

@Service
public class ContentServiceImpl implements ContentService{

	@Resource
	CommonDAO commonDAO;
	@Resource
	TemplateService templateService;
	@Resource
	FileService fileService;
	@Override
	public void insertItem(ContentEntity entity) {
		// TODO Auto-generated method stub
		commonDAO.save(entity);
	}

	@Override
	public void updateItem(ContentEntity entity) {
		// TODO Auto-generated method stub
		commonDAO.update(entity);
	}

	@Override
	public void deleteItemById(Object id) {
		// TODO Auto-generated method stub
		commonDAO.deleteById(ContentEntity.class, id);
	}

	@Override
	public ContentEntity getItemById(Object id) {
		// TODO Auto-generated method stub
		ContentEntity entity = commonDAO.findOne(ContentEntity.class, id);
		return entity;
	}

	@Override
	public Map<String, Object> getItemsMap(Map argsMap,String whereSql,List<Order> orders,
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
	public List<ContentEntity> getAllItems(Map argsMap, List<Order> orders) {
		// TODO Auto-generated method stub
		List<ContentEntity> items = commonDAO.findByMapArg(ContentEntity.class, "", argsMap, orders);
		return items;
	}

	@Override
	public void generateHtml(HttpServletRequest request, Long contentId)  throws Exception{
		// TODO Auto-generated method stub
		ContentEntity content = getItemById(contentId);
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("projectPath", request.getContextPath());
		root.put("content", content);
		List<Map> fileList = new ArrayList<Map>();
		String attachmentId = content.getAttachmentId();
		if(attachmentId!=null){
			String[] ids = attachmentId.split(",");
			for (String id : ids) {
				FileEntity file = fileService.findOne(id);
				Map map = new HashMap();
				map.put("id", file.getId());
				map.put("name", file.getName());
				map.put("size", file.getFilesize());
				map.put("fileUrl", file.getUrl());
				fileList.add(map);
			}
		}
		root.put("files", fileList);
		String templatesPath =  ConfigUtil.getConfigContent("dm",
				"templateFolder");
		TemplateEntity e = new TemplateEntity();
		if(content.getTemplateId()!=null){
				e = templateService.getItemById(content.getTemplateId());
		}
		String templateFile = e.getTemplateName()==null?"/content_default.ftl":(e.getTemplatePath()+"/"+e.getTemplateName()+".ftl");
		
		String htmlFolder = ConfigUtil.getConfigContent("dm",
				"htmlDir")+"/content";
		String htmlUrl = ConfigUtil.getConfigContent("dm",
				"htmlUrl")+"/content";
		File folder = new File(htmlFolder);
		if(!folder.exists()){
			folder.mkdirs();
		}
		String htmlFile = htmlFolder+"/"+content.getContentId()+".html";
		
		File file = new File(htmlFile);
		if(file.exists()){
			file.delete();
		}
		FreeMarkertUtil.analysisTemplate(templatesPath, templateFile, htmlFile,root,request);
		content.setIsStaticPage("1");
		content.setStaticPageUrl(htmlUrl+"/"+content.getContentId()+".html");
		updateItem(content);
	}

}
