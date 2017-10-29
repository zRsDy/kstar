package com.ibm.kstar.action.support.docmanagement.permission;

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

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.common.permission.ICommonPermissionService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/common/permission")
public class CommonPermissionAction extends BaseAction{
	
	@Autowired
	ICommonPermissionService service;
	
	@RequestMapping("/tab")
	public String atts(String businessId, String groupId, Model model) {
		model.addAttribute("businessId", businessId);
		model.addAttribute("groupId", groupId);
		return forward("permission");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "selectedPage" )
	public String selectedPage(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		IPage p = service.getPermissionPage(condition);
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/config")
	public String config(String businessId,String groupId, Model model) {
		List<LovMember> selectedPermissionList = service.getPermissionList(businessId, groupId);
		model.addAttribute("selectedPermissionList",selectedPermissionList);
		model.addAttribute("selectedPermissions",JSON.toJSONString(selectedPermissionList));
		//这里判断,如果非系统管理员使用roleService.getUserPermissionList(appId, userId)
		List<LovMember> allPermissionList = service.getAllPermissionList();
		model.addAttribute("allPermissionList",allPermissionList);
		model.addAttribute("allPermissions",JSON.toJSONString(allPermissionList));
		model.addAttribute("businessId",businessId);
		model.addAttribute("groupId",groupId);
		return forward("config");
	}
	
	@ResponseBody
	@RequestMapping(value="/config" ,method=RequestMethod.POST)
	public String config(String businessId,String groupId, String permissions,Model model) {
		service.configPermission(businessId,permissions.split(","));
		return sendSuccessMessage();
	}
}
