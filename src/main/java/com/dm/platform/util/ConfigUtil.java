package com.dm.platform.util;

import java.util.ResourceBundle;

/**
 * 
 *    
 * 项目名称：base_spring_framework   
 * 类名称：ConfigUtil   
 * 类描述：    properties资源获取类
 * 创建人：huangxiong   
 * 创建时间：2015年2月4日 下午3:19:34   
 * 修改人：huangxiong   
 * 修改时间：2015年2月4日 下午3:19:34   
 * 修改备注：   
 * @version    
 *
 */
public class ConfigUtil {
	/**
	 * 
	* @Title: getConfigContent
	* @Description: 获取properties文件
	* @param @param configName properties文件路径，如:conf/user 
	* @param @param key key值
	* @param @return
	* @return String    返回类型
	* @throws
	 */
	public static String  getConfigContent(String configName,String key){
	
		ResourceBundle resource = ResourceBundle.getBundle("config/properties/"+configName);
		
		return resource.getString(key)==null?"":resource.getString(key);
	}

}
