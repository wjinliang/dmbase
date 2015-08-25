package com.dm.platform.service;

import java.util.List;

import com.dm.platform.model.Dict;
import com.dm.platform.model.DictItem;


public interface DictService {
	public List<Dict> listDict(int thispage, int pagesize,Dict entity,String whereSql);
	public List<Dict> listDictByDictCode(String dicCode); 
	public Long countDict();
	public Dict findOne(String Id);
	public void deleteOne(Dict entity);
	public void insertDict(Dict entity);
	public void updateDict(Dict entity);
	
	
	public Long countDictItem() ;
	public DictItem findOneItem(String Id) ;
	public void deleteOneItem(DictItem entity);
	public void insertDictItem(DictItem entity) ;
	public void updateDictItem(DictItem entity) ;
	public List<DictItem> listDictItemByDictId(String dictId);
	public List<DictItem> listDictItemByPid(String itemId);
	public List<Dict> listEnableDict();
}
