package com.dm.platform.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.dto.RegeistDto;
import com.dm.platform.model.TempUser;
import com.dm.platform.model.UserAccount;
import com.dm.platform.model.UserAttrEntity;
import com.dm.platform.model.UserMenu;
import com.dm.platform.model.UserRole;
import com.dm.platform.service.InboxService;
import com.dm.platform.service.UserAccountService;
import com.dm.platform.service.UserAttrService;
import com.dm.platform.service.UserRoleService;
import com.dm.platform.util.CommonStatics;
import com.dm.platform.util.DmDateUtil;
import com.dm.platform.util.MailUtil;
import com.dm.platform.util.UserAccountUtil;

@Controller
public class BaseController extends DefaultController {

	@Resource
	UserRoleService userRoleService;

	@Resource
	UserAccountService userAccountService;

	@Resource
	CommonDAO commonDAO;


	@Resource
	UserAttrService userAttrService;


	@Resource
	InboxService inboxService;
	
	@Resource
	Cache myCache;
	
	@RequestMapping("/login")
	public ModelAndView login(ModelAndView model) {
		model.setViewName("/login");
		return model;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/mainpage")
	public ModelAndView mainpage(ModelAndView model) {
		try {
			UserAccount currentUser = UserAccountUtil.getInstance()
					.getCurrentUserAccount();

			if (currentUser.getRoles().size() > 0) {
				Map<String, Object> map = new HashMap<String, Object>();
				model.setViewName("/admin/dashboard");
				return Model(model);
			} else {
				return Error("未授权角色");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	@RequestMapping("/saveRegiest")
	public ModelAndView saveRegiest(ModelAndView model, RegeistDto ruser) {
		try {
			TempUser tempUser = new TempUser();
			String userId = String.valueOf(System.currentTimeMillis());
			tempUser.setCode(userId);
			tempUser.setStatus("0");
			tempUser.setName(ruser.getFullname());
			tempUser.setLoginname(ruser.getUsername());
			tempUser.setMobile(ruser.getMobile());
			tempUser.setEmail(ruser.getEmail());
			tempUser.setPassword(ruser.getPassword());
			tempUser.setCreatDate(DmDateUtil.DateToStr(new Date(),
					"yyyy-MM-dd HH:mm:ss"));
			commonDAO.save(tempUser);

			model.addObject("redirect", getRootPath() + "/login");
			model.setViewName("/pages/content/redirect");
			return model;
		} catch (Exception e) {
			e.printStackTrace();
			model.addObject("msg", "注册失败！");
			model.addObject("redirect", getRootPath() + "/login");
			model.setViewName("/pages/content/fail");
			return model;
		}
	}

	@RequestMapping("/center")
	public ModelAndView infoCenter(ModelAndView model) {
		model.setViewName("/admin/center");
		return Model(model);
	}

	@RequestMapping("/checkunique")
	public void checkloginname(HttpServletResponse response,
			@RequestParam(value = "param", required = false) String param,
			@RequestParam(value = "name", required = false) String name) {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			if (commonDAO.findByPropertyName(UserAccount.class, name, param)
					.size() > 0) {
				out.write("false");
			} else {
				if (commonDAO.findByPropertyName(TempUser.class, name, param)
						.size() > 0) {
					out.write("false");
				} else {
					out.write("true");
				}

			}
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/resetPassword")
	public ModelAndView resetPassword(ModelAndView model,
			@RequestParam(value = "email", required = true) String email)
			throws IOException {
		try {
			UserAccount user = new UserAccount();
			user = userAccountService.findByEmail(email);
			if (user != null) {
				String newPassword = getRandomString(12);
				ShaPasswordEncoder sha = new ShaPasswordEncoder();
				sha.setEncodeHashAsBase64(false);
				String jiamicode = sha.encodePassword(newPassword, null);
				user.setPassword(jiamicode);
				commonDAO.update(user);
				MailUtil.getInstance().sendMail(user.getEmail(), "重置密码",
						newPassword);
				return Redirect(model, getRootPath() + "/login",
						"重置密码成功，请查收邮件！");
			} else {
				return Redirect(model, getRootPath() + "/login", "该邮箱注册的用户不存在！");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return RedirectError(model, getRootPath() + "/login", "重置密码失败！");
		}
	}

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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/loadMenus")
	public @ResponseBody
	Object loadMenus() {
		try {
			Comparator<UserMenu> c = new Comparator<UserMenu>() {
				public int compare(UserMenu o1, UserMenu o2) {
					return (int) ((o1.getSeq()==null?0:o1.getSeq()) - (o2.getSeq()==null?0:o2.getSeq()));
				}
			};
			Set<UserMenu> menus = new HashSet<UserMenu>();
			List list = new ArrayList();
			String[] roleid = UserAccountUtil.getInstance().getCurrentRoles()
					.split(",");
			List<UserMenu> menuList = new ArrayList<UserMenu>();
			for (String id : roleid) {
				if (myCache.get(id) == null) {
					putOneRole(id);
				}
				for (UserMenu m : (Set<UserMenu>) myCache.get(id)
						.getObjectValue()) {
					menus.add(m);
				}
			}
			for (UserMenu userMenu : menus) {
				menuList.add(userMenu);
			}
			Collections.sort(menuList, c);
			for (UserMenu m : menuList) {
				Map map = new HashMap();
				map.put("id", m.getId());
				map.put("name", m.getName());
				if (m.getPuserMenu() != null) {
					if (!menuList.contains(m.getPuserMenu())) {
						map.put("pId", 0);
					} else {
						map.put("pId", m.getPuserMenu().getId());
					}
				} else {
					map.put("pId", 0);
				}
				map.put("icon", m.getIcon());
				map.put("url", m.getUrl());
				if (m.getChildren().size() == 0) {
					map.put("isLeaf", true);
				} else {
					map.put("isLeaf", false);
				}
				list.add(map);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常");
		}
	}

	private void putOneRole(String roleId) {
		UserRole u = commonDAO.findOne(UserRole.class, roleId);
		putMenusByRole(u);
	}

	private void putMenusByRole(UserRole u) {
		Set<UserMenu> menus = new HashSet<UserMenu>();
		menus = u.getMenus();
		Element element = new Element(u.getCode(), menus);
		myCache.put(element);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/userInfo")
	public @ResponseBody
	Object userInfo() {
		try {
			UserAccount user = UserAccountUtil.getInstance()
					.getCurrentUserAccount();
			Map map = new HashMap();
			map.put("avatar", user.getHeadpic());
			map.put("userName", user.getName());
			map.put("status", "1");
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部异常");
		}
	}
}
