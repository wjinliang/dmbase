package com.dm.cms.handler;

import com.dm.cms.model.CmsSite;
import com.dm.cms.model.CmsTemplate;
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

}
