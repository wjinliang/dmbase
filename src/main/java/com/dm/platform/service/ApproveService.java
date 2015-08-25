package com.dm.platform.service;

import java.util.List;
import java.util.Map;

import com.dm.platform.model.ApproveEntity;

public interface ApproveService {
	public Map<String,String> newApprove(ApproveEntity entity);
	public void editApprove(ApproveEntity entity);
	public void changeStatus(String UserId,String approveStatus,String applyId);
	public List<ApproveEntity> getApprovesByApplyId(String applyId);
	public List<ApproveEntity> getApprovesByApproveUserId(String approveUserId);
	public ApproveEntity getApprove(String applyId,String approveUserId);
	public ApproveEntity getApproveById(String approveId);
	//业务操作
	public boolean doProxy(String applyId,String applyUserId,String applyObjType,String applyObjId,boolean result);
}
