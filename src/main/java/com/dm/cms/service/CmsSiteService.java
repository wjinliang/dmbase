package com.dm.cms.service;

import com.dm.cms.model.CmsSite;
import com.dm.platform.dto.TreeNode;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by cgj on 2015/11/22.
 */
public interface CmsSiteService {
    void insertCmsSite(CmsSite cmsSite);
    void updateCmsSite(CmsSite cmsSite);
    void deleteCmsSite(int cmsSiteId);
    CmsSite findOneById(int cmsSiteId);
    CmsSite findOneByDomain(String domain);
    PageInfo<CmsSite> findCmsSite(Integer pageNum,Integer pageSize,Map argMap);
    List<TreeNode> findCmsSiteTreeNodes();
}
