package com.dm.cms.controller;

import com.dm.cms.handler.CmsControllerHandler;
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
        model.addAttribute("site", cmsSite);
        return siteUrl(cmsSite);

    }
}
