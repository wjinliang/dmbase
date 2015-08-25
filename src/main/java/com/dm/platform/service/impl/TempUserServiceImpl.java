package com.dm.platform.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.TempUser;
import com.dm.platform.model.UserAccount;
import com.dm.platform.service.TempUserService;

@Service
public class TempUserServiceImpl implements TempUserService{
	
	@Resource
	CommonDAO commonDAO;
	
	@Override
	public TempUser getTempUserById(String tempUserId) {
		// TODO Auto-generated method stub
		TempUser entity = commonDAO.findOne(TempUser.class, tempUserId);
		return entity;
	}

	@Override
	public UserAccount copyToUserAccount(TempUser entity) {
		// TODO Auto-generated method stub
		UserAccount user = new UserAccount();
		user.setName(entity.getName());
		user.setLoginname(entity.getLoginname());
		user.setPassword(entity.getPassword());
		user.setEmail(entity.getEmail());
		user.setMobile(entity.getMobile());
		return user;
	}

	@Override
	public void deleteTempUserById(String tempUserId) {
		// TODO Auto-generated method stub
		TempUser entity = commonDAO.findOne(TempUser.class, tempUserId);
		entity.setStatus("-1");
		entity.setPassword("");
		commonDAO.update(entity);
	}

	@Override
	public void insert(TempUser entity) {
		// TODO Auto-generated method stub
		commonDAO.save(entity);
	}

	@Override
	public void update(TempUser entity) {
		// TODO Auto-generated method stub
		commonDAO.update(entity);
	}

}
