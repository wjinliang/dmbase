package com.dm.platform.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.LogEntity;
import com.dm.platform.model.UserAccount;
import com.dm.platform.util.DmDateUtil;

public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {
	@Resource
	CommonDAO commonDAO;
	private static Logger logger = Logger.getLogger("login");
    public final static String TRY_MAX_COUNT="tryMaxCount";  
    private int maxTryCount=3;  
    
    public AjaxAuthenticationFailureHandler() {  
    }  
  
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,  
            AuthenticationException exception) throws IOException, ServletException {
		/**
		 * 次数限制
		 */
		String tryMsg=null;
		HttpSession session = request.getSession();
		if (exception.getMessage().equals("密码不正确")) {
			String name = request
					.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
			UserAccount u = null;
			if (commonDAO.findByPropertyName(UserAccount.class, "loginname", name)
					.size() > 0) {
				u = commonDAO.findByPropertyName(UserAccount.class, "loginname",
						name).get(0);
			}
			Integer tryCount = (Integer) session.getAttribute(name + "_"
					+ TRY_MAX_COUNT)==null?1:(Integer)session.getAttribute(name + "_"
							+ TRY_MAX_COUNT)+1;
			if (tryCount > maxTryCount - 1) {
				// 锁定账户
				u.setNonLocked(false);
				commonDAO.update(u);
				LogEntity le = new LogEntity();
				le.setDate(DmDateUtil.Current());
				le.setType("0");
				le.setTitle("登录失败");
				le.setContent("用户名:" + u.getName() + "--登录失败，密码错误超过最大登录尝试次数，已被锁定");
				le.setUser(u.getName() + "(" + u.getLoginname() + "["+u.getCode()+"])");
				le.setIp(getIpAddress(request));
				commonDAO.save(le);
				logger.info(le.getUser()+">>"+le.getContent());
				session.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION,
						exception);
				session.removeAttribute(name+"_"+ TRY_MAX_COUNT);
				tryMsg = "超过最大登录尝试次数"+maxTryCount+",用户已被锁定";
			}else{
				session.setAttribute(name+"_"+ TRY_MAX_COUNT,tryCount);
				LogEntity le = new LogEntity();
				le.setDate(DmDateUtil.Current());
				le.setType("0");
				le.setTitle("登录失败");
				le.setContent("用户名:" + u.getName() + "--登录失败");
				le.setUser(u.getName() + "(" + u.getLoginname() + "["+u.getCode()+"])");
				le.setIp(getIpAddress(request));
				commonDAO.save(le);
				logger.info(le.getUser()+">>"+le.getContent());
			}
			
		}

		/**
		 * 次数限制结束------------------
		 */
        ObjectMapper objectMapper = new ObjectMapper();  
        response.setHeader("Content-Type", "application/json;charset=UTF-8");  
        JsonGenerator jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(response.getOutputStream(),  
                JsonEncoding.UTF8);  
        try {  
        	String msg = tryMsg==null?exception.getMessage():tryMsg;
        	//失败为1  
        	Map map = new HashMap();
        	map.put("status", 1);
        	map.put("msg",msg==null?"":msg);
        	JSONObject jsonData = JSONObject.fromObject(map);  
            objectMapper.writeValue(jsonGenerator, jsonData);  
        } catch (JsonProcessingException ex) {  
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);  
        }  
    }
    private String getIpAddress(HttpServletRequest request){    
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
}
