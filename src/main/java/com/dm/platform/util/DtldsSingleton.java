package com.dm.platform.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class DtldsSingleton {
	public ApplicationContext ct = new ClassPathXmlApplicationContext(
			"config/spring/applicationContext.xml");
	public JdbcTemplate jdbcTemplate = (JdbcTemplate)ct.getBean("jdbcTemplate");
	private static DtldsSingleton instance = new DtldsSingleton();
	private DtldsSingleton(){
	}
	public static DtldsSingleton getInstance(){
		return instance;
	}
	
	public String getApproveStatus(String userId,String applyId) {
		// TODO Auto-generated method stub
		String result="";
		String sql="SELECT T.APPROVE_STATUS AS STAUTS FROM T_APPLY_USER T WHERE T.APPROVE_USER_ID=? AND T.APPLY_ID=?";
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list=jdbcTemplate.queryForList(sql,new Object[]{userId,applyId});
		for (Map<String, Object> map : list) {
			if(map.get("STAUTS").equals("0")){
				result=result+"未审批";
			}else if(map.get("STAUTS").equals("1")){
				result=result+"待审批";
			}else if(map.get("STAUTS").equals("2")){
				result=result+"同意";
			}else if(map.get("STAUTS").equals("3")){
				result=result+"不同意";
			}
		}
		return result;
	}
	
	public int getApproveNum(String userId,String approveStatus){
		String sql="SELECT COUNT(*) FROM T_APPLY_USER T WHERE T.APPROVE_USER_ID=? AND T.APPROVE_STATUS=?";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[]{userId,approveStatus});
		return count.intValue();
	}
	
	public int getApproveNum(String userId, String approveStatus,
			String applyObjType) {
		String sql = "";
		Integer count = 0;
		Map<String, Object> parameters = new HashMap<String, Object>();
		if (!applyObjType.equals("")) {
			List<String> typeList = new ArrayList<String>();
			String[] types = applyObjType.split(",");
			for (String string : types) {
				typeList.add(string);
			}
			parameters.put("applyObjType", typeList);
		}
		if (!approveStatus.equals("")) {
			parameters.put("approveStatus", approveStatus);
		}
		parameters.put("userId", userId);
		NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate);
		if (!approveStatus.equals("") && !applyObjType.equals("")) {
			sql = "SELECT COUNT(*) FROM T_APPLY_USER T,T_APPLY G WHERE T.APPLY_ID=G.APPLY_ID "
					+ " AND T.APPROVE_USER_ID=:userId AND T.APPROVE_STATUS=:approveStatus AND G.APPLY_OBJ_TYPE IN (:applyObjType)";
		} else if (!approveStatus.equals("") && applyObjType.equals("")) {
			sql = "SELECT COUNT(*) FROM T_APPLY_USER T WHERE T.APPROVE_USER_ID=:userId AND T.APPROVE_STATUS=:approveStatus";
		} else if (approveStatus.equals("") && !applyObjType.equals("")) {
			sql = "SELECT COUNT(*) FROM T_APPLY_USER T,T_APPLY G WHERE T.APPLY_ID=G.APPLY_ID AND T.APPROVE_USER_ID=:userId AND G.APPLY_OBJ_TYPE IN (:applyObjType)";
		} else {
			sql = "SELECT COUNT(*) FROM T_APPLY_USER T WHERE T.APPROVE_USER_ID=:userId";
		}
		count = namedJdbcTemplate
				.queryForObject(sql, parameters, Integer.class);
		return count.intValue();
	}
	
	public boolean ApproveAble(String userId,String applyId){
		String sql="SELECT T.APPROVE_STATUS FROM T_APPLY_USER T WHERE T.APPROVE_USER_ID=? AND T.APPLY_ID=?";
		String status = jdbcTemplate.queryForObject(sql, String.class, new Object[]{userId,applyId});
		if(status!=null&&status.equals("1")){
			return true;
		}else{
			return false;
		}
	}
	
	public String getApplyDetail(String applyId) {
		String detail="";
		String sql="SELECT T.APPROVE_STATUS AS STAUTS,COUNT(T.APPROVE_USER_ID) AS COUNT FROM T_APPLY_USER T GROUP BY T.APPROVE_STATUS,T.APPLY_ID HAVING T.APPLY_ID=?";
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list=jdbcTemplate.queryForList(sql,new Object[]{applyId});
		int count = 0;
		for (Map<String, Object> map : list) {
			count+=(Long)map.get("COUNT");
			if(map.get("STAUTS").equals("0")){
				detail=detail+"已过期："+String.valueOf(map.get("COUNT"))+"人 ";
			}else if(map.get("STAUTS").equals("1")){
				detail=detail+"待审批："+String.valueOf(map.get("COUNT"))+"人 ";
			}else if(map.get("STAUTS").equals("2")){
				detail=detail+"同意："+String.valueOf(map.get("COUNT"))+"人 ";
			}else if(map.get("STAUTS").equals("3")){
				detail=detail+"不同意："+String.valueOf(map.get("COUNT"))+"人 ";
			}
		}
		detail="发送"+count+"人："+detail;
		return detail;
	}
	
}
