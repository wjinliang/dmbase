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
import com.dm.platform.model.NoticeEntity;
import com.dm.platform.service.NoticeService;

@Service
public class NoticeServiceImpl  implements NoticeService{
	
	@Resource
	CommonDAO commonDAO;
	
	@Override
	public void insert(NoticeEntity entity) {
		// TODO Auto-generated method stub
		commonDAO.save(entity);
	}

	@Override
	public void update(NoticeEntity entity) {
		// TODO Auto-generated method stub
		commonDAO.update(entity);
	}

	@Override
	public NoticeEntity findOne(String noticeId) {
		// TODO Auto-generated method stub
		return commonDAO.findOne(NoticeEntity.class, noticeId);
	}

	@Override
	public void deleteOne(String noticeId) {
		// TODO Auto-generated method stub
		commonDAO.deleteById(NoticeEntity.class, noticeId);
	}

	@Override
	public void setTop(String noticeId) {
		// TODO Auto-generated method stub
		NoticeEntity entity = findOne(noticeId);
		if(entity!=null)
		{
			entity.setIsTop(1);
			update(entity);
		}
	}

	@Override
	public void cancelTop(String noticeId) {
		// TODO Auto-generated method stub
		NoticeEntity entity = findOne(noticeId);
		if(entity!=null)
		{
			entity.setIsTop(0);
			update(entity);
		}
	}

	@Override
	public Map<String, Object> getAll(Integer thispage, Integer pagesize) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		String queryString = "from NoticeEntity t where 1=1 ";
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.DESC, "t.isTop"));
		orders.add(new Order(Direction.DESC, "t.noticeDate"));
		List<NoticeEntity> list = commonDAO.findByPage(queryString, thispage, pagesize,orders);
		map.put("items", list);
		String queryString2 = "select count(*) from NoticeEntity t";
		Long totalcount = commonDAO.count(queryString2);
		map.put("totalcount", totalcount);
		return map;
		
	}

}
