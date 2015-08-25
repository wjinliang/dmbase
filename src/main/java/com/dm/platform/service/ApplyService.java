package com.dm.platform.service;

import java.util.List;
import java.util.Map;

import com.dm.platform.model.ApplyEntity;

public interface ApplyService {
	public void newApply(ApplyEntity entity);
	public void editApply(ApplyEntity entity);
	public ApplyEntity getApplyById(String applyId);
	public void sendApply(String applyId,String approveUserId,String approveStatus,Integer approveLevel);
	//并联审批当需要结束审批时，清理其它用户关联状态
	public void cleanApproveStatus(String applyId,Integer approveLevel);
	public void cleanApproveStatus(String applyId);
	public void endApply(String applyId,String applyStatus);
	//返回申请表的意见总结
	public String getApplyResult(String applyId);
	public String getApproveStatus(String userId,String applyId);
	public Integer getApproveLevel(String userId,String applyId);
	public List<ApplyEntity> getApplies(String applyUserId,String applyStatus);
	public Map<String,Object> getApplies(String applyUserId,String applyStatus,Integer thispage,Integer pagesize);
	public List<ApplyEntity> getApplies(String applyType,String applyObjId,String applyStatus);
	public Map<String,Object> getApplies(String applyType,String applyObjId,String applyStatus,Integer thispage,Integer pagesize);
	public List<ApplyEntity> getApproves(String approveUserId,String approveStatus);
	public Map<String,Object> getApproves(String approveUserId,
			String approveStatus,String applyType,Integer thispage,Integer pagesize);
	
}
