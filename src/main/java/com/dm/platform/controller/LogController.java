package com.dm.platform.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dm.platform.model.LogEntity;
import com.dm.platform.service.LogService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/log")
public class LogController extends DefaultController {
	
	@Resource
	LogService logService;
	
	@RequestMapping("/page")
	public ModelAndView page(ModelAndView model) {
		try {
			model.setViewName("/admin/log/page");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}
	
	@RequestMapping("/ajaxList")
	@ResponseBody
	public Object ajaxList(
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sort", required = false) String sort,
			LogEntity log,
			@RequestParam(value = "beginDate", required = false) String beginDate,
			@RequestParam(value = "endDate", required = false) String endDate) {
		try {
			if(pageSize==null){
				pageSize=30;
			}
			if (pageNum == null) {
				pageNum = 0;
			}
			Map argMap = new HashMap();
			if (!StringUtils.isEmpty(sort)) {
				if (sort.indexOf("_desc") != -1) {
					argMap.put("sort", sort.replace("_desc", "").toUpperCase()
							+ " DESC");
				} else if (sort.indexOf("_asc") != -1) {
					argMap.put("sort", sort.replace("_asc", "").toUpperCase()
							+ " ASC");
				}
			}
			if(!StringUtils.isEmpty(log.getUser())){
				argMap.put("user", log.getUser());
			}
			if(!StringUtils.isEmpty(log.getIp())){
				argMap.put("ip", log.getIp());
			}
			if(!StringUtils.isEmpty(log.getType())){
				argMap.put("type", log.getType());
			}
			if(!StringUtils.isEmpty(log.getContent())){
				argMap.put("content", log.getContent());
			}
			if(!StringUtils.isEmpty(beginDate)){
				argMap.put("beginDate", beginDate);
			}
			if(!StringUtils.isEmpty(endDate)){
				argMap.put("endDate", endDate);
			}
			PageInfo<Map> page = logService.findLogs(pageNum,
					pageSize, argMap);
			List<Map> data = page.getList();
			Long totalcount = page.getTotal();
			Map map = new HashMap();
			map.put("status", "1");
			map.put("data", data);
			map.put("total", totalcount);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		
		}
	}
	
	@RequestMapping("/ajaxDelete")	
	@ResponseBody
	public Object ajaxDelete(
			@RequestParam(value = "logId", required = false) String logId) throws Exception {
		try {
			if (!StringUtils.isEmpty(logId)) {
				String[] lid = logId.split(",");
				for (String str : lid) {
					LogEntity l = new LogEntity();
					l = logService.findOne(new Long(str));
					logService.deleteOne(l);
				}
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	@RequestMapping("/ajaxDeleteAllLogs")	
	@ResponseBody
	public Object ajaxDeleteAllLogs() throws Exception {
		try {
			logService.deleteAllLogs();
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
}
