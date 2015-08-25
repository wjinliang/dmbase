package com.dm.platform.service;

import com.dm.platform.model.UserAttrEntity;

public interface UserAttrService {
	public void insert(UserAttrEntity entity);
	public void update(UserAttrEntity entity);
	public UserAttrEntity findOne(String userId);
}
