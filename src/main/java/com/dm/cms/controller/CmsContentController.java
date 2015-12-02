package com.dm.cms.controller;

import com.dm.cms.model.CmsChannel;
import com.dm.cms.model.CmsContent;
import com.dm.cms.service.CmsChannelService;
import com.dm.cms.service.CmsContentService;
import com.dm.platform.util.PageConvertUtil;
import com.dm.platform.util.ResponseUtil;
import com.dm.platform.util.SqlParam;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by cgj on 2015/11/29.
 */
@Controller @RequestMapping("/cms/content") public class CmsContentController {
    @Autowired CmsContentService cmsContentService;
    @Autowired CmsChannelService cmsChannelService;

    @RequestMapping("/page") public String page(Model model) {
        return "/cms/content/page";
    }

    @RequestMapping("/list") public @ResponseBody Object list(
        @RequestParam(value = "pageNum", required = false) Integer pageNum,
        @RequestParam(value = "pageSize", required = false) Integer pageSize, CmsContent cmsContent,
        @RequestParam(value = "sort", required = false) String sort) {
        if (cmsContent.getChannelId() == null)
            return PageConvertUtil.emptyGrid();
        Map map = new SqlParam<CmsContent>().autoParam(cmsContent, sort);
        map.put("model", cmsContent);
        PageInfo<CmsContent> page = cmsContentService.findCmsContentByPage(pageNum, pageSize, map);
        return PageConvertUtil.grid(page);
    }

    @RequestMapping("/load") public @ResponseBody Object load(
        @RequestParam(value = "contentId", required = true) Integer contentId) {
        CmsContent cmsContent = cmsContentService.findOneById(contentId);
        return cmsContent;
    }

    @RequestMapping("/insertOrUpdate") public @ResponseBody Object insertOrUpdate(
        CmsContent cmsContent) {
        if (cmsContent.getId() == null) {
            insert(cmsContent);
        } else {
            update(cmsContent);
        }
        return ResponseUtil.success();
    }

    private void insert(CmsContent cmsContent) {
        CmsChannel cmsChannel = cmsChannelService.findOneById(cmsContent.getChannelId());
        cmsContent.setSiteDomain(cmsChannel.getSiteDomain());
        cmsContent.setChannelEnName(cmsChannel.getEnName());
        cmsContentService.insertCmsContent(cmsContent);
    }

    private void update(CmsContent cmsContent) {
        CmsChannel cmsChannel = cmsChannelService.findOneById(cmsContent.getChannelId());
        cmsContent.setSiteDomain(cmsChannel.getSiteDomain());
        cmsContent.setChannelEnName(cmsChannel.getEnName());
        cmsContentService.updateCmsContent(cmsContent);
    }

    @RequestMapping("/delete") public @ResponseBody Object delete(
        @RequestParam(value = "contentId", required = true) String contentId) {
        if (StringUtils.isNotBlank(contentId)) {
            String[] id = contentId.split(",");
            for (String primaryKey : id) {
                cmsContentService.deleteCmsContentById(Integer.valueOf(primaryKey));
            }
        }
        return ResponseUtil.success();
    }
}
