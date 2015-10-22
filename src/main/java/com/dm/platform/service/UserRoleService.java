package com.dm.platform.service;

import java.util.List;
import java.util.Map;

import com.dm.platform.model.UserRole;
import com.github.pagehelper.PageInfo;

public interface UserRoleService {

    public List<UserRole> listUserRole(int thispage, int pagesize);

    public void insertUserRole(UserRole entity);

    public void updateUserRole(UserRole entity);

    public void deleteUserRole(UserRole entity);

    public UserRole findOne(String id);

    public UserRole findOneByRoleName(String roleName);

    public Long countUserRole();

    public void refreshService();

    /**
     * ibatics查询角色list
     *
     * @param pageNum  当前页码
     * @param pageSize 一页条数
     * @param argMap   查询参数集合
     * @return
     */
    public PageInfo<Map> findRoleList(Integer pageNum, Integer pageSize, Map argMap);
}
