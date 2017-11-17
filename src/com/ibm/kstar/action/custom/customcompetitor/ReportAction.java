package com.ibm.kstar.action.custom.customcompetitor;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import com.ibm.kstar.api.custom.ICustomCompReportService;
import com.ibm.kstar.api.custom.ICustomNumberService;
import com.ibm.kstar.entity.custom.CustomCompetitorReport;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/report")
public class ReportAction extends BaseAction {

	@Autowired
	ICustomCompReportService service;
	
	@Autowired
	ICustomNumberService numberService;
	
	@RequestMapping("/list")
	public String list(String id,Model model){
		return forward("list");
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：竞争对手提报",notes="${user}页面：竞争对手提报一览")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		
		String customBaseFlg = condition.getStringCondition("customBaseFlg");
		if (StringUtils.equals("Y", customBaseFlg)) {
			// from custom base
			IPage p = service.queryReportByCustomCode(condition);
			return sendSuccessMessage(p);
		}
		
		String reportCode = condition.getStringCondition("reportCode");
		if(StringUtil.isNotEmpty(reportCode)){
			condition.getFilterObject().addCondition("reportCode", "like", "%" + reportCode + "%");
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
		
		IPage p = service.queryReport(condition);
		return sendSuccessMessage(p);
	}
	
	@LogOperate(module="客户管理模块：竞争对手提报",notes="${user}页面：竞争对手提报一览记录添加")
	@RequestMapping("/add")
	public String add(String id,Model model){
		CustomCompetitorReport customCompetitorReport = new CustomCompetitorReport();
		customCompetitorReport.setId(null);
		
		customCompetitorReport.setReportCode(numberService.getCompetitorNumber());
		
		customCompetitorReport.setApplierName(getUserObject().getEmployee().getNo().concat("/").concat(getUserObject().getEmployee().getName()));
		customCompetitorReport.setApplier(getUserObject().getEmployee().getId());
		
		customCompetitorReport.setApplierOrg(getUserObject().getOrg().getId());
		customCompetitorReport.setApplierPos(getUserObject().getPosition().getId());
		
		SimpleDateFormat sdf = new SimpleDateFormat(IConstants.YMDHMS_FORMAT_1);
		sdf = new SimpleDateFormat(IConstants.YMD_FORMAT_1);
		
		customCompetitorReport.setReoprtDate(sdf.format(new Date()));
		
		customCompetitorReport.setStatus(IConstants.CUSTOM_NORMAL_STATUS_10);
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);
		model.addAttribute("tabMain", tabMain);
		model.addAttribute("customCompetitorReport",customCompetitorReport);
		return forward("add");
	}
	
	@LogOperate(module="客户管理模块：竞争对手提报",notes="${user}页面：竞争对手提报一览记录添加保存")
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(CustomCompetitorReport customCompetitorReport){
		// 功能字段设值
		// 创建字段
		customCompetitorReport.setCreatedById(getUserObject().getEmployee().getId());
		customCompetitorReport.setCreatedAt(new Date());
		customCompetitorReport.setCreatedPosId(getUserObject().getPosition().getId());
		customCompetitorReport.setCreatedOrgId(getUserObject().getOrg().getId());
		// 更新字段
		customCompetitorReport.setUpdatedById(getUserObject().getEmployee().getId());
		customCompetitorReport.setUpdatedAt(new Date());
		
		service.saveReportInfo(customCompetitorReport);
		return sendSuccessMessage(customCompetitorReport.getReportCode());
	}
	
	@LogOperate(module="客户管理模块：竞争对手提报",notes="${user}页面：竞争对手提报一览记录编辑")
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomCompetitorReport customCompetitorReport = service.getReportInfo(id);
		if(customCompetitorReport==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);
		if(hasPermission("P02CompetitorReportT1PerformacePage")) {
			tabMain.addTab("对手业绩", "/custom/report/achievement.html?id=" + customCompetitorReport.getId());
		}
		if(hasPermission("P02CompetitorReportT1PerformacePage")) {
			tabMain.addTab("对手产品", "/custom/report/achievementCompare.html?id=" + customCompetitorReport.getId());
		}
		
		model.addAttribute("tabMain", tabMain);
		model.addAttribute("customCompetitorReport",customCompetitorReport);
		return forward("add");
	}
	
	@LogOperate(module="客户管理模块：竞争对手提报",notes="${user}页面：竞争对手提报一览记录编辑保存")
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String reportEdit(CustomCompetitorReport customCompetitorReport){
		
		// 功能字段设值
		// 更新字段
		customCompetitorReport.setUpdatedById(getUserObject().getEmployee().getId());
		customCompetitorReport.setUpdatedAt(new Date());
		service.updateReportInfo(customCompetitorReport);
		return sendSuccessMessage();
	}
	
	@LogOperate(module="客户管理模块：竞争对手提报",notes="${user}页面：竞争对手提报一览记录删除")
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		service.deleteReportInfo(id);
		return sendSuccessMessage();
	}
	
	@LogOperate(module="客户管理模块：竞争对手提报",notes="${user}页面：对手概况一览")
	@RequestMapping("/achievement")
	public String achievement(String id,Model model){
		CustomCompetitorReport customCompetitorReport = service.getReportInfo(id);
		model.addAttribute("customCompetitorReport",customCompetitorReport);
		return forward("achievement");
	}
	
	@LogOperate(module="客户管理模块：竞争对手提报",notes="${user}页面：对手概况比较")
	@RequestMapping("/achievementCompare")
	public String achievementCompare(String id,Model model){
		CustomCompetitorReport customCompetitorReport = service.getReportInfo(id);
		model.addAttribute("customCompetitorReport",customCompetitorReport);
		return forward("achievementCompare");
	}
	
}
