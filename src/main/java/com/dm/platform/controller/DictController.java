package com.dm.platform.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.Dict;
import com.dm.platform.model.DictItem;
import com.dm.platform.model.UserAccount;
import com.dm.platform.service.DictService;
import com.dm.platform.util.DictUtil;

@Controller
@RequestMapping("/dict")
public class DictController extends DefaultController {
	@Resource
	DictService dictService;

	@Resource
	CommonDAO commonDAO;

	@RequestMapping("/list")
	public ModelAndView list(
			ModelAndView model,
			@RequestParam(value = "thispage", required = false) Integer thispage,
			@RequestParam(value = "pagesize", required = false) Integer pagesize) {
		try {
			// System.out.println("===="+DictUtil.getDictItemName("dict_status","1"));
			if (pagesize == null) {
				pagesize = 10;
			}
			if (thispage == null) {
				thispage = 0;
			}
			Long totalcount = dictService.countDict();
			if ((thispage) * pagesize >= totalcount && totalcount > 0) {
				thispage--;
			}
			Dict entity = new Dict();
			String whereSql = "";
			model.addObject("dicts",
					dictService.listDict(thispage, pagesize, entity, whereSql));
			model.setViewName("/pages/admin/dict/list");
			return Model(model, thispage,pagesize, totalcount);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	@RequestMapping("/form/{mode}")
	public ModelAndView form(ModelAndView model, HttpServletRequest request,
			@PathVariable String mode,
			@RequestParam(value = "dictId", required = false) String dictId) {
		try {
			Dict de = new Dict();
			if (mode != null && !mode.equals("new")) {
				if (StringUtils.isNotEmpty(dictId)) {
					de = dictService.findOne(dictId);
					model.addObject("dict", de);
					if(mode.equals("view")){
						model.setViewName("/pages/admin/dict/view");
						return Model(model);
					}
				}
			}
			model.setViewName("/pages/admin/dict/form");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	@RequestMapping("/save")
	public ModelAndView save(ModelAndView model, Dict dict) {
		try {
			Dict de = new Dict();
			if (StringUtils.isNotEmpty(dict.getDictId())) {
				de = dictService.findOne(dict.getDictId());
				de.setDictName(dict.getDictName());
				de.setDictCode(dict.getDictCode());
				de.setDictDesc(dict.getDictDesc());
				de.setDictSeq(dict.getDictSeq());
				de.setDictStatus(dict.getDictStatus());
				dictService.updateDict(de);
			} else {
				dictService.insertDict(dict);
			}
			model.addObject("redirect",getRootPath()+"/dict/list");
			model.setViewName("/pages/content/redirect");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	@RequestMapping("/delete")
	public void delete(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "dictId", required = false) String dictId)
			throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			if (StringUtils.isNotEmpty(dictId)) {
				String[] did = dictId.split(",");
				for (String str : did) {
					Dict de = new Dict();
					de = dictService.findOne(str);
					dictService.deleteOne(de);
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
	
	
	@RequestMapping("/checkunique")
	public void checkloginname(HttpServletResponse response,
			@RequestParam(value = "param", required = false) String param,
			@RequestParam(value = "name", required = false) String name) {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			if (commonDAO.findByPropertyName(Dict.class, name, param)
					.size() > 0) {
				out.write("n");
			} else {
				out.write("y");
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@RequestMapping("/refreshCache")
	public void refreshCache(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "dictId", required = false) String dictId)
			throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			if (StringUtils.isNotEmpty(dictId)) {
				Dict de = new Dict();
				de = dictService.findOne(dictId);
				DictUtil.refreshDict(dictId, de.getDictCode(), dictService);
			}
			out.write("ok");
			out.flush();
			out.close();
		} catch (Exception e) {
			out.write("error");
			out.flush();
			out.close();
		}
	}

	@RequestMapping("/dictItemList")
	public ModelAndView list(ModelAndView model,
			@RequestParam(value = "dictId", required = false) String dictId) {
		try {
			Dict dict = dictService.findOne(dictId);
			model.addObject("dictId", dict.getDictId());
			model.addObject("dictName", dict.getDictName());
			model.setViewName("/pages/admin/dict/dictItems");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/load/{dictId}")
	public void load(HttpServletResponse response, @PathVariable String dictId) {
		try {
			List ml = new ArrayList();
			List<Order> orders = new ArrayList<Order>();
			orders.add(new Order(Direction.ASC, "itemSeq"));
			Map args = new HashMap();
			args.put("dictId", dictId);
			List<DictItem> itemlist = commonDAO.findByMapArg(DictItem.class,
					" AND dictId = :dictId", args, orders);
			for (DictItem dictItem : itemlist) {
				Map m = new HashMap();
				m.put("id", dictItem.getItemId());
				m.put("name", dictItem.getItemName());
				m.put("pId", dictItem.getItemPid());
				m.put("darg", true);
				ml.add(m);
			}
			JSONArray json = JSONArray.fromObject(ml);
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.write(json.toString());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/loadByCode/{dictCode}")
	@ResponseBody
	public Object loadByCode(HttpServletResponse response, @PathVariable String dictCode) {
		try {
			List ml = new ArrayList();
			List<Order> orders = new ArrayList<Order>();
			orders.add(new Order(Direction.ASC, "itemSeq"));
			Map args = new HashMap();
			args.put("dictCode", dictCode);
			List<DictItem> itemlist = commonDAO.findByMapArg(DictItem.class,
					" AND dictCode = :dictCode", args, orders);
			for (DictItem dictItem : itemlist) {
				Map m = new HashMap();
				m.put("id", dictItem.getItemId());
				m.put("name", dictItem.getItemName());
				m.put("pId", dictItem.getItemPid());
				m.put("darg", true);
				ml.add(m);
			}
			return ml;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}

	@RequestMapping("/dictItem/form/{mode}")
	public ModelAndView ItemForm(
			ModelAndView model,
			HttpServletRequest request,
			@PathVariable String mode,
			@RequestParam(value = "dictId", required = false) String dictId,
			@RequestParam(value = "dictItemId", required = false) String dictItemId) {
		try {
			DictItem de = new DictItem();
			if (mode != null && !mode.equals("new")) {
				if (StringUtils.isNotEmpty(dictItemId)) {
					de = dictService.findOneItem(dictItemId);
					model.addObject("dictItem", de);
					if(mode.equals("view")){
						model.setViewName("/pages/admin/dict/dictItemView");
						return Model(model);
					}
				}
			}
			model.addObject("mode", mode);
			model.addObject("dictId", dictId);
			model.addObject("itemPid", dictItemId);
			model.setViewName("/pages/admin/dict/dictItemForm");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	@RequestMapping("/dictItem/save")
	public ModelAndView itemSave(ModelAndView model, DictItem dictItem) {
		try {
			DictItem de = new DictItem();
			if (StringUtils.isNotEmpty(dictItem.getItemId())) {
				de = dictService.findOneItem(dictItem.getItemId());
				de.setItemCode(dictItem.getItemCode());
				de.setItemName(dictItem.getItemName());
				de.setItemSeq(dictItem.getItemSeq());
				// de.setDictId(dictItem.getDictId());
				// de.setItemPid(dictItem.getItemPid());
				dictService.updateDictItem(de);
			} else {
				dictService.insertDictItem(dictItem);
			}
			model.setViewName("/pages/content/success");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	@RequestMapping("/dictItem/delete")
	public void itemDelete(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "dictItemId", required = false) String dictItemId)
			throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			if (StringUtils.isNotEmpty(dictItemId)) {
				String[] did = dictItemId.split(",");
				for (String str : did) {
					DictItem de = new DictItem();
					de = dictService.findOneItem(str);
					dictService.deleteOneItem(de);
				}
			}
			out.write("ok");
			out.flush();
			out.close();
		} catch (Exception e) {
			out.write("error");
			out.flush();
			out.close();
		}
	}

}
