package com.dm.platform.util;

public class DtldsUtil {
	public static String getUserName(String userId){
		return UserAccountUtil.getInstance().getUserNameById(userId);
	}
	public static String getUserLoginname(String userId){
		return UserAccountUtil.getInstance().getCurrentUser();
	}
	public static String getRemoteAddr(String userId){
		return UserAccountUtil.getInstance().getUserRemoteIpById(userId);
	}
	public static String getLastLoginTime(String userId){
		return UserAccountUtil.getInstance().getUserLastLoginTimeById(userId);
	}
	public static String getMyStatus(String applyId){
		//待用缓存
		String userId = UserAccountUtil.getInstance().getCurrentUserId();
		return DtldsSingleton.getInstance().getApproveStatus(userId,applyId);
	}
	public static int getApproveNum(String approveStatus){
		String userId = UserAccountUtil.getInstance().getCurrentUserId();
		return DtldsSingleton.getInstance().getApproveNum(userId, approveStatus);
	}
	public static int getApproveNum(String approveStatus,String applyObjType){
		String userId = UserAccountUtil.getInstance().getCurrentUserId();
		return DtldsSingleton.getInstance().getApproveNum(userId, approveStatus,applyObjType);
	}
	public static boolean ApproveAble(String applyId){
		String userId = UserAccountUtil.getInstance().getCurrentUserId();
		return DtldsSingleton.getInstance().ApproveAble(userId, applyId);
	}
	public static String getApplyDetail(String applyId){
		return DtldsSingleton.getInstance().getApplyDetail(applyId);
	}
	
}
