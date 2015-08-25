package com.dm.platform.security;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.userdetails.UserCache;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.UserAccount;

public class UserCacheUtil {
	private static UserCacheUtil instance = new UserCacheUtil();

	private UserCacheUtil() {
	}

	public ApplicationContext ct = new ClassPathXmlApplicationContext(
			"config/spring/applicationContext.xml");
	public CommonDAO commonDAO = (CommonDAO) ct.getBean("commonDAOImpl");

	public static UserCacheUtil getInstance() {
		return instance;
	}

	public void init() {

	}

	public void refreshUserCache(UserCache userCache, String loginname) {
		if (commonDAO.findByPropertyName(UserAccount.class, "loginname",
				loginname).size() > 0) {
			UserAccount newUser = commonDAO.findByPropertyName(
					UserAccount.class, "loginname", loginname).get(0);
			if (userCache.getUserFromCache(loginname) != null) {
				userCache.removeUserFromCache(loginname);
				userCache.putUserInCache(newUser);
			}
		}
	}
	
	public void refreshUserCache(UserCache userCache, UserAccount ua) {
		if (userCache.getUserFromCache(ua.getLoginname()) != null) {
			userCache.removeUserFromCache(ua.getLoginname());
			userCache.putUserInCache(ua);
		}
	}

}
