package com.dm.platform.service;

import java.util.List;
import java.util.Map;

import com.dm.platform.dto.InboxDto;
import com.dm.platform.model.FileEntity;
import com.dm.platform.model.InboxEntity;
import com.github.pagehelper.PageInfo;

public interface InboxService {
	/**
	 * 插入消息
	 * @param entity 
	 * @return 插入消息的id
	 */
	public String insert(InboxEntity entity);
	/**
	 * 更新消息
	 * @param entity
	 */
	public void update(InboxEntity entity);
	/**
	 * 删除消息
	 * @param id 消息id
	 */
	public void delete(String id);
	/**
	 * 查找消息
	 * @param id
	 * @return
	 */
	public InboxEntity findOne(String id);
	/**
	 * 获取消息
	 * @param tId
	 * @return
	 */
	public InboxDto read(String tId);
	/**
	 * 获取消息列表
	 * @param pageNum
	 * @param pageSize
	 * @param argMap
	 * @return
	 */
	public PageInfo<InboxDto> findInboxList(Integer pageNum,Integer pageSize,Map argMap);
	/**
	 * 发送消息
	 * @param inboxId 消息id
	 * @param userId 接收人id
	 */
	public void sendInbox(String inboxId,String toUserId);
	/**
	 * 保存已发送消息
	 * @param inboxId 消息id
	 * @param fromUserId 发送人id
	 */
	public void saveSentInbox(String inboxId,String fromUserId);
	/**
	 * 保存消息附件
	 * @param inboxId
	 * @param attachmentId
	 */
	public void saveAttachement(String inboxId,String attachmentId);
	/**
	 * 
	 * @param inboxId
	 */
	public void deleteAttachements(String inboxId);
	/**
	 * 设置消息未读已读
	 * @param tId 关联表id
	 * @param readState 0 未读 1 已读
	 */
	public void isRead(String tId,String readState);
	/**
	 * 设置消息未标记 标记
	 * @param tId 关联表id
	 * @param markState 0未标记 1已标记
	 */
	public void isMark(String tId,String markState);
	/**
	 * 获取消息附件列表
	 * @param inboxId
	 * @return
	 */
	public List<FileEntity> findAttachements(String inboxId);
	/**
	 * 新增草稿
	 * @param inboxId
	 * @param fromUserId
	 * @return
	 */
	public Integer insertDraft(String inboxId,String fromUserId);
	/**
	 * 更新草稿时间
	 * @param tId
	 * @return
	 */
	public void updateDraftTime(Integer tId);
	/**
	 * 删除草稿
	 * @param tId
	 */
	public void deleteDraft(Integer tId);
	/**
	 * 创建新的消息
	 * @param subject
	 * @param content
	 * @return
	 */
	public String newSimpleInbox(String subject,String content,String toUserStr);
	
	public void isTrash(Integer tId,String trashState);
	
	public void deleteInbox(Integer tId);
}
