package com.dm.cms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dm.cms.model.FriendLinkEntity;
import com.dm.cms.service.FriendLinkService;
import com.dm.platform.controller.DefaultController;
@Controller
@RequestMapping("/cms/friendlink")
public class FriendLinkController extends DefaultController {
	/**
	 * @author ChenGJ 
	 * @version 创建时间：2015-4-15 下午4:25:42
	 * 类说明 友情链接Controller
	 */
	@Resource
	FriendLinkService friendLinkService;
	
	/**
	 * 进入友情链接首页
	 * @param model
	 * @return
	 */
	@RequestMapping("/page")
	public ModelAndView page(
			ModelAndView model) {
		try {
			model.setViewName("/pages/cms/friend-link/page");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}
	
	/**
	 * 获取友情链接list
	 * @param response
	 * @param pageNum
	 * @param pageSize
	 * @param searchEntity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/jsonList")
	public @ResponseBody Object jsonList(
			HttpServletResponse response,
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			FriendLinkEntity searchEntity) throws Exception {
		try {
			if (pageSize == null) {
				pageSize = 10;
			}
			if (pageNum == null) {
				pageNum = 1;
			}
			List<Order> orders = new ArrayList<Order>();
			orders.add(new Order(Direction.ASC, "seq"));
			Map<String, Object> map = new HashMap<String, Object>();
			Map argsMap = new HashMap();
			String whereSql = "";
			if(!StringUtils.isEmpty(searchEntity.getName())){
				argsMap.put("name", "%" + searchEntity.getName()+ "%");
				whereSql+=" and t.name like :name";
			}
			map = friendLinkService.getItemsMap(argsMap,whereSql,orders, pageNum-1, pageSize);
			Map result = new HashMap();
			result.put("data", (List<FriendLinkEntity>)map.get("data"));
			result.put("total", (Long)map.get("total"));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
	/**
	 * ajax保存友情链接
	 * @param entity
	 * @return
	 */
	@RequestMapping("/ajaxSave")
	public @ResponseBody
	Object ajaxSave(FriendLinkEntity entity) {
		try {
			if (entity.getId() != null) {
				friendLinkService.updateItem(entity);
			} else {
				friendLinkService.insertItem(entity);
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
	/**
	 * 加载友情链接数据
	 * @param id
	 * @return
	 */
	@RequestMapping("/ajaxLoad")
	public @ResponseBody
	Object ajaxLoad(
			@RequestParam(value = "id", required = true) Long id) {
		try {
			FriendLinkEntity e = new FriendLinkEntity();
			if (id != null) {
				e = friendLinkService.getItemById(id);
			}
			return e;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常！");
		}
	}
	
	/**
	 * 删除友情链接
	 * @param id
	 * @return
	 */
	@RequestMapping("/ajaxDelete")
	public @ResponseBody
	Object ajaxDelete(@RequestParam(value = "id", required = true) String id) {
		try {
			if (id != null) {
				String[] ids = id.split(",");
				for (String str : ids) {
					friendLinkService.deleteItemById(Long.valueOf(str));
				}
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
}
