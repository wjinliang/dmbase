package com.dm.cms.service.impl;

import com.dm.cms.model.CmsContent;
import com.dm.cms.service.CmsContentService;
import com.dm.cms.sqldao.CmsContentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cgj on 2015/11/29.
 */
@Service public class CmsContentServiceImpl implements CmsContentService {
    @Autowired CmsContentMapper cmsContentMapper;

    @Override public void insertCmsContent(CmsContent cmsContent) {
        cmsContentMapper.insertSelective(cmsContent);
    }

    @Override public void updateCmsContent(CmsContent cmsContent) {
        cmsContentMapper.updateByPrimaryKeySelective(cmsContent);
    }

    @Override public CmsContent findOneById(int cmsContentId) {
        return cmsContentMapper.selectByPrimaryKey(cmsContentId);
    }

    @Override public void deleteCmsContentById(int cmsContentId) {
        cmsContentMapper.deleteByPrimaryKey(cmsContentId);
    }

    @Override public PageInfo<CmsContent> findCmsContentByPage(Integer pageNum, Integer pageSize,
        Map argMap) {
        PageHelper.startPage(pageNum, pageSize);
        List<CmsContent> list = cmsContentMapper.selectRecordByArgMap(argMap);
        PageInfo<CmsContent> pageInfo = new PageInfo<CmsContent>(list);
        return pageInfo;
    }

    @Override
    public CmsContent findOneByPortal(String siteDomain, String channelEnName, int cmsContentId) {
        Map argMap = new HashMap();
        argMap.put("domain", siteDomain);
        argMap.put("enName", channelEnName);
        argMap.put("contentId", cmsContentId);
        return cmsContentMapper.selectByParams(argMap);
    }
}
