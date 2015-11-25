package com.dm.cms.controller;

import com.dm.cms.service.CmsChannelService;
import com.dm.platform.dto.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by cgj on 2015/11/24.
 */
@Controller @RequestMapping("/cms/channel") public class CmsChannelController {
    @Autowired CmsChannelService cmsChannelService;

    @RequestMapping("/page") public String page(Model model) {
        return "/cms/channel/page";
    }

    @RequestMapping("/tree") public @ResponseBody Object treeJson(
        @RequestParam(value = "siteId", required = true) Integer siteId) {
        List<TreeNode> treeNodes = cmsChannelService.findCmsChannelTreeNodeBySiteId(siteId);
        return treeNodes;
    }
}
