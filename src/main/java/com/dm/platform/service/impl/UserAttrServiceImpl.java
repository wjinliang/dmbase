package com.dm.platform.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.UserAttrEntity;
import com.dm.platform.service.UserAttrService;

@Service
public class UserAttrServiceImpl implements UserAttrService{
	
	@Resource
	CommonDAO commonDAO;

	@Override
	public void insert(UserAttrEntity entity) {
		// TODO Auto-generated method stub
		commonDAO.save(entity);
	}

	@Override
	public void update(UserAttrEntity entity) {
		// TODO Auto-generated method stub
		commonDAO.update(entity);
	}

	@Override
	public UserAttrEntity findOne(String userId) {
		// TODO Auto-generated method stub
		UserAttrEntity entity = commonDAO.findOne(UserAttrEntity.class, userId);
		return entity;
	}
	

}
