package com.dm.cms.service;

import com.dm.cms.model.CmsContent;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * Created by cgj on 2015/11/29.
 */
public interface CmsContentService {
    void insertCmsContent(CmsContent cmsContent);

    void updateCmsContent(CmsContent cmsContent);

    CmsContent findOneById(int cmsContentId);

    void deleteCmsContentById(int cmsContentId);

    PageInfo<CmsContent> findCmsContentByPage(Integer pageNum, Integer pageSize, Map argMap);

    CmsContent findOneByPortal(String siteDomain,String channelEnName,int cmsContentId);
}
