package com.dm.platform.service;

import java.util.List;
import java.util.Map;

import com.dm.platform.model.LogEntity;
import com.github.pagehelper.PageInfo;

public interface LogService {
	public List<LogEntity> listLogEntity(LogEntity le, int thispage, int pagesize); 
	public Long countLog(LogEntity le);
	public LogEntity findOne(long Id);
	public void insert(LogEntity le);
	public void deleteOne(LogEntity entity);
	public void Log(String user,String ip,String type,String title,String content);
	
	public PageInfo<Map> findLogs(Integer pageNum,Integer pageSize,Map argMap);
	
	public void deleteAllLogs();
}
