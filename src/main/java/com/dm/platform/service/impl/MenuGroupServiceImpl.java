package com.dm.platform.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.MenuGroup;
import com.dm.platform.service.MenuGroupService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class MenuGroupServiceImpl implements MenuGroupService {
	
	@Resource
	private CommonDAO commonDAO;
	@Resource
	private SqlSessionTemplate sqlSession;
	
	@Override
	public List<MenuGroup> listMenuGroup(int thispage, int pagesize) {
		// TODO Auto-generated method stub
		String hql = "from  MenuGroup ";
		return commonDAO.findByPage(hql, thispage, pagesize);
	}

	@Override
	public void insertMenuGroup(MenuGroup entity) {
		// TODO Auto-generated method stub
		commonDAO.save(entity);
	}

	@Override
	public void updateMenuGroup(MenuGroup entity) {
		// TODO Auto-generated method stub
		commonDAO.update(entity);
	}

	@Override
	public void deleteMenuGroup(MenuGroup entity) {
		// TODO Auto-generated method stub
		commonDAO.delete(entity);
	}

	@Override
	public Long countMenuGrou() {
		// TODO Auto-generated method stub
		String hql = "select count(*) from  MenuGroup ";
		return commonDAO.count(hql);
	}

	@Override
	public MenuGroup findOne(Long id) {
		// TODO Auto-generated method stub
		return commonDAO.findOne(MenuGroup.class, id);
	}

	@Override
	public void refreshService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PageInfo<Map> findMgList(Integer pageNum, Integer pageSize,
			Map argMap) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		List<Map> list = sqlSession
				.selectList("com.dm.platform.model.MenuGroup.findMgList",argMap);
		PageInfo<Map> page = new PageInfo<Map>(list);
		return page;
	}

	@Override
	public List<Map> findAllMgList() {
		// TODO Auto-generated method stub
		List<Map> list = sqlSession
				.selectList("com.dm.platform.model.MenuGroup.findAllMgList");
		return list;
	}

}
