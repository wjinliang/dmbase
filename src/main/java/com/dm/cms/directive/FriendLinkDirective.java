package com.dm.cms.directive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.dm.cms.model.FriendLinkEntity;
import com.dm.cms.service.FriendLinkService;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class FriendLinkDirective implements TemplateDirectiveModel {
	@Autowired
	FriendLinkService friendLinkService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(Environment env, Map params, TemplateModel[] model,  
            TemplateDirectiveBody body) throws TemplateException, IOException {
		// TODO Auto-generated method stub
		
		String type = params.get("type")==null?null:params.get("type").toString();
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.DESC, "seq"));
		Map argsMap = new HashMap();
		String whereSql = "";
		if(type!=null){
			argsMap.put("type", type);
			whereSql+=" and t.type = :type";
		}
		List<FriendLinkEntity> list = friendLinkService.getItems(argsMap, whereSql, orders);
		//传递到页面
		env.setVariable("links",ObjectWrapper.DEFAULT_WRAPPER.wrap(list));
		if (body != null) {      
             body.render(env.getOut());    
        }  
	}

}
