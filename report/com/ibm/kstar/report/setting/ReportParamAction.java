package com.ibm.kstar.report.setting;

import java.util.List;

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

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.product.permission.IProductPermissionService;
import com.ibm.kstar.api.report.IReportParamService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.report.RankHeader;
import com.ibm.kstar.entity.report.RankLine;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/report/param")
public class ReportParamAction extends BaseAction {

	@Autowired
	IReportParamService reportParamService;
	
	@RequestMapping("/list")
	public String list(){
		return forward("list");
	}
	
	@ResponseBody
	@RequestMapping("/rankList")
	public String rankList(){
		return sendSuccessMessage(reportParamService.rankList());
	}
	
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		return sendSuccessMessage(reportParamService.query(condition));
	}
	
	@RequestMapping("/add")
	public String add(){
		return forward("rankHeader");
	}
	
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(RankHeader rk){
		if(rk== null || rk.getName() == null){
			throw new AnneException("排名组不能为空");
		}
		reportParamService.save(rk);
		return sendSuccessMessage();
	}

	@RequestMapping("/edit")
	public String edit(String id,Model model){
		RankHeader rh = reportParamService.get(id);
		model.addAttribute("rh",rh);
		return forward("rankHeader");
	}
	
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(String id){
		reportParamService.delete(id);
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(RankHeader rk){
		if(rk== null || rk.getName() == null){
			throw new AnneException("排名组不能为空");
		}
		reportParamService.update(rk);
		return sendSuccessMessage();
	}

	@Autowired
	IProductPermissionService productPermissionService;
	
	@NoRight
	@RequestMapping("/config")
	public String config(String headerId,HttpServletRequest request,Model model) {
		List<RankLine> selected = reportParamService.getLineList(headerId);
		model.addAttribute("selected",JSON.toJSONString(selected));
		List<LovMember> all = productPermissionService.getAllPermissionList();
		model.addAttribute("all",JSON.toJSONString(all));
		model.addAttribute("headerId",headerId);
		return forward("config");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/config" ,method=RequestMethod.POST)
	public String config(String headerId,String orgs,Model model) {
		reportParamService.config(headerId,orgs.split(","));
		return sendSuccessMessage();
	}
}
