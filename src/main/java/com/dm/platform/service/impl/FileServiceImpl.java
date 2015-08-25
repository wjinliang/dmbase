package com.dm.platform.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.tools.ant.util.DateUtils;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.dto.FileDto;
import com.dm.platform.model.FileEntity;
import com.dm.platform.service.FileService;
import com.dm.platform.util.DmDateUtil;
import com.dm.platform.util.FileUtil;
import com.dm.platform.util.UserAccountUtil;

@Service
public class FileServiceImpl implements FileService {

	@Resource
	private CommonDAO commonDAO;

	@Override
	public List<FileEntity> listFileEntity(FileEntity le, int thispage,
			int pagesize) {
		// TODO Auto-generated method stub
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.DESC, "saveFlag"));
		orders.add(new Order(Direction.DESC, "id"));
		String hql = "from  FileEntity f where 1=1";
		if (le.getType() != null && !le.getType().equals("")) {
			hql += " and f.type='" + le.getType() + "'";
		}
		return commonDAO.findByPage(hql, thispage, pagesize, orders);
	}

	@Override
	public Long countFile(FileEntity le) {
		// TODO Auto-generated method stub
		String hql = "select count(*) from  FileEntity f where 1=1";
		if (le.getType() != null && !le.getType().equals("")) {
			hql += " and f.type='" + le.getType() + "'";
		}
		return commonDAO.count(hql);
	}

	@Override
	public FileEntity findOne(String Id) {
		// TODO Auto-generated method stub
		return commonDAO.findOne(FileEntity.class, Id);
	}

	@Override
	public void insertFile(String id, String url, String fileSize,
			String fileName, String fileType, String realPath, String flag) {
		FileEntity f = new FileEntity();
		f.setId(id);
		f.setUrl(url);
		f.setFilesize(fileSize);
		f.setName(fileName);
		f.setType(fileType);
		f.setRealPath(realPath);
		f.setcDate(DmDateUtil.DateToStr(new Date(), "yy-MM-dd HH:mm:ss"));
		f.setSaveFlag(flag);
		f.setcUser(UserAccountUtil.getInstance().getCurrentUserId());
		this.insert(f);
	}

	@Override
	public void deleteOne(FileEntity entity) {
		// TODO Auto-generated method stub
		entity.setSaveFlag("0");
		commonDAO.update(entity);
	}

	@Override
	public void update(FileEntity file) {
		// TODO Auto-generated method stub
		commonDAO.update(file);
	}

	@Override
	public void insert(FileEntity file) {
		// TODO Auto-generated method stub
		commonDAO.save(file);
	}

	@Override
	public boolean deleteReal(FileEntity entity) {
		// TODO Auto-generated method stub
		commonDAO.delete(entity);
		return FileUtil.deleteFileOrFolder(entity.getRealPath());
	}

	@Override
	public List<FileEntity> listByObjId(String objType, String objId,
			String saveFlag, int thispage, int pagesize) {
		// TODO Auto-generated method stub
		FileEntity entity = new FileEntity();
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.DESC, "cDate"));
		String hql = "";
		if (objId != null && !objId.equals("")) {
			hql += " and t.objId=:objId";
			entity.setObjId(objId);
		}
		if (objType != null && !objType.equals("")) {
			hql += " and t.objType=:objType";
			entity.setObjType(objType);
		}
		if (saveFlag != null && !saveFlag.equals("")) {
			hql += " and t.saveFlag=:saveFlag";
			entity.setSaveFlag(saveFlag);
		}
		return commonDAO.findByPage(hql, FileEntity.class, entity, thispage,
				pagesize, orders);
	}

	@Override
	public List<Map> getFilesJson(String rootPath, String filePath) {
		// TODO Auto-generated method stub
		List<Map> flist = new ArrayList<Map>();
		File root = new File(filePath);
		if (!rootPath.equals(filePath)) {
			long i = System.nanoTime();
			Map r = new HashMap();
			r.put("id", i);
			r.put("filePath",
					root.getParentFile().getAbsolutePath().replace("\\", "/")
							.replace(rootPath, ""));
			r.put("fileName", "返回上级");
			r.put("isFolder", true);
			r.put("icon", "icon-folder-open");
			r.put("root", true);
			flist.add(r);
		}
		getFiles(rootPath, filePath, root, flist);
		return flist;
	}

	private void getFiles(String rootPath, String filePath, File root,
			List<Map> flist) {
		File[] files = root.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					long i = System.nanoTime();
					Map f = new HashMap();
					f.put("id", i);
					f.put("filePath", file.getAbsolutePath().replace("\\", "/")
							.replace(rootPath, ""));
					f.put("fileName", file.getName());
					f.put("isFolder", true);
					f.put("icon", "icon-folder-close");
					f.put("root", false);
					f.put("size", file.length());
					f.put("date", DateUtils.format(file.lastModified(), "yyyy-MM-dd HH:mm:ss"));
					flist.add(f);
				} else {
					long i = System.nanoTime();
					Map f = new HashMap();
					f.put("id", i);
					f.put("filePath", file.getAbsolutePath().replace("\\", "/")
							.replace(rootPath, ""));
					f.put("fileName", file.getName());
					f.put("isFolder", false);
					f.put("icon", "icon-file");
					f.put("root", false);
					f.put("size", file.length());
					f.put("date", DateUtils.format(file.lastModified(), "yyyy-MM-dd HH:mm:ss"));
					flist.add(f);
				}
			}
		}
	}

	@Override
	public List<Map> getFilesJson(String rootPath, String filePath,
			String keyword) {
		// TODO Auto-generated method stub
		List<Map> flist = new ArrayList<Map>();
		File root = new File(filePath);
		getFiles(rootPath, root, flist, keyword);
		return flist;
	}

	private void getFiles(String rootPath, File root, List<Map> flist,
			String keyword) {
		File[] files = root.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					if (file.getName().indexOf(keyword) != -1) {
						long i = System.nanoTime();
						Map f = new HashMap();
						f.put("id", i);
						f.put("filePath",
								file.getAbsolutePath().replace("\\", "/")
										.replace(rootPath, ""));
						f.put("fileName", file.getName());
						f.put("isFolder", true);
						f.put("icon", "icon-folder-close");
						f.put("root", false);
						f.put("size", file.length());
						f.put("date", DateUtils.format(file.lastModified(), "yyyy-MM-dd HH:mm:ss"));
						flist.add(f);
					}
					getFiles(rootPath, file, flist, keyword);
				} else {
					if (file.getName().indexOf(keyword) != -1) {
						long i = System.nanoTime();
						Map f = new HashMap();
						f.put("id", i);
						f.put("filePath",
								file.getAbsolutePath().replace("\\", "/")
										.replace(rootPath, ""));
						f.put("fileName", file.getName());
						f.put("isFolder", false);
						f.put("icon", "icon-file");
						f.put("root", false);
						f.put("size", file.length());
						f.put("date", DateUtils.format(file.lastModified(), "yyyy-MM-dd HH:mm:ss"));
						flist.add(f);
					}
				}
			}
		}
	}

	@Override
	public List<FileDto> getFilesJson(String rootPath) {
		// TODO Auto-generated method stub
		Map<String, Long> map = new HashMap<String, Long>();
		List<FileDto> flist = new ArrayList<FileDto>();
		File root = new File(rootPath);
		if(root.listFiles()==null){
			return new ArrayList<FileDto>();
		}
		long i = System.nanoTime();
		map.put(root.getAbsolutePath().replace("\\", "/").replace(rootPath, ""),
				i);
		FileDto r = new FileDto();
		r.setId(i);
		r.setpId(new Long(0));
		r.setFilePath(root.getAbsolutePath().replace("\\", "/")
				.replace(rootPath, ""));
		r.setName(root.getName().replace("\\", "/").replace(rootPath, ""));
		r.setIsFile(false);
		r.setIconSkin("level");
		if (root.listFiles().length > 0) {
			r.setHasChild(true);
			r.setOpen(true);
		} else {
			r.setHasChild(false);
		}
		flist.add(r);
		getFiles(rootPath, flist, root, map);
		return flist;
	}

	private void getFiles(String filePath, List<FileDto> flist, File root,
			Map<String, Long> map) {
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				long i = System.nanoTime();
				map.put(file.getAbsolutePath().replace("\\", "/")
						.replace(filePath, ""), i);
				FileDto f = new FileDto();
				f.setId(i);
				f.setpId(map.get(file.getParent().replace("\\", "/")
						.replace(filePath, "")));
				f.setFilePath(file.getAbsolutePath().replace("\\", "/")
						.replace(filePath, ""));
				f.setName(file.getName());
				f.setIsFile(false);
				f.setIconSkin("level");
				if (file.listFiles().length > 0) {
					f.setHasChild(true);
				} else {
					f.setHasChild(false);
				}
				flist.add(f);
				getFiles(filePath, flist, file, map);
			} else {
				long i = System.nanoTime();
				map.put(file.getAbsolutePath(), i);
				FileDto f = new FileDto();
				f.setId(i);
				f.setpId(map.get(file.getParent().replace("\\", "/")
						.replace(filePath, "")));
				f.setFilePath(file.getAbsolutePath().replace("\\", "/")
						.replace(filePath, ""));
				f.setName(file.getName());
				f.setIsFile(true);
				f.setHasChild(false);
				flist.add(f);
			}
		}
	}

	@Override
	public boolean createFolder(String folderPath) {
		// TODO Auto-generated method stub
		File file = new File(folderPath);
		if (!file.exists()) {
			return file.mkdir();
		} else {
			return true;
		}
	}

	@Override
	public boolean deleteFolderOrFile(String folderPath) {
		// TODO Auto-generated method stub
		return FileUtil.deleteFileOrFolder(folderPath);
	}

	@Override
	public FileEntity uploadTempFile(String filePath, String fileUrl,
			MultipartFile file) throws IOException {
		// TODO Auto-generated method stub
		String id = "";
		String path = filePath;
		String realfileName = file.getOriginalFilename();
		String fileName = String.valueOf(System.currentTimeMillis())
				+ realfileName.substring(realfileName.lastIndexOf("."));
		File targetFile = new File(path);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		SaveFileFromInputStream(file.getInputStream(), path, fileName);
		String url = fileUrl + "/" + fileName;
		id = String.valueOf(System.currentTimeMillis());
		insertFile(id, url, String.valueOf(file.getSize()), realfileName,
				file.getContentType(), path + "/" + fileName, "0");
		FileEntity resultFile = findOne(id);
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

	@Override
	public Map renameFolderOrFile(String path, String oldName, String newName) {
		// TODO Auto-generated method stub
		return FileUtil.renameFile(path, oldName, newName);
	}

}
