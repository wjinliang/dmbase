package com.dm.platform.service;

import java.util.List;

import com.dm.platform.model.Org;

public interface OrgService {
	public List<Org> listOrg(int thispage,int pagesize);
	public Org findOne(Long id);
	public void insertOrg(Org entity);
	public void updateOrg(Org entity);
	public void deleteOrg(Org entity);
	public Long countMenuGrou();
}
