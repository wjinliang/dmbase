package com.dm.platform.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.FileEntity;
import com.dm.platform.service.FileService;
import com.dm.platform.util.ConfigUtil;

@Controller @RequestMapping("/file") public class FileController extends DefaultController {

    @Resource FileService fileService;

    @Resource CommonDAO commonDAO;

    @Value("${imagePath}") String imagePath;
    @Value("${imageUrl}") String imageUrl;
    @Value("${filePath}") String filePath;
    @Value("${fileUrl}") String fileUrl;

    @RequestMapping("/list") public ModelAndView list(ModelAndView model, FileEntity file,
        @RequestParam(value = "thispage", required = false) Integer thispage,
        @RequestParam(value = "pagesize", required = false) Integer pagesize) {
        try {
            if (pagesize == null) {
                pagesize = 20;
            }
            if (thispage == null) {
                thispage = 0;
            }
            Long totalcount = fileService.countFile(file);
            if ((thispage) * pagesize >= totalcount && totalcount > 0) {
                thispage--;
            }
            model.addObject("files", fileService.listFileEntity(file, thispage, pagesize));
            model.setViewName("/pages/admin/file/list");
            return Model(model, thispage, pagesize, totalcount);
        } catch (Exception e) {
            e.printStackTrace();
            return Error(e);
        }
    }

    @RequestMapping("/download/{fileid}")
    public void downloadFile(@PathVariable String fileid, HttpServletResponse response)
        throws UnsupportedEncodingException {
        FileEntity f = new FileEntity();
        f = commonDAO.findOne(FileEntity.class, fileid);
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        String fileName = f.getRealPath().substring(f.getRealPath().lastIndexOf("/") + 1);
        response.setHeader("Content-Disposition",
            "attachment;fileName=" + new String(fileName.getBytes("gbk"), "iso-8859-1"));
        try {
            File file = new File(f.getRealPath());
            InputStream inputStream = new FileInputStream(file);
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[1024];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/delete") public void delete(HttpServletResponse response,
        @RequestParam(value = "fileid", required = false) String fileid) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            if (fileid != null) {
                String[] ids = fileid.split(",");
                for (String str : ids) {
                    FileEntity f = new FileEntity();
                    f = fileService.findOne(str);
                    if (f.getUserObject() != null && !f.getUserObject().equals("")) {
                        // 去掉关联
                        commonDAO.execute(
                            "update " + f.getUserObject() + " g set g." + f.getUrlField()
                                + "= null,g." + f.getObjField() + " = null where g." + f
                                .getObjField() + ".id='" + str + "'");
                        // 逻辑删除
                    }
                    fileService.deleteOne(f);
                }
                out.write("ok");
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write("error");
            out.flush();
            out.close();
        }
    }

    @RequestMapping("/deleteReal") public void deleteReal(HttpServletResponse response,
        @RequestParam(value = "fileid", required = false) String fileid) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            if (fileid != null) {
                String[] ids = fileid.split(",");
                for (String str : ids) {
                    FileEntity f = new FileEntity();
                    f = fileService.findOne(str);
                    if (f.getUserObject() != null && !f.getUserObject().equals("")) {
                        // 去掉关联
                        commonDAO.execute(
                            "update " + f.getUserObject() + " g set g." + f.getUrlField()
                                + "= null,g." + f.getObjField() + " = null where g." + f
                                .getObjField() + ".id='" + str + "'");
                        // 逻辑删除
                    }
                    fileService.deleteReal(f);
                }
                out.write("ok");
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write("error");
            out.flush();
            out.close();
        }
    }


    @SuppressWarnings({"unchecked", "rawtypes"}) @RequestMapping(value = "/upload")
    public void upload(HttpServletResponse response, HttpServletRequest httpServletRequest,
        @RequestParam(value = "files[]", required = false) MultipartFile[] file) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String path = httpServletRequest.getSession().getServletContext().getRealPath(filePath);
            if (file.length > 0) {
                for (MultipartFile multipartFile : file) {
                    String id = "";
                    String realfileName = multipartFile.getOriginalFilename();
                    String fileName = String.valueOf(System.currentTimeMillis()) + realfileName
                        .substring(realfileName.lastIndexOf("."));
                    File targetFile = new File(path);
                    if (!targetFile.exists()) {
                        targetFile.mkdirs();
                    }
                    SaveFileFromInputStream(multipartFile.getInputStream(), path, fileName);
                    String url = fileUrl + "/" + fileName;
                    id = String.valueOf(System.currentTimeMillis());
                    fileService
                        .insertFile(id, url, String.valueOf(multipartFile.getSize()), realfileName,
                            multipartFile.getContentType(), path + "/" + fileName, "0");
                    Map map = new HashMap();
                    map.put("fileUrl", url);
                    map.put("id", id);
                    JSONObject jsonlist = JSONObject.fromObject(map);
                    out.write(jsonlist.toString());
                    out.flush();
                    out.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.write("error");
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "/uploadImage", method = {RequestMethod.POST})
    public void uploadImage(HttpServletResponse response, HttpServletRequest httpServletRequest,
        @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            if (file == null || file.isEmpty()) {
                JSONObject resultJson = JSONObject.fromObject(errorJson("请选择文件！"));
                out.write(resultJson.toString());
                out.flush();
                out.close();
                return;
            }
            String id = "";
            String path = httpServletRequest.getSession().getServletContext().getRealPath(imagePath);
            String realfileName = file.getOriginalFilename();
            String fileName = String.valueOf(System.currentTimeMillis()) + realfileName
                .substring(realfileName.lastIndexOf("."));
            File targetFile = new File(path);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            SaveFileFromInputStream(file.getInputStream(), path, fileName);
            String url = imageUrl + "/" + fileName;
            id = String.valueOf(System.currentTimeMillis());
            fileService.insertFile(id, url, String.valueOf(file.getSize()), realfileName,
                file.getContentType(), path + "/" + fileName, "0");
            Map result = new HashMap();
            result.put("status", "1");
            result.put("result", id);
            result.put("fileId", id);
            result.put("fileUrl", url);
            JSONObject resultJson = JSONObject.fromObject(result);
            out.write(resultJson.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject resultJson = JSONObject.fromObject(errorJson("请选择文件！"));
            out.write(resultJson.toString());
            out.flush();
            out.close();
        }
    }

    private void SaveFileFromInputStream(InputStream stream, String path, String filename)
        throws IOException {
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

    @RequestMapping("/fileJson") public @ResponseBody Object fileJson(
        @RequestParam(value = "fileId", required = true) String fileId) {
        try {
            FileEntity f = new FileEntity();
            f = fileService.findOne(fileId);
            Map result = new HashMap();
            Map data = new HashMap();
            data.put("id", f.getId());
            data.put("name", f.getName());
            data.put("size", f.getFilesize());
            result.put("result", data);
            result.put("status", "1");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return errorJson("异常！");
        }
    }


    @RequestMapping("/openFile") public @ResponseBody Object openFile(
        @RequestParam(value = "filePath", required = true) String filePath) {
        try {
            File file = new File(filePath);
            StringBuffer sb = new StringBuffer();
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "utf8");
                BufferedReader br = new BufferedReader(read);
                String lineTXT = null;
                while ((lineTXT = br.readLine()) != null) {
                    sb.append(lineTXT);
                    sb.append("\n ");
                }
                read.close();
            }
            Map map = new HashMap();
            map.put("content", sb);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return errorJson("异常！");
        }
    }

    @RequestMapping("/updateFile") public @ResponseBody Object updateFile(
        @RequestParam(value = "filePath", required = true) String filePath,
        @RequestParam(value = "content", required = false) String content) {
        try {
            File oldFile = new File(filePath);
            oldFile.delete();
            FileOutputStream o = null;
            o = new FileOutputStream(filePath);
            o.write(content.getBytes("utf8"));
            o.close();
            return successJson();
        } catch (Exception e) {
            e.printStackTrace();
            return errorJson("异常！");
        }
    }

}
