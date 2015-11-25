package com.dm.cms.controller;

import com.dm.cms.model.CmsSite;
import com.dm.cms.service.CmsSiteService;
import com.dm.platform.controller.ControllerExceptionHandler;
import com.dm.platform.dto.TreeNode;
import com.dm.platform.util.PageConvertUtil;
import com.dm.platform.util.ResponseUtil;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by cgj on 2015/11/22.
 */

@Controller @RequestMapping("/cms/site") public class CmsSiteController {
    @Autowired CmsSiteService cmsSiteService;

    @RequestMapping("/page") public String page(Model model) {
        return "/cms/site/page";
    }

    @RequestMapping("/list") public @ResponseBody Object findSites(
        @RequestParam(value = "pageNum", required = false) Integer pageNum,
        @RequestParam(value = "pageSize", required = false) Integer pageSize,
        @RequestParam(value = "site", required = false) CmsSite site,
        @RequestParam(value = "sort", required = false) String sort) {
        PageInfo<CmsSite> page = cmsSiteService.findCmsSite(pageNum, pageSize, site);
        return PageConvertUtil.grid(page);
    }

    @RequestMapping("/load") public @ResponseBody Object load(
        @RequestParam(value = "siteId", required = true) Integer siteId) {
        CmsSite cmsSite = cmsSiteService.findOneById(siteId);
        return cmsSite;
    }

    @RequestMapping("/insertOrUpdate") public @ResponseBody Object insertOrUpdate(CmsSite site) {
        if (site.getId() == null) {
            insert(site);
        } else {
            update(site);
        }
        return ResponseUtil.success();
    }

    private void insert(CmsSite site) {
        cmsSiteService.insertCmsSite(site);
    }

    private void update(CmsSite site) {
        cmsSiteService.updateCmsSite(site);
    }

    @RequestMapping("/delete") public @ResponseBody Object delete(
        @RequestParam(value = "siteId", required = true) String siteId) {
        if (StringUtils.isNotBlank(siteId)) {
            String[] id = siteId.split(",");
            for (String cmsSiteId : id) {
                cmsSiteService.deleteCmsSite(Integer.valueOf(cmsSiteId));
            }
        }
        return ResponseUtil.success();
    }

    @RequestMapping("/tree") public @ResponseBody Object treeJson() {
        List<TreeNode> treeNodes = cmsSiteService.findCmsSiteTreeNodes();
        return treeNodes;
    }
}


