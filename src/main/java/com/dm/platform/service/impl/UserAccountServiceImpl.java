package com.dm.platform.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.UserAccount;
import com.dm.platform.service.UserAccountService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class UserAccountServiceImpl implements UserAccountService {

	@Resource
	private CommonDAO commonDAO;
	@Resource
	private SqlSessionTemplate sqlSession;

	@Override
	public List<UserAccount> listUserAccount(String orgid, String orgids,
			int thispage, int pagesize) {
		// TODO Auto-generated method stub
		String hql = "select ua from UserAccount ua where 1=1";
		if (orgid != null && !orgid.equals("")) {
			if (orgids != null && !orgids.equals("")) {
				hql += " and ua.org.id in (" + orgids + ")";
			}
		} else {
			if (orgids != null && !orgids.equals("")) {
				hql += " and (ua.org.id in (" + orgids + ") or ua.org is null)";
			}
		}
		return commonDAO.findByPage(hql, thispage, pagesize);
	}

	@Override
	public List<UserAccount> listUserByRoleId(String roleId, boolean mode) {
		// TODO Auto-generated method stub
		String hql = "";
		if (mode) {
			hql = "select user from UserAccount user,UserRole role where user in elements(role.users) and role.code=:roleId order by user.code asc";
		} else {
			hql = "select user from UserAccount user,UserRole role where user not in elements(role.users) and role.code=:roleId  order by user.code asc";
		}
		Map argsMap = new HashMap();
		argsMap.put("roleId", roleId);
		return commonDAO.findByMap(hql, argsMap);
	}

	@Override
	public void insertUser(UserAccount entity) {
		// TODO Auto-generated method stub
		commonDAO.save(entity);
	}

	@Override
	public void updateUser(UserAccount entity) {
		// TODO Auto-generated method stub
		commonDAO.update(entity);
	}

	@Override
	public void deleteUser(UserAccount entity) {
		// TODO Auto-generated method stub
		commonDAO.delete(entity);
	}

	@Override
	public Long countUserAccount(String orgid, String orgids) {
		// TODO Auto-generated method stub
		String hql = "select count(*) from UserAccount ua where 1=1";
		if (orgid != null && !orgid.equals("")) {
			if (orgids != null && !orgids.equals("")) {
				hql += " and ua.org.id in (" + orgids + ")";
			}
		} else {
			if (orgids != null && !orgids.equals("")) {
				hql += " and (ua.org.id in (" + orgids + ") or ua.org is null)";
			}
		}
		return commonDAO.count(hql);
	}

	@Override
	public UserAccount findOne(String code) {
		// TODO Auto-generated method stub
		return commonDAO.findOne(UserAccount.class, code);
	}

	@Override
	public UserAccount findByEmail(String email) {
		// TODO Auto-generated method stub
		if (commonDAO.findByPropertyName(UserAccount.class, "email", email)
				.size() > 0) {
			UserAccount entity = commonDAO.findByPropertyName(
					UserAccount.class, "email", email).get(0);
			return entity;
		} else {
			return null;
		}
	}

	@Override
	public UserAccount findByLoginName(String loginName) {
		// TODO Auto-generated method stub
		if (commonDAO.findByPropertyName(UserAccount.class, "loginname",
				loginName).size() > 0) {
			UserAccount ua = (UserAccount) commonDAO.findByPropertyName(
					UserAccount.class, "loginname", loginName).get(0);
			return ua;
		} else {
			return null;
		}
	}

	@Override
	public Long countByOrgIds(String sql, String orgids, Object[] objects) {
		// TODO Auto-generated method stub
		return commonDAO.count(sql, orgids, objects);
	}

	@Override
	public Long countByOrgIds(String sql, Map argsMap) {
		// TODO Auto-generated method stub
		return commonDAO.count(sql, argsMap);
	}

	@Override
	public List<UserAccount> listAllUser() {
		// TODO Auto-generated method stub
		return commonDAO.findAll(UserAccount.class);
	}

	@Override
	public void refreshService() {
		// TODO Auto-generated method stub

	}

	@Override
	public PageInfo<Map> findUserList(Integer pageNum,Integer pageSize,Map argMap) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		List<Map> list = sqlSession
				.selectList("com.orange.platform.model.UserAccount.findUserList",argMap);
		PageInfo<Map> page = new PageInfo<Map>(list);
		return page;
	}

	@Override
	public Long countUserForCheck(Map argMap) {
		// TODO Auto-generated method stub
		Long count = sqlSession.selectOne("com.orange.platform.model.UserAccount.countUserForUnique",argMap);
		return count;
	}

	@Override
	public List<Map> findAllUserList() {
		// TODO Auto-generated method stub
		List<Map> list = sqlSession
				.selectList("com.orange.platform.model.UserAccount.findAllUserList");
		return list;
	}
	
	@Override
	public String getUserHeadPic(String userId) {
		// TODO Auto-generated method stub
		Map argMap = new HashMap();
		argMap.put("userId", userId);
		String headPic = sqlSession
				.selectOne("com.orange.platform.model.UserAccount.getUserHeadPic",argMap);
		return headPic;
	}
	
}
