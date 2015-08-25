package com.dm.platform.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.dto.InboxDto;
import com.dm.platform.model.FileEntity;
import com.dm.platform.model.InboxEntity;
import com.dm.platform.service.InboxService;
import com.dm.platform.statics.InboxS;
import com.dm.platform.util.DmDateUtil;
import com.dm.platform.util.UUIDUtils;
import com.dm.platform.util.UserAccountUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class InboxServiceImpl implements InboxService{
	@Resource
	private CommonDAO commonDAO;
	@Resource
	private SqlSessionTemplate sqlSession;
	@Override
	public String insert(InboxEntity entity) {
		// TODO Auto-generated method stub
		String inboxId = UUIDUtils.getUUID32();
		entity.setInboxId(inboxId);
		commonDAO.save(entity);
		return inboxId;
	}

	@Override
	public void update(InboxEntity entity) {
		// TODO Auto-generated method stub
		commonDAO.update(entity);
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		commonDAO.deleteById(InboxEntity.class, id);
	}
	
	@Override
	public PageInfo<InboxDto> findInboxList(Integer pageNum, Integer pageSize,
			Map argMap) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		List<InboxDto> list = sqlSession
				.selectList("com.dm.platform.model.InboxEntity.findInboxList",argMap);
		PageInfo<InboxDto> page = new PageInfo<InboxDto>(list);
		return page;
	}

	@Override
	public void sendInbox(String inboxId, String toUserId) {
		// TODO Auto-generated method stub
		Map argMap = new HashMap();
		argMap.put("inboxId", inboxId);
		argMap.put("userId", toUserId);
		argMap.put("inboxType", InboxS.INBOX);
		argMap.put("inboxDate", DmDateUtil.Current());
		sqlSession.insert("com.dm.platform.model.InboxEntity.sendUser", argMap);
	}

	@Override
	public void saveSentInbox(String inboxId, String fromUserId) {
		// TODO Auto-generated method stub
		Map argMap = new HashMap();
		argMap.put("inboxId", inboxId);
		argMap.put("userId", fromUserId);
		argMap.put("inboxType", InboxS.SENT);
		argMap.put("inboxDate", DmDateUtil.Current());
		sqlSession.insert("com.dm.platform.model.InboxEntity.sendUser", argMap);
	}

	@Override
	public void saveAttachement(String inboxId, String attachmentId) {
		// TODO Auto-generated method stub
		Map argMap = new HashMap();
		argMap.put("inboxId", inboxId);
		argMap.put("attachmentId", attachmentId);
		sqlSession.insert("com.dm.platform.model.InboxEntity.saveAttachement", argMap);
	}

	@Override
	public InboxEntity findOne(String id) {
		// TODO Auto-generated method stub
		return commonDAO.findOne(InboxEntity.class, id);
	}

	@Override
	public InboxDto read(String tId) {
		// TODO Auto-generated method stub
		InboxDto inbox = sqlSession.selectOne("com.dm.platform.model.InboxEntity.readInbox",tId);
		return inbox;
	}

	@Override
	public void isRead(String tId, String readState) {
		// TODO Auto-generated method stub
		Map argMap = new HashMap();
		argMap.put("tId", tId);
		argMap.put("readState", readState);
		sqlSession.update("com.dm.platform.model.InboxEntity.isReadInbox",argMap);
	}

	@Override
	public List<FileEntity> findAttachements(String inboxId) {
		// TODO Auto-generated method stub
		Map argMap = new HashMap();
		argMap.put("inboxId", inboxId);
		List<FileEntity> list = sqlSession
				.selectList("com.dm.platform.model.InboxEntity.findAttachments",argMap);
		return list;
	}

	@Override
	public void isMark(String tId, String markState) {
		// TODO Auto-generated method stub
		Map argMap = new HashMap();
		argMap.put("tId", tId);
		argMap.put("markState", markState);
		sqlSession.update("com.dm.platform.model.InboxEntity.isMarkInbox",argMap);
	}

	@Override
	public Integer insertDraft(String inboxId, String fromUserId) {
		// TODO Auto-generated method stub
		Map argMap = new HashMap();
		argMap.put("inboxId", inboxId);
		argMap.put("userId", fromUserId);
		argMap.put("inboxType", InboxS.DRAFT);
		argMap.put("inboxDate", DmDateUtil.Current());
		sqlSession.insert("com.dm.platform.model.InboxEntity.insertDraft", argMap);
		return (Integer)argMap.get("tId");
	}

	@Override
	public void deleteDraft(Integer tId) {
		// TODO Auto-generated method stub
		Map argMap = new HashMap();
		argMap.put("tId", tId);
		sqlSession.delete("com.dm.platform.model.InboxEntity.deleteDraft", argMap);
	}

	@Override
	public String newSimpleInbox(String subject, String content,String toUserStr) {
		// TODO Auto-generated method stub
		InboxEntity inbox = new InboxEntity();
		inbox.setSubject(subject);
		inbox.setContent(content);
		inbox.setCreatUserId(UserAccountUtil.getInstance().getCurrentUserId());
		inbox.setFormUserStr(UserAccountUtil.getInstance().getCurrentUserInboxName());
		inbox.setToUserStr(toUserStr);
		inbox.setHasAttachment("0");
		inbox.setCreatTime(DmDateUtil.Current());
		return insert(inbox);
	}

	@Override
	public void updateDraftTime(Integer tId) {
		// TODO Auto-generated method stub
		String inboxDate = DmDateUtil.Current();
		Map argMap = new HashMap();
		argMap.put("tId", tId);
		argMap.put("inboxDate", inboxDate);
		sqlSession.delete("com.dm.platform.model.InboxEntity.updateDraft", argMap);
	}

	@Override
	public void deleteAttachements(String inboxId) {
		// TODO Auto-generated method stub
		Map argMap = new HashMap();
		argMap.put("inboxId", inboxId);
		sqlSession.delete("com.dm.platform.model.InboxEntity.deleteAttachments", argMap);
	}

	@Override
	public void isTrash(Integer tId, String trashState) {
		// TODO Auto-generated method stub
		Map argMap = new HashMap();
		argMap.put("tId", tId);
		argMap.put("trashState", trashState);
		sqlSession.update("com.dm.platform.model.InboxEntity.isTrashInbox",argMap);
	}

	@Override
	public void deleteInbox(Integer tId) {
		// TODO Auto-generated method stub
		Map argMap = new HashMap();
		argMap.put("tId", tId);
		argMap.put("deleteState", "1");
		sqlSession.update("com.dm.platform.model.InboxEntity.deleteInbox",argMap);
	}


}
