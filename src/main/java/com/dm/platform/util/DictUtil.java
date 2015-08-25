package com.dm.platform.util;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dm.platform.model.Dict;
import com.dm.platform.model.DictItem;
import com.dm.platform.service.DictService;

public class DictUtil{
	public static ApplicationContext ct = new ClassPathXmlApplicationContext(
			"config/spring/applicationContext.xml");
	public static DictService dictService = (DictService)ct.getBean("dictServiceImpl");
	public static String toName(String dictCode,String itemCode){
		return DictCache.getInstance().getDictItemNameByCode(dictCode, itemCode);
	}
	public static List<DictItem> itemList(String dictCode){
		return DictCache.getInstance().getItemListByDictCode(dictCode);
	}
	public static void refreshDict(String dictId,String dictCode,DictService dictService){
		DictCache.getInstance().refreshDict(dictId, dictCode, dictService);
		DictCache.getInstance().refreshJsonDic(dictService, dictId);
	}
	public static String getJson(String dictId){
		return DictCache.getInstance().getJsonDic(dictService, dictId);
	}
	public static String getJsonByDictCode(String dictCode){
		String dictId = "";
		List<Dict> list = dictService.listDictByDictCode(dictCode);
		if(list.size()>0){
			dictId=list.get(0).getDictId();
			return DictCache.getInstance().getJsonDic(dictService, dictId);
		}else{
			return "";
		}
	}
}