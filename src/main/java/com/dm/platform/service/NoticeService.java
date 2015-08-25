package com.dm.platform.service;

import java.util.Map;

import com.dm.platform.model.NoticeEntity;

public interface NoticeService {
	public void insert(NoticeEntity entity);
	public void update(NoticeEntity entity);
	public NoticeEntity findOne(String noticeId);
	public void deleteOne(String noticeId);
	public void setTop(String noticeId);
	public void cancelTop(String noticeId);
	public Map<String,Object> getAll(Integer thispage, Integer pagesize);
}
