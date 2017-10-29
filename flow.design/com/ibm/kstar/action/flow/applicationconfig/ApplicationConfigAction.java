package com.ibm.kstar.action.flow.applicationconfig;

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
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.AppFlowParam;
import com.ibm.kstar.entity.AppFlowPower;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import com.ibm.kstar.service.IAppFlowParamService;
import com.ibm.kstar.service.IAppFlowPowerService;

@Controller
@RequestMapping("/application/config")
public class ApplicationConfigAction extends BaseAction{
	
	@Autowired
	ILovMemberService lovMemberService;
	
	@Autowired
	IAppFlowParamService appFlowParamService;
	
	@Autowired
	IAppFlowPowerService appFlowPowerService;

	@RequestMapping("/list")
	public String list() {
		return forward("list");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		condition.getFilterObject().addCondition("groupCode", "=", "FLOW_APPLICATION");
		String code = condition.getStringCondition("code");
		if(StringUtil.isNotEmpty(code)){
			condition.getFilterObject().addCondition("code", "like", "%"+code+"%");
		}
		String name = condition.getStringCondition("name");
		if(StringUtil.isNotEmpty(name)){
			condition.getFilterObject().addCondition("name", "like", "%"+name+"%");
		}
		String optTxt1 = condition.getStringCondition("optTxt1");
		if(StringUtil.isNotEmpty(optTxt1)){
			condition.getFilterObject().addCondition("optTxt1", "like", "%"+optTxt1+"%");
		}
		IPage p = lovMemberService.query(condition);
		return sendSuccessMessage(p);
	}
	
	@ResponseBody
	@RequestMapping("/getFlowApplication")
	public String getFlowApplication(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		String searchKey = condition.getStringCondition("searchKey");
		condition.getFilterObject().addCondition("groupCode", "=", "FLOW_APPLICATION");
		if(searchKey != null && !"".equals(searchKey)){
			condition.getFilterObject().addOrCondition("code", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("optTxt1", "like", "%"+searchKey+"%");
		}
		List<LovMember>  list =  lovMemberService.getList(condition);
		return sendSuccessMessage(list);
	}
	
	@RequestMapping("/config")
	public String config(String id, Model model) {
		model.addAttribute("lovMember",lovMemberService.get(id));
		return forward("config");
	}
	
	@ResponseBody
	@RequestMapping(value = "/config", method = RequestMethod.POST)
	public String config(LovMember lov) {
		lovMemberService.update(lov);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping("/getFlows")
	public String getFlows(String optTxt1) {
		List<LovMember> list = appFlowParamService.getList(optTxt1);
		return sendSuccessMessage(list);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/pagePower")
	public String pagePower(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		String searchKey = condition.getStringCondition("searchKey");
		String applicationId = condition.getStringCondition("applicationId");
		condition.getFilterObject().addCondition("applicationId", "eq", applicationId);
		if(searchKey != null && !"".equals(searchKey)){
			condition.getFilterObject().addOrCondition("areaName", "like", "%"+searchKey+"%");
		}
		IPage p = appFlowPowerService.query(condition);
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/configPower")
	public String configPower(String id, Model model) {
		model.addAttribute("power",appFlowPowerService.get(id));
		return forward("powerEdit");
	}
	
	@ResponseBody
	@RequestMapping(value = "/configPower", method = RequestMethod.POST)
	public String configPower(AppFlowPower power) {
		appFlowPowerService.addOrUpdate(power);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/deletePower", method = RequestMethod.POST)
	public String deletePower(String id) {
		appFlowPowerService.delete(id);
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/pageParam")
	public String pageParam(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		String searchKey = condition.getStringCondition("searchKey");
		String applicationId = condition.getStringCondition("applicationId");
		condition.getFilterObject().addCondition("applicationId", "eq", applicationId);
		if(searchKey != null && !"".equals(searchKey)){
			condition.getFilterObject().addOrCondition("paramName", "like", "%"+searchKey+"%");
		}
		IPage p = appFlowParamService.query(condition);
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/configParam")
	public String configParam(String id, Model model) {
		model.addAttribute("param",appFlowParamService.get(id));
		return forward("paramEdit");
	}
	
	@ResponseBody
	@RequestMapping(value = "/configParam", method = RequestMethod.POST)
	public String configParam(AppFlowParam param) {
		appFlowParamService.addOrUpdate(param);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteParam", method = RequestMethod.POST)
	public String deleteParam(String id) {
		appFlowParamService.delete(id);
		return sendSuccessMessage();
	}
}
