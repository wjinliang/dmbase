package com.dm.platform.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.UserRole;
import com.dm.platform.service.UserRoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service public class UserRoleServiceImpl implements UserRoleService {

    @Resource private CommonDAO commonDAO;
    @Resource private SqlSessionTemplate sqlSession;

    @Override public List<UserRole> listUserRole(int thispage, int pagesize) {
        // TODO Auto-generated method stub
        String hql = "from  UserRole ";
        return commonDAO.findByPage(hql, thispage, pagesize);
    }

    @Override public void insertUserRole(UserRole entity) {
        // TODO Auto-generated method stub
        commonDAO.save(entity);
    }

    @Override public void updateUserRole(UserRole entity) {
        // TODO Auto-generated method stub
        commonDAO.update(entity);
    }

    @Override public void deleteUserRole(UserRole entity) {
        // TODO Auto-generated method stub
        commonDAO.delete(entity);
    }

    @Override public UserRole findOne(String id) {
        // TODO Auto-generated method stub
        return commonDAO.findOne(UserRole.class, id);
    }

    @Override public UserRole findOneByRoleName(String roleName) {
        return commonDAO.findByPropertyName(UserRole.class, "name", roleName).get(0);
    }

    @Override public Long countUserRole() {
        // TODO Auto-generated method stub
        String hql = "select count(*) from  UserRole ";
        return commonDAO.count(hql);
    }

    @Override public void refreshService() {
        // TODO Auto-generated method stub

    }

    @Override public PageInfo<Map> findRoleList(Integer pageNum, Integer pageSize, Map argMap) {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        PageHelper.startPage(pageNum, pageSize);
        List<Map> list =
            sqlSession.selectList("com.dm.platform.model.UserRole.findRoleList", argMap);
        PageInfo<Map> page = new PageInfo<Map>(list);
        return page;
    }
}
