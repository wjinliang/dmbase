package com.dm.platform.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dm.platform.dto.InboxDto;
import com.dm.platform.model.FileEntity;
import com.dm.platform.model.InboxEntity;
import com.dm.platform.model.UserAccount;
import com.dm.platform.service.InboxService;
import com.dm.platform.service.UserAccountService;
import com.dm.platform.statics.InboxS;
import com.dm.platform.util.DmDateUtil;
import com.dm.platform.util.UserAccountUtil;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/inbox")
public class InboxController extends DefaultController {
	@Resource
	InboxService inboxService;
	@Resource
	UserAccountService userAccountService;

	@RequestMapping("/page")
	public ModelAndView messageCenter(ModelAndView model) {
		try {
			model.setViewName("/admin/inbox/page");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/news")
	public @ResponseBody
	Object news() {
		try {
			String userId = UserAccountUtil.getInstance().getCurrentUserId();
			Map argMap =  new HashMap();
			argMap.put("type", InboxS.INBOX);
			argMap.put("isRead", InboxS.NOREAD);
			argMap.put("userId", userId);
			PageInfo<InboxDto> page  = inboxService.findInboxList(0, 5, argMap);
			List<InboxDto> list = page.getList();
			List items = new ArrayList();
			for (InboxDto dto : list) {
				Map msg = new HashMap();
				msg.put("userName", dto.getFormUserStr());
				String avatar = userAccountService.getUserHeadPic(userId);
				msg.put("avatar", avatar);
				msg.put("time", dto.getFormatTime());
				msg.put("message", dto.getSubject());
				msg.put("tId",dto.gettId());
				msg.put("attch", dto.getHasAttachment());
				items.add(msg);
			}
			
			Map result =  new HashMap();
			result.put("status", 1);
			result.put("total", page.getTotal());
			result.put("inboxs", items);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	@RequestMapping("/listInbox")
	public @ResponseBody
	Object listInbox(
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		try {
			if (pageSize == null) {
				pageSize = 10;
			}
			if (pageNum == null) {
				pageNum = 1;
			}
			Map argMap =  new HashMap();
			argMap.put("type", InboxS.INBOX);
			argMap.put("isTrash", InboxS.NOTRASH);
			argMap.put("userId", UserAccountUtil.getInstance().getCurrentUserId());
			PageInfo<InboxDto> page  = inboxService.findInboxList(pageNum, pageSize, argMap);
			Map result =  new HashMap();
			result.put("data", page.getList());
			result.put("total", page.getTotal());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	@RequestMapping("/listSent")
	public @ResponseBody
	Object listSent(
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		try {
			if (pageSize == null) {
				pageSize = 10;
			}
			if (pageNum == null) {
				pageNum = 1;
			}
			Map argMap =  new HashMap();
			argMap.put("type", InboxS.SENT);
			argMap.put("isTrash", InboxS.NOTRASH);
			argMap.put("userId", UserAccountUtil.getInstance().getCurrentUserId());
			PageInfo<InboxDto> page  = inboxService.findInboxList(pageNum, pageSize, argMap);
			Map result =  new HashMap();
			result.put("data", page.getList());
			result.put("total", page.getTotal());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	@RequestMapping("/listDraft")
	public @ResponseBody
	Object listDraft(
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		try {
			if (pageSize == null) {
				pageSize = 10;
			}
			if (pageNum == null) {
				pageNum = 1;
			}
			Map argMap =  new HashMap();
			argMap.put("type", InboxS.DRAFT);
			argMap.put("userId", UserAccountUtil.getInstance().getCurrentUserId());
			PageInfo<InboxDto> page  = inboxService.findInboxList(pageNum, pageSize, argMap);
			Map result =  new HashMap();
			result.put("data", page.getList());
			result.put("total", page.getTotal());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	@RequestMapping("/listTrash")
	public @ResponseBody
	Object listTrash(
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		try {
			if (pageSize == null) {
				pageSize = 10;
			}
			if (pageNum == null) {
				pageNum = 1;
			}
			Map argMap =  new HashMap();
			argMap.put("isTrash", InboxS.ISTRASH);
			argMap.put("userId", UserAccountUtil.getInstance().getCurrentUserId());
			PageInfo<InboxDto> page  = inboxService.findInboxList(pageNum, pageSize, argMap);
			Map result =  new HashMap();
			result.put("data", page.getList());
			result.put("total", page.getTotal());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	@RequestMapping("/listMark")
	public @ResponseBody
	Object listMark(
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		try {
			if (pageSize == null) {
				pageSize = 10;
			}
			if (pageNum == null) {
				pageNum = 1;
			}
			Map argMap =  new HashMap();
			argMap.put("isMark", InboxS.ISMARK);
			argMap.put("userId", UserAccountUtil.getInstance().getCurrentUserId());
			PageInfo<InboxDto> page  = inboxService.findInboxList(pageNum, pageSize, argMap);
			Map result =  new HashMap();
			result.put("data", page.getList());
			result.put("total", page.getTotal());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	@RequestMapping("/users")
	@ResponseBody
	public Object users() {
		try {
			List<UserAccount> users = userAccountService.listAllUser();
			List<String> userList = new ArrayList<String>();
			for (UserAccount userAccount : users) {
				String str = userAccount.getName() + "<"
						+ userAccount.getEmail() + ">";
				userList.add(str);
			}
			return userList;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}
	
	@RequestMapping("/sendInbox")
	@ResponseBody
	public Object sendInbox(String toUsers,String subject,String content,String attId,String saveSent,@RequestParam(value="tId",required=false) String tId) {
		try {
			//保存消息实体
			InboxEntity inbox = new InboxEntity();
			inbox.setToUserStr(toUsers);
			UserAccount user= UserAccountUtil.getInstance().getCurrentUserAccount();
			inbox.setFormUserStr(user.getName()+"<"+user.getEmail()+">");
			inbox.setCreatUserId(user.getCode());
			inbox.setCreatTime(DmDateUtil.Current());
			if(!StringUtils.isEmpty(attId)){
				inbox.setHasAttachment(InboxS.HASATTACHMENT);
			}else{
				inbox.setHasAttachment(InboxS.NOATTACHMENT);
			}
			inbox.setSubject(subject);
			inbox.setContent(content);
			String inboxId = inboxService.insert(inbox);
			//保存附件关联
			if(!StringUtils.isEmpty(attId)){
				String[] ids = attId.split(",");
				for (String id : ids) {
					inboxService.saveAttachement(inboxId, id);
				}
			}
			String[] users = toUsers.split(",");
			for (String str : users) {
				if (str.indexOf("<") != -1 && str.indexOf(">") != -1) {
					str = str.substring(str.indexOf("<") + 1,
							str.indexOf(">"));
					UserAccount u = userAccountService.findByEmail(str);
					// 发送消息
					inboxService.sendInbox(inboxId, u.getCode());
				}
			}
			if(!StringUtils.isEmpty(tId)){
				inboxService.deleteDraft(Integer.valueOf(tId));
			}
			if(!StringUtils.isEmpty(saveSent)&&"1".equals(saveSent)){
				inboxService.saveSentInbox(inboxId, user.getCode());
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	@RequestMapping("/draftInbox")
	@ResponseBody
	public Object draftInbox(String toUsers,String subject,String content,String attId,String saveSent,String inboxId,String tId) {
		try {
			Map result = new HashMap();
			InboxEntity inbox = new InboxEntity();
			UserAccount user= UserAccountUtil.getInstance().getCurrentUserAccount();
			if(!StringUtils.isEmpty(inboxId)){
				inbox = inboxService.findOne(inboxId);
				inbox.setToUserStr(toUsers);
				inbox.setFormUserStr(user.getName()+"<"+user.getEmail()+">");
				inbox.setCreatUserId(user.getCode());
				inbox.setCreatTime(DmDateUtil.Current());
				if(!StringUtils.isEmpty(attId)){
					inbox.setHasAttachment(InboxS.HASATTACHMENT);
				}else{
					inbox.setHasAttachment(InboxS.NOATTACHMENT);
				}
				inbox.setSubject(subject);
				inbox.setContent(content);
				inboxService.update(inbox);
				inboxService.deleteAttachements(inboxId);
				if(!StringUtils.isEmpty(attId)){
					String[] ids = attId.split(",");
					for (String id : ids) {
						inboxService.saveAttachement(inboxId, id);
					}
				}
				if(!StringUtils.isEmpty(tId))
					inboxService.updateDraftTime(Integer.valueOf(tId));
			}else{
				inbox.setToUserStr(toUsers);
				inbox.setFormUserStr(user.getName()+"<"+user.getEmail()+">");
				inbox.setCreatUserId(user.getCode());
				inbox.setCreatTime(DmDateUtil.Current());
				if(!StringUtils.isEmpty(attId)){
					inbox.setHasAttachment(InboxS.HASATTACHMENT);
				}else{
					inbox.setHasAttachment(InboxS.NOATTACHMENT);
				}
				inbox.setSubject(subject);
				inbox.setContent(content);
				String newInboxId = inboxService.insert(inbox);
				//保存附件关联
				inboxService.deleteAttachements(inboxId);
				if(!StringUtils.isEmpty(attId)){
					String[] ids = attId.split(",");
					for (String id : ids) {
						inboxService.saveAttachement(newInboxId, id);
					}
				}
				Integer newtId = inboxService.insertDraft(newInboxId, user.getCode());
				result.put("inboxId", newInboxId);
				result.put("tId", newtId);
			}
			result.put("state", "1");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	@RequestMapping("/readInbox")
	@ResponseBody
	public Object readInbox(String tId) {
		try {
			//保存消息实体
			InboxDto inbox = new InboxDto();
			inbox = inboxService.read(tId);
			inboxService.isRead(tId, InboxS.ISREAD);
			List<FileEntity> attachments = inboxService.findAttachements(inbox.getInboxId());
			Map result =  new HashMap();
			result.put("inbox", inbox);
			result.put("attachments", attachments);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	@RequestMapping("/replyData")
	@ResponseBody
	public Object replyData(String tId) {
		try {
			//保存消息实体
			InboxDto inbox = new InboxDto();
			inbox = inboxService.read(tId);
			String fStr = inbox.getFormUserStr();
			inbox.setContent(getReplyContent(inbox.getFormUserStr(),inbox.getToUserStr(),inbox.getInboxDate(),inbox.getSubject(),inbox.getContent()));
			inbox.setToUserStr(fStr);
			List<FileEntity> attachments = inboxService.findAttachements(inbox.getInboxId());
			Map result =  new HashMap();
			result.put("inbox", inbox);
			result.put("attachments", attachments);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	@RequestMapping("/draftData")
	@ResponseBody
	public Object draftData(String tId) {
		try {
			//保存消息实体
			InboxDto inbox = new InboxDto();
			inbox = inboxService.read(tId);
			List<FileEntity> attachments = inboxService.findAttachements(inbox.getInboxId());
			Map result =  new HashMap();
			result.put("inbox", inbox);
			result.put("attachments", attachments);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	private String getReplyContent(String from, String to,
			String messageDate, String subject, String m_content) {
		StringBuffer content = new StringBuffer();
		content.append("<br/>");
		content.append("<br/>");
		content.append("<div style=\"background-color:#FFFFFF;font-family:'Arial Narrow';padding:2px 0px;\">");
		content.append("------------------&nbsp;原始邮件&nbsp;------------------");
		content.append("</div>");
		content.append("<div style=\"font-family:Verdana;background-color:#EFEFEF;padding:8px;\">");
		content.append("<div><b>发件人:</b>&nbsp;" + from + "</div>");
		content.append("<div><b>发送时间:</b>&nbsp;" + messageDate + "</div>");
		content.append("<div><b>收件人:</b>&nbsp;" + to + "</div>");
		content.append("<div></div>");
		content.append("<div><b>主题:</b>&nbsp;" + subject + "</div>");
		content.append("</div>");
		content.append("<br/>");
		content.append(m_content);
		return content.toString();
	}
	
	@RequestMapping("/mark")
	@ResponseBody
	public Object mark(String tId) {
		try {
			inboxService.isMark(tId, InboxS.ISMARK);
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	@RequestMapping("/cancelMark")
	@ResponseBody
	public Object cancelMark(String tId) {
		try {
			inboxService.isMark(tId, InboxS.NOMARK);
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	@RequestMapping("/trash")
	@ResponseBody
	public Object trash(String tId) {
		try {
			String [] tids = tId.split(",");
			for (String id : tids) {
				inboxService.isTrash(Integer.valueOf(id), InboxS.ISTRASH);
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	@RequestMapping("/cancelTrash")
	@ResponseBody
	public Object cancelTrash(String tId) {
		try {
			String [] tids = tId.split(",");
			for (String id : tids) {
				inboxService.isTrash(Integer.valueOf(id), InboxS.NOTRASH);
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	@RequestMapping("/deleteInbox")
	@ResponseBody
	public Object deleteInbox(String tId) {
		try {
			String [] tids = tId.split(",");
			for (String id : tids) {
				inboxService.deleteInbox(Integer.valueOf(id));
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
	@RequestMapping("/deleteDraft")
	@ResponseBody
	public Object deleteDraft(String tId) {
		try {
			String [] tids = tId.split(",");
			for (String id : tids) {
				inboxService.deleteDraft(Integer.valueOf(id));
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson("内部错误");
		}
	}
	
}
