package com.dm.cms.service.impl;

import com.dm.cms.sqldao.CmsSiteMapper;
import com.dm.cms.model.CmsSite;
import com.dm.cms.service.CmsSiteService;
import com.dm.platform.dto.SelectOptionDto;
import com.dm.platform.dto.TreeNode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cgj on 2015/11/22.
 */
@Service public class CmsSiteServiceImpl implements CmsSiteService {
    @Autowired CmsSiteMapper cmsSiteMapper;

    @Override public void insertCmsSite(CmsSite cmsSite) {
        cmsSiteMapper.insertSelective(cmsSite);
    }

    @Override public void updateCmsSite(CmsSite cmsSite) {
        cmsSiteMapper.updateByPrimaryKeySelective(cmsSite);
    }

    @Override public void deleteCmsSite(int cmsSiteId) {
        cmsSiteMapper.deleteByPrimaryKey(cmsSiteId);
    }

    @Override public CmsSite findOneById(int cmsSiteId) {
        return cmsSiteMapper.selectByPrimaryKey(cmsSiteId);
    }

    @Override public CmsSite findOneByDomain(String domain) {
        return cmsSiteMapper.selectByDomain(domain);
    }

    @Override public PageInfo<CmsSite> findCmsSite(Integer pageNum, Integer pageSize, Map argMap) {
        PageHelper.startPage(pageNum, pageSize);
        List<CmsSite> list = cmsSiteMapper.selectRecordsByArgMap(argMap);
        PageInfo<CmsSite> page = new PageInfo<CmsSite>(list);
        return page;
    }

    @Override public List<TreeNode> findCmsSiteTreeNodes() {
        return cmsSiteMapper.selectTreeNodesByArgMap(null);
    }

    @Override public List<SelectOptionDto> findCmsSiteSelectOption() {
        return cmsSiteMapper.selectSelectOptionByArgMap(null);
    }
}
