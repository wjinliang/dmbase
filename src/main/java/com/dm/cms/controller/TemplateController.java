package com.dm.cms.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.aspectj.util.FileUtil;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dm.cms.model.TemplateEntity;
import com.dm.cms.service.TemplateService;
import com.dm.platform.controller.DefaultController;
import com.dm.platform.dto.FileDto;
import com.dm.platform.model.FileEntity;
import com.dm.platform.service.FileService;
import com.dm.platform.util.ConfigUtil;

@Controller
@RequestMapping("/cms/template")
public class TemplateController extends DefaultController {
	@Resource
	TemplateService templateService;
	@Resource
	FileService fileService;

	@RequestMapping("/page")
	public ModelAndView page(ModelAndView model) {
		try {
			model.setViewName("/pages/cms/template/page");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/list")
	@ResponseBody
	public Object list(
			@RequestParam(value = "pageNum", required = false) Integer thispage,
			@RequestParam(value = "pageSize", required = false) Integer pagesize,
			@RequestParam(value = "templatePath", required = false) String templatePath,
			TemplateEntity searchEntity) throws Exception {
		try {
			if (pagesize == null) {
				pagesize = 10;
			}
			if (thispage == null) {
				thispage = 1;
			}
			List<Order> orders = new ArrayList<Order>();
			orders.add(new Order(Direction.DESC, "templateId"));
			Map<String, Object> map = new HashMap<String, Object>();
			Map argsMap = new HashMap();
			String whereSql = "";
			if (searchEntity.getTemplateName() != null) {
				whereSql += " and t.templateName like :templateName ";
				argsMap.put("templateName",
						"%" + searchEntity.getTemplateName() + "%");
			}
			if (templatePath != null) {
				whereSql += " and t.templatePath like :templatePath ";
				argsMap.put("templatePath", templatePath + "%");
			}
			map = templateService.getItemsMap(argsMap, whereSql, orders,
					thispage - 1, pagesize);
			Map result = new HashMap();
			result.put("data", (List<TemplateEntity>) map.get("items"));
			result.put("total", (Long) map.get("totalcount"));
			Long totalcount = (Long) map.get("totalcount");
			if ((thispage) * pagesize >= totalcount && totalcount > 0) {
				thispage--;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}

	@RequestMapping("/load/{templateId}")
	public @ResponseBody
	Object load(@PathVariable(value = "templateId") Long templateId) {
		try {
			TemplateEntity e = new TemplateEntity();
			if (templateId != null) {
				e = templateService.getItemById(templateId);
			}
			return e;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常！");
		}
	}

	@RequestMapping("/save")
	public @ResponseBody
	Object save(TemplateEntity entity,
			@RequestParam(value = "fileId", required = false) String fileId) {
		try {
			if (entity.getTemplateId() != null
					&& !entity.getTemplateId().equals("")) {
				TemplateEntity e = templateService.getItemById(entity
						.getTemplateId());
				e.setTemplateName(entity.getTemplateName());
				if (fileId != null) {
					FileEntity f = fileService.findOne(fileId);
					File temp = new File(f.getRealPath());
					String filePath = ConfigUtil.getConfigContent("dm",
							"templateFolder");
					File target = new File(filePath + e.getTemplatePath() + "/"
							+ e.getTemplateName() + ".ftl");
					FileUtil.copyFile(temp, target);
					e.setTemplatePath(entity.getTemplatePath());
					temp.delete();
				}
				templateService.updateItem(e);
			} else {
				if (fileId != null) {
					FileEntity f = fileService.findOne(fileId);
					File temp = new File(f.getRealPath());
					String filePath = ConfigUtil.getConfigContent("dm",
							"templateFolder");
					File target = new File(filePath + entity.getTemplatePath()
							+ "/" + entity.getTemplateName() + ".ftl");
					FileUtil.copyFile(temp, target);
					entity.setTemplatePath(entity.getTemplatePath());
					temp.delete();
				}
				templateService.insertItem(entity);
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}

	@RequestMapping("/delete")
	public @ResponseBody
	Object delete(
			@RequestParam(value = "templateId", required = false) String templateId) {
		try {
			if (templateId != null) {
				String[] ids = templateId.split(",");
				for (String str : ids) {
					TemplateEntity t = templateService.getItemById(Long
							.valueOf(str));
					templateService.deleteItemById(Long.valueOf(str));
					String filePath = ConfigUtil.getConfigContent("dm",
							"templateFolder");
					com.dm.platform.util.FileUtil.deleteFile(filePath
							+ t.getTemplatePath() + "/" + t.getTemplateName()
							+ ".ftl");
				}
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/loadList")
	@ResponseBody
	public Object loadList() throws Exception {
		try {
			List<Order> orders = new ArrayList<Order>();
			orders.add(new Order(Direction.DESC, "templateId"));
			Map argsMap = new HashMap();
			List<TemplateEntity> list = templateService.getAllItems(argsMap,
					orders);
			List result = new ArrayList();
			for (TemplateEntity t : list) {
				Map map = new HashMap();
				map.put("text", t.getTemplateName());
				map.put("value", t.getTemplateId());
				result.add(map);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}

	@RequestMapping("/fileJson")
	@ResponseBody
	public Object fileJson() {
		try {
			String rootPath = ConfigUtil.getConfigContent("dm",
					"templateFolder");
			List<FileDto> flist = new ArrayList<FileDto>();
			flist = fileService.getFilesJson(rootPath);
			return flist;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}

	@RequestMapping("/createFolder")
	@ResponseBody
	public Object createFolder(
			@RequestParam(value = "folderName", required = false) String folderName,
			@RequestParam(value = "parentPath", required = false) String parentPath) {
		try {
			String filePath = ConfigUtil.getConfigContent("dm",
					"templateFolder");
			parentPath = filePath + parentPath;
			String folder = parentPath + "/" + folderName;
			if (fileService.createFolder(folder)) {
				return successJson();
			} else {
				return errorJson("创建文件夹异常");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}

	@RequestMapping("/deleteFolderOrFile")
	@ResponseBody
	public Object deleteFolderOrFile(
			@RequestParam(value = "folderName", required = false) String folderName,
			@RequestParam(value = "parentPath", required = false) String parentPath) {
		try {
			String filePath = ConfigUtil.getConfigContent("dm",
					"templateFolder");
			String tempaltePath = parentPath;
			parentPath = filePath + parentPath;
			String folder = parentPath + "/" + folderName;
			File file = new File(folder);
			if (file.isDirectory()) {
				deleteDir(file);
			} else {
				file.delete();
				String templateName = folderName.substring(0,
						folderName.lastIndexOf("."));
				TemplateEntity t = templateService.getItemByPathAndName(
						tempaltePath, templateName);
				if (t != null) {
					templateService.deleteItemById(t.getTemplateId());
				}
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}

	private boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				File file = new File(dir, children[i]);
				boolean success = true;
				if (file.isDirectory()) {
					success = deleteDir(file);
				} else {
					success = file.delete();
					String parentPath = "";
					String fileName = file.getName();
					String filePath = ConfigUtil.getConfigContent("dm",
							"templateFolder");
					parentPath = file.getAbsolutePath().replace("\\", "/")
							.replace(filePath, "").replace("/" + fileName, "");
					TemplateEntity t = templateService.getItemByPathAndName(
							parentPath,
							fileName.substring(0, fileName.lastIndexOf(".")));
					if (t != null) {
						templateService.deleteItemById(t.getTemplateId());
					}
				}

				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	@RequestMapping("/uploadTpl")
	@ResponseBody
	public Object uploadTpl(
			@RequestParam(value = "file", required = false) MultipartFile file) {
		try {
			String realfileName = file.getOriginalFilename();
			if (!realfileName.substring(realfileName.lastIndexOf(".")).equals(
					".ftl")) {
				return errorJson("格式必须是.ftl");
			}
			if (file.getSize() > (2 * 1024 * 2014)) {
				return errorJson("附件大小必须小于2M");
			}
			FileEntity f = upload(file);
			Map map = new HashMap();
			map.put("status", 1);
			map.put("file_id", f.getId());
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}

	public FileEntity upload(MultipartFile file) throws IOException {
		String id = "";
		String path = ConfigUtil.getConfigContent("dm", "uploadTemp");
		String realfileName = file.getOriginalFilename();
		String fileName = String.valueOf(System.currentTimeMillis())
				+ realfileName.substring(realfileName.lastIndexOf("."));
		File targetFile = new File(path);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		SaveFileFromInputStream(file.getInputStream(), path, fileName);
		String url = ConfigUtil.getConfigContent("dm", "uploadTempUrl") + "/"
				+ fileName;
		id = String.valueOf(System.currentTimeMillis());
		fileService.insertFile(id, url, String.valueOf(file.getSize()),
				fileName, file.getContentType(), path + "/" + fileName, "1");
		FileEntity resultFile = fileService.findOne(id);
		return resultFile;
	}

	private void SaveFileFromInputStream(InputStream stream, String path,
			String filename) throws IOException {
		FileOutputStream fs = new FileOutputStream(path + "/" + filename);
		byte[] buffer = new byte[1024 * 1024];
		int byteread = 0;
		while ((byteread = stream.read(buffer)) != -1) {
			fs.write(buffer, 0, byteread);
			fs.flush();
		}
		fs.close();
		stream.close();
	}

	@RequestMapping("/loadFtl/{templateId}")
	@ResponseBody
	public Object loadFtl(@PathVariable(value = "templateId") Long templateId) {
		try {
			TemplateEntity t = templateService.getItemById(templateId);
			String folderPath = ConfigUtil.getConfigContent("dm",
					"templateFolder");
			String filePath = folderPath + t.getTemplatePath() + "/"
					+ t.getTemplateName() + ".ftl";
			File file = new File(filePath);
			StringBuffer sb = new StringBuffer();
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), "utf8");
				BufferedReader br = new BufferedReader(read);
				String lineTXT = null;
				while ((lineTXT = br.readLine()) != null) {
					sb.append(lineTXT);
					sb.append("\n ");
				}
				read.close();
			}
			Map map = new HashMap();
			map.put("templateId", templateId);
			map.put("content", sb);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}

	@RequestMapping("/saveFtl")
	@ResponseBody
	public Object saveFtl(
			@RequestParam(value = "templateId", required = false) Long templateId,
			@RequestParam(value = "content", required = false) String content) {
		try {
			TemplateEntity t = templateService.getItemById(templateId);
			String folderPath = ConfigUtil.getConfigContent("dm",
					"templateFolder");
			String filePath = folderPath + t.getTemplatePath() + "/"
					+ t.getTemplateName() + ".ftl";
			File oldFile = new File(filePath);
			oldFile.delete();
			FileOutputStream o = null;
			o = new FileOutputStream(filePath);
			o.write(content.getBytes("utf8"));
			o.close();
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
}
