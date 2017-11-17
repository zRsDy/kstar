package com.ibm.kstar.action.custom.customcompetitor.analyse;

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
import com.ibm.kstar.entity.custom.CustomCompetitorAchi;
import com.ibm.kstar.entity.custom.CustomCompetitorReport;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/report/analyse")
public class CompetitorAnalyseAction extends BaseAction {

	@Autowired
	ICustomCompReportService service;
	
	//对手业绩
	@NoRight
	@LogOperate(module="客户管理模块：竞争对手提报",notes="${user}页面：对手业绩一览")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		String id = condition.getStringCondition("id");
		condition.getFilterObject().addCondition("reportId", "eq", id);
		
		IPage p = service.queryAchievement(condition);
		
		return sendSuccessMessage(p);
	}
	
	@LogOperate(module="客户管理模块：竞争对手提报",notes="${user}页面：对手业绩一览记录添加")
	@RequestMapping("/add")
	public String add(String id,Model model){
		CustomCompetitorReport customCompetitorReport = service.getReportInfo(id);
		
		CustomCompetitorAchi customCompetitorAchi = new CustomCompetitorAchi();
		customCompetitorAchi.setReportId(customCompetitorReport.getId());
		customCompetitorAchi.setCustomCode(customCompetitorReport.getCustomCode());
		
		model.addAttribute("customCompetitorAchi",customCompetitorAchi);
		return forward("add");
	}
	
	@LogOperate(module="客户管理模块：竞争对手提报",notes="${user}页面：对手业绩一览记录添加保存")
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(CustomCompetitorAchi customCompetitorAchi, HttpServletRequest request){
		
		// 功能字段设值
		// 创建字段
		customCompetitorAchi.setCreatedById(getUserObject().getEmployee().getId());
		customCompetitorAchi.setCreatedAt(new Date());
		customCompetitorAchi.setCreatedPosId(getUserObject().getPosition().getId());
		customCompetitorAchi.setCreatedOrgId(getUserObject().getOrg().getId());
		// 更新字段
		customCompetitorAchi.setUpdatedById(getUserObject().getEmployee().getId());
		customCompetitorAchi.setUpdatedAt(new Date());
		service.saveAchievementInfo(customCompetitorAchi);
		return sendSuccessMessage(customCompetitorAchi.getReportId());
	}
	
	@LogOperate(module="客户管理模块：竞争对手提报",notes="${user}页面：对手业绩一览记录编辑")
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomCompetitorAchi customCompetitorAchi = service.getAchievementInfo(id);
		if(customCompetitorAchi==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("customCompetitorAchi",customCompetitorAchi);
		return forward("add");
	}
	
	@LogOperate(module="客户管理模块：竞争对手提报",notes="${user}页面：对手业绩一览记录编辑保存")
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(CustomCompetitorAchi customCompetitorAchi){
		
		// 功能字段设值
		// 更新字段
		customCompetitorAchi.setUpdatedById(getUserObject().getEmployee().getId());
		customCompetitorAchi.setUpdatedAt(new Date());
		service.updateAchievementInfo(customCompetitorAchi);
		return sendSuccessMessage();
	}
	
	@LogOperate(module="客户管理模块：竞争对手提报",notes="${user}页面：对手业绩一览记录删除")
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		service.deleteAchievementInfo(id);
		return sendSuccessMessage();
	}
	
}
