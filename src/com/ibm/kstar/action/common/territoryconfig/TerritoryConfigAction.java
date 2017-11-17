package com.ibm.kstar.action.common.territoryconfig;

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

import com.ibm.kstar.api.common.territory.ITerritory;
import com.ibm.kstar.entity.common.territory.TerritoryConfig;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/common/territoryconfig")
public class TerritoryConfigAction extends BaseAction {

	@Autowired
	ITerritory service;
	
	@RequestMapping("/list")
	public String list(String id,Model model){
		return forward("list");
	}
	
	@NoRight
	@LogOperate(module="销售区域分配",notes="${user}页面：一览")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		
		IPage p = service.queryTerritoryConfig(condition);
		
		return sendSuccessMessage(p);
	}
	
	@LogOperate(module="销售区域分配",notes="${user}页面：一览记录添加")
	@RequestMapping("/add")
	public String add(String id,Model model){
		model.addAttribute("id",id);
		
		return forward("add");
	}
	
	@LogOperate(module="销售区域分配",notes="${user}页面：一览记录添加保存")
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(TerritoryConfig territoryConfig, HttpServletRequest request){
		
		service.saveTerritoryConfig(territoryConfig, getUserObject());
		
		
		return sendSuccessMessage();
	}
	
	@LogOperate(module="销售区域分配",notes="${user}页面：一览记录编辑")
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		TerritoryConfig territoryConfig = service.getTerritoryConfig(id);
		
		if(territoryConfig==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		
		model.addAttribute("territoryConfig",territoryConfig);
		return forward("add");
	}
	
	@LogOperate(module="销售区域分配",notes="${user}页面：一览记录编辑保存")
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(TerritoryConfig territoryConfig){
		service.updateTerritoryConfig(territoryConfig, getUserObject());
		return sendSuccessMessage();
	}
	
	@LogOperate(module="销售区域分配",notes="${user}页面：一览记录删除")
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		service.deleteTerritoryConfig(id);
		return sendSuccessMessage();
	}
	
}
