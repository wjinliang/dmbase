package com.dm.platform.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.dto.OrgDto;
import com.dm.platform.dto.UserMenuDto;
import com.dm.platform.model.Org;
import com.dm.platform.model.UserAccount;
import com.dm.platform.model.UserMenu;
import com.dm.platform.service.OrgService;
import com.dm.platform.service.UserAccountService;
import com.dm.platform.util.UUIDUtils;

@Controller
@RequestMapping("/org")
public class OrgController extends DefaultController {
	@Resource
	OrgService orgService;
	@Resource
	UserAccountService userAccountService;
	@Resource
	CommonDAO commonDAO;
	
	
	
	@RequestMapping("/page")
	public ModelAndView page(ModelAndView model) {
		try {
			model.setViewName("/admin/org/page");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	@RequestMapping("/list")
	public ModelAndView list(ModelAndView model, HttpServletRequest request) {
		try {
			model.addObject("totalcount", orgService.countMenuGrou());
			model.setViewName("/pages/admin/org/list");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/loadOrgs")
	public @ResponseBody Object loadOrgs() {
		try {
			List ml = new ArrayList();
			List<Order> orders = new ArrayList<Order>();
			orders.add(new Order(Direction.ASC, "seq"));
			orders.add(new Order(Direction.DESC, "id"));
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
			return errorJson("内部错误");
		}
	}
	
	
	@RequestMapping("/ajaxLoad")
	public @ResponseBody
	Object ajaxLoad(
			@RequestParam(value = "orgId", required = false) String orgId) {
		try {
			Org o = new Org();
			if (!StringUtils.isEmpty(orgId)) {
				o = orgService.findOne(Long.valueOf(orgId));
			}
			return new OrgDto(o);
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常！");
		}
	}

	@RequestMapping("/ajaxSave")
	public @ResponseBody
	Object ajaxSave(
			OrgDto dto) {
		try {
			Org org = new Org();
			if(dto.getId()==null){
				org.setId(System.currentTimeMillis());
				org.setCode(UUIDUtils.getUUID16());
				org.setName(dto.getName());
				org.setSeq(dto.getSeq());
				if(dto.getpId()!=null){
					Org po = orgService.findOne(dto.getpId());
					org.setParent(po);
				}
				orgService.insertOrg(org);
			}else{
				org = orgService.findOne(dto.getId());
				org.setName(dto.getName());
				org.setSeq(dto.getSeq());
				orgService.updateOrg(org);
			}
			if(!StringUtils.isEmpty(dto.getUserIds())){
				String[] userIds = dto.getUserIds().split(",");
				for (String userId : userIds) {
					UserAccount user = userAccountService.findOne(userId);
					user.setOrg(org);
					userAccountService.updateUser(user);
				}
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("异常！");
		}
	}
	
	@RequestMapping("/setSeq")
	@ResponseBody
	public Object setSeq(
			HttpServletResponse response,
			@RequestParam(value = "currentid", required = false) String currentid,
			@RequestParam(value = "targetid", required = false) String targetid,
			@RequestParam(value = "moveType", required = false) String moveType,
			@RequestParam(value = "moveMode", required = false) String moveMode)
			throws Exception {
		try {
			if (currentid != null && targetid != null) {
				Org starto = new Org();
				Org targeto = new Org();
				starto = orgService.findOne(Long.valueOf(currentid));
				targeto = orgService.findOne(Long.valueOf(targetid));
				Long start = starto.getSeq();
				Long limit = targeto.getSeq();
				if (!moveType.equals("inner")) {
					if (moveMode.equals("same")) {
						if (start > limit) {
							Long sl;
							if (moveType.equals("prev")) {
								sl = limit;
							} else {
								sl = limit + 1;
							}
							for (Long i = start - 1; i >= sl; i--) {
								Org entity = new Org();
								List<Org> orglist = new ArrayList<Org>();
								if (targeto.getParent() != null) {
									entity.setParent(targeto.getParent());
									orglist = commonDAO.findAll(Org.class,
											" and t.parent = :parent and t.seq = "
													+ i, entity);
								} else {
									orglist = commonDAO.findAll(Org.class,
											" and t.parent is null and t.seq = "
													+ i, entity);
								}
								for (Org org : orglist) {
									org.setSeq(i + 1);
									orgService.updateOrg(org);
								}
							}
							starto.setSeq(sl);
							orgService.updateOrg(starto);
						} else if (start < limit) {
							Long tl;
							if (moveType.equals("prev")) {
								tl = limit - 1;
							} else {
								tl = limit;
							}
							for (Long i = start + 1; i <= tl; i++) {
								Org entity = new Org();
								List<Org> orglist = new ArrayList<Org>();
								if (targeto.getParent() != null) {
									entity.setParent(targeto.getParent());
									orglist = commonDAO.findAll(Org.class,
											" and t.parent = :parent and t.seq = "
													+ i, entity);
								} else {
									orglist = commonDAO.findAll(Org.class,
											" and t.parent is null and t.seq = "
													+ i, entity);
								}
								for (Org org : orglist) {
									org.setSeq(i - 1);
									orgService.updateOrg(org);
								}
							}
							starto.setSeq(tl);
							orgService.updateOrg(starto);
						}
					} else {
						Long currentcount;
						if (starto.getParent() != null) {
							currentcount = commonDAO
									.count("select count(*) from Org o where o.parent.id="
											+ starto.getParent().getId()) + 1;
						} else {
							currentcount = commonDAO
									.count("select count(*) from Org o where o.parent is null") + 1;
						}
						for (Long i = start + 1; i < currentcount; i++) {
							Org entity = new Org();
							List<Org> orglist = new ArrayList<Org>();
							if (starto.getParent() != null) {
								entity.setParent(starto.getParent());
								orglist = commonDAO.findAll(Org.class,
										" and t.parent = :parent and t.seq = "
												+ i, entity);
							} else {
								orglist = commonDAO.findAll(Org.class,
										" and t.parent is null and t.seq = "
												+ i, entity);
							}
							for (Org org : orglist) {
								org.setSeq(i - 1);
								orgService.updateOrg(org);
							}
						}

						// -------------------------------------------------

						Long targetcount;
						if (targeto.getParent() != null) {
							targetcount = commonDAO
									.count("select count(*) from Org o where o.parent.id="
											+ targeto.getParent().getId());
						} else {
							targetcount = commonDAO
									.count("select count(*) from Org o where o.parent is null");
						}
						Long startc;
						if (moveType.equals("prev")) {
							startc = limit;
						} else {
							startc = limit + 1;
						}
						for (Long i = targetcount; i >= startc; i--) {
							Org entity = new Org();
							List<Org> orglist = new ArrayList<Org>();
							if (targeto.getParent() != null) {
								entity.setParent(targeto.getParent());
								orglist = commonDAO.findAll(Org.class,
										" and t.parent = :parent and t.seq = "
												+ i, entity);
							} else {
								orglist = commonDAO.findAll(Org.class,
										" and t.parent is null and t.seq = "
												+ i, entity);
							}
							for (Org org : orglist) {
								org.setSeq(i + 1);
								orgService.updateOrg(org);
							}
						}
						starto.setParent(targeto.getParent());
						starto.setSeq(startc);
						orgService.updateOrg(starto);
					}
				} else {
					Long currentcount;
					if (starto.getParent() != null) {
						currentcount = commonDAO
								.count("select count(*) from Org o where o.parent.id="
										+ starto.getParent().getId()) + 1;
					} else {
						currentcount = commonDAO
								.count("select count(*) from Org o where o.parent is null") + 1;
					}
					for (Long i = start + 1; i < currentcount; i++) {
						Org entity = new Org();
						List<Org> orglist = new ArrayList<Org>();
						if (starto.getParent() != null) {
							entity.setParent(starto.getParent());
							orglist = commonDAO.findAll(Org.class,
									" and t.parent = :parent and t.seq = " + i,
									entity);
						} else {
							orglist = commonDAO.findAll(Org.class,
									" and t.parent is null and t.seq = " + i,
									entity);
						}
						for (Org org : orglist) {
							org.setSeq(i - 1);
							orgService.updateOrg(org);
						}
					}
					Long count = commonDAO
							.count("select count(*) from Org o where o.parent.id="
									+ targeto.getId()) + 1;
					starto.setParent(targeto);
					starto.setSeq(count);
					orgService.updateOrg(starto);
				}
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	@RequestMapping("/ajaxDelete")
	@ResponseBody
	public Object ajaxDelete(
			@RequestParam(value = "orgId", required = false) String orgId)
			throws Exception {
		try {
			if (!StringUtils.isEmpty(orgId)) {
				Org o = new Org();
				o = orgService.findOne(new Long(orgId));
				List<UserAccount> ualist = commonDAO.findByPropertyName(
						UserAccount.class, "org.id", new Long(orgId));
				for (UserAccount userAccount : ualist) {
					userAccount.setOrg(null);
					userAccountService.updateUser(userAccount);
				}
				orgService.deleteOrg(o);
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	
	
	@RequestMapping("/form/{mode}")
	public ModelAndView form(HttpServletRequest request, ModelAndView model,
			@PathVariable String mode,
			@RequestParam(value = "orgid", required = false) String orgid) {
		try {
			Org o = new Org();
			if (mode != null && !mode.equals("new")) {
				if (orgid != null) {
					o = orgService.findOne(Long.valueOf(orgid));
					model.addObject("org", o);
					if(mode.equals("view")){
						model.setViewName("/pages/admin/org/view");
						return Model(model);
					}
				}
			} else {
				if (orgid != null) {
					o.setSeq(commonDAO
							.count("select count(*) from Org o where o.parent.id="
									+ orgid) + 1);
					model.addObject("parentid", orgid);
				} else {
					o.setSeq(commonDAO
							.count("select count(*) from Org o where o.parent is null") + 1);
				}
				model.addObject("org", o);
			}
			model.setViewName("/pages/admin/org/form");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	@RequestMapping("/save")
	public ModelAndView save(ModelAndView model, Org org,
			@RequestParam(value = "parentid", required = false) String parentid) {
		try {
			Org o = new Org();
			if (org.getId() != null) {
				o = orgService.findOne(org.getId());
				o.setName(org.getName());
				o.setCode(org.getCode());
				o.setSeq(org.getSeq());
				orgService.updateOrg(o);
			} else {
				if (parentid != null && !parentid.equals("")) {
					o = orgService.findOne(Long.valueOf(parentid));
					org.setParent(o);
				}
				org.setId(System.currentTimeMillis());
				orgService.insertOrg(org);
			}
			model.setViewName("/pages/admin/org/success");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	@RequestMapping("/delete")
	public void delete(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "orgid", required = false) String orgid)
			throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			if (orgid != null) {
				Org o = new Org();
				o = commonDAO.findOne(Org.class, new Long(orgid));
				List<UserAccount> ualist = commonDAO.findByPropertyName(
						UserAccount.class, "org.id", new Long(orgid));
				for (UserAccount userAccount : ualist) {
					userAccount.setOrg(null);
					commonDAO.update(userAccount);
				}
				commonDAO.delete(o);
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/load")
	public @ResponseBody Object load() {
		try {
			List ml = new ArrayList();
			List<Order> orders = new ArrayList<Order>();
			orders.add(new Order(Direction.ASC, "seq"));
			orders.add(new Order(Direction.DESC, "id"));
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
			return errorJson("内部错误");
		}
	}
}
