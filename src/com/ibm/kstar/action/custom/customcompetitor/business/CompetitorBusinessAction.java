package com.ibm.kstar.action.custom.customcompetitor.business;

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
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.api.custom.ICustomCompReportService;
import com.ibm.kstar.entity.custom.CustomCompAchiBusiness;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/report/business")
public class CompetitorBusinessAction extends BaseAction {

	@Autowired
	ICustomCompReportService service;
	
	//对手业绩
	@NoRight
	@LogOperate(module="客户管理模块：竞争对手提报",notes="${user}页面：对手业绩一览")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		
		ActionUtil.requestToCondition(condition, request);
		String achiId = condition.getStringCondition("achiIdIndustry");
		String reportId = condition.getStringCondition("reportIdIndustry");
		
		condition.getFilterObject().addCondition("achiPid", "eq", achiId);
		condition.getFilterObject().addCondition("reportPid", "eq", reportId);
		
		IPage p = service.queryAnalyseBusiness(condition);
		
		return sendSuccessMessage(p);
	}
	
	@LogOperate(module="客户管理模块：竞争对手提报",notes="${user}页面：对手业绩一览记录添加")
	@RequestMapping("/add")
	public String add(String achiId, String reportId, Model model){
		
		CustomCompAchiBusiness customCompAchiBusiness = new CustomCompAchiBusiness();
		customCompAchiBusiness.setReportPid(reportId);
		customCompAchiBusiness.setAchiPid(achiId);
		
		model.addAttribute("customCompAchiBusiness",customCompAchiBusiness);
		return forward("add");
	}
	
	@LogOperate(module="客户管理模块：竞争对手提报",notes="${user}页面：对手业绩一览记录添加保存")
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(CustomCompAchiBusiness customCompAchiBusiness, HttpServletRequest request){
		
		// 功能字段设值
		// 创建字段
		customCompAchiBusiness.setCreatedById(getUserObject().getEmployee().getId());
		customCompAchiBusiness.setCreatedAt(new Date());
		customCompAchiBusiness.setCreatedPosId(getUserObject().getPosition().getId());
		customCompAchiBusiness.setCreatedOrgId(getUserObject().getOrg().getId());
		// 更新字段
		customCompAchiBusiness.setUpdatedById(getUserObject().getEmployee().getId());
		customCompAchiBusiness.setUpdatedAt(new Date());
		service.saveAnalyseBusiness(customCompAchiBusiness);
		return sendSuccessMessage();
	}
	
	@LogOperate(module="客户管理模块：竞争对手提报",notes="${user}页面：对手业绩一览记录编辑")
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomCompAchiBusiness customCompAchiBusiness = service.getAnalyseBusinessInfo(id);
		if(customCompAchiBusiness==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("customCompAchiBusiness",customCompAchiBusiness);
		return forward("add");
	}
	
	@LogOperate(module="客户管理模块：竞争对手提报",notes="${user}页面：对手业绩一览记录编辑保存")
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(CustomCompAchiBusiness customCompAchiBusiness){
		
		// 功能字段设值
		// 更新字段
		customCompAchiBusiness.setUpdatedById(getUserObject().getEmployee().getId());
		customCompAchiBusiness.setUpdatedAt(new Date());
		service.updateAnalyseBusinessInfo(customCompAchiBusiness);
		return sendSuccessMessage();
	}
	
	@LogOperate(module="客户管理模块：竞争对手提报",notes="${user}页面：对手业绩一览记录删除")
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		service.deleteAnalyseBusinessInfo(id);
		return sendSuccessMessage();
	}
	
}
