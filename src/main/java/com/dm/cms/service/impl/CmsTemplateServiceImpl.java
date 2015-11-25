package com.dm.cms.service.impl;

import com.dm.cms.model.CmsTemplate;
import com.dm.cms.model.CmsTemplateBlob;
import com.dm.cms.service.CmsTemplateService;
import com.dm.cms.sqldao.CmsTemplateBlobMapper;
import com.dm.cms.sqldao.CmsTemplateMapper;
import com.dm.platform.util.FileUtil;
import com.dm.platform.util.RequestUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cgj on 2015/11/22.
 */
@Service public class CmsTemplateServiceImpl implements CmsTemplateService {
    @Autowired CmsTemplateMapper cmsTemplateMapper;
    @Autowired CmsTemplateBlobMapper cmsTemplateBlobMapper;

    @Override public void insertCmsTemplate(CmsTemplate cmsTemplate) {
        int blobId = insertCmsTemplateBlob(cmsTemplate);
        cmsTemplate.setTemplateBlobId(blobId);
        cmsTemplateMapper.insertSelective(cmsTemplate);

    }

    private int insertCmsTemplateBlob(CmsTemplate cmsTemplate){
        CmsTemplateBlob cmsTemplateBlob = new CmsTemplateBlob();
        String realPath = RequestUtil.getRealPath(cmsTemplate.getTemplatePath());
        byte[] templateBlob = FileUtil.getBytesFromPath(realPath);
        cmsTemplateBlob.setTemplateBlob(templateBlob);
        cmsTemplateBlobMapper.insertSelective(cmsTemplateBlob);
        return cmsTemplateBlob.getId();
    }

    private void updateCmsTemplateBlob(CmsTemplate cmsTemplate){
        CmsTemplateBlob cmsTemplateBlob = new CmsTemplateBlob();
        cmsTemplateBlob.setId(cmsTemplate.getTemplateBlobId());
        String realPath = RequestUtil.getRealPath(cmsTemplate.getTemplatePath());
        byte[] templateBlob = FileUtil.getBytesFromPath(realPath);
        cmsTemplateBlob.setTemplateBlob(templateBlob);
        cmsTemplateBlobMapper.updateByPrimaryKeyWithBLOBs(cmsTemplateBlob);
    }

    @Override public void updateCmsTemplate(CmsTemplate cmsTemplate) {
        CmsTemplate oldCmsTemplate = findOneById(cmsTemplate.getId());
        if(!oldCmsTemplate.getTemplatePath().equals(cmsTemplate.getTemplatePath())){
            updateCmsTemplateBlob(cmsTemplate);
        }
        cmsTemplateMapper.updateByPrimaryKeySelective(cmsTemplate);
    }

    @Override public void deleteCmsTemplate(int cmsTemplateId) {
        CmsTemplate cmsTemplate = findOneById(cmsTemplateId);
        cmsTemplateMapper.deleteByPrimaryKey(cmsTemplateId);
        cmsTemplateBlobMapper.deleteByPrimaryKey(cmsTemplate.getTemplateBlobId());
    }

    @Override public CmsTemplate findOneById(int cmsTemplateId) {
        return cmsTemplateMapper.selectByPrimaryKey(cmsTemplateId);
    }

    @Override public PageInfo<CmsTemplate> findCmsTemplate(Integer pageNum, Integer pageSize,
        CmsTemplate cmsTemplate) {
        Map argMap = new HashMap();
        PageHelper.startPage(pageNum, pageSize);
        List<CmsTemplate> list = cmsTemplateMapper.selectRecordsByArgMap(argMap);
        PageInfo<CmsTemplate> page = new PageInfo<CmsTemplate>(list);
        return page;
    }

    @Override public List<CmsTemplate> findCmsTemplateList() {
        List<CmsTemplate> list = cmsTemplateMapper.selectRecordsByArgMap(null);
        return list;
    }
}
