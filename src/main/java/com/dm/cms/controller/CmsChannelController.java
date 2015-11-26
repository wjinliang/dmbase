package com.dm.cms.controller;

import com.dm.cms.model.CmsChannel;
import com.dm.cms.service.CmsChannelService;
import com.dm.platform.dto.TreeNode;
import com.dm.platform.util.PageConvertUtil;
import com.dm.platform.util.PageUtil;
import com.dm.platform.util.RequestUtil;
import com.dm.platform.util.ResponseUtil;
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

    @RequestMapping("/tree") public @ResponseBody Object treeJsonBySiteId(
        @RequestParam(value = "siteId", required = true) Integer siteId) {
        List<TreeNode> treeNodes = cmsChannelService.findCmsChannelTreeNodeBySiteId(siteId);
        return treeNodes;
    }

    @RequestMapping("/list") public @ResponseBody Object list(
        @RequestParam(value = "pageNum", required = false) Integer pageNum,
        @RequestParam(value = "pageSize", required = false) Integer pageSize,
        CmsChannel cmsChannel) {
        if (cmsChannel.getSiteId() == null && cmsChannel.getPid() == null)
            return PageConvertUtil.emptyGrid();
        return PageConvertUtil
            .grid(cmsChannelService.findCmsChannelByPage(pageNum, pageSize, cmsChannel));
    }

    @RequestMapping("/load") public @ResponseBody Object load(
        @RequestParam(value = "channelId", required = true) Integer channelId) {
        return cmsChannelService.findOneById(channelId);
    }

    @RequestMapping("/insertOrUpdate") public @ResponseBody Object insertOrUpdate(
        CmsChannel cmsChannel) {
        if (cmsChannel.getId() == null)
            insertChannel(cmsChannel);
        updateChannel(cmsChannel);
        return ResponseUtil.success();
    }

    private void insertChannel(CmsChannel cmsChannel) {
        cmsChannelService.insertCmsChannel(cmsChannel);
    }

    private void updateChannel(CmsChannel cmsChannel) {
        cmsChannelService.updateCmsChannel(cmsChannel);
    }
}
