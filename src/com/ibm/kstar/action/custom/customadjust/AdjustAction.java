package com.ibm.kstar.action.custom.customadjust;

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
import com.ibm.kstar.api.custom.ICustomCreditAdjustService;
import com.ibm.kstar.api.custom.ICustomNumberService;
import com.ibm.kstar.entity.custom.CustomCreditAdjustment;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/adjust")
public class AdjustAction extends BaseAction {

	@Autowired
	ICustomCreditAdjustService service;
	
	@Autowired
	ICustomNumberService numberService;
	
	@RequestMapping("/list")
	public String list(String id,Model model){
		return forward("list");
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：评估调整",notes="${user}页面：评估调整一览")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		String creditAdjustmentCode = condition.getStringCondition("creditAdjustmentCode");
		if(StringUtil.isNotEmpty(creditAdjustmentCode)){
			condition.getFilterObject().addCondition("creditAdjustmentCode", "like", "%" + creditAdjustmentCode + "%");
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
		
		String status = condition.getStringCondition("status");
		if(StringUtil.isNotEmpty(status)){
			condition.getFilterObject().addCondition("status", "=", status);
		}
		IPage p = service.queryAdjustment(condition);
		
		return sendSuccessMessage(p);
	}
	
	@LogOperate(module="客户管理模块：评估调整",notes="${user}页面：评估调整一览记录添加")
	@RequestMapping("/add")
	public String add(String id,Model model){
		model.addAttribute("id",id);
		CustomCreditAdjustment customCreditAdjustment = new CustomCreditAdjustment();
		customCreditAdjustment.setId(null);
		
		customCreditAdjustment.setCreditAdjustmentCode(numberService.getAdjustNumber());
		
		customCreditAdjustment.setApplierName(getUserObject().getEmployee().getNo().concat("/").concat(getUserObject().getEmployee().getName()));
		customCreditAdjustment.setApplier(getUserObject().getEmployee().getId());
		
		customCreditAdjustment.setApplierOrg(getUserObject().getOrg().getId());
		customCreditAdjustment.setApplierPos(getUserObject().getPosition().getId());
		
		SimpleDateFormat sdf = new SimpleDateFormat(IConstants.YMDHMS_FORMAT_1);
		sdf = new SimpleDateFormat(IConstants.YMD_FORMAT_1);
		
		customCreditAdjustment.setCreateDate(sdf.format(new Date()));
		
		customCreditAdjustment.setStatus(IConstants.CUSTOM_NORMAL_STATUS_10);
		customCreditAdjustment.setLimitCurrency("CURRENCY_CNY");
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);
		model.addAttribute("tabMain", tabMain);
		model.addAttribute("customCreditAdjustment",customCreditAdjustment);
		return forward("add");
	}
	
	@LogOperate(module="客户管理模块：评估调整",notes="${user}页面：评估调整一览记录添加保存")
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(CustomCreditAdjustment customCreditAdjustment, HttpServletRequest request){
		// 功能字段设值
		// 创建字段
		customCreditAdjustment.setCreatedById(getUserObject().getEmployee().getId());
		customCreditAdjustment.setCreatedAt(new Date());
		customCreditAdjustment.setCreatedPosId(getUserObject().getPosition().getId());
		customCreditAdjustment.setCreatedOrgId(getUserObject().getOrg().getId());
		// 更新字段
		customCreditAdjustment.setUpdatedById(getUserObject().getEmployee().getId());
		customCreditAdjustment.setUpdatedAt(new Date());
		service.saveAdjustmentInfo(customCreditAdjustment, getUserObject());
		
		
		return sendSuccessMessage(customCreditAdjustment.getCreditAdjustmentCode());
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：评估调整",notes="${user}页面：评估调整一览记录编辑")
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomCreditAdjustment customCreditAdjustment = service.getAdjustmentInfo(id);
		
		if(customCreditAdjustment==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);
		if(hasPermission("P02AssessmentT1FilePage")) {
			tabMain.addTab("附件","/common/attachment/attachment.html?businessType=EVALUATION_ADJUSTMENT&docGroupCode=CONTRACTDOCTYPE&businessId="+customCreditAdjustment.getId());
		}
		
		if (hasPermission("P02AssessmentT2TeamPosPage")) {
			tabMain.addTab("销售团队", "/team/list.html?businessType="+ IConstants.CUSTOM_ADJUST_PROC +"&businessId=" + customCreditAdjustment.getId());
		}

		if (hasPermission("P02AssessmentT3ProReviewHistoryPage")) {
			tabMain.addTab("审批历史", "/standard/history.html?contrId="+customCreditAdjustment.getId());
		}
		
		model.addAttribute("tabMain", tabMain);
		model.addAttribute("customCreditAdjustment",customCreditAdjustment);
		return forward("add");
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：评估调整",notes="${user}页面：评估调整一览记录编辑保存")
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(CustomCreditAdjustment customCreditAdjustment){
		// 功能字段设值
		// 更新字段
		customCreditAdjustment.setUpdatedById(getUserObject().getEmployee().getId());
		customCreditAdjustment.setUpdatedAt(new Date());
		service.updateAdjustmentInfo(customCreditAdjustment, IConstants.CUSTOM_NORMAL_STATUS_10);
		
		
		return sendSuccessMessage();
	}
	
	@LogOperate(module="客户管理模块：评估调整",notes="${user}页面：评估调整一览记录删除")
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		CustomCreditAdjustment customCreditAdjustment = service.getAdjustmentInfo(id);
		if(!customCreditAdjustment.getStatus().equals("CUSTOM_NORMAL_STATUS_10")){
			throw new AnneException("评估调整不为新建状态下无法删除！");
		}
		service.deleteAdjustmentInfo(id);
		return sendSuccessMessage();
	}
	
}
