package com.ibm.kstar.action.custom.customhandover.process;

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

import com.ibm.kstar.api.custom.ICustomHandoverService;
import com.ibm.kstar.entity.custom.CustomHandoverProc;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/handover/proc")
public class HandoverProcAction extends BaseAction {

	@Autowired
	ICustomHandoverService service;
	
	//未结事项
	@NoRight
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：未结事项-未结事项一览")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String handoverCode = condition.getStringCondition("id");
		condition.getFilterObject().addCondition("handoverCode", "eq", handoverCode);
		IPage p = service.queryHandoverProc(condition);
		
		return sendSuccessMessage(p);
	}
	
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：未结事项-未结事项记录新增")
	@RequestMapping("/add")
	public String add(String id, Model model){
		model.addAttribute("userId",getUserObject().getEmployee().getId());
		
		CustomHandoverProc customHandoverProc = new CustomHandoverProc();
		customHandoverProc.setHandoverCode(id);
		model.addAttribute("customHandoverProc",customHandoverProc);
		return forward("add");
	}
	
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：未结事项-未结事项记录新增保存")
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(CustomHandoverProc customHandoverProc, HttpServletRequest request){
		
		// 功能字段设值
		// 创建字段
		customHandoverProc.setCreatedById(getUserObject().getEmployee().getId());
		customHandoverProc.setCreatedAt(new Date());
		customHandoverProc.setCreatedPosId(getUserObject().getPosition().getId());
		customHandoverProc.setCreatedOrgId(getUserObject().getOrg().getId());
		// 更新字段
		customHandoverProc.setUpdatedById(getUserObject().getEmployee().getId());
		customHandoverProc.setUpdatedAt(new Date());
		service.saveHandoverProcInfo(customHandoverProc);
		return sendSuccessMessage(customHandoverProc.getHandoverCode());
	}
	
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：未结事项-未结事项记录编辑")
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomHandoverProc customHandoverProc = service.getHandoverProcInfo(id);
		if(customHandoverProc==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("customHandoverProc",customHandoverProc);
		return forward("add");
	}
	
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：未结事项-未结事项记录编辑保存")
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(CustomHandoverProc customHandoverProc){
		
		// 功能字段设值
		// 更新字段
		customHandoverProc.setUpdatedById(getUserObject().getEmployee().getId());
		customHandoverProc.setUpdatedAt(new Date());
		service.updateHandoverProcInfo(customHandoverProc);
		return sendSuccessMessage();
	}
	
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：未结事项-未结事项记录删除")
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		service.deleteHandoverProcInfo(id);
		return sendSuccessMessage();
	}
	
}
