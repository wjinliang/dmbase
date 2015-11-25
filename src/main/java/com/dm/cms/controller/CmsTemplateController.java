package com.dm.cms.controller;

import com.dm.cms.model.CmsTemplate;
import com.dm.cms.service.CmsTemplateService;
import com.dm.platform.util.FileUtil;
import com.dm.platform.util.PageConvertUtil;
import com.dm.platform.util.ResponseUtil;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cgj on 2015/11/22.
 */

@Controller @RequestMapping("/cms/template") public class CmsTemplateController {
    @Value("${template.basePath}")
    String templateBasePath;
    @Autowired CmsTemplateService cmsTemplateService;

    @RequestMapping("/page") public String page(ModelAndView model) {
        return "/cms/template/page";
    }

    @RequestMapping("/list") public @ResponseBody Object findSites(
        @RequestParam(value = "pageNum", required = false) Integer pageNum,
        @RequestParam(value = "pageSize", required = false) Integer pageSize,
        @RequestParam(value = "template", required = false) CmsTemplate template,
        @RequestParam(value = "sort", required = false) String sort) {
        PageInfo<CmsTemplate> page =
            cmsTemplateService.findCmsTemplate(pageNum, pageSize, template);
        return PageConvertUtil.grid(page);
    }

    @RequestMapping("/load") public @ResponseBody Object load(
        @RequestParam(value = "templateId", required = true) Integer templateId) {
        CmsTemplate cmsTemplate = cmsTemplateService.findOneById(templateId);
        return cmsTemplate;
    }

    @RequestMapping("/insertOrUpdate") public @ResponseBody Object insertOrUpdate(
        CmsTemplate template) {
        if (template.getId() == null) {
            insert(template);
        } else {
            update(template);
        }
        return ResponseUtil.success();
    }

    private void insert(CmsTemplate template) {
        cmsTemplateService.insertCmsTemplate(template);
    }

    private void update(CmsTemplate template) {
        cmsTemplateService.updateCmsTemplate(template);
    }

    @RequestMapping("/delete") public @ResponseBody Object delete(
        @RequestParam(value = "templateId", required = true) String templateId) {
        if (StringUtils.isNotBlank(templateId)) {
            String[] id = templateId.split(",");
            for (String cmsTemplateId : id) {
                cmsTemplateService.deleteCmsTemplate(Integer.valueOf(cmsTemplateId));
            }
        }
        return ResponseUtil.success();
    }

    @RequestMapping(value = "/upload", method = {RequestMethod.POST}) public @ResponseBody
    Object upload(HttpServletRequest httpServletRequest,@RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            try {
                String realFileName = file.getOriginalFilename();
                if (!realFileName.endsWith(".ftl")) {
                    return ResponseUtil.error("格式必须是.ftl");
                }
                if (file.getSize() > (2 * 1024 * 2014)) {
                    return ResponseUtil.error("附件大小必须小于2M");
                }
                String templateContextPath = httpServletRequest.getSession().getServletContext().getRealPath(templateBasePath);
                String fileName = FileUtil.saveFileFromMultipartFile(file,templateContextPath);
                String finalPath = templateBasePath+"/"+fileName;
                Map map = new HashMap();
                map.put("status",1);
                map.put("path",finalPath);
                return map;
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseUtil.error("异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error();
        }
    }

    @RequestMapping("/selects") public @ResponseBody Object selects() {
        List<CmsTemplate> list = cmsTemplateService.findCmsTemplateList();
        List<Map> selects = new ArrayList<Map>();
        for (CmsTemplate cmsTemplate : list) {
            Map select = new HashMap();
            select.put("value",cmsTemplate.getId());
            select.put("text",cmsTemplate.getTemplateName());
            selects.add(select);
        }
        return selects;
    }
}


