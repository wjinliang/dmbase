package com.dm.cms.controller;

import com.dm.cms.model.CmsSite;
import com.dm.cms.service.CmsSiteService;
import com.dm.platform.dto.SelectOptionDto;
import com.dm.platform.dto.TreeNode;
import com.dm.platform.util.SqlParam;
import com.dm.platform.util.PageConvertUtil;
import com.dm.platform.util.ResponseUtil;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        @RequestParam(value = "pageSize", required = false) Integer pageSize, CmsSite cmsSite,
        @RequestParam(value = "sort", required = false) String sort) {
        Map map = new SqlParam<CmsSite>().autoParam(cmsSite, sort);
        PageInfo<CmsSite> page = cmsSiteService.findCmsSite(pageNum, pageSize, map);
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

    @RequestMapping("/selectOptions") public @ResponseBody Object selectOptions() {
        List<SelectOptionDto> selectOptionDtos = cmsSiteService.findCmsSiteSelectOption();
        return selectOptionDtos;
    }
}


