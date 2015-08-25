package com.dm.platform.service;

import java.util.List;
import java.util.Map;

import com.dm.platform.model.UserAccount;
import com.github.pagehelper.PageInfo;

public interface UserAccountService {
	public List<UserAccount> listUserAccount(String orgid,String orgids,int thispage,int pagesize); 
	//根据角色id获取用户：mode 0 选择该角色用户 1：非该角色用户
	public List<UserAccount> listUserByRoleId(String roleId,boolean mode);
	public List<UserAccount> listAllUser();
	public void insertUser(UserAccount entity);
	public void updateUser(UserAccount entity);
	public void deleteUser(UserAccount entity);
	public Long countUserAccount(String orgid,String orgids);
	public UserAccount findOne(String code);
	public UserAccount findByEmail(String email);
	public UserAccount findByLoginName(String loginName);
	public Long countByOrgIds(String sql,String orgids,Object[] objects);
	public Long countByOrgIds(String sql,Map argsMap);
	public void refreshService();
	
	public PageInfo<Map> findUserList(Integer pageNum,Integer pageSize,Map argMap);
	public List<Map> findAllUserList();
	public Long countUserForCheck(Map argMap);
	public String getUserHeadPic(String userId);
}
