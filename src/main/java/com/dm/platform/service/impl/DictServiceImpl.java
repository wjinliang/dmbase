package com.dm.platform.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.Dict;
import com.dm.platform.model.DictItem;
import com.dm.platform.service.DictService;

@Service

public class DictServiceImpl implements DictService{
	
	@Resource
	private CommonDAO commonDAO;
	
	@Override
	public List<Dict> listDict(int thispage, int pagesize,Dict entity,String whereSql) {
		// TODO Auto-generated method stub
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.ASC, "dictSeq"));
		return commonDAO.findByPage(whereSql,Dict.class,entity,thispage, pagesize,orders);
	}

	@Override
	public Long countDict() {
		// TODO Auto-generated method stub
		String hql = "select count(*) from  Dict ";
		return commonDAO.count(hql);
	}

	@Override
	public Dict findOne(String Id) {
		// TODO Auto-generated method stub
		return commonDAO.findOne(Dict.class, Id);
	}

	@Override
	public void deleteOne(Dict entity) {
		// TODO Auto-generated method stub
		Map<String,String> m=new HashMap();
		m.put("dictId", entity.getDictId());
		commonDAO.deleteBySql(DictItem.class, " AND dictId=:dictId", m);
		commonDAO.delete(entity);
	}

	@Override
	public void insertDict(Dict entity) {
		// TODO Auto-generated method stub
		commonDAO.save(entity);
	}

	@Override
	public void updateDict(Dict entity) {
		// TODO Auto-generated method stub
		commonDAO.update(entity);
	}

	@Override
	public List<Dict> listDictByDictCode(String dictCode) {
		// TODO Auto-generated method stub
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.ASC, "dictSeq"));
		return commonDAO.findByPropertyName(Dict.class, "dictCode", dictCode,orders);
	}
	
	
	
	@Override
	public Long countDictItem() {
		// TODO Auto-generated method stub
		String hql = "select count(*) from  DictItem ";
		return commonDAO.count(hql);
	}

	@Override
	public DictItem findOneItem(String Id) {
		// TODO Auto-generated method stub
		return commonDAO.findOne(DictItem.class, Id);
	}

	@Override
	public void deleteOneItem(DictItem entity) {
		// TODO Auto-generated method stub
		commonDAO.delete(entity);
	}

	@Override
	public void insertDictItem(DictItem entity) {
		// TODO Auto-generated method stub
		commonDAO.save(entity);
	}

	@Override
	public void updateDictItem(DictItem entity) {
		// TODO Auto-generated method stub
		commonDAO.update(entity);
	}

	@Override
	public List<DictItem> listDictItemByDictId(String dictId) {
		// TODO Auto-generated method stub
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.ASC, "itemSeq"));
		return commonDAO.findByPropertyName(DictItem.class, "dictId", dictId,orders);
	}
	
	@Override
	public List<DictItem> listDictItemByPid(String itemId) {
		// TODO Auto-generated method stub
		return commonDAO.findByPropertyName(DictItem.class, "itemPid", itemId);
	}
	
	@Override
	public List<Dict> listEnableDict() {
		// TODO Auto-generated method stub
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.ASC, "dictSeq"));
		return commonDAO.findByPropertyName(Dict.class, "dictStatus", Long.valueOf(1));
	}
	
	
}
