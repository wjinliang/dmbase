package com.dm.platform.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.UserMenu;
import com.dm.platform.model.UserRole;

public class EhCacheUtil {
	private static EhCacheUtil instance = new EhCacheUtil();
	private EhCacheUtil(){
	}
	public ApplicationContext ct = new ClassPathXmlApplicationContext(
			"config/spring/applicationContext.xml");
	public CommonDAO commonDAO = (CommonDAO) ct
			.getBean("commonDAOImpl");
	public JdbcTemplate jdbcTemplate = (JdbcTemplate)ct.getBean("jdbcTemplate");
	public static EhCacheUtil getInstance(){
		return instance;
	}
	/*private void commonMethod(Cache myCache,UserRole u){
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
	}*/
	
	private void putMenusByRole(Cache myCache,UserRole u){
		Set<UserMenu> menus = new HashSet<UserMenu>();
		menus=u.getMenus();
		Element element = new Element(u.getCode(), menus);  
		myCache.put(element);
	}
	//启动listener加载的时候初始化数据
	public void init(WebApplicationContext context){
		CommonDAO commonDAO = (CommonDAO) ct
				.getBean("commonDAOImpl");
		Cache myCache = (Cache)context
				.getBean("myCache");
		List<UserRole> userRoleList = commonDAO.findAll(UserRole.class);
		for (UserRole u : userRoleList) {
			//commonMethod(myCache, u);
			putMenusByRole(myCache, u);
		}
	}
	//无参数刷新
	public void refreshNavMenus(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();  
		Cache myCache =(Cache) wac.getBean("myCache"); 
		myCache.removeAll();
		List<UserRole> userRoleList = commonDAO.findAll(UserRole.class);
		for (UserRole u : userRoleList) {
			//commonMethod(myCache, u);
			putMenusByRole(myCache, u);
		}
	}
	public void refreshNavMenus(Cache myCache){
		myCache.removeAll();
		List<UserRole> userRoleList = commonDAO.findAll(UserRole.class);
		for (UserRole u : userRoleList) {
			putMenusByRole(myCache, u);
		}
	}
	
	private void putOneRole(Cache myCache,String roleId){
		UserRole u = commonDAO.findOne(UserRole.class, roleId);
		putMenusByRole(myCache, u);
	}
	
	public Set<Map<String, Object>> getNavMenusSet(Cache myCache,String roles){
		Set<Map<String, Object>> nmset = new HashSet<Map<String, Object>>();
		String[] roleid = roles.split(",");
		for (String id : roleid) {
			if(myCache.get(id)==null){
				putOneRole(myCache,id);
			}
			for (Map<String, Object> o : (Set<Map<String, Object>>)myCache.get(id).getObjectValue()) {
				nmset.add(o);
			}
		}
		return nmset;
	}
	public Set<Map<String, Object>> getNavMenusSet(String roles){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();  
		Cache myCache =(Cache) wac.getBean("myCache"); 
		Set<Map<String, Object>> nmset = new HashSet<Map<String, Object>>();
		String[] roleid = roles.split(",");
		for (String id : roleid) {
			if(myCache.get(id)==null){
				putOneRole(myCache,id);
			}
			for (Map<String, Object> o : (Set<Map<String, Object>>)myCache.get(id).getObjectValue()) {
				nmset.add(o);
			}
		}
		return nmset;
	}
	
	
	//获取各个角色菜单总和
	public Set<UserMenu> getNavMenus(Cache myCache,String roles){
		Set<UserMenu> mset = new HashSet<UserMenu>();
		String[] roleid = roles.split(",");
		for (String id : roleid) {
			if(myCache.get(id)==null){
				putOneRole(myCache,id);
			}
			for (UserMenu m : (Set<UserMenu>)myCache.get(id).getObjectValue()) {
				mset.add(m);
			}
		}
		return mset;
	}
	
	//获取各个角色菜单总和
	public Set<UserMenu> getNavMenus(String roles){
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();  
			Cache myCache =(Cache) wac.getBean("myCache"); 
			Set<UserMenu> mset = new HashSet<UserMenu>();
			String[] roleid = roles.split(",");
			for (String id : roleid) {
				if(myCache.get(id)==null){
					putOneRole(myCache,id);
				}
				for (UserMenu m : (Set<UserMenu>)myCache.get(id).getObjectValue()) {
					mset.add(m);
				}
			}
			return mset;
	}
	
}
