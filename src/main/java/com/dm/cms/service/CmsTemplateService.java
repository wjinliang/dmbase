package com.dm.cms.service;

import com.dm.cms.model.CmsTemplate;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by cgj on 2015/11/22.
 */
public interface CmsTemplateService {
    void insertCmsTemplate(CmsTemplate cmsTemplate);
    void updateCmsTemplate(CmsTemplate cmsTemplate);
    void deleteCmsTemplate(int cmsTemplateId);
    CmsTemplate findOneById(int cmsTemplateId);
    PageInfo<CmsTemplate> findCmsTemplate(Integer pageNum,Integer pageSize,CmsTemplate cmsTemplate);
    List<CmsTemplate> findCmsTemplateList();
}
