package com.dm.platform.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@SuppressWarnings("deprecation")
public class MyAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint{
	 private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();  
	  
	    @SuppressWarnings({ "rawtypes", "unchecked" })
		public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)  
	            throws IOException, ServletException {  
	  
	        HttpServletRequest httpRequest = (HttpServletRequest) request;  
	        HttpServletResponse httpResponse = (HttpServletResponse) response;  
	  
	        String redirectUrl = null;  
	  
	        String url = request.getRequestURI();  
	  
	        // 非ajax请求  
	        if (url.indexOf("ajax") == -1) {  
	  
	            if (this.isUseForward()) {  
	  
	                if (this.isForceHttps() && "http".equals(request.getScheme())) {  
	                    // First redirect the current request to HTTPS.  
	                    // When that request is received, the forward to the login page will be used.  
	                    redirectUrl = buildHttpsRedirectUrlForRequest(httpRequest);  
	                }  
	  
	                if (redirectUrl == null) {  
	                    String loginForm = determineUrlToUseForThisRequest(httpRequest, httpResponse, authException);  
	  
	                    RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(loginForm);  
	  
	                    dispatcher.forward(request, response);  
	  
	                    return;  
	                }  
	            } else {  
	                // redirect to login page. Use https if forceHttps true  
	  
	                redirectUrl = buildRedirectUrlToLoginPage(httpRequest, httpResponse, authException);  
	  
	            }  
	  
	            redirectStrategy.sendRedirect(httpRequest, httpResponse, redirectUrl);  
	        } else {  
	  
	            ObjectMapper objectMapper = new ObjectMapper();  
	            response.setHeader("Content-Type", "application/json;charset=UTF-8");
	            JsonGenerator jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(response.getOutputStream(),  
	                    JsonEncoding.UTF8);  
	            try {  
	            	Map map = new HashMap();
	            	map.put("status", "timeout");
	                JSONObject jsonData = JSONObject.fromObject(map); 
	                objectMapper.writeValue(jsonGenerator, jsonData); 
	            } catch (JsonProcessingException ex) {  
	                throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);  
	            }  
	        }  
	    }  
}
