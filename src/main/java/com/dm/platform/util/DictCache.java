package com.dm.platform.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dm.platform.model.Dict;
import com.dm.platform.model.DictItem;
import com.dm.platform.service.DictService;

public class DictCache {
	private static final Logger log = LogManager.getLogger(DictCache.class);

	public HashMap<String, HashMap<String, String>> keyNameContainer;
	public HashMap<String, List<DictItem>> dictItemContainer;
	public HashMap<String, String> dictJsonContainer;

	/** 保证单例 */
	static class DictCacheHolder {
		static DictCache instance = new DictCache();
	}

	public static DictCache getInstance() {
		return DictCacheHolder.instance;
	}

	private DictCache() {
		init();
	}

	protected void init() {
		keyNameContainer = new HashMap<String, HashMap<String, String>>();
		dictItemContainer = new HashMap<String, List<DictItem>>();
		dictJsonContainer = new HashMap<String, String>();
	}

	public String getDictItemNameByCode(String dictCode, String itemCode) {
		if (keyNameContainer.get(dictCode) == null)
			return "";
		else {
			return keyNameContainer.get(dictCode).get(itemCode);
		}
	}

	public List<DictItem> getItemListByDictCode(String dictCode) {
		return dictItemContainer.get(dictCode);
	}

	public void refreshDict(String dictId, String dictCode,
			DictService dictService) {
		if (keyNameContainer.get(dictCode) != null)
			keyNameContainer.get(dictCode).clear();
		if (dictItemContainer.get(dictCode) != null)
			dictItemContainer.get(dictCode).clear();
		List<Dict> d = dictService.listEnableDict();
		DictCache dictCache = DictCache.getInstance();
		List<DictItem> itemList = dictService.listDictItemByDictId(dictId);
		HashMap mp = new HashMap<String, String>();
		for (DictItem item : itemList) {
			mp.put(item.getItemCode(), item.getItemName());
		}
		dictCache.keyNameContainer.put(dictCode, mp);
		dictCache.dictItemContainer.put(dictCode, itemList);
		mp = null;
		itemList = null;
	}

	// 刷新某一dictId的json数据
	public void refreshJsonDic(DictService dictService, String dictId) {
		List<DictItem> itemList = dictService.listDictItemByDictId(dictId);
		List<Map<String, Object>> jsonList = new ArrayList<Map<String, Object>>();
		for (DictItem dictItem : itemList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", dictItem.getItemId());
			map.put("itemCode", dictItem.getItemCode());
			map.put("itemName", dictItem.getItemName());
			map.put("pid",
					dictItem.getItemPid() == null ? "0" : dictItem.getItemPid());
			jsonList.add(map);
		}
		JSONArray jsonArray = JSONArray.fromObject(jsonList);
		DictCache dictCache = DictCache.getInstance();
		dictCache.dictJsonContainer.put(dictId, jsonArray.toString());
	}

	public void initAllJsonDic(DictService dictService) {
		List<Dict> d = dictService.listEnableDict();
		for (Dict dict : d) {
			List<DictItem> itemList = dictService.listDictItemByDictId(dict
					.getDictId());
			List<Map<String, Object>> jsonList = new ArrayList<Map<String, Object>>();
			for (DictItem dictItem : itemList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", dictItem.getItemId());
				map.put("itemCode", dictItem.getItemCode());
				map.put("itemName", dictItem.getItemName());
				map.put("pid",
						dictItem.getItemPid() == null ? "0" : dictItem
								.getItemPid());
				jsonList.add(map);
			}
			JSONArray jsonArray = JSONArray.fromObject(jsonList);
			dictJsonContainer.put(dict.getDictId(), jsonArray.toString());
		}
	}

	public String getJsonDic(DictService dictService, String dictId) {
		DictCache dictCache = DictCache.getInstance();
		return dictCache.dictJsonContainer.get(dictId);
	}
}