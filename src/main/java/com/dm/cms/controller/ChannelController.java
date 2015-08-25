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
import com.dm.cms.model.SiteEntity;
import com.dm.cms.model.TemplateEntity;
import com.dm.cms.service.ChannelService;
import com.dm.cms.service.TemplateService;
import com.dm.platform.controller.DefaultController;
import com.dm.platform.util.ConfigUtil;
import com.dm.platform.util.FileUtil;
import com.dm.platform.util.FreeMarkertUtil;
@Controller
@RequestMapping("/cms/channel")
public class ChannelController extends DefaultController{
	@Resource
	ChannelService channelService;
	@Resource
	TemplateService templateService;
	
	@RequestMapping("/page")
	public ModelAndView page(
			ModelAndView model) {
		try {
			model.setViewName("/pages/cms/channel/page");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/jsonList")
	@ResponseBody
	public Object jsonList(
			HttpServletResponse response,
			@RequestParam(value = "pageNum", required = false) Integer thispage,
			@RequestParam(value = "pageSize", required = false) Integer pagesize,
			ChannelEntity searchEntity) throws Exception {
		try {
			if (pagesize == null) {
				pagesize = 10;
			}
			if (thispage == null) {
				thispage = 1;
			}
			List<Order> orders = new ArrayList<Order>();
			orders.add(new Order(Direction.DESC, "channelId"));
			Map<String, Object> map = new HashMap<String, Object>();
			Map argsMap = new HashMap();
			String whereSql = "";
			if(searchEntity.getChannelName()!=null){
				whereSql += " and t.channelName like :channelName ";
				argsMap.put("channelName", "%"+searchEntity.getChannelName()+"%");
			}
			map = channelService.getItemsMap(argsMap,whereSql, orders, thispage-1, pagesize);
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
	
	@RequestMapping("/jsonTree")
	public @ResponseBody Object jsonTree() throws Exception {
		try {
			List<Order> orders = new ArrayList<Order>();
			orders.add(new Order(Direction.DESC, "channelId"));
			List<ChannelEntity> list = new ArrayList<ChannelEntity>();
			Map argsMap = new HashMap();
			list = channelService.getAllItems(argsMap, orders);
			List json_list = new ArrayList();
			Map root = new HashMap();
			root.put("id", 0);
			root.put("name", "根目录");
			root.put("pId", null);
			root.put("open", true);
			json_list.add(root);
			for (ChannelEntity channelEntity : list) {
				Map map = new HashMap();
				map.put("id", channelEntity.getChannelId());
				map.put("name", channelEntity.getChannelName());
				map.put("pId", channelEntity.getParentId()==null?0:channelEntity.getParentId());
				json_list.add(map);
			}
			return json_list;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
	@RequestMapping("/ajaxLoad")
	public @ResponseBody
	Object ajaxLoad(
			@RequestParam(value = "channelId", required = false) Long channelId) {
		try {
			ChannelEntity e = new ChannelEntity();
			if (channelId != null) {
				e = channelService.getItemById(channelId);
			}
			return e;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常！");
		}
	}
	
	@RequestMapping("/ajaxSave")
	public @ResponseBody
	Object ajaxSave(ChannelEntity entity) {
		try {
			if (entity.getChannelId() != null && !entity.getChannelId().equals("")) {
				ChannelEntity e = channelService.getItemById(entity.getChannelId());
				e.setSiteId(entity.getSiteId());
				e.setChannelName(entity.getChannelName());
				e.setChannelPath(entity.getChannelPath());
				e.setTemplateId(entity.getTemplateId());
				e.setChannelPageSize(entity.getChannelPageSize());
				channelService.updateItem(e);
			} else {
				channelService.insertItem(entity);
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
	@RequestMapping("/ajaxDelete")
	public @ResponseBody
	Object ajaxDelete(@RequestParam(value = "channelId", required = false) String channelId) {
		try {
			if (channelId != null) {
				String[] ids = channelId.split(",");
				for (String str : ids) {
					channelService.deleteItemById(Long.valueOf(str));
				}
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	@RequestMapping("/loadChannels")
	public @ResponseBody Object loadChannels() throws Exception {
		try {
			List<Order> orders = new ArrayList<Order>();
			orders.add(new Order(Direction.ASC, "siteId"));
			List<ChannelEntity> list = new ArrayList<ChannelEntity>();
			Map argsMap = new HashMap();
			list = channelService.getAllItems(argsMap, orders);
			List json_list = new ArrayList();
			for (ChannelEntity channelEntity : list) {
				Map map = new HashMap();
				map.put("value", channelEntity.getChannelId());
				map.put("text", channelEntity.getChannelName());
				json_list.add(map);
			}
			return json_list;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
	@RequestMapping("/generateHtml")
	public @ResponseBody Object generateHtml(HttpServletRequest request,
			@RequestParam(value = "channelId", required = true) Long channelId) {
		try{
			channelService.generateHtml(request, channelId);
			return successJson();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return errorJson("异常！");
		}
	}
	
	@RequestMapping("/cancelStaticHtml")
	public @ResponseBody
	Object cancelStaticHtml(@RequestParam(value = "channelId", required = false) Long channelId) {
		try {
			ChannelEntity channel = channelService.getItemById(channelId);
			String htmlFolder = ConfigUtil.getConfigContent("dm",
					"htmlDir")+"/"+channel.getChannelId();
			FileUtil.deleteDirectory(htmlFolder);
			channel.setIsStaticPage("0");
			channel.setStaticPageDir(null);
			channel.setStaticPageUrl(null);
			channelService.updateItem(channel);
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
}
