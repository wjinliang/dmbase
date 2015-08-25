package com.dm.platform.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.LogEntity;
import com.dm.platform.model.UserAccount;
import com.dm.platform.model.UserRole;
import com.dm.platform.service.UserRoleService;
import com.dm.platform.util.DmDateUtil;

public class MySavedRequestAwareAuthenticationSuccessHandler extends
		SimpleUrlAuthenticationSuccessHandler {
	private static Logger logger = Logger.getLogger("login");
	@Resource
	CommonDAO commonDAO;  
	@Resource
	UserRoleService userRoleService;
	private RequestCache requestCache = new HttpSessionRequestCache();
	private String LOCAL_SERVER_URL;
	public final static String TRY_MAX_COUNT = "tryMaxCount";

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {
		String loginTime = DmDateUtil.DateToStr(new Date(),
				"yyyy-MM-dd HH:mm:ss");
		UserAccount user = (UserAccount) authentication.getPrincipal();
		LogEntity le = new LogEntity();
		le.setDate(loginTime);
		le.setType("0");
		le.setTitle("登录成功");
		le.setContent("用户名:" + user.getName()+"--登陆成功");
		le.setUser(user.getName() + "(" + user.getLoginname() + "["+user.getCode()+"])");
		le.setIp(getIpAddress(request));
		commonDAO.save(le);
		user.setLastLoginTime(loginTime);
		user.setLoginCount(user.getLoginCount() + 1);
		user.setRemoteIpAddr(getIpAddress(request));
		commonDAO.update(user);
		logger.info(le.getUser()+">>"+le.getContent());
		// 清除最大尝试次数
		request.getSession().removeAttribute(
				user.getLoginname() + "_tryMaxCount");
		// targetUrlParameter 是否存在
		String targetUrlParameter = getTargetUrlParameter();
		if (isAlwaysUseDefaultTargetUrl()
				|| (targetUrlParameter != null && StringUtils
						.isNotEmpty(request.getParameter(targetUrlParameter)))) {
			requestCache.removeRequest(request, response);
			super.setAlwaysUseDefaultTargetUrl(false);
			super.setDefaultTargetUrl(targetUrlParameter);
			super.onAuthenticationSuccess(request, response, authentication);
			return;
		}
		// 清除属性
		clearAuthenticationAttributes(request);
		Collection<? extends GrantedAuthority> authorities = authentication
				.getAuthorities();
		for (GrantedAuthority grantedAuthority : authorities) {
			UserRole ur = userRoleService.findOne(grantedAuthority
					.getAuthority());
			if (ur != null && !ur.getHomePage().equals(""))
				LOCAL_SERVER_URL = ur.getHomePage();
			break;
		}
		if (LOCAL_SERVER_URL == null) {
			LOCAL_SERVER_URL = "/login";
		} else {
			super.setDefaultTargetUrl(LOCAL_SERVER_URL);
		}
		String targetUrl = LOCAL_SERVER_URL;
		if (targetUrl == null) {
			throw new AuthenticationServiceException("未授权角色！");
		}
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	public String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}

}
