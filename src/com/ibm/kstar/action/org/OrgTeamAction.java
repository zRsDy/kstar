package com.ibm.kstar.action.org;

import java.util.List;

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
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.org.IOrgTeamService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/orgTeam")
public class OrgTeamAction extends BaseAction{
	
	@Autowired
	IOrgTeamService service;
	
	@NoRight
	@RequestMapping("/list")
	public String list(String businessId, String groupId,String businessType, Model model) {
		model.addAttribute("businessId", businessId);
		model.addAttribute("businessType", businessType);
		return forward("list");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/page" )
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		IPage p = service.page(condition);
		List<LovMember> lovMembers = (List<LovMember>)p.getList();
		
		LovMember lovMemberPri = service.getPrimaryOrg(condition.getStringCondition("businessId"), condition.getStringCondition("businessType"));
		
		for (LovMember lovMember : lovMembers) {
			lovMember.setOptTxt1("N");
			if (StringUtils.equals(lovMember.getId(), lovMemberPri.getId())) {
				lovMember.setOptTxt1("Y");
			}
		}
		
		
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@RequestMapping("/config")
	public String config(String businessId,String businessType ,HttpServletRequest request,Model model) {
		
		List<LovMember> selectedPermissionList = service.getOrgList(businessId, businessType);
		model.addAttribute("selectedPermissionList",selectedPermissionList);
		model.addAttribute("selectedPermissions",JSON.toJSONString(selectedPermissionList));
		
		List<LovMember> allPermissionList = service.getAllOrgList();
		model.addAttribute("allPermissionList",allPermissionList);
		model.addAttribute("allPermissions",JSON.toJSONString(allPermissionList));
		
		LovMember lov = service.getPrimaryOrg(businessId, businessType);
		
		model.addAttribute("businessType",businessType);
		model.addAttribute("businessId",businessId);
		model.addAttribute("primaryOrgName",lov.getNamePath());
		model.addAttribute("primaryOrgId",lov.getId());
		return forward("config");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/config" ,method=RequestMethod.POST)
	public String config(String businessId,String businessType, String primaryOrgId, String permissions,Model model) {
		service.config(businessId,businessType,permissions.split(","), primaryOrgId);
		return sendSuccessMessage();
	}
}
