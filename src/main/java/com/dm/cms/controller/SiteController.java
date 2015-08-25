package com.dm.cms.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
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
import com.dm.cms.service.ChannelService;
import com.dm.cms.service.ContentService;
import com.dm.cms.service.SiteService;
import com.dm.cms.service.TemplateService;
import com.dm.platform.controller.DefaultController;
import com.dm.platform.util.ConfigUtil;
import com.dm.platform.util.FreeMarkertUtil;

@Controller
@RequestMapping("/cms/site")
public class SiteController extends DefaultController {
	@Resource
	SiteService siteService;
	@Resource
	ChannelService channelService;
	@Resource
	ContentService contentService;
	@Resource
	TemplateService templateService;
	
	@RequestMapping("/page")
	public ModelAndView page(
			ModelAndView model) {
		try {
			model.setViewName("/pages/cms/site/page");
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
			SiteEntity searchEntity) throws Exception {
		try {
			if (pagesize == null) {
				pagesize = 10;
			}
			if (thispage == null) {
				thispage = 0;
			}
			List<Order> orders = new ArrayList<Order>();
			orders.add(new Order(Direction.ASC, "sortSeq"));
			orders.add(new Order(Direction.DESC, "siteId"));
			Map<String, Object> map = new HashMap<String, Object>();
			Map argsMap = new HashMap();
			String whereSql = "";
			if(searchEntity.getSiteName()!=null){
				whereSql += " and t.siteName like :siteName ";
				argsMap.put("siteName", "%"+searchEntity.getSiteName()+"%");
			}
			map = siteService.getItemsMap(argsMap,whereSql, orders, thispage-1, pagesize);
			Map result = new HashMap();
			result.put("data", (List<SiteEntity>)map.get("items"));
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
	
	
	@RequestMapping("/ajaxLoad")
	public @ResponseBody
	Object ajaxLoad(
			@RequestParam(value = "siteId", required = false) Long siteId) {
		try {
			SiteEntity item = siteService.getItemById(siteId);
			return item;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常！");
		}
	}
	
	@RequestMapping("/ajaxSave")
	public @ResponseBody
	Object ajaxSave(SiteEntity entity) {
		try {
			if (entity.getSiteId() != null) {
				SiteEntity e = siteService.getItemById(entity.getSiteId());
				e.setSiteName(entity.getSiteName());
				e.setTemplateId(entity.getTemplateId());
				e.setDomain(entity.getDomain());
				siteService.updateItem(e);
			} else {
				siteService.insertItem(entity);
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
	@RequestMapping("/ajaxDelete")
	public @ResponseBody
	Object ajaxDelete(@RequestParam(value = "siteId", required = false) String siteId) {
		try {
			if (siteId != null) {
				String[] ids = siteId.split(",");
				for (String str : ids) {
					siteService.deleteItemById(Long.valueOf(str));
				}
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
	
	@RequestMapping("/checkDomain")
	public void checkDomain(@RequestParam(value = "siteId", required = false) String siteId,
			@RequestParam(value = "domain", required = false) String domain,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			SiteEntity item = siteService.getItemByDomain(domain);
			if(item!=null){
				if(siteId!=null&&siteId.equals(String.valueOf(item.getSiteId()))){
					out.write("true");
				}else{
					out.write("false");
				}
			}else{
				out.write("true");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/generateHtml")
	public @ResponseBody Object generateHtml(HttpServletRequest request,
			@RequestParam(value = "siteId", required = true) Long siteId) {
		try{
			siteService.generateHtml(request, siteId);
			return successJson();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return errorJson("异常！");
		}
	}
	
	@RequestMapping("/cancelStaticHtml")
	public @ResponseBody
	Object cancelStaticHtml(@RequestParam(value = "siteId", required = false) Long siteId) {
		try {
			SiteEntity item = siteService.getItemById(siteId);
			String htmlFolder = ConfigUtil.getConfigContent("dm",
					"htmlDir")+"/"+item.getDomain();
			String htmlFile = htmlFolder+"/index.html";
			File file = new File(htmlFile);
			if(file.exists()){
				file.delete();
			}
			item.setIsStaticIndex("0");
			item.setStaticDir(null);
			siteService.updateItem(item);
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
	@RequestMapping("/loadSites")
	public @ResponseBody Object loadSites() throws Exception {
		try {
			List<Order> orders = new ArrayList<Order>();
			orders.add(new Order(Direction.ASC, "siteId"));
			List<SiteEntity> list = new ArrayList<SiteEntity>();
			Map argsMap = new HashMap();
			list = siteService.getAllItems(argsMap, orders);
			List json_list = new ArrayList();
			for (SiteEntity siteEntity : list) {
				Map map = new HashMap();
				map.put("value", siteEntity.getSiteId());
				map.put("text", siteEntity.getSiteName());
				json_list.add(map);
			}
			return json_list;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
	/**
	 * 静态化所有页面
	 * @param request
	 * @param siteId 站点id
	 * @return json
	 */
	@RequestMapping("/generateHtmlAll")
	public @ResponseBody Object generateHtmlAll(HttpServletRequest request,
			@RequestParam(value = "siteId", required = true) Long siteId) {
		try{
			SiteEntity item = siteService.getItemById(siteId);
			
			List<ChannelEntity> clist = siteService.getAllChannelsByID(siteId);
			
			for (ChannelEntity channel : clist) {
				List<ContentEntity> colist = channelService.getContentsById(channel.getChannelId());
				for (ContentEntity content : colist) {
					contentService.generateHtml(request, content.getContentId());
				}
				channelService.generateHtml(request, channel.getChannelId());
			}
			
			siteService.generateHtml(request, siteId);
			
			return successJson();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return errorJson("异常！");
		}
	}
	
	
	

}
