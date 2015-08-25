package com.dm.platform.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.Org;
import com.dm.platform.service.OrgService;

@Service
public class OrgServiceImpl implements OrgService {
	
	@Resource
	private CommonDAO commonDAO;
	
	@Override
	public List<Org> listOrg(int thispage,int pagesize) {
		// TODO Auto-generated method stub
		String hql = "from  Org ";
		return commonDAO.findByPage(hql, thispage, pagesize);
	}

	@Override
	public void insertOrg(Org entity) {
		// TODO Auto-generated method stub
		commonDAO.save(entity);
	}

	@Override
	public void updateOrg(Org entity) {
		// TODO Auto-generated method stub
		commonDAO.update(entity);
	}

	@Override
	public void deleteOrg(Org entity) {
		// TODO Auto-generated method stub
		commonDAO.delete(entity);
	}

	@Override
	public Long countMenuGrou() {
		String hql = "select count(*) from  Org ";
		return commonDAO.count(hql);
	}

	@Override
	public Org findOne(Long id) {
		// TODO Auto-generated method stub
		return commonDAO.findOne(Org.class, id);
	}
}
