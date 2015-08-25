package com.dm.platform.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.ApplyEntity;
import com.dm.platform.service.ApplyService;
import com.dm.platform.util.DmDateUtil;

@Service
public class ApplyServiceImpl implements ApplyService{
	@Resource
	CommonDAO commonDAO;
	@Resource
	JdbcTemplate jdbcTemplate;
	@Override
	public void newApply(ApplyEntity entity) {
		// TODO Auto-generated method stub
		commonDAO.save(entity);
	}

	@Override
	public void editApply(ApplyEntity entity) {
		// TODO Auto-generated method stub
		commonDAO.update(entity);
	}

	@Override
	public void sendApply(String applyId, String approveUserId, String approveStatus,Integer approveLevel) {
		// TODO Auto-generated method stub
		String sql="INSERT INTO T_APPLY_USER(APPLY_ID,APPROVE_USER_ID,APPROVE_STATUS,APPROVE_LV) VALUES(?,?,?,?)";
		jdbcTemplate.update(sql,new Object[]{applyId,approveUserId,approveStatus,approveLevel});
	}

	@Override
	public void cleanApproveStatus(String applyId,Integer approveLevel) {
		// TODO Auto-generated method stub
		String sql="UPDATE T_APPLY_USER T SET T.APPROVE_STATUS='0' WHERE T.APPLY_ID=? AND T.APPROVE_LV=? AND T.APPROVE_STATUS='1'";
		jdbcTemplate.update(sql,new Object[]{applyId,approveLevel});
	}
	
	@Override
	public void cleanApproveStatus(String applyId) {
		// TODO Auto-generated method stub
		String sql="UPDATE T_APPLY_USER T SET T.APPROVE_STATUS='0' WHERE T.APPLY_ID=? AND T.APPROVE_STATUS='1'";
		jdbcTemplate.update(sql,new Object[]{applyId});
	}

	@Override
	public void endApply(String applyId, String applyStatus) {
		// TODO Auto-generated method stub
		ApplyEntity entity=getApplyById(applyId);
		if(entity!=null){
			entity.setApplyStatus(applyStatus);
			entity.setApprovedDate(DmDateUtil.DateToStr(new Date()));
			editApply(entity);
		}
	}

	@Override
	public String getApplyResult(String applyId) {
		// TODO Auto-generated method stub
		String result="";
		String sql="SELECT T.APPROVE_STATUS AS STAUTS,COUNT(T.APPROVE_USER_ID) AS COUNT FROM T_APPLY_USER T GROUP BY T.APPROVE_STATUS";
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list=jdbcTemplate.queryForList(sql);
		for (Map<String, Object> map : list) {
			if(map.get("STAUTS").equals("0")){
				result=result+"未审批："+String.valueOf(map.get("COUNT"))+"人；";
			}else if(map.get("STAUTS").equals("1")){
				result=result+"待审批："+String.valueOf(map.get("COUNT"))+"人；";
			}else if(map.get("STAUTS").equals("2")){
				result=result+"同意："+String.valueOf(map.get("COUNT"))+"人；";
			}else if(map.get("STAUTS").equals("3")){
				result=result+"不同意："+String.valueOf(map.get("COUNT"))+"人；";
			}
		}
		
		return result;
	}

	@Override
	public List<ApplyEntity> getApplies(String applyUserId, String approveStatus) {
		// TODO Auto-generated method stub
		List<ApplyEntity> list = new ArrayList<ApplyEntity>();
		String[] names=null;
		Object[] objects = null;
		if(applyUserId!=null&&approveStatus!=null){
			names=new String[]{"applyUserId","applyStatus"};
			objects=new Object[]{applyUserId,approveStatus};
		}else if(applyUserId==null&&approveStatus!=null){
			names=new String[]{"applyStatus"};
			objects=new Object[]{approveStatus};
		}else if(applyUserId!=null&&approveStatus==null){
			names=new String[]{"applyUserId"};
			objects=new Object[]{applyUserId};
		}else{
			return null;
		}
		list=commonDAO.findByPropertyName(ApplyEntity.class, names,objects);
		return list;
	}

	@Override
	public List<ApplyEntity> getApproves(String approveUserId,
			String approveStatus) {
		// TODO Auto-generated method stub
		List<ApplyEntity> list = new ArrayList<ApplyEntity>();
		String sql="SELECT G.* FROM T_APPLY_USER T,T_APPLY G WHERE G.APPLY_ID=T.APPLY_ID AND T.APPROVE_USER_ID=? AND APPROVE_STATUS=? ORDER BY G.APPLY_DATE DESC";
		List<Map<String, Object>> applies= jdbcTemplate.queryForList(sql, new Object[]{approveUserId,approveStatus});
		for (Map<String, Object> apply : applies) {
			ApplyEntity entity= new ApplyEntity();
			entity.setApplyId((String)apply.get("APPLY_ID"));
			entity.setApplyContent((String)apply.get("APPLY_CONTENT"));
			entity.setApplyDate((String)apply.get("APPLY_DATE"));
			entity.setApplyObjId((String)apply.get("APPLY_OBJ_ID"));
			entity.setApplyObjType((String)apply.get("APPLY_OBJ_TYPE"));
			entity.setApplyStatus((String)apply.get("APPLY_STATUS"));
			entity.setApplyUserId((String)apply.get("APPLY_USER_ID"));
			entity.setApplyStatus((String)apply.get("APPROVEDDATE"));
			list.add(entity);
		}
		return list;
	}

	@Override
	public ApplyEntity getApplyById(String applyId) {
		// TODO Auto-generated method stub
		ApplyEntity entity = commonDAO.findOne(ApplyEntity.class, applyId);
		return entity;
	}

	@Override
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

	@Override
	public Map<String, Object> getApplies(String applyUserId,
			String applyStatus, Integer thispage, Integer pagesize) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new  HashMap<String,Object>();
		List<ApplyEntity> list = new ArrayList<ApplyEntity>();
		String whereSql=" from ApplyEntity t where 1=1";
		ApplyEntity entity = new ApplyEntity();
		if(applyUserId!=null&&!applyUserId.equals("")){
			entity.setApplyUserId(applyUserId);
			whereSql+=" and t.applyUserId=:applyUserId";
		}
		if(applyStatus!=null&&!applyStatus.equals("")){
			entity.setApplyStatus(applyStatus);
			whereSql+=" and t.applyStatus=:applyStatus";
		}
		list=commonDAO.findByPage(whereSql, ApplyEntity.class, entity, thispage, pagesize, null);
		map.put("items",list);
		map.put("totalcount", commonDAO.count(ApplyEntity.class, whereSql, entity));
		return map;
	}

	@Override
	public List<ApplyEntity> getApplies(String applyType, String applyObjId,
			String applyStatus) {
		// TODO Auto-generated method stub
		List<ApplyEntity> list = new ArrayList<ApplyEntity>();
		String whereSql=" from ApplyEntity t where 1=1";
		ApplyEntity entity = new ApplyEntity();
		if(applyObjId!=null&&!applyObjId.equals("")){
			entity.setApplyObjId(applyObjId);
			whereSql+=" and t.applyObjId=:applyObjId";
		}
		if(applyType!=null&&!applyType.equals("")){
			entity.setApplyObjType(applyType);
			whereSql+=" and t.applyType=:applyType";
		}
		if(applyStatus!=null&&!applyStatus.equals("")){
			entity.setApplyStatus(applyStatus);
			whereSql+=" and t.applyStatus=:applyStatus";
		}
		commonDAO.findAll(ApplyEntity.class,whereSql, entity, null);
		return list;
	}

	@Override
	public Map<String, Object> getApplies(String applyType, String applyObjId,
			String applyStatus, Integer thispage, Integer pagesize) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new  HashMap<String,Object>();
		List<ApplyEntity> list = new ArrayList<ApplyEntity>();
		String whereSql=" from ApplyEntity t where 1=1";
		ApplyEntity entity = new ApplyEntity();
		if(applyObjId!=null&&!applyObjId.equals("")){
			entity.setApplyObjId(applyObjId);
			whereSql+=" and t.applyObjId=:applyObjId";
		}
		if(applyType!=null&&!applyType.equals("")){
			entity.setApplyObjType(applyType);
			whereSql+=" and t.applyType=:applyType";
		}
		if(applyStatus!=null&&!applyStatus.equals("")){
			entity.setApplyStatus(applyStatus);
			whereSql+=" and t.applyStatus=:applyStatus";
		}
		list=commonDAO.findByPage(whereSql, ApplyEntity.class, entity, thispage, pagesize, null);
		map.put("items",list);
		map.put("totalcount", commonDAO.count(ApplyEntity.class, whereSql, entity));
		return map;
	}

	@Override
	public Map<String,Object> getApproves(String approveUserId,String applyType,
			String approveStatus,Integer thispage,Integer pagesize) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String,Object>();
		List<ApplyEntity> list = new ArrayList<ApplyEntity>();
		String sql="";
		List<Map<String, Object>> applies=null;
		if(!applyType.equals("")){
			sql="SELECT G.* FROM T_APPLY_USER T,T_APPLY G WHERE G.APPLY_ID=T.APPLY_ID AND T.APPROVE_USER_ID=:approveUserId AND APPROVE_STATUS=:approveStatus" +
				" AND G.APPLY_OBJ_TYPE IN (:applType) ORDER BY G.APPLY_DATE DESC limit :thispage,:pagesize";
			List<String> typeList = new ArrayList<String>(); 
			String[] types = applyType.split(",");
			for (String string : types) {
				typeList.add(string);
			}
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("applType", typeList);
			parameters.put("approveUserId", approveUserId);
			parameters.put("approveStatus", approveStatus);
			parameters.put("thispage", thispage);
			parameters.put("pagesize", pagesize);
			NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
			applies= namedJdbcTemplate.queryForList(sql,parameters);
		}else{
			sql="SELECT G.* FROM T_APPLY_USER T,T_APPLY G WHERE G.APPLY_ID=T.APPLY_ID AND T.APPROVE_USER_ID=? AND APPROVE_STATUS=?" +
					" ORDER BY G.APPLY_DATE DESC limit ?,?";
				applies= jdbcTemplate.queryForList(sql, new Object[]{approveUserId,approveStatus,thispage,pagesize});
		}
		for (Map<String, Object> apply : applies) {
			ApplyEntity entity= new ApplyEntity();
			entity.setApplyId((String)apply.get("APPLY_ID"));
			entity.setApplyContent((String)apply.get("APPLY_CONTENT"));
			entity.setApplyDate((String)apply.get("APPLY_DATE"));
			entity.setApplyObjId((String)apply.get("APPLY_OBJ_ID"));
			entity.setApplyObjType((String)apply.get("APPLY_OBJ_TYPE"));
			entity.setApplyStatus((String)apply.get("APPLY_STATUS"));
			entity.setApplyUserId((String)apply.get("APPLY_USER_ID"));
			entity.setApprovedDate((String)apply.get("APPROVEDDATE"));
			list.add(entity);
		}
		map.put("items", list);
		String countSql="";
		Long totalcount = new Long(0);
		if(!applyType.equals("")){
			countSql="SELECT COUNT(*) FROM T_APPLY_USER T,T_APPLY G WHERE G.APPLY_ID=T.APPLY_ID AND T.APPROVE_USER_ID=:approveUserId AND APPROVE_STATUS=:approveStatus" +
					" AND G.APPLY_OBJ_TYPE IN (:applType)";
			List<String> typeList = new ArrayList<String>(); 
			String[] types = applyType.split(",");
			for (String string : types) {
				typeList.add(string);
			}
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("applType", typeList);
			parameters.put("approveUserId", approveUserId);
			parameters.put("approveStatus", approveStatus);
			NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
			totalcount= namedJdbcTemplate.queryForObject(countSql,parameters,Long.class);
		}else{
			countSql="SELECT COUNT(*) FROM T_APPLY_USER T,T_APPLY G WHERE G.APPLY_ID=T.APPLY_ID AND T.APPROVE_USER_ID=? AND APPROVE_STATUS=?";
			totalcount =jdbcTemplate.queryForObject(countSql, new Object[]{approveUserId,approveStatus}, Long.class);
		}
		map.put("totalcount", totalcount);
		return map;
	}

	@Override
	public Integer getApproveLevel(String userId, String applyId) {
		// TODO Auto-generated method stub
		String countSql="SELECT COUNT(*) FROM T_APPLY_USER T,T_APPLY G WHERE G.APPLY_ID=? AND T.APPROVE_USER_ID=?";
		Integer totalcount =jdbcTemplate.queryForObject(countSql, new Object[]{applyId,userId}, Integer.class);
		return totalcount;
	}

}
