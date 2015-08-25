package com.dm.platform.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dm.platform.model.NoticeEntity;
import com.dm.platform.model.UserAccount;
import com.dm.platform.model.UserRole;
import com.dm.platform.service.NoticeService;
import com.dm.platform.util.DmDateUtil;

@Controller
@RequestMapping("/notice")
public class NoticeController extends DefaultController {
	@Resource
	NoticeService noticeService;

	@SuppressWarnings("unchecked")
	@RequestMapping("/list")
	public ModelAndView list(ModelAndView model, Integer thispage,
			Integer pagesize) {
		try {
			if (thispage == null) {
				thispage = 0;
			}
			if (pagesize == null) {
				pagesize = 20;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map = noticeService.getAll(thispage, pagesize);
			List<NoticeEntity> list = new ArrayList<NoticeEntity>();
			list = (List<NoticeEntity>) map.get("items");
			model.addObject("notices", list);
			Long totalcount = (Long) map.get("totalcount");
			model.setViewName("/pages/admin/notice/list");
			return Model(model, thispage, pagesize, totalcount);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}
	
	@RequestMapping("/form/{mode}")
	public ModelAndView form(ModelAndView model,
			@PathVariable String mode,
			@RequestParam(value = "noticeId", required = false) String noticeId){
		try {
			NoticeEntity entity = new NoticeEntity();
			if (mode != null && !mode.equals("new")) {
				if (noticeId != null) {
					if(noticeId!=null&&!noticeId.equals("")){
						entity = noticeService.findOne(noticeId);
					}
					model.addObject("notice", entity);
					if (mode.equals("view")) {
						model.setViewName("/pages/admin/notice/view");
						return Model(model);
					}
				}
			} else {
				model.addObject("notice", entity);
			}
			model.setViewName("/pages/admin/notice/form");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}
	
	@RequestMapping("/save")
	public ModelAndView save(ModelAndView model,NoticeEntity notice){
		try {	
			if(notice.getNoticeId()!=null&&!notice.getNoticeId().equals("")){
				NoticeEntity entity = noticeService.findOne(notice.getNoticeId());
				entity.setNoticeTitle(notice.getNoticeTitle());
				entity.setNoticeOriginal(notice.getNoticeOriginal());
				entity.setNoticeDate(notice.getNoticeDate());
				entity.setNoticeContent(notice.getNoticeContent());
				noticeService.update(entity);
			}else{
				notice.setNoticeId(String.valueOf(System.currentTimeMillis()));
				if(notice.getNoticeDate()!=null&&!notice.getNoticeDate().equals("")){
				}else{
					notice.setNoticeDate(DmDateUtil.Current());
				}
				noticeService.insert(notice);
			}
			model.addObject("redirect", getRootPath() + "/notice/list");
			model.setViewName("/pages/content/redirect");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}
	
	@RequestMapping("/delete")
	public void delete(HttpServletResponse response,
			@RequestParam(value = "noticeId", required = false) String noticeId)
			throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			if (noticeId != null) {
				String[] rid = noticeId.split(",");
				for (String str : rid) {
					noticeService.deleteOne(str);
				}
			}
			out.write("ok");
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			out.write("error");
			out.flush();
			out.close();
		}
	}
	@RequestMapping("/setTop")
	public void setTop(HttpServletResponse response,
			@RequestParam(value = "noticeId", required = false) String noticeId)
			throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			if (noticeId != null) {
				noticeService.setTop(noticeId);
			}
			out.write("ok");
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			out.write("error");
			out.flush();
			out.close();
		}
	}
	
	@RequestMapping("/cancelTop")
	public void cancelTop(HttpServletResponse response,
			@RequestParam(value = "noticeId", required = false) String noticeId)
			throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			if (noticeId != null) {
				noticeService.cancelTop(noticeId);
			}
			out.write("ok");
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			out.write("error");
			out.flush();
			out.close();
		}
	}
	
}
