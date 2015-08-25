package com.dm.platform.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.ApproveEntity;
import com.dm.platform.model.TempUser;
import com.dm.platform.model.UserAccount;
import com.dm.platform.model.UserRole;
import com.dm.platform.service.ApplyService;
import com.dm.platform.service.ApproveService;
import com.dm.platform.service.TempUserService;
import com.dm.platform.service.UserAccountService;
import com.dm.platform.util.CommonStatics;
import com.dm.platform.util.DmDateUtil;
import com.dm.platform.util.UserAccountUtil;


@Service
public class ApproveServiceImpl implements ApproveService {
	@Resource
	CommonDAO commonDAO;
	@Resource
	JdbcTemplate jdbcTemplate;
	@Resource
	TempUserService tempUserService;
	@Resource
	UserAccountService userAccountService;
	@Resource
	ApplyService applyService;
	@Override
	public Map<String,String> newApprove(ApproveEntity entity) {
		// TODO Auto-generated method stub
			Map<String,String> map = new HashMap<String,String>();
			entity.setApproveId(String.valueOf(System.currentTimeMillis()));
			String approveUserId = UserAccountUtil.getInstance()
					.getCurrentUserId();
			entity.setApproveUserId(approveUserId);
			entity.setApproveDate(DmDateUtil.DateToStr(new Date()));
			commonDAO.save(entity);
			map.put("approveUserId", approveUserId);
			map.put("approveStatus", entity.getApproveStatus());
			map.put("applyId", entity.getApplyId());
			return map;
	}

	@Override
	public void editApprove(ApproveEntity entity) {
		// TODO Auto-generated method stub
		commonDAO.update(entity);
	}

	@Override
	public List<ApproveEntity> getApprovesByApplyId(String applyId) {
		// TODO Auto-generated method stub
		return commonDAO.findByPropertyName(ApproveEntity.class, "applyId",
				applyId);
	}

	@Override
	public List<ApproveEntity> getApprovesByApproveUserId(String approveUserId) {
		// TODO Auto-generated method stub
		return commonDAO.findByPropertyName(ApproveEntity.class,
				"approveUserId", approveUserId);
	}

	@Override
	public ApproveEntity getApprove(String applyId, String approveUserId) {
		// TODO Auto-generated method stub
		ApproveEntity entity = null;
		entity = commonDAO.findByPropertyName(ApproveEntity.class,
				new String[] { "applyId", "approveUserId" },
				new Object[] { applyId, approveUserId }).get(0);
		return entity;
	}

	@Override
	public ApproveEntity getApproveById(String approveId) {
		// TODO Auto-generated method stub
		return commonDAO.findOne(ApproveEntity.class, approveId);
	}

	@Override
	public void changeStatus(String UserId, String approveStatus,String applyId) {
		// TODO Auto-generated method stub
		String sql="UPDATE T_APPLY_USER T SET T.APPROVE_STATUS=? WHERE T.APPLY_ID=? AND T.APPROVE_USER_ID=?";
		if(approveStatus.equals("2")){
			jdbcTemplate.update(sql,new Object[]{"2",applyId,UserId});
		}else{
			jdbcTemplate.update(sql,new Object[]{"3",applyId,UserId});
		}
	}

	@Override
	public boolean doProxy(String applyId,String applyUserId,String applyObjType,String applyObjId,boolean result) {
		// TODO Auto-generated method stub
		try{
			if(applyObjType.equals(CommonStatics.USER_REG)){
				approveReg(applyUserId,applyObjType,applyObjId,result);
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	private void approveReg(String applyUserId,String applyObjType,String applyObjId,boolean result){
		TempUser tuser = tempUserService.getTempUserById(applyUserId);
		if(result){
			UserAccount user = tempUserService.copyToUserAccount(tuser);
			user.setCode(String.valueOf(System.currentTimeMillis()));
			ShaPasswordEncoder sha = new ShaPasswordEncoder();
			sha.setEncodeHashAsBase64(false);
			user.setPassword(sha.encodePassword(
					tuser.getPassword(), null));
			user.setEnabled(true);
			user.setNonLocked(true);
			user.setAccountExpired(true);
			user.setPasswordExpired(true);
			UserRole role = commonDAO.findOne(UserRole.class, applyObjId);
			user.addRole(role);
			user.setOrg(null);
			userAccountService.insertUser(user);
			//发送消息
			tuser.setStatus("1");
			tempUserService.update(tuser);
		}else{
			tempUserService.deleteTempUserById(applyUserId);
		}
	}

}
