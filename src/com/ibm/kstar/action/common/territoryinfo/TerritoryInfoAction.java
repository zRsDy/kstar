package com.ibm.kstar.action.common.territoryinfo;

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
import com.ibm.kstar.entity.common.territory.TerritoryInfo;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/common/territoryinfo")
public class TerritoryInfoAction extends BaseAction {

	@Autowired
	ITerritory service;
	
	@RequestMapping("/list")
	public String list(String id,Model model){
		return forward("list");
	}
	
	@NoRight
	@LogOperate(module="销售区域映射",notes="${user}页面：一览")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
//		String searchKey = condition.getStringCondition("searchKey");
//		if(searchKey !=null){
//			condition.getFilterObject().addOrCondition("statementCode", "like", "%"+searchKey+"%");
//			condition.getFilterObject().addOrCondition("proposerName", "like", "%"+searchKey+"%");
//		}
		ActionUtil.doSearch(condition);
		IPage p = service.queryTerritoryInfo(condition);
		
		return sendSuccessMessage(p);
	}
	
	@LogOperate(module="销售区域映射",notes="${user}页面：一览记录添加")
	@RequestMapping("/add")
	public String add(String id,Model model){
		
		return forward("add");
	}
	
	@LogOperate(module="销售区域映射",notes="${user}页面：一览记录添加保存")
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(TerritoryInfo territoryInfo, HttpServletRequest request){
		
		service.saveTerritoryInfo(territoryInfo, getUserObject());
		
		
		return sendSuccessMessage();
	}
	
	@LogOperate(module="销售区域映射",notes="${user}页面：一览记录编辑")
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		TerritoryInfo territoryInfo = service.getTerritoryInfo(id);
		
		if(territoryInfo==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		
		model.addAttribute("territoryInfo",territoryInfo);
		return forward("add");
	}
	
	@LogOperate(module="销售区域映射",notes="${user}页面：一览记录编辑保存")
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(TerritoryInfo territoryInfo){
		service.updateTerritoryInfo(territoryInfo, getUserObject());
		return sendSuccessMessage();
	}
	
	@LogOperate(module="销售区域映射",notes="${user}页面：一览记录删除")
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		service.deleteTerritoryInfo(id);
		return sendSuccessMessage();
	}
	
}
