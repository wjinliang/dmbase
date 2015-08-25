package com.dm.cms.controller;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dm.cms.model.ChannelEntity;
import com.dm.cms.model.ContentEntity;
import com.dm.cms.model.SiteEntity;
import com.dm.cms.model.TemplateEntity;
import com.dm.cms.service.ChannelService;
import com.dm.cms.service.ContentService;
import com.dm.cms.service.SiteService;
import com.dm.cms.service.TemplateService;
import com.dm.platform.controller.DefaultController;
import com.dm.platform.model.FileEntity;
import com.dm.platform.model.UserAccount;
import com.dm.platform.service.FileService;
import com.dm.platform.service.UserAccountService;

@Controller
@RequestMapping("/portal")
public class IndexController extends DefaultController {
	@Resource
	SiteService siteService;
	@Resource
	ChannelService channelService;
	@Resource
	ContentService contentService;
	@Resource
	TemplateService templateService;
	@Resource
	FileService fileService;
	@Resource
	UserAccountService userAccountService;
	@RequestMapping("{domain}/index.page")
	public ModelAndView index(
			ModelAndView model,
			@PathVariable(value="domain") String domain) {
		try {
			model.addObject("projectPath",getRootPath());
			SiteEntity item = siteService.getItemByDomain(domain);
			model.addObject("own",item.getSiteId());
			if(item.getTemplateId()!=null){
				TemplateEntity template = templateService.getItemById(item.getTemplateId());
				if(template!=null){
					model.setViewName("/pages/template"+template.getTemplatePath()+"/"+template.getTemplateName());
				}else{
					model.setViewName("/pages/template/index_default");
				}
			}else{
				model.setViewName("/pages/template/index_default");
			}
			return model;
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}
	@RequestMapping(value={"channel/{channelId}_{pageNum:[\\d]+}.htm","channel/{channelId}{pageNum}.htm"})
	public ModelAndView channelPage(@PathVariable(value="channelId") Long channelId,
			@PathVariable(value="pageNum") String pageNum,
			ModelAndView model) {
		try {
			model.addObject("projectPath",getRootPath());
			ChannelEntity channel = channelService.getItemById(channelId);
			Integer pN = 1;
			if(pageNum!=null&&!pageNum.equals("")){
				pN = Integer.valueOf(pageNum);
			}
			model.addObject("own", channelId);
			model.addObject("pageNum", pN);
			if(channel.getTemplateId()!=null&&!channel.getTemplateId().equals("")){
				TemplateEntity template = templateService.getItemById(channel.getTemplateId());
				if(template!=null){
					model.setViewName("/pages/template"+template.getTemplatePath()+"/"+template.getTemplateName());
				}else{
					model.setViewName("/pages/template/channel_default");
				}
			}else{
				model.setViewName("/pages/template/channel_default");
			}
			return model;
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}
	@RequestMapping("content/{contentId}.htm")
	public ModelAndView content(@PathVariable Long contentId,
			ModelAndView model) {
		try {
			model.addObject("projectPath",getRootPath());
			ContentEntity content = contentService.getItemById(contentId);
			model.addObject("content", content);
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
			model.addObject("files", fileList);
			if(content.getTemplateId()!=null&&!content.getTemplateId().equals("")){
				TemplateEntity template = templateService.getItemById(content.getTemplateId());
				if(template!=null){
					model.setViewName("/pages/template"+template.getTemplatePath()+"/"+template.getTemplateName());
				}else{
					model.setViewName("/pages/template/content_default");
				}
			}else{
				model.setViewName("/pages/template/content_default");
			}
			return model;
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/userInfo")
	@ResponseBody
	public Object userInfo()
			throws Exception {
		try {
			Map result = new HashMap();
			if((SecurityContextHolder
					.getContext().getAuthentication().getPrincipal() instanceof String)){
				result.put("isLogin", false);
			}else if(SecurityContextHolder
					.getContext().getAuthentication().getPrincipal() instanceof UserDetails){
				UserDetails user = (UserDetails) SecurityContextHolder
						.getContext().getAuthentication().getPrincipal();
				UserAccount ua =(UserAccount) userAccountService.findByLoginName(user.getUsername());
				if(ua!=null){
					result.put("isLogin", true);
					Map userInfo = new HashMap();
					userInfo.put("userName", ua.getUsername());
					userInfo.put("avatar", ua.getHeadpic());
					result.put("userInfo", userInfo);
				}else{
					result.put("isLogin", false);
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
}
