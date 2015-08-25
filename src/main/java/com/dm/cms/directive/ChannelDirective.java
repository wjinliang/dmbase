package com.dm.cms.directive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.StringUtils;

import com.dm.cms.model.ChannelEntity;
import com.dm.cms.service.ChannelService;
import com.dm.platform.util.PageUtil;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class ChannelDirective implements TemplateDirectiveModel {
	@Autowired
	ChannelService channelService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(Environment env, Map params, TemplateModel[] model,  
            TemplateDirectiveBody body) throws TemplateException, IOException {
		// TODO Auto-generated method stub
		
		String channelIdStr = getRequiredParam(params, "channelId");
		String siteIdStr = params.get("siteId")==null?null:params.get("siteId").toString();
		String pageSizeStr = params.get("pageSize")==null?"":params.get("pageSize").toString();
		Integer pageNum = params.get("pageNum")==null?1:Integer.valueOf(params.get("pageNum").toString());
		Long channelId = new Long(channelIdStr);
		ChannelEntity channel = channelService.getItemById(channelId);
		if(siteIdStr!=null){
			if(!channel.getSiteId().toString().equals(siteIdStr)){
				throw new TemplateModelException("频道("+channelIdStr+")不在站点("+siteIdStr+")下，请重新确认！");  
			}
		}
		Integer pageSize;
		if(StringUtils.isEmpty(pageSizeStr)){
			pageSize = channel.getChannelPageSize();
		}else{
			pageSize = Integer.valueOf(pageSizeStr);
		}
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.DESC, "publishTime"));
		Map argsMap = new HashMap();
		String whereSql = " and t.channelId=:channelId";
		argsMap.put("channelId", channelId);
		Map<String, Object> map = channelService.getContents(argsMap, whereSql, orders, pageNum-1, pageSize);
		List<ChannelEntity> list = (List<ChannelEntity>)map.get("items");
		Long total = (Long)map.get("totalcount");
		env.setVariable("pagination",
				ObjectWrapper.DEFAULT_WRAPPER.wrap(PageUtil.getInstance().channelPagination(channel,pageNum,total,pageSize)));
		//传递到页面
		env.setVariable("channel",ObjectWrapper.DEFAULT_WRAPPER.wrap(channel));
		env.setVariable("contents",ObjectWrapper.DEFAULT_WRAPPER.wrap(list));
		if (body != null) {      
             body.render(env.getOut());    
        }  
	}
	static String getRequiredParam(Map params,String key) throws TemplateException {  
        Object value = params.get(key);  
        if(value == null || StringUtils.isEmpty(value.toString())) {  
            throw new TemplateModelException("not found required parameter:"+key+" for directive");  
        }  
        return value.toString();  
    }

}
