package com.ibm.kstar.action.team;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.Condition;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.team.vo.TeamVo;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping(value="/team")
public class TeamAction extends BaseAction{

	@Autowired
	IEmployeeService employeeService;
	
	@Autowired
	ITeamService teamService;
	
	@NoRight
	@RequestMapping(value="/list")
	public String team(String orgId,String businessType,String businessId,String layerId,Model model){
		model.addAttribute("businessId",businessId);
		model.addAttribute("businessType",businessType);
		model.addAttribute("orgId",orgId);
		model.addAttribute("layerId",layerId);
		return forward("list");
	}
	
	@NoRight
	@RequestMapping("/config")
	public String config(Condition condition,HttpServletRequest request,Model model){
		ActionUtil.requestToCondition(condition, request);
		
		model.addAttribute("businessId",condition.getStringCondition("businessId"));
		String businessId1 = condition.getStringCondition("businessId1");
		if(StringUtil.isNotEmpty(businessId1)&&!"undefined".equals(businessId1)){
			model.addAttribute("businessId",businessId1);
		}
		model.addAttribute("businessType",condition.getStringCondition("businessType"));
		model.addAttribute("orgId",condition.getStringCondition("orgId"));
		model.addAttribute("layerId",condition.getStringCondition("layerId"));
		
		List<TeamVo> list = employeeService.findTeamByBusinessId(condition);
		model.addAttribute("temeListJson",JSON.toJSONString(list));
		return forward("config");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/config",method=RequestMethod.POST)
	public String config(String positionIds,String employeeIds,String businessType,String businessId){
		teamService.config(positionIds, employeeIds, businessType, businessId);
		return sendSuccessMessage();
	}
	
}
