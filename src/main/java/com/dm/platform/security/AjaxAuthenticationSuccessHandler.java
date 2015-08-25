package com.dm.platform.security;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.LogEntity;
import com.dm.platform.model.UserAccount;
import com.dm.platform.util.DmDateUtil;

public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {  
	@Resource
	CommonDAO commonDAO;  
    public AjaxAuthenticationSuccessHandler() {  
    }  
    private static Logger logger = Logger.getLogger("login");
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,  
            Authentication authentication) throws IOException, ServletException {  
    	
    	String loginTime = DmDateUtil
 				.DateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        UserAccount user=(UserAccount)authentication.getPrincipal();
        LogEntity le = new LogEntity();
 		le.setDate(loginTime);
 		le.setType("0");
 		le.setTitle("登录成功");
 		le.setContent("用户名:" + user.getName()+"--登录成功");
 		le.setUser(user.getName() + "(" + user.getLoginname() + "["+user.getCode()+"])");
 		le.setIp(getIpAddress(request));
 		commonDAO.save(le);
 		logger.info(le.getUser()+">>"+le.getContent());
 		user.setLastLoginTime(loginTime);
 		user.setLoginCount(user.getLoginCount()+1);
 		user.setRemoteIpAddr(getIpAddress(request));
 		commonDAO.update(user);
    	//清除最大尝试次数
 		request.getSession().removeAttribute(user.getLoginname()+"_tryMaxCount");  
 		
        ObjectMapper objectMapper = new ObjectMapper();  
        response.setHeader("Content-Type", "application/json;charset=UTF-8");  
        JsonGenerator jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(response.getOutputStream(),  
                JsonEncoding.UTF8);  
        try {  
            //成功为0  
        	Map map = new HashMap();
        	map.put("status", 0);
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
  
