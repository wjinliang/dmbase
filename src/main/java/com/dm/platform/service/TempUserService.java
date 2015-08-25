package com.dm.platform.service;

import com.dm.platform.model.TempUser;
import com.dm.platform.model.UserAccount;

public interface TempUserService {
	public TempUser getTempUserById(String tempUserId);
	public UserAccount copyToUserAccount(TempUser entity);
	public void deleteTempUserById(String tempUserId);
	public void insert(TempUser entity);
	public void update(TempUser entity);
}
