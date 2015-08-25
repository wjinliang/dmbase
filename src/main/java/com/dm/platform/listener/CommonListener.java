package com.dm.platform.listener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContextEvent;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.Dict;
import com.dm.platform.model.DictItem;
import com.dm.platform.model.MenuGroup;
import com.dm.platform.model.UserRole;
import com.dm.platform.security.UserCacheUtil;
import com.dm.platform.service.DictService;
import com.dm.platform.util.DictCache;
import com.dm.platform.util.EhCacheUtil;
import com.dm.platform.util.RandomValidateCode;



public class CommonListener extends ContextLoaderListener {
	private static Logger log = LogManager.getLogger(CommonListener.class);

	private static WebApplicationContext ct;
    
	public CommonListener() {
	}
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			ct = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
			initRandomVcode();
			EhCacheUtil.getInstance().init(ct);
			UserCacheUtil.getInstance().init();
			initDict();
		} catch (Exception ce) {
			ce.printStackTrace();
			log.error("初始化数据异常：", ce);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}
	
	public void initDict(){
		DictService dictService=(DictService)ct.getBean("dictServiceImpl");
		 List<Dict> d=dictService.listEnableDict();
		 DictCache dictCache=DictCache.getInstance();
		 for(Dict dict:d){
			 List<DictItem> itemList=dictService.listDictItemByDictId(dict.getDictId());
			 HashMap mp=new HashMap<String,String>();
			 for(DictItem item:itemList){
				 mp.put(item.getItemCode(), item.getItemName());
			 }
			 dictCache.keyNameContainer.put(dict.getDictCode(), mp);
			 dictCache.dictItemContainer.put(dict.getDictCode(), itemList);
			 mp=null;
			 itemList=null;
		 }
		 dictCache.initAllJsonDic(dictService);
	}
	
	public void initRandomVcode(){
		RandomValidateCode.getInstance().getRandcode();
	}
	public void initNavMenusEhcache(){
		CommonDAO commonDAO = (CommonDAO) ct
				.getBean("commonDAOImpl");
		JdbcTemplate jdbcTemplate= (JdbcTemplate)ct.getBean("jdbcTemplate");
		Cache myCache = (Cache)ct
				.getBean("myCache");
		List<UserRole> userRoleList = commonDAO.findAll(UserRole.class);
		for (UserRole u : userRoleList) {
			Set<MenuGroup> mglist = u.getMenugroups();
			Set<Map<String, Object>> rset = new LinkedHashSet<Map<String, Object>>();
			for (MenuGroup menuGroup : mglist) {
					Map<String, Object> map = new HashMap<String, Object>();
					final List<Map> mapList = new ArrayList<Map>();
					map.put("id", String.valueOf(menuGroup.getId()));
					map.put("gname", String.valueOf(menuGroup.getName()));
					String sql = "select m.`NAME` as name,m.MENUURL as url from t_usermenu_menugroup mg,t_user_menu m where mg.MENU_GROUP_ID=? and mg.MENU_CODE=m.id and m.PID  is not null and m.menuUrl!='/#' and m.menuUrl!='/mainpage' ORDER BY m.SEQ asc";
					jdbcTemplate.query(sql,new Object[]{menuGroup.getId()},
							new RowCallbackHandler() {
								@Override
								public void processRow(ResultSet rs)
										throws SQLException {
									// 2.处理结果集
									Map map2 = new HashMap();
									map2.put("name", rs.getString("name"));
									map2.put("url", rs.getString("url"));
									mapList.add(map2);
								}
							});
					map.put("menus", mapList);
					rset.add(map);
			}
			Element element = new Element(u.getCode(), rset);  
			myCache.put(element);
		}
	}
}
