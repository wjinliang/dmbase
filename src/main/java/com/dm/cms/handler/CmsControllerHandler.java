package com.dm.cms.handler;

import com.dm.cms.model.CmsChannel;
import com.dm.cms.model.CmsContent;
import com.dm.cms.model.CmsSite;
import com.dm.cms.model.CmsTemplate;
import com.dm.cms.service.CmsChannelService;
import com.dm.cms.service.CmsContentService;
import com.dm.cms.service.CmsSiteService;
import com.dm.cms.service.CmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by cgj on 2015/11/23.
 */
public class CmsControllerHandler {
    @Autowired protected CmsSiteService cmsSiteService;
    @Autowired protected CmsChannelService cmsChannelService;
    @Autowired protected CmsContentService cmsContentService;
    @Autowired protected CmsTemplateService cmsTemplateService;
    @Value("${template.basePath}") protected String templateBasePath;

    protected String getWholePath() {
        HttpServletRequest request =
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String path = request.getContextPath();
        String basePath =
            request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + path;
        return basePath;
    }

    public String siteUrl(CmsSite cmsSite) {
        if (cmsSite.getIsHtml()) {
            return "redirect:" + getWholePath() + "/html/" + cmsSite.getDomain() + "/index.html";
        } else {
            CmsTemplate cmsTemplate = cmsTemplateService.findOneById(cmsSite.getTemplateId());
            return cmsTemplate.getTemplatePath().replace(".ftl", "");
        }
    }

    public String channelUrl(CmsChannel cmsChannel) {
        if (cmsChannel.getIsHtml()) {
            return "redirect:" + getWholePath() + "/html/" + cmsChannel.getSiteDomain() + "/"
                + cmsChannel.getEnName() + "/index.html";
        } else {
            CmsTemplate cmsTemplate = cmsTemplateService.findOneById(cmsChannel.getTemplateId());
            return cmsTemplate.getTemplatePath().replace(".ftl", "");
        }
    }

    public String contentUrl(CmsContent cmsContent) {
        if (cmsContent.getIsHtml()) {
            return "redirect:" + getWholePath() + "/html/" + cmsContent.getSiteDomain() + "/"
                + cmsContent.getChannelEnName() + "/" + cmsContent.getId() + ".html";
        } else {
            CmsTemplate cmsTemplate = cmsTemplateService.findOneById(cmsContent.getTemplateId());
            return cmsTemplate.getTemplatePath().replace(".ftl", "");
        }
    }


}
