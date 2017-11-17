package com.ibm.kstar.action.product.reportPermissionConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.product.permission.IProductPermissionService;
import com.ibm.kstar.api.system.lov.ILovGroupService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovGroup;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.product.permission.PermissionOptRel;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/product/reportPermissionConfig")
public class ReportPermissionConfigAction extends BaseAction{
	
	@Autowired
	IProductPermissionService productPermissionService;
	
	@Autowired
	protected ILovGroupService lovGroupService;
	
	@Autowired
	protected ILovMemberService lovMemberService;
	
	@RequestMapping("list")
	public String list(Model model){
		LovGroup lovGroup = lovGroupService.get("POSITION");
		model.addAttribute("lovGroup", lovGroup);
		List<LovMember> lovOrgList = lovMemberService.getAllOrgList();
		model.addAttribute("lovOrgList", JSON.toJSONString(lovOrgList));
		return forward("list");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/reportPermissionPostData" ,method=RequestMethod.POST)
	public String permissionPostData(String leftId,Model model) {
		List<LovMember>  jobSelectPermissionList = new ArrayList<LovMember>();
		if(!StringUtil.isNullOrEmpty(leftId)) {
			jobSelectPermissionList = productPermissionService.getReportSelectPermissionList(leftId);
		}
		model.addAttribute("jobSelectPermissionList",JSON.toJSONString(jobSelectPermissionList));
		return sendSuccessMessage(jobSelectPermissionList);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/saveReportPermissionPostData" ,method=RequestMethod.POST)
	public String savePermissionPostData(String rightzTreeId,String leftzTreeId,Model model) {
		productPermissionService.updateReportPermission(rightzTreeId, leftzTreeId);
		return sendSuccessMessage();
	}
}
