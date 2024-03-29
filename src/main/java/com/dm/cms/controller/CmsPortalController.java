package com.dm.cms.controller;

import com.dm.cms.handler.CmsControllerHandler;
import com.dm.cms.model.CmsChannel;
import com.dm.cms.model.CmsContent;
import com.dm.cms.model.CmsSite;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by cgj on 2015/11/23.
 */
@Controller @RequestMapping("/portal") public class CmsPortalController
    extends CmsControllerHandler {

    @RequestMapping("/{domain}")
    public String site(Model model, @PathVariable("domain") String domain) {
        CmsSite cmsSite = cmsSiteService.findOneByDomain(domain);
        if (cmsSite == null) {
            return "404";
        }
        model.addAttribute("basePath", getWholePath());
        model.addAttribute("site", cmsSite);
        return siteUrl(cmsSite);

    }

    @RequestMapping("/{domain}/{enName}")
    public String channel(Model model, @PathVariable("domain") String domain,
        @PathVariable("enName") String enName) {
        model.addAttribute("basePath", getWholePath());
        CmsChannel cmsChannel = cmsChannelService.findOneByPortal(domain, enName);
        if (cmsChannel == null)
            return "404";
        return channelUrl(cmsChannel);

    }

    @RequestMapping("/{domain}/{enName}/{contentId}")
    public String content(Model model, @PathVariable("domain") String domain,
        @PathVariable("enName") String enName, @PathVariable("contentId") Integer contentId) {
        model.addAttribute("basePath", getWholePath());
        CmsContent cmsContent = cmsContentService.findOneByPortal(domain, enName, contentId);
        if (cmsContent == null)
            return "404";
        return contentUrl(cmsContent);

    }
}
