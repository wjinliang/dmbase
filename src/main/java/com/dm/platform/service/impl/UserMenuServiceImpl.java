package com.dm.platform.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.UserMenu;
import com.dm.platform.service.UserMenuService;
import com.github.pagehelper.PageInfo;

@Service
public class UserMenuServiceImpl implements UserMenuService {
	
	@Resource
	private CommonDAO commonDAO;
	@Resource
	private SqlSessionTemplate sqlSession;
	
	@Override
	public List<UserMenu> listUserMenu() {
		// TODO Auto-generated method stub
		return (List<UserMenu>)commonDAO.findAll(UserMenu.class);
	}

	@Override
	public void insertUserMenu(UserMenu entity) {
		// TODO Auto-generated method stub
		commonDAO.save(entity);
	}

	@Override
	public void updateUserMenu(UserMenu entity) {
		// TODO Auto-generated method stub
		commonDAO.update(entity);
	}

	@Override
	public void deleteUserMenu(UserMenu entity) {
		// TODO Auto-generated method stub
		commonDAO.delete(entity);
	}

	@Override
	public void refreshService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Map> findAllMenuSimpleList() {
		// TODO Auto-generated method stub
		List<Map> list = sqlSession
				.selectList("com.dm.platform.model.UserMenu.findAllMenuSimpleList");
		return list;
	}

	@Override
	public UserMenu findOne(Object id) {
		// TODO Auto-generated method stub
		UserMenu entity = commonDAO.findOne(UserMenu.class, id);
		return entity;
	}
}
