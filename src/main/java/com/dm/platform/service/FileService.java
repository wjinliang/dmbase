package com.dm.platform.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.dm.platform.dto.FileDto;
import com.dm.platform.model.FileEntity;

public interface FileService {
	public List<FileEntity> listFileEntity(FileEntity le,int thispage,int pagesize);
	public List<FileEntity> listByObjId(String objType,String objId,String saveFlag,int thispage,int pagesize);
	public Long countFile(FileEntity le);
	public FileEntity findOne(String Id);
	public void insertFile(String id,String url,String fileSize,String fileName,String fileType,String realPath,String flag);
	public void insert(FileEntity file);
	public void update(FileEntity file);
	public void deleteOne(FileEntity entity);
	public boolean deleteReal(FileEntity entity);
	
	/**
	 * 根据根目录返回整个目录的list
	 * @param rootPath
	 * @return
	 */
	public List<FileDto> getFilesJson(String rootPath);
	
	/**
	 * 根据根目录返回目录的list
	 * @param rootPath
	 * @param isErgodic 是否遍历
	 * @return
	 */
	public List<Map> getFilesJson(String rootPath,String filePath);
	
	
	public List<Map> getFilesJson(String rootPath,String filePath,String keyword);
	/**
	 * 新建文件夹
	 * @param folderPath
	 * @return
	 */
	public boolean createFolder(String folderPath);
	
	/**
	 * 重命名文件
	 * @param path
	 * @param oldName
	 * @param newName
	 * @return
	 */
	public Map renameFolderOrFile(String path, String oldName, String newName);
	
	/**
	 * 删除文件或文件夹下所有文件
	 * @param folderPath
	 * @return
	 */
	public boolean deleteFolderOrFile(String folderPath);
	
	public FileEntity uploadTempFile(String filePath,String fileUrl,MultipartFile file) throws IOException;
}
