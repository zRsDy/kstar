package com.ibm.kstar.action.custom.customshare;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.custom.ICustomNumberService;
import com.ibm.kstar.api.custom.ICustomShareService;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.custom.CustomShareInfo;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/share")
public class ShareAction extends BaseAction {

	@Autowired
	ICustomShareService service;
	
	@Autowired
	ICustomNumberService numberService;
	
	@Autowired
	ICustomInfoService customservice;
	
	@RequestMapping("/list")
	public String list(String id,Model model){
		return forward("list");
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：共享授权",notes="${user}页面：共享授权一览")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String shareCode = condition.getStringCondition("shareCode");
		if(StringUtil.isNotEmpty(shareCode)){
			condition.getFilterObject().addCondition("shareCode", "like", "%" + shareCode + "%");
		}
		
		String dateFrom = condition.getStringCondition("dateFrom");
		if(StringUtil.isNotEmpty(dateFrom)){
			condition.getFilterObject().addCondition("createdAt", ">=", dateFrom);
		}
		
		String dateTo = condition.getStringCondition("dateTo");
		if(StringUtil.isNotEmpty(dateTo)){
			condition.getFilterObject().addCondition("createdAt", "<=", dateTo);
		}
		
		String applier = condition.getStringCondition("applier");
		if(StringUtil.isNotEmpty(applier)){
			condition.getFilterObject().addCondition("createdById", "=", applier);
		}
		
		String shareStatus = condition.getStringCondition("shareStatus");
		if(StringUtil.isNotEmpty(shareStatus)){
			condition.getFilterObject().addCondition("shareStatus", "=", shareStatus);
		}
		
		String shareCreateReason = condition.getStringCondition("shareCreateReason");
		if(StringUtil.isNotEmpty(shareCreateReason)){
			condition.getFilterObject().addCondition("shareCreateReason", "=", shareCreateReason);
		}
		
		IPage p = service.queryShare(condition);
		
		return sendSuccessMessage(p);
	}
	
	@LogOperate(module="客户管理模块：共享授权",notes="${user}页面：共享授权记录添加")
	@RequestMapping("/add")
	public String add(Model model){
		CustomShareInfo customShareInfo = new CustomShareInfo();
		
		customShareInfo.setShareCode(numberService.getShareNumber());
		
		customShareInfo.setApplierName(getUserObject().getEmployee().getNo().concat("/").concat(getUserObject().getEmployee().getName()));
		customShareInfo.setApplier(getUserObject().getEmployee().getId());
		
		customShareInfo.setApplierOrg(getUserObject().getOrg().getId());
		customShareInfo.setApplierPos(getUserObject().getPosition().getId());
		
		SimpleDateFormat sdf = new SimpleDateFormat(IConstants.YMDHMS_FORMAT_1);
		sdf = new SimpleDateFormat(IConstants.YMD_FORMAT_1);
		
		customShareInfo.setShareCreateTime(sdf.format(new Date()));
		
		customShareInfo.setShareStatus(IConstants.CUSTOM_NORMAL_STATUS_10);
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);
		model.addAttribute("tabMain",tabMain);
		
		model.addAttribute("customShareInfo",customShareInfo);
		return forward("add");
	}
	
	@LogOperate(module="客户管理模块：共享授权",notes="${user}页面：共享授权记录添加保存")
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(CustomShareInfo customShareInfo, HttpServletRequest request){
		
		// 功能字段设值
		// 创建字段
		customShareInfo.setCreatedById(getUserObject().getEmployee().getId());
		customShareInfo.setCreatedAt(new Date());
		customShareInfo.setCreatedPosId(getUserObject().getPosition().getId());
		customShareInfo.setCreatedOrgId(getUserObject().getOrg().getId());
		// 更新字段
		customShareInfo.setUpdatedById(getUserObject().getEmployee().getId());
		customShareInfo.setUpdatedAt(new Date());
		
		service.saveShareInfo(customShareInfo, getUserObject());
		return sendSuccessMessage(customShareInfo.getCustomCode());
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：共享授权",notes="${user}页面：共享授权记录编辑")
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomShareInfo customShareInfo = service.getShareInfo(id);
		if(customShareInfo==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		
		CustomInfo customInfo = customservice.getCustomInfoByCode(customShareInfo.getCustomCode());
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);
		if(hasPermission("P02CusShareT1TeamMemberPage")) {
			tabMain.addTab("已授权的销售团队", "/team/list.html?businessType="+ IConstants.CUSTOM_REPORT_PROC+"&businessId="+customInfo.getId());
		}
		if(hasPermission("P02CusShareT2ProReviewHistoryPage")) {
			tabMain.addTab("审批历史", "/standard/history.html?contrId="+customShareInfo.getId());
		}
		
		model.addAttribute("tabMain",tabMain);
		model.addAttribute("customShareInfo",customShareInfo);
		return forward("add");
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：共享授权",notes="${user}页面：共享授权记录编辑保存")
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(CustomShareInfo customShareInfo){
		
		// 功能字段设值
		// 更新字段
		customShareInfo.setUpdatedById(getUserObject().getEmployee().getId());
		customShareInfo.setUpdatedAt(new Date());
		service.updateShareInfo(customShareInfo, IConstants.CUSTOM_NORMAL_STATUS_10);
		return sendSuccessMessage();
	}
	
	@LogOperate(module="客户管理模块：共享授权",notes="${user}页面：共享授权记录删除")
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		CustomShareInfo customShareInfo = service.getShareInfo(id);
		if(!customShareInfo.getShareStatus().equals("CUSTOM_NORMAL_STATUS_10")){
			throw new AnneException("客户共享不为新建状态下无法删除");	
		}
		service.deleteShareInfo(id);
		return sendSuccessMessage();
	}
	
}
