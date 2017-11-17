package com.ibm.kstar.action.custom.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomCompReportService;
import com.ibm.kstar.api.custom.ICustomCreditAdjustService;
import com.ibm.kstar.api.custom.ICustomEventService;
import com.ibm.kstar.api.custom.ICustomHandoverService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.custom.ICustomMergeService;
import com.ibm.kstar.api.custom.ICustomShareService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.custom.CustomCompetitorReport;
import com.ibm.kstar.entity.custom.CustomCreditAdjustment;
import com.ibm.kstar.entity.custom.CustomErpInfo;
import com.ibm.kstar.entity.custom.CustomEventInfo;
import com.ibm.kstar.entity.custom.CustomHandoverList;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.custom.CustomMerge;
import com.ibm.kstar.entity.custom.CustomShareInfo;
import com.ibm.kstar.entity.custom.vo.CustomErpAddress;

@Controller
@RequestMapping("/custom")
public class CustomProcessAction extends BaseAction {
	
	@Autowired
	IXflowProcessServiceWrapper xflowProcessServiceWrapper;
	
	@Autowired
	private ILovMemberService lovMemberService;
	
	@Autowired
	ICustomCompReportService report;
	
	@LogOperate(module = "客户管理模块：竞争对手提报", notes = "${user}工作流：竞争对手提报申请")
	@ResponseBody
	@RequestMapping("/reportApply")
	public String reportApply(CustomCompetitorReport customCompetitorReport) {
		customCompetitorReport.setStatus(IConstants.CUSTOM_NORMAL_STATUS_40);
		
		if (StringUtils.isEmpty(customCompetitorReport.getId())) {
			report.saveReportInfo(customCompetitorReport);
		} else {
			report.updateReportInfo(customCompetitorReport);
		}
		
		return sendSuccessMessage("操作成功");
	}
	
	@Autowired
	ICustomMergeService merge;
	
	@LogOperate(module = "客户管理模块：客户合并", notes = "${user}工作流：客户合并申请")
	@ResponseBody
	@RequestMapping("/mergeApply")
	public String mergeApply(CustomMerge customMerge) {
		customMerge.setMergeStatus(IConstants.CUSTOM_NORMAL_STATUS_40);
		
		if (StringUtils.isEmpty(customMerge.getId())) {
			merge.saveMergeInfo(customMerge);
		} else {
			merge.updateMergeInfo(customMerge);
		}
		
		return sendSuccessMessage("操作成功");
	}
	
	@Autowired
	ICustomCreditAdjustService adjust;
	
	@LogOperate(module = "客户管理模块：评估调整", notes = "${user}工作流：评估调整申请")
	@ResponseBody
	@RequestMapping("/adjustApply")
	public String adjustApply(CustomCreditAdjustment customCreditAdjustment) {
		String id = customCreditAdjustment.getId();
		customCreditAdjustment.setStatus(IConstants.CUSTOM_NORMAL_STATUS_20);
		if (StringUtils.isEmpty(id)) {
			adjust.saveAdjustmentInfo(customCreditAdjustment, getUserObject());
			
			id = customCreditAdjustment.getId();
		} else {
			adjust.updateAdjustmentInfo(customCreditAdjustment, IConstants.CUSTOM_NORMAL_STATUS_20);
		}
		
		String model = lovMemberService.getFlowCodeByAppCode(IConstants.CUSTOM_ADJUST_PROC);
		
		Map<String,String> varmap = new HashMap<>();
		varmap.put("title", "评估调整 - 评估编号["+ customCreditAdjustment.getCreditAdjustmentCode() +"]");
		varmap.put("app", IConstants.CUSTOM_ADJUST_PROC);
		
		varmap.put("SalesCenter", lovMemberService.getSaleCenter(customCreditAdjustment.getApplierOrg()));
		
		xflowProcessServiceWrapper.start(model
				, id
				, getUserObject()
				,varmap);
		
		return sendSuccessMessage("操作成功");
	}
	
	@Autowired
	ICustomInfoService custom;
	
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}工作流：客户报备申请")
	@ResponseBody
	@RequestMapping("/customApply")
	public String customApply(CustomInfo customInfo) {
		String area = customInfo.getCustomArea();
		
		LovMember lov = lovMemberService.get(area);
		if (lov != null && StringUtils.equals(lov.getCode(), IConstants.ADDRESSREGION_CN)) {
			if (StringUtils.isEmpty(customInfo.getCustomAreaSub1()) 
					|| StringUtils.isEmpty(customInfo.getCustomAreaSub2())) {
				throw new AnneException("当国家为[中国]时，请输入省市!");
			}
			
			if (StringUtils.isEmpty(customInfo.getCorpTrn())) {
				throw new AnneException("当国家为[中国]时，报备必须输入纳税识别号!");
			}
		}
		
		
		
		String id = customInfo.getId();
		
		String model = lovMemberService.getFlowCodeByAppCode(IConstants.CUSTOM_REPORT_PROC);
		
		Map<String,String> varmap = new HashMap<>();
		varmap.put("title", "客户建档报备 - 客户名称["+customInfo.getCustomFullName()+"]");
		varmap.put("app", IConstants.CUSTOM_REPORT_PROC);
		
		varmap.put("CustomerClass", customInfo.getCustomGrade());
		varmap.put("EmployeeType", getUserObject().getOrg().getOptTxt3());
		varmap.put("SalesCenter", lovMemberService.getSaleCenter(getUserObject().getOrg().getId()));
		
		varmap.put("app", IConstants.CUSTOM_REPORT_PROC);
		
		xflowProcessServiceWrapper.start(model
				, id
				, getUserObject()
				,varmap);
	
		customInfo.setCustomReportedFlg(IConstants.CUSTOM_NORMAL_STATUS_20);
		custom.updateCustomInfo(customInfo);
		
		return sendSuccessMessage("操作成功");
	}
	
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}工作流：ERP引入申请")
	@ResponseBody
	@RequestMapping("/erpApply")
	public String erpApply(CustomInfo customInfo) {
		String id = customInfo.getId();
		CustomInfo tempCustomInfo =  custom.getCustomInfo(id);
		if(tempCustomInfo == null){
			throw new AnneException("没有找到客户");
		}
		if(StringUtil.isEmpty(tempCustomInfo.getCustomArea())){
			throw new AnneException("国家不能为空");
		}
		LovMember customArea = lovMemberService.get(tempCustomInfo.getCustomArea());
		if(customArea == null){
			throw new AnneException("没有找到对应的国家");
		}
		if(IConstants.ADDRESSREGION_CN.equals(customArea.getCode()) || "中国".equals(customArea.getName())){
			if(StringUtil.isEmpty(tempCustomInfo.getCustomAreaSub1()) 
					|| StringUtil.isEmpty(tempCustomInfo.getCustomAreaSub2())){
				throw new AnneException("国家为中国时，省份、城市不能为空");
			}
		}
		if(StringUtil.isEmpty(tempCustomInfo.getCustomCategory())){
			throw new AnneException("行业大类不能为空");
		}
		if(StringUtil.isEmpty(tempCustomInfo.getCustomCategorySub())){
			throw new AnneException("行业小类不能为空");
		}
		
		String model = lovMemberService.getFlowCodeByAppCode(IConstants.CUSTOM_ERP_PROC);
		
		String[] status = new String[]{IConstants.CUSTOM_NORMAL_STATUS_10, IConstants.CUSTOM_NORMAL_STATUS_30};
		
		List<CustomErpAddress> customErpAddress =  custom.getErpInfoByIdStatus(id, status);
		
		if(customErpAddress == null || customErpAddress.size() <= 0) {
			throw new AnneException("在“ERP引入”标签页下，没有维护可引入ERP的业务实体。");
		}
	
		Map<String,String> varmap = new HashMap<>();
		varmap.put("title", "ERP引入申请 - 客户名称["+tempCustomInfo.getCustomFullName()+"]");
		varmap.put("app", IConstants.CUSTOM_ERP_PROC);
		
		varmap.put("SalesCenter", lovMemberService.getSaleCenter(getUserObject().getOrg().getId()));
		
		xflowProcessServiceWrapper.start(model
				, id
				, getUserObject()
				, varmap);
	
		
		
		for (CustomErpAddress temp : customErpAddress) {
			CustomErpInfo customErpInfo = temp.getCustomErpInfo();
			customErpInfo.setCorpErpStatus(IConstants.CUSTOM_NORMAL_STATUS_20);
			custom.updateErpInfo(customErpInfo, IConstants.CUSTOM_NORMAL_STATUS_20);
		}
		
		return sendSuccessMessage("操作成功");
	}
	
	@Autowired
	ICustomEventService event;
	
	@LogOperate(module = "客户管理模块：客户活动", notes = "${user}工作流：客户活动申请")
	@ResponseBody
	@RequestMapping("/eventApply")
	public String eventApply(CustomEventInfo customEventInfo) {
		String id = customEventInfo.getId();
		customEventInfo.setEventStatus(IConstants.CUSTOM_NORMAL_STATUS_20);
		if (StringUtils.isEmpty(id)) {
			event.saveEventInfo(customEventInfo, getUserObject());
			
			id = customEventInfo.getId();
		} else {
			event.updateEventInfo(customEventInfo);
		}
		
		String model = lovMemberService.getFlowCodeByAppCode(IConstants.CUSTOM_EVENT_PROC);
		
		Map<String,String> varmap = new HashMap<>();
		varmap.put("title", "来访接待 - 接待编号["+customEventInfo.getEventCode()+"]");
		varmap.put("app", IConstants.CUSTOM_EVENT_PROC);
		
		varmap.put("CustomerClass", customEventInfo.getCustomGrade());
		varmap.put("SalesCenter", lovMemberService.getSaleCenter(customEventInfo.getApplierOrg()));
		
		xflowProcessServiceWrapper.start(model
				, id
				, getUserObject()
				,varmap);
	
		return sendSuccessMessage("操作成功");
	}

	@Autowired
	ICustomHandoverService handover;
	
	@LogOperate(module = "客户管理模块：客户交接", notes = "${user}工作流：客户交接申请")
	@ResponseBody
	@RequestMapping("/handoverApply")
	public String handoverApply(CustomHandoverList customHandoverList) {
//		if (StringUtils.equals(customHandoverList.getHandoverFrom(), customHandoverList.getHandoverTo())) {
//			throw new AnneException("交接人相同，不能合并!");
//		}
	
		
		String id = customHandoverList.getId();
		customHandoverList.setHandoverStatus(IConstants.CUSTOM_NORMAL_STATUS_20);
		
		Map<String,String> varmap = new HashMap<>();
		varmap.put("title", "客户交接 - 交接编号[" + customHandoverList.getHandoverCode() + "]");
		varmap.put("app", IConstants.CUSTOM_HANDOVER_PROC);
		varmap.put("FromSalesCenter", lovMemberService.getSaleCenter(customHandoverList.getHandoverFromOrg()));
		varmap.put("ToSalesCenter", lovMemberService.getSaleCenter(customHandoverList.getHandoverToOrg()));
		
		varmap.put("employeeIdInForm", customHandoverList.getHandoverTo());
		varmap.put("employeeNameInForm", customHandoverList.getHandoverToName());
		
		if (StringUtils.isEmpty(id)) {
			handover.startAndSaveHandoverProcess(getUserObject(), varmap, customHandoverList, IConstants.CUSTOM_NORMAL_STATUS_20);
		} else{
			handover.startHandoverProcess(id, getUserObject(), varmap, customHandoverList, IConstants.CUSTOM_NORMAL_STATUS_20);
		}
		
		return sendSuccessMessage("操作成功");
	}
	
	@Autowired
	ICustomShareService share;
	
	@LogOperate(module = "客户管理模块：共享授权", notes = "${user}工作流：共享授权申请")
	@ResponseBody
	@RequestMapping("/shareApply")
	public String shareApply(CustomShareInfo customShareInfo) {
		String id = customShareInfo.getId();
		
		customShareInfo.setShareStatus(IConstants.CUSTOM_NORMAL_STATUS_20);
		if (StringUtils.isEmpty(id)) {
			share.saveShareInfo(customShareInfo , getUserObject());
			
			id = customShareInfo.getId();
		} else {
			share.updateShareInfo(customShareInfo, IConstants.CUSTOM_NORMAL_STATUS_20);
		}
		
		String model = lovMemberService.getFlowCodeByAppCode(IConstants.CUSTOM_SHARE_PROC);
		
		Map<String,String> varmap = new HashMap<>();
		varmap.put("title", "共享授权 - 共享编号["+customShareInfo.getShareCode()+"]");
		varmap.put("app", IConstants.CUSTOM_SHARE_PROC);
		varmap.put("ApplicantSalesCenter", lovMemberService.getSaleCenter(customShareInfo.getApplierOrg()));
		varmap.put("ExistSalesCenter", lovMemberService.getSaleCenter(customShareInfo.getShareDept()));

		varmap.put("employeeIdInForm", customShareInfo.getShareSale());
		varmap.put("employeeNameInForm", customShareInfo.getShareSaleName());
		
		xflowProcessServiceWrapper.start(model
				, id
				, getUserObject()
				,varmap);
	
		return sendSuccessMessage("操作成功");
	}
	
	
	@LogOperate(module = "客户管理模块[PRM]：建档报备", notes = "${user}工作流：客户报备申请")
	@ResponseBody
	@RequestMapping("/prm/customPrmApply")
	public String customPrmApply(CustomInfo customInfo) {
		
		String id = customInfo.getId();
		
		String model = lovMemberService.getFlowCodeByAppCode(IConstants.CUSTOM_REPORT_PRM_PROC);
		
		Map<String,String> varmap = new HashMap<>();
		varmap.put("title", "客户报备 - 客户名称["+customInfo.getCustomFullName()+"]");
		varmap.put("app", IConstants.CUSTOM_REPORT_PRM_PROC);
		
		varmap.put("CustomerClass", customInfo.getCustomGrade());
		varmap.put("EmployeeType", getUserObject().getOrg().getOptTxt3());
		varmap.put("SalesCenter", lovMemberService.getSaleCenter(getUserObject().getOrg().getId()));

		varmap.put("app", IConstants.CUSTOM_REPORT_PRM_PROC);
		
		xflowProcessServiceWrapper.start(model
				, id
				, getUserObject()
				,varmap);
	
		customInfo.setCustomReportedFlg(IConstants.CUSTOM_NORMAL_STATUS_20);
		custom.updateCustomInfo(customInfo);
		
		return sendSuccessMessage("操作成功");
	}
}
