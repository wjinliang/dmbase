package com.dm.platform.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dm.example.model.ExampleEntity;
import com.dm.platform.dao.CommonDAO;
import com.dm.platform.dto.UserAccountDto;
import com.dm.platform.model.FileEntity;
import com.dm.platform.model.Org;
import com.dm.platform.model.UserAccount;
import com.dm.platform.model.UserAttrEntity;
import com.dm.platform.model.UserRole;
import com.dm.platform.service.FileService;
import com.dm.platform.service.UserAccountService;
import com.dm.platform.service.UserAttrService;
import com.dm.platform.util.ConfigUtil;
import com.dm.platform.util.MailUtil;
import com.dm.platform.util.UUIDUtils;
import com.dm.platform.util.UserAccountUtil;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/useraccount")
public class UserAccountController extends DefaultController {
	@Resource
	UserAccountService userAccountService;

	@Resource
	UserAttrService userAttrService;

	@Resource
	FileService fileService;

	@Resource
	CommonDAO commonDAO;

	@Resource
	SessionRegistry sessionRegistry;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/list")
	public ModelAndView list(
			ModelAndView model,
			@RequestParam(value = "thispage", required = false) Integer thispage,
			@RequestParam(value = "orgid", required = false) String orgid,
			@RequestParam(value = "orgids", required = false) String orgids,
			@RequestParam(value = "pagesize", required = false) Integer pagesize) {
		try {
			if (pagesize == null) {
				pagesize = 10;
			}
			if (thispage == null) {
				thispage = 0;
			}
			Long totalcount = userAccountService
					.countUserAccount(orgid, orgids);
			if ((thispage) * pagesize >= totalcount && totalcount > 0) {
				thispage--;
			}
			List ml = new ArrayList();
			Map root = new HashMap();
			root.put("id", "");
			root.put("name",
					"全部用户" + "(" + commonDAO.count(UserAccount.class, "", null)
							+ ")");
			root.put("pId", null);
			root.put("open", true);
			ml.add(root);
			List<Order> orders = new ArrayList<Order>();
			orders.add(new Order(Direction.ASC, "seq"));
			List<Org> olist = commonDAO.findAll(Org.class, orders);
			for (Org org : olist) {
				Map m = new HashMap();
				m.put("id", org.getId());
				String ids = UserAccountUtil.getInstance().getDownOrgidsStrs(
						String.valueOf(org.getId()));
				List<Long> orglist = new ArrayList<Long>();
				for (String id : ids.split(",")) {
					orglist.add(new Long(id));
				}
				String sql = "select count(*) from UserAccount u where u.org.id in (:orgids)";
				Map argsMap = new HashMap();
				argsMap.put("orgids", orglist);
				Long count = userAccountService.countByOrgIds(sql, argsMap);
				m.put("name", org.getName() + "(" + count + ")");
				if (org.getParent() != null) {
					m.put("pId", org.getParent().getId());
				} else {
					m.put("pId", "");
				}
				if (org.getChildren().size() != 0) {
					m.put("open", true);
				}
				ml.add(m);
			}
			JSONArray json = JSONArray.fromObject(ml);
			model.addObject("orgStr", json.toString());
			model.addObject("orgid", orgid);
			model.addObject("orgids", orgids);
			model.addObject("useraccounts", userAccountService.listUserAccount(
					orgid, orgids, thispage, pagesize));
			model.setViewName("/pages/admin/useraccount/list");
			return Model(model, thispage, pagesize, totalcount);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	@RequestMapping("/listActiveUsers")
	public ModelAndView listActiveUsers(
			ModelAndView model,
			@RequestParam(value = "thispage", required = false) Integer thispage,
			@RequestParam(value = "pagesize", required = false) Integer pagesize) {
		try {
			if (pagesize == null) {
				pagesize = 20;
			}
			if (thispage == null) {
				thispage = 0;
			}
			List<Map<String, String>> list = UserAccountUtil.getInstance()
					.listActiveUsers(sessionRegistry);
			Long totalcount = Long.valueOf(list.size());
			model.addObject("lusers", list);
			model.setViewName("/pages/admin/useraccount/activity_list");
			return Model(model, thispage, pagesize, totalcount);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	@RequestMapping("/kickUser")
	public void kickUser(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "sessionId", required = false) String sessionId)
			throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			UserAccountUtil.getInstance().kickUser(sessionRegistry, sessionId);
			out.write("ok");
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			out.write("error");
			out.flush();
			out.close();
		}
	}

	@RequestMapping("/form/{mode}")
	public ModelAndView form(
			HttpServletRequest request,
			ModelAndView model,
			@PathVariable String mode,
			@RequestParam(value = "useraccountid", required = false) String useraccountid,
			@RequestParam(value = "orgid", required = false) String orgid) {
		try {
			UserAccount ua = new UserAccount();
			if (mode != null && !mode.equals("new")) {
				if (useraccountid != null) {
					ua = userAccountService.findOne(useraccountid);
					model.addObject("useraccount", ua);
					if (mode.equals("view")) {
						model.setViewName("/pages/admin/useraccount/view");
						return Model(model);
					}
				}
			} else {
				if (orgid != null && !orgid.equals("")) {
					ua.setSeq(commonDAO
							.count("select count(*) from UserAccount u where u.org.id="
									+ orgid) + 1);
				} else {
					ua.setSeq(commonDAO
							.count("select count(*) from UserAccount") + 1);
				}
				ua.setEnabled(true);
				model.addObject("useraccount", ua);
			}
			model.addObject("orgid", orgid);
			model.setViewName("/pages/admin/useraccount/form");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	@RequestMapping("/changepassword")
	public ModelAndView changepassword(
			HttpServletRequest request,
			@RequestParam(value = "useraccountid", required = false) String useraccountid,
			ModelAndView model) {
		try {
			model.addObject("useraccountid", useraccountid);
			model.setViewName("/pages/admin/useraccount/passwordform");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	@RequestMapping("/savepassword")
	public ModelAndView savepassword(
			ModelAndView model,
			@RequestParam(value = "useraccountid", required = false) String useraccountid,
			@RequestParam(value = "oldpassword") String oldpassword,
			@RequestParam(value = "newpassword") String newpassword) {
		try {
			UserAccount ua = new UserAccount();
			ua = userAccountService.findOne(useraccountid);
			ShaPasswordEncoder sha = new ShaPasswordEncoder();
			sha.setEncodeHashAsBase64(false);
			String opsw = sha.encodePassword(oldpassword, null);
			if (ua.getPassword().equals(opsw)) {
				ua.setPassword(sha.encodePassword(newpassword, null));
				userAccountService.updateUser(ua);
				// model.setViewName("/pages/content/success");
				return Redirect(model, getRootPath() + "/infoCenter", "修改密码成功");
			} else {
				return RedirectError(model, getRootPath() + "/infoCenter",
						"修改失败：旧密码不正确！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	

	

	@RequestMapping("/save")
	public ModelAndView save(ModelAndView model, UserAccount useraccount,
			HttpServletRequest request,
			@RequestParam(value = "orgid", required = false) String orgid,
			@RequestParam(value = "imgid", required = false) String imgid) {
		try {
			UserAccount user = new UserAccount();
			if (useraccount.getCode() != null
					&& !useraccount.getCode().equals("")) {
				user = userAccountService.findOne(useraccount.getCode());
				user.setEnabled(useraccount.isEnabled());
				user.setName(useraccount.getName());
				user.setSeq(useraccount.getSeq());
				user.setEmail(useraccount.getEmail());
				user.setMobile(useraccount.getMobile());
				if (imgid != null && !imgid.equals("")) {
					if (user.getHeadphoto() == null) {
						FileEntity f = new FileEntity();
						f = commonDAO.findOne(FileEntity.class, imgid);
						user.setHeadphoto(f);
						user.setHeadpic(f.getUrl());
						f.setSaveFlag("1");
						f.setUserObject("UserAccount");
						f.setObjField("headphoto");
						f.setUrlField("headpic");
						fileService.update(f);
					} else {
						if (!user.getHeadphoto().getId().equals(imgid)) {
							// 将旧文件逻辑删除
							FileEntity oldfile = user.getHeadphoto();
							oldfile.setSaveFlag("0");
							fileService.update(oldfile);
							// 添加新文件
							FileEntity f = new FileEntity();
							f = commonDAO.findOne(FileEntity.class, imgid);
							user.setHeadphoto(f);
							user.setHeadpic(f.getUrl());
							f.setSaveFlag("1");
							f.setUserObject("UserAccount");
							f.setObjField("headphoto");
							f.setUrlField("headpic");
							f.setObjId(useraccount.getCode());
							fileService.update(f);
						}
					}
				}
				userAccountService.updateUser(user);
			} else {
				String code = String.valueOf(System.currentTimeMillis());
				useraccount.setCode(code);
				ShaPasswordEncoder sha = new ShaPasswordEncoder();
				sha.setEncodeHashAsBase64(false);
				useraccount.setPassword(sha.encodePassword(
						useraccount.getPassword(), null));
				useraccount.setNonLocked(true);
				useraccount.setAccountExpired(false);
				useraccount.setPasswordExpired(false);
				if (orgid != null && !orgid.equals("")) {
					Org o = new Org();
					o = commonDAO.findOne(Org.class, Long.valueOf(orgid));
					useraccount.setOrg(o);
				} else {
					useraccount.setOrg(null);
				}
				if (imgid != null && !imgid.equals("")) {
					FileEntity f = new FileEntity();
					f = commonDAO.findOne(FileEntity.class, imgid);
					useraccount.setHeadphoto(f);
					useraccount.setHeadpic(f.getUrl());
					f.setSaveFlag("1");
					f.setUserObject("UserAccount");
					f.setObjField("headphoto");
					f.setUrlField("headpic");
					f.setObjId(code);
					fileService.update(f);
				}
				userAccountService.insertUser(useraccount);
			}
			model.addObject("redirect", getRootPath() + "/useraccount/list");
			model.setViewName("/pages/content/redirect");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	

	@RequestMapping("/infoCenterUpdate")
	public ModelAndView infoCenterUpdate(ModelAndView model,
			UserAccount useraccount, HttpServletRequest request,
			UserAttrEntity userAttr,
			@RequestParam(value = "imgid", required = false) String imgid) {
		UserAccount entity = userAccountService.findOne(useraccount.getCode());
		UserAttrEntity userExt = userAttrService.findOne(useraccount.getCode());
		if (userExt == null) {
			userAttr.setUserId(useraccount.getCode());
			userAttrService.insert(userExt);
		} else {
			userExt.setGender(userAttr.getGender());
			userExt.setIntroduce(userAttr.getIntroduce());
			userExt.setBirthDate(userAttr.getBirthDate());
			userAttrService.update(userExt);
		}
		entity.setName(useraccount.getName());
		entity.setMobile(useraccount.getMobile());
		entity.setEmail(useraccount.getEmail());
		if (imgid != null && !imgid.equals("")) {
			if (entity.getHeadphoto() == null) {
				FileEntity f = new FileEntity();
				f = commonDAO.findOne(FileEntity.class, imgid);
				entity.setHeadphoto(f);
				entity.setHeadpic(f.getUrl());
				f.setSaveFlag("1");
				f.setUserObject("UserAccount");
				f.setObjField("headphoto");
				f.setUrlField("headpic");
				f.setObjId(useraccount.getCode());
				fileService.update(f);
			} else {
				if (!entity.getHeadphoto().getId().equals(imgid)) {
					// 将旧文件逻辑删除
					FileEntity oldfile = entity.getHeadphoto();
					oldfile.setSaveFlag("0");
					fileService.update(oldfile);
					// 添加新文件
					FileEntity f = new FileEntity();
					f = commonDAO.findOne(FileEntity.class, imgid);
					entity.setHeadphoto(f);
					entity.setHeadpic(f.getUrl());
					f.setSaveFlag("1");
					f.setUserObject("UserAccount");
					f.setObjField("headphoto");
					f.setUrlField("headpic");
					f.setObjId(useraccount.getCode());
					fileService.update(f);
				}
			}
		}
		userAccountService.updateUser(entity);
		return Redirect(model, getRootPath() + "/infoCenter", "更新成功！");
	}

	@RequestMapping(value = "/upload", method = { RequestMethod.POST })
	public void upload(HttpServletResponse response,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		try {
			String id = "";
			String path = ConfigUtil.getConfigContent("dm", "imagePath");
			String realfileName = file.getOriginalFilename();
			String fileName = String.valueOf(System.currentTimeMillis())
					+ realfileName.substring(realfileName.lastIndexOf("."));
			File targetFile = new File(path);
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			SaveFileFromInputStream(file.getInputStream(), path, fileName);
			String url = ConfigUtil.getConfigContent("dm", "imageUrl") + "/"
					+ fileName;
			id = String.valueOf(System.currentTimeMillis());
			fileService.insertFile(id, url, String.valueOf(file.getSize()),
					realfileName, file.getContentType(), path + "/" + fileName,
					"0");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.write(url + "," + id);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	

	@RequestMapping("/uploadHeadPic")
	public ModelAndView uploadPic(ModelAndView model,
			@RequestParam(value = "userId", required = true) String userId) {
		try {
			String url = getRootPath() + "/useraccount/saveUploadPic?userId="
					+ userId;
			model.addObject("url", url);
			model.setViewName("/pages/admin/upload/upload_pic");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	public FileEntity upload(MultipartFile file) throws IOException {
		String id = "";
		String path = ConfigUtil.getConfigContent("dm", "imagePath");
		String realfileName = file.getOriginalFilename();
		String fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
		File targetFile = new File(path);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		SaveFileFromInputStream(file.getInputStream(), path, fileName);
		String url = ConfigUtil.getConfigContent("dm", "imageUrl") + "/"
				+ fileName;
		id = String.valueOf(System.currentTimeMillis());
		fileService
				.insertFile(id, url, String.valueOf(file.getSize()),
						realfileName, file.getContentType(), path + "/"
								+ fileName, "1");
		FileEntity resultFile = fileService.findOne(id);
		return resultFile;
	}

	@RequestMapping("/saveUploadPic")
	public void saveUploadPic(
			HttpServletResponse response,
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "__source", required = false) MultipartFile __source,
			@RequestParam(value = "__avatar1", required = false) MultipartFile __avatar1,
			@RequestParam(value = "__avatar2", required = false) MultipartFile __avatar2,
			@RequestParam(value = "__avatar3", required = false) MultipartFile __avatar3)
			throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			UserAccount ua = new UserAccount();
			ua = userAccountService.findOne(userId);
			FileEntity source = upload(__source);
			FileEntity avatar1 = upload(__avatar1);
			ua.setHeadpic(avatar1.getUrl());
			ua.setHeadphoto(avatar1);
			userAccountService.updateUser(ua);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("success", true);
			result.put("sourceUrl", source.getUrl());
			result.put("avatarUrls", "");
			JSONObject jsonOject = JSONObject.fromObject(result);
			out.write(jsonOject.toString());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("success", false);
			result.put("msg", "异常");
			JSONObject jsonOject = JSONObject.fromObject(result);
			out.write(jsonOject.toString());
			out.flush();
			out.close();
		}
	}

	@RequestMapping("/loadUsers")
	public ModelAndView loadUsers(ModelAndView model) {
		try {
			List<UserAccount> users = userAccountService.listAllUser();
			String userStrs = "";
			int i = 1;
			for (UserAccount userAccount : users) {
				userStrs += userAccount.getName() + " ["
						+ userAccount.getEmail() + "]";
				if (i < users.size()) {
					userStrs += ",";
				}
				i++;
			}
			model.addObject("users", userStrs);
			model.setViewName("/pages/admin/useraccount/load_users");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	/**
	 * ajax
	 * =============================================================================================
	 */
	//进入用户列表页
	@RequestMapping("/page")
	public ModelAndView page(ModelAndView model) {
		try {
			model.setViewName("/admin/user/page");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}
	
	/**
	 * 用户列表数据
	 * @param pageNum 当前页
	 * @param pageSize 页码
	 * @param name 用户名
	 * @param orgName 组织机构名
	 * @param sort 排序参数
	 * @return status 状态位 data 数据集 total 总条数
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/ajaxList")
	@ResponseBody
	public Object ajaxList(
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "orgName", required = false) String orgName,
			@RequestParam(value = "sort", required = false) String sort) {
		try {
			if (pageSize == null) {
				pageSize = 10;
			}
			if (pageNum == null) {
				pageNum = 0;
			}
			Map argMap = new HashMap();
			if (!StringUtils.isEmpty(sort)) {
				if (sort.indexOf("_desc") != -1) {
					argMap.put("sort", sort.replace("_desc", "").toUpperCase()
							+ " DESC");
				} else if (sort.indexOf("_asc") != -1) {
					argMap.put("sort", sort.replace("_asc", "").toUpperCase()
							+ " ASC");
				}
			}
			if (!StringUtils.isEmpty(name)) {
				argMap.put("name", name);
			}
			if (!StringUtils.isEmpty(orgName)) {
				argMap.put("orgName", orgName);
			}
			PageInfo<Map> page = userAccountService.findUserList(pageNum,
					pageSize, argMap);
			List<Map> data = page.getList();
			Long totalcount = page.getTotal();
			Map map = new HashMap();
			map.put("status", "1");
			map.put("data", data);
			map.put("total", totalcount);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson(e.toString());
		}
	}

	/**
	 * 异步加载用户数据
	 * @param userAccountId 用户id
	 * @return
	 */
	@RequestMapping("/ajaxLoad")
	public @ResponseBody
	Object ajaxLoad(
			@RequestParam(value = "userAccountId", required = false) String userAccountId) {
		try {
			UserAccount item = new UserAccount();
			if (userAccountId != null) {
				item = userAccountService.findOne(userAccountId);
			}
			return new UserAccountDto(item);
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常！");
		}
	}

	/**
	 * 组织json
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/loadOrg")
	@ResponseBody
	public Object loadOrg() {
		try {
			List ml = new ArrayList();
			List<Order> orders = new ArrayList<Order>();
			orders.add(new Order(Direction.ASC, "seq"));
			List<Org> olist = commonDAO.findAll(Org.class, orders);
			for (Org org : olist) {
				Map m = new HashMap();
				m.put("id", org.getId());
				m.put("name", org.getName());
				if (org.getParent() != null) {
					m.put("pId", org.getParent().getId());
				} else {
					m.put("pId", 0);
				}
				if (org.getChildren().size() != 0) {
					m.put("open", true);
				}
				ml.add(m);
			}
			return ml;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误！");
		}
	}

	/**
	 * 角色json
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/loadRoles")
	@ResponseBody
	public Object loadRoles() {
		try {
			List ml = new ArrayList();
			List<Order> orders = new ArrayList<Order>();
			orders.add(new Order(Direction.ASC, "seq"));
			List<UserRole> list = commonDAO.findAll(UserRole.class, orders);
			for (UserRole role : list) {
				Map m = new HashMap();
				m.put("id", role.getCode());
				m.put("name", role.getName());
				m.put("pId", 0);
				ml.add(m);
			}
			return ml;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误！");
		}
	}
	
	/**
	 * 锁定用户
	 * @param useraccountid 用户id
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/lockUser")
	@ResponseBody
	public Object lockUser(
			@RequestParam(value = "useraccountid", required = true) String useraccountid)
			throws IOException {
		try {
			UserAccount user = new UserAccount();
			user = userAccountService.findOne(useraccountid);
			user.setNonLocked(false);
			userAccountService.updateUser(user);
			return successJson();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}

	/**
	 * 激活用户
	 * @param useraccountid 用户id
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/unLockUser")
	@ResponseBody
	public Object unLockUser(
			@RequestParam(value = "useraccountid", required = true) String useraccountid)
			throws IOException {
		try {
			UserAccount user = new UserAccount();
			user = userAccountService.findOne(useraccountid);
			user.setNonLocked(true);
			userAccountService.updateUser(user);
			return successJson();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	/**
	 * 保存用户
	 * @param dto 用户实体dto
	 * @param password 密码
	 * @return
	 */
	@RequestMapping("/ajaxSave")
	public @ResponseBody
	Object ajaxSave(UserAccountDto dto,@RequestParam(value = "password", required = false) String password) {
		try {
			if (StringUtils.isEmpty(dto.getCode())) {
				// 插入操作
				UserAccount user = new UserAccount();
				user.setCode(UUIDUtils.getUUID16());
				user.setName(dto.getName());
				user.setLoginname(dto.getLoginName());
				user.setEnabled(dto.isEnabled());
				user.setEmail(dto.getEmail());
				user.setMobile(dto.getMobile());
				user.setNonLocked(dto.isNonLocked());
				ShaPasswordEncoder sha = new ShaPasswordEncoder();
				sha.setEncodeHashAsBase64(false);
				user.setPassword(sha.encodePassword(
						password, null));
				user.setPasswordExpired(false);
				user.setAccountExpired(false);
				//角色
				if (!StringUtils.isEmpty(dto.getRoleIds())) {
					Set<UserRole> urset = new HashSet<UserRole>();
					String ids[] = dto.getRoleIds().split(",");
					for (String roleid : ids) {
						UserRole ur = new UserRole();
						ur = commonDAO.findOne(UserRole.class, roleid);
						urset.add(ur);
					}
					user.setRoles(urset);
				} else {
					user.setRoles(null);
				}
				//组织
				if (dto.getOrgId()!=null) {
					Org o = new Org();
					o = commonDAO.findOne(Org.class,dto.getOrgId());
					user.setOrg(o);
				} else {
					user.setOrg(null);
				}
				userAccountService.insertUser(user);
			} else {
				// 更新操作
				UserAccount user = userAccountService.findOne(dto
						.getCode());
				user.setName(dto.getName());
				user.setLoginname(dto.getLoginName());
				user.setEnabled(dto.isEnabled());
				user.setEmail(dto.getEmail());
				user.setMobile(dto.getMobile());
				user.setNonLocked(dto.isNonLocked());
				//角色
				if (!StringUtils.isEmpty(dto.getRoleIds())) {
					Set<UserRole> urset = new HashSet<UserRole>();
					String ids[] = dto.getRoleIds().split(",");
					for (String roleid : ids) {
						UserRole ur = new UserRole();
						ur = commonDAO.findOne(UserRole.class, roleid);
						urset.add(ur);
					}
					user.setRoles(urset);
				} else {
					user.setRoles(null);
				}
				//组织
				if (dto.getOrgId()!=null) {
					Org o = new Org();
					o = commonDAO.findOne(Org.class,dto.getOrgId());
					user.setOrg(o);
				} else {
					user.setOrg(null);
				}
				userAccountService.updateUser(user);
			}
			userAccountService.refreshService();
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}
	
	/**
	 * 检测登录户名唯一
	 * @param loginName 登录名
	 * @param code 用户id
	 * @return
	 */
	@RequestMapping("/checkLoginName")
	@ResponseBody
	public Object checkLoginName(@RequestParam(value = "loginName", required = false) String loginName,
			@RequestParam(value = "code", required = false) String code) {
		try {
			Map argMap = new HashMap();
			argMap.put("loginName", loginName);
			argMap.put("code", code);
			Long count = userAccountService.countUserForCheck(argMap);
			if(count.intValue()>0){
				return false;
			}else{
				return true;
			} 
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 删除用户
	 * @param userId 用户id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/ajaxDelete")
	@ResponseBody
	public Object ajaxDelete(
			@RequestParam(value = "userId", required = false) String userId)
			throws Exception {
		try {
			if (!StringUtils.isEmpty(userId)) {
				String[] rid = userId.split(",");
				for (String id : rid) {
					UserAccount ua = new UserAccount();
					ua = userAccountService.findOne(id);
					ua.setOrg(null);
					ua.setHeadphoto(null);
					ua.setRoles(null);
					userAccountService.updateUser(ua);
					userAccountService.deleteUser(ua);
				}
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	/**
	 * 重置密码
	 * @param newPassword 新密码
	 * @param useraccountId 用户id
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/resetPassword")
	@ResponseBody
	public Object resetPassword(
			@RequestParam(value = "newPassword", required = false) String newPassword,
			@RequestParam(value = "useraccountId", required = true) String useraccountId)
			throws IOException {
		try {
			UserAccount user = new UserAccount();
			user = userAccountService.findOne(useraccountId);
			if(StringUtils.isEmpty(newPassword)){
				newPassword = getRandomString(12);
			}
			ShaPasswordEncoder sha = new ShaPasswordEncoder();
			sha.setEncodeHashAsBase64(false);
			String jiamicode = sha.encodePassword(newPassword, null);
			user.setPassword(jiamicode);
			userAccountService.updateUser(user);
			MailUtil.getInstance().sendMail(user.getEmail(), "重置密码",
					"新的密码：" + newPassword + "请妥善保管！");
			Map result = new HashMap();
			result.put("status", 1);
			result.put("password", newPassword);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}

	/**
	 * 所以生成密码
	 * @param length 密码长度
	 * @return
	 */
	private String getRandomString(int length) { // length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	
	@RequestMapping("/loadAllUsers")
	@ResponseBody
	public Object loadAllUsers() {
		try {
			List<Map> users = userAccountService.findAllUserList();
			return users;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
}
