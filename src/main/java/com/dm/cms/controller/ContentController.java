package com.dm.cms.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dm.cms.model.ChannelEntity;
import com.dm.cms.model.ContentEntity;
import com.dm.cms.model.SiteEntity;
import com.dm.cms.model.TemplateEntity;
import com.dm.cms.service.ContentService;
import com.dm.cms.service.TemplateService;
import com.dm.platform.controller.DefaultController;
import com.dm.platform.util.ConfigUtil;
import com.dm.platform.util.FreeMarkertUtil;
@Controller
@RequestMapping("/cms/content")
public class ContentController extends DefaultController {
	@Resource
	ContentService contentService;
	@Resource
	TemplateService templateService;
	
	@RequestMapping("/page")
	public ModelAndView page(
			ModelAndView model) {
		try {
			model.setViewName("/pages/cms/content/page");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/jsonList")
	public @ResponseBody Object jsonList(
			HttpServletResponse response,
			@RequestParam(value = "pageNum", required = false) Integer thispage,
			@RequestParam(value = "pageSize", required = false) Integer pagesize,
			@RequestParam(value = "channelIds", required = false) String channelIds,
			ChannelEntity searchEntity) throws Exception {
		try {
			if (pagesize == null) {
				pagesize = 10;
			}
			if (thispage == null) {
				thispage = 1;
			}
			List<Order> orders = new ArrayList<Order>();
			orders.add(new Order(Direction.DESC, "publishTime"));
			Map<String, Object> map = new HashMap<String, Object>();
			Map argsMap = new HashMap();
			String whereSql = "";
			if(channelIds!=null&&!channelIds.equals("")){
				List<Long> list = new ArrayList<Long>();
				String[] ids = channelIds.split(",");
				for (String id : ids) {
					list.add(new Long(id));
				}
				whereSql+=" and t.channelId in (:channelId)";
				argsMap.put("channelId", list);
			}
			map = contentService.getItemsMap(argsMap,whereSql,orders, thispage-1, pagesize);
			Map result = new HashMap();
			result.put("data", (List<ChannelEntity>)map.get("items"));
			result.put("total", (Long)map.get("totalcount"));
			Long totalcount = (Long)map.get("totalcount");
			if ((thispage) * pagesize >= totalcount && totalcount > 0) {
				thispage--;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
	@RequestMapping("/ajaxSave")
	public @ResponseBody
	Object ajaxSave(ContentEntity entity) {
		try {
			if(entity.getContentType().equals("1")){
				if (entity.getContentId() != null) {
					contentService.updateItem(entity);
				} else {
					contentService.insertItem(entity);
				}
			}else if(entity.getContentType().equals("2")){
				if (entity.getContentId() != null) {
					contentService.updateItem(entity);
				} else {
					contentService.insertItem(entity);
				}
			}
			
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
	@RequestMapping("/ajaxLoad")
	public @ResponseBody
	Object ajaxLoad(
			@RequestParam(value = "contentId", required = true) Long contentId) {
		try {
			ContentEntity e = new ContentEntity();
			if (contentId != null) {
				e = contentService.getItemById(contentId);
			}
			return e;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常！");
		}
	}
	
	@RequestMapping("/ajaxDelete")
	public @ResponseBody
	Object ajaxDelete(@RequestParam(value = "contentId", required = true) String contentId) {
		try {
			if (contentId != null) {
				String[] ids = contentId.split(",");
				for (String str : ids) {
					contentService.deleteItemById(Long.valueOf(str));
				}
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
	@RequestMapping("/generateHtml")
	public @ResponseBody Object generateHtml(HttpServletRequest request,
			@RequestParam(value = "contentId", required = true) Long contentId) {
		try{
			contentService.generateHtml(request, contentId);
			return successJson();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return errorJson("异常！");
		}
	}
	
	@RequestMapping("/cancelStaticHtml")
	public @ResponseBody
	Object cancelStaticHtml(@RequestParam(value = "contentId", required = false) Long contentId) {
		try {
			ContentEntity content = contentService.getItemById(contentId);
			String htmlFolder = ConfigUtil.getConfigContent("dm",
					"htmlDir")+"/content";
			String htmlFile = htmlFolder+"/"+content.getContentId()+".html";;
			File file = new File(htmlFile);
			if(file.exists()){
				file.delete();
			}
			content.setIsStaticPage("0");
			content.setStaticPageUrl(null);
			contentService.updateItem(content);
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
}
