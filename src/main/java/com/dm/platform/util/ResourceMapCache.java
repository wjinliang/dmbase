package com.dm.platform.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;


public class ResourceMapCache {
	private static ResourceMapCache instance = new ResourceMapCache();
	public Map<String, Collection<ConfigAttribute>> resourceMap = null;
	
	private ResourceMapCache() { 
		init();
	}  
	
	public static ResourceMapCache getInstance() {
		 return instance;
	}
	
	protected void init() {
		refreshResourceMap();
	}
	
	public void refreshResourceMap(){
		resourceMap=new HashMap<String, Collection<ConfigAttribute>>();
		String sql = "SELECT b.CODE AS role,g.MENUURL AS url FROM t_user_menu g, t_role_menu f,t_user_role b WHERE g.id=f.MENU_CODE AND  f.ROLE_CODE=b.CODE AND g.MENUURL!='/#' AND g.MENUURL!='#' ORDER BY role,url"; 
		JDBCUtil.getJdbcTemplate().query(sql, new RowCallbackHandler() {  
            @Override  
            public void processRow(ResultSet rs) throws SQLException {  
                //2.处理结果集  
            	String role = rs.getString("role");  
                String url = rs.getString("url"); 
                if(!resourceMap.containsKey(url)){
                	Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
                	configAttributes.add(new SecurityConfig(role));
                	resourceMap.put(url,configAttributes);
                }else{
                	 ConfigAttribute configAttribute =  new SecurityConfig(role);
                	 Collection<ConfigAttribute> configAttributes = resourceMap.get(url);
                	 configAttributes.add(configAttribute);
                     resourceMap.put(url, configAttributes);
                }
            }  
        }); 
		
		String sql2 = "SELECT m.MENUURL as url from t_user_menu m where m.`MENUURL`!='#' AND m.`MENUURL`!='/#' AND m.id not in(SELECT g.id FROM t_user_menu g, t_role_menu f WHERE g.`id` = f.MENU_CODE ) ORDER BY url";
		JDBCUtil.getJdbcTemplate().query(sql2, new RowCallbackHandler() {  
            @Override  
            public void processRow(ResultSet rs) throws SQLException {  
                //2.处理结果集  
                String url = rs.getString("url"); 
                if(!resourceMap.containsKey(url)){
                	Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
                	configAttributes.add(new SecurityConfig("NOONELOVEME"));
                	resourceMap.put(url,configAttributes);
                }
            }  
        });
		// 如果只存在action,不存在角色，则都可以访问
		
	
	}
	
	public Map<String, Collection<ConfigAttribute>> getResourceMap(){
		if(resourceMap!=null){
			return resourceMap;
		}else{
			refreshResourceMap();
			return resourceMap;
		}
	}
}
