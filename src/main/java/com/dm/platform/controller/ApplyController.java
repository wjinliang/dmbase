package com.dm.platform.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dm.platform.model.ApplyEntity;
import com.dm.platform.model.ApproveEntity;
import com.dm.platform.service.ApplyService;
import com.dm.platform.service.ApproveService;
import com.dm.platform.util.CommonStatics;
import com.dm.platform.util.UserAccountUtil;

@Controller
@RequestMapping("/apply")
public class ApplyController extends DefaultController{
	@Resource
	ApplyService applyService;
	@Resource
	ApproveService approveService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/list")
	public ModelAndView list(ModelAndView model, String approveStatus,
			String applyObjType,
			Integer thispage,Integer pagesize) {
		try {
			if(pagesize==null){
				pagesize=20;
			}
			if (thispage == null) {
				thispage = 0;
			}
			if (approveStatus == null)
				approveStatus = CommonStatics.NEED_APPROVE;
			if (applyObjType == null)
				applyObjType = "";
			List<ApplyEntity> list = new ArrayList<ApplyEntity>();
			String currentUserId = UserAccountUtil.getInstance()
					.getCurrentUserId();
			Map<String,Object> map = applyService.getApproves(currentUserId,applyObjType,approveStatus,thispage, pagesize);
			list = (List<ApplyEntity>)map.get("items");
			Long totalcount = (Long)map.get("totalcount");
			if((thispage)*pagesize>=totalcount&&totalcount>0){
				thispage--;
			}
			model.addObject("applys", list);
			model.addObject("approveStatus", approveStatus);
			model.addObject("applyObjType", applyObjType);
			model.setViewName("/pages/admin/apply/list");
			return Model(model,thispage,pagesize,totalcount);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/myApplyList")
	public ModelAndView myApplyList(ModelAndView model, String approveStatus,
			String applyObjType,Integer thispage,Integer pagesize) {
		try {
			if(pagesize==null){
				pagesize=20;
			}
			if (thispage == null) {
				thispage = 0;
			}
			if (approveStatus == null)
				approveStatus = CommonStatics.NEED_APPROVE;
			if (applyObjType == null)
				applyObjType = "";
			List<ApplyEntity> list = new ArrayList<ApplyEntity>();
			String currentUserId = UserAccountUtil.getInstance()
					.getCurrentUserId();
			Map<String,Object> map = applyService.getApplies(currentUserId, approveStatus, thispage, pagesize);
			list = (List<ApplyEntity>)map.get("items");
			Long totalcount = (Long)map.get("totalcount");
			if((thispage)*pagesize>=totalcount&&totalcount>0){
				thispage--;
			}
			model.addObject("applys", list);
			model.addObject("approveStatus", approveStatus);
			model.addObject("applyObjType", applyObjType);
			model.setViewName("/pages/admin/apply/myApplyList");
			return Model(model,thispage,pagesize,totalcount);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}
	
	@RequestMapping("/approveForm")
	public ModelAndView approveForm(
			ModelAndView model,String applyId) {
		try {
			if(applyId!=null){
				ApplyEntity entity = new ApplyEntity();
				entity=applyService.getApplyById(applyId);
				model.addObject("apply", entity);
				List<ApproveEntity> list = new ArrayList<ApproveEntity>();
				list = approveService.getApprovesByApplyId(applyId);
				model.addObject("approves",list);
				model.setViewName("/pages/admin/apply/approveForm");
			}else{
				model.addObject("msg","异常：id不存在！");
				model.addObject("redirect",getRootPath()+"/apply/list");
				model.setViewName("/pages/content/redirect");
			}
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}
	
	@RequestMapping("/saveApprove")
	public ModelAndView saveApprove(ModelAndView model, String applyObjType,
			String applyObjId, ApproveEntity entity) {
		try {
			ApplyEntity apply = applyService.getApplyById(entity.getApplyId());
			if (apply.getApplyStatus().equals("1")) {
				// 生成审批
				Map<String, String> resultMap = new HashMap<String, String>();
				resultMap = approveService.newApprove(entity);
				// 同步关联表审批状态
				approveService.changeStatus(resultMap.get("approveUserId"),
						resultMap.get("approveStatus"),
						resultMap.get("applyId"));
				//获取当前审批等级
				Integer lv = applyService.getApproveLevel(resultMap.get("approveUserId"), resultMap.get("applyId"));
				//清理同级别的其余审批（将其余审批状态置为已过期）
				applyService.cleanApproveStatus(resultMap.get("applyId"),lv);

				boolean result = resultMap.get("approveStatus").equals("2") ? true
						: false;
				// 业务操作
				if (approveService.doProxy(apply.getApplyId(),
						apply.getApplyUserId(), applyObjType, applyObjId,
						result)) {
					// 结束审批
					applyService.endApply(resultMap.get("applyId"),
							resultMap.get("approveStatus"));
				}
				return Redirect(model, getRootPath() + "/apply/list", "审批成功！");
			} else {
				return Redirect(model, getRootPath() + "/apply/list", "审批已结束！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RedirectError(model, getRootPath() + "/apply/list", "审批失败！");
		}
	}
	
	@RequestMapping("/batchApprove")
	public ModelAndView batchApprove(ModelAndView model, String applyIds,
			String batchApproveStatus) {
		try {
			String[] applyIdArray = applyIds.split(",");
			for (String applyId : applyIdArray) {
				ApplyEntity entity = applyService.getApplyById(applyId);
				// 只操作状态为待审批的申请单
				if (entity.getApplyStatus().equals("1")) {
					// 同意
					if (batchApproveStatus.equals("2")) {
						ApproveEntity approve = new ApproveEntity();
						approve.setApplyId(applyId);
						approve.setApproveStatus("2");
						approve.setApproveComment("同意。（批量审批）");
						Map<String, String> resultMap = new HashMap<String, String>();
						resultMap = approveService.newApprove(approve);
						approveService.changeStatus(
								resultMap.get("approveUserId"),
								resultMap.get("approveStatus"),
								resultMap.get("applyId"));
						//获取当前审批等级
						Integer lv = applyService.getApproveLevel(resultMap.get("approveUserId"), resultMap.get("applyId"));
						//清理同级别的其余审批（将其余审批状态置为已过期）
						applyService.cleanApproveStatus(resultMap.get("applyId"),lv);
						boolean result = true;
						// 业务操作
						if(approveService.doProxy(applyId,
								entity.getApplyUserId(),
								entity.getApplyObjType(),
								entity.getApplyObjId(), result)){
							applyService.endApply(resultMap.get("applyId"),
									resultMap.get("approveStatus"));
						}
					} else if (batchApproveStatus.equals("3")) {
						ApproveEntity approve = new ApproveEntity();
						approve.setApplyId(applyId);
						approve.setApproveStatus("3");
						approve.setApproveComment("驳回。（批量审批）");
						Map<String, String> resultMap = new HashMap<String, String>();
						resultMap = approveService.newApprove(approve);
						approveService.changeStatus(
								resultMap.get("approveUserId"),
								resultMap.get("approveStatus"),
								resultMap.get("applyId"));
						//获取当前审批等级
						Integer lv = applyService.getApproveLevel(resultMap.get("approveUserId"), resultMap.get("applyId"));
						//清理同级别的其余审批（将其余审批状态置为已过期）
						applyService.cleanApproveStatus(resultMap.get("applyId"),lv);
						boolean result = false;
						// 业务操作
						if(approveService.doProxy(applyId,
								entity.getApplyUserId(),
								entity.getApplyObjType(),
								entity.getApplyObjId(), result)){
							applyService.endApply(resultMap.get("applyId"),
									resultMap.get("approveStatus"));
						}
					}
				}
			}
			return Redirect(model, getRootPath() + "/apply/list", "批量审批成功！");
		} catch (Exception e) {
			return RedirectError(model, getRootPath() + "/apply/list",
					"批量审批失败！");
		}
	}
}
