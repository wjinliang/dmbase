package com.dm.platform.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dm.platform.model.UserAccount;
import com.dm.platform.service.UserAccountService;
import com.dm.platform.service.UserAttrService;
import com.dm.platform.util.DmDateUtil;

@Controller
@RequestMapping("/activeuser")
public class ActiveUserController extends DefaultController {
	@Resource
	UserAccountService userAccountService;
	@Resource
	UserAttrService userAttrService;
	@Resource
	SessionRegistry sessionRegistry;

	@RequestMapping("/page")
	public ModelAndView page(ModelAndView model) {
		try {
			model.setViewName("/admin/activeuser/page");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}
	@RequestMapping("/isOnline")
	@ResponseBody
	public Object isOnline(String userId) {
		Map result = new HashMap();
		boolean flag= false;
		for (Object principal : sessionRegistry.getAllPrincipals()) {
			UserAccount user = (UserAccount) principal;
			if(user.getCode().equals(userId)){
				flag = true;
				List sessionIds = new ArrayList();
				for (SessionInformation session : sessionRegistry.getAllSessions(
						principal, false)) {
					sessionIds.add(session.getSessionId());
				}
				result.put("sessionId", sessionIds);
			}
		}
		if(flag){
			result.put("online", true);
		}else{
			result.put("online", false);
		}
		return result;
	}
}
