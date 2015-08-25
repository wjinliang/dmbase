package com.dm.cms.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.aspectj.util.FileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dm.cms.model.TemplateEntity;
import com.dm.platform.controller.DefaultController;
import com.dm.platform.dto.FileDto;
import com.dm.platform.model.FileEntity;
import com.dm.platform.service.FileService;
import com.dm.platform.util.CompressFile;
import com.dm.platform.util.ConfigUtil;
/**
 * @author ChenGJ 
 * @version 创建时间：2015-4-19 下午2:17:10
 * 类说明 资源管理controller
 */
@Controller
@RequestMapping("/cms/res")
public class ResourceController extends DefaultController{
	
	@Resource
	FileService fileService;
	
	/**
	 * 资源管理主页
	 * @param model
	 * @return
	 */
	@RequestMapping("/page")
	public ModelAndView page(ModelAndView model) {
		try {
			model.setViewName("/pages/cms/res-manage/page");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}
	
	/**
	 * 返回所有文件目录json
	 * @return
	 */
	@RequestMapping("/resJson")
	@ResponseBody
	public Object resJson() {
		try {
			String rootPath = ConfigUtil.getConfigContent("dm",
					"resourceRootFolder");
			List<FileDto> flist = new ArrayList<FileDto>();
			flist=fileService.getFilesJson(rootPath);
			return flist;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
	/**
	 * 获取某个文件夹下所有文件的json
	 * @return
	 */
	@RequestMapping("/resFolderJson")
	@ResponseBody
	public Object resFolderJson(@RequestParam(value = "rootPath", required = false) String rootPath,
			@RequestParam(value = "rootUrl", required = false) String rootUrl,
			@RequestParam(value = "targetPath", required = false) String targetPath,
			@RequestParam(value = "keyword", required = false) String keyword) {
		try {
			if(StringUtils.isEmpty(rootPath)){
				rootPath = ConfigUtil.getConfigContent("dm",
						"resourceRootFolder");
				
			}
			if(StringUtils.isEmpty(rootUrl)){
				rootUrl = ConfigUtil.getConfigContent("dm",
						"resourceRootFolderUrl");
				
			}
			List<Map> flist = new ArrayList<Map>();
			if(StringUtils.isEmpty(keyword)){
				flist=fileService.getFilesJson(rootPath,rootPath+(targetPath==null?"":targetPath));
			}else{
				flist=fileService.getFilesJson(rootPath,rootPath+(targetPath==null?"":targetPath),keyword);
			}
			Map result = new HashMap();
			result.put("status", "1");
			result.put("currentPath", rootPath+(targetPath==null?"":targetPath));
			result.put("currentUrl", rootUrl+(targetPath==null?"":targetPath));
			result.put("fakeCurrentPath", (targetPath==null?"":targetPath));
			result.put("list", flist);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	/**
	 * 新建文件夹
	 * @param folderName 文件夹名称
	 * @param parentPath 文件夹父节点
	 * @return
	 */
	@RequestMapping("/createFolder")
	@ResponseBody
	public Object createFolder(
			@RequestParam(value = "rootPath", required = false) String rootPath,
			@RequestParam(value = "folderName", required = false) String folderName,
			@RequestParam(value = "parentPath", required = false) String parentPath) {
		try {
			if(StringUtils.isEmpty(rootPath)){
				rootPath = ConfigUtil.getConfigContent("dm",
						"resourceRootFolder");
			}
			parentPath = rootPath + (parentPath==null?"":parentPath);
			String folder = parentPath + "/" + folderName;
			if(fileService.createFolder(folder)){
				return successJson();
			}else{
				return errorJson("创建文件夹异常");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
	@RequestMapping("/renameFolder")
	@ResponseBody
	public Object renameFolder(
			@RequestParam(value = "rootPath", required = false) String rootPath,
			@RequestParam(value = "oldName", required = false) String oldName,
			@RequestParam(value = "newName", required = false) String newName,
			@RequestParam(value = "parentPath", required = false) String parentPath) {
		try {
			if(StringUtils.isEmpty(rootPath)){
				rootPath = ConfigUtil.getConfigContent("dm",
						"resourceRootFolder");
			}
			parentPath = rootPath + (parentPath==null?"":parentPath);
			return fileService.renameFolderOrFile(parentPath, oldName, newName);
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
	@RequestMapping("/deleteFolderOrFile")
	@ResponseBody
	public Object deleteFolderOrFile(
			@RequestParam(value = "rootPath", required = false) String rootPath,
			@RequestParam(value = "folderName", required = false) String folderName,
			@RequestParam(value = "parentPath", required = false) String parentPath) {
		try {
			if(StringUtils.isEmpty(rootPath)){
				rootPath = ConfigUtil.getConfigContent("dm",
						"resourceRootFolder");
			}
			parentPath = rootPath + (parentPath==null?"":parentPath);
			String[] fileNames = folderName.split(","); 
			for (String name : fileNames) {
				String folder = parentPath + "/" + name;
				fileService.deleteFolderOrFile(folder);
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	@RequestMapping("/uploadFile")
	@ResponseBody
	public Object uploadFile(
			@RequestParam(value = "file", required = false) MultipartFile file) {
		try {
			String filePath = ConfigUtil.getConfigContent("dm", "uploadTemp");
			String fileUrl = ConfigUtil.getConfigContent("dm", "uploadTempUrl");
			FileEntity f = fileService.uploadTempFile(filePath, fileUrl, file);
			Map map = new HashMap();
			map.put("status", 1);
			map.put("file_id", f.getId());
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
	@RequestMapping("/saveUpload")
	public @ResponseBody
	Object saveUpload(
			@RequestParam(value = "rootPath", required = false) String rootPath,
			@RequestParam(value = "fileId", required = false) String fileId,
			@RequestParam(value = "filePath", required = false) String filePath) {
		try {
			if (fileId != null) {
				FileEntity f = fileService.findOne(fileId);
				File temp = new File(f.getRealPath());
				if(StringUtils.isEmpty(rootPath)){
					rootPath = ConfigUtil.getConfigContent("dm",
							"resourceRootFolder");
				}
				String targetPath = rootPath+(filePath==null?"":filePath);
				File target = new File(targetPath+"/"+f.getName());
				FileUtil.copyFile(temp, target);
				temp.delete();
				fileService.deleteOne(f);
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
	
	@RequestMapping("/unCompressFile")
	@ResponseBody
	public Object unCompressFile(
			@RequestParam(value = "rootPath", required = false) String rootPath,
			@RequestParam(value = "targetPath", required = false) String targetPath,
			@RequestParam(value = "fileName", required = false) String fileName) {
		try {
			if(StringUtils.isEmpty(rootPath)){
				rootPath = ConfigUtil.getConfigContent("dm",
						"resourceRootFolder");
			}
			targetPath = rootPath+(targetPath==null?"":targetPath);
			if (fileName.toLowerCase().endsWith(".rar")) {
				CompressFile.unRarFile(targetPath+"/"+fileName, targetPath);
			}else if(fileName.toLowerCase().endsWith(".zip")){
				CompressFile.unZipFiles(targetPath+"/"+fileName, targetPath);
			}else{
				return errorJson("文件格式不正确");
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
	@RequestMapping("/compressFile")
	@ResponseBody
	public Object compressFile(
			@RequestParam(value = "rootPath", required = false) String rootPath,
			@RequestParam(value = "targetPath", required = false) String targetPath,
			@RequestParam(value = "fileName", required = false) String fileName) {
		try {
			if(StringUtils.isEmpty(rootPath)){
				rootPath = ConfigUtil.getConfigContent("dm",
						"resourceRootFolder");
			}
			targetPath = rootPath+(targetPath==null?"":targetPath);
			String srcPath = targetPath+"/"+fileName;
			if(fileName.indexOf(".")!=-1){
				fileName = fileName.substring(0,fileName.lastIndexOf("."));
			}
			String tarPath = targetPath+"/"+fileName+".zip";
			CompressFile.compressFile(srcPath, tarPath);
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
}
