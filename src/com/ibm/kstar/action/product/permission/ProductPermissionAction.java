package com.ibm.kstar.action.product.permission;

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

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.product.permission.IProductPermissionService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/product/permission")
public class ProductPermissionAction extends BaseAction{
	
	@Autowired
	IProductPermissionService productPermissionService;
	@Autowired
	protected ILovMemberService lovMemberService;
	@RequestMapping("list")
	public String list(Model model){
		List<LovMember> lovCatalogList = lovMemberService.getAllCatalogList();
		model.addAttribute("lovCatalogList", JSON.toJSONString(lovCatalogList));
		return forward("list");
	}
	
	@ResponseBody
	@RequestMapping(value = "selectedPage" )
	public String selectedPage(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		return sendSuccessMessage(productPermissionService.getPermissionPage(condition));
	}
	
	@RequestMapping("/config")
	public String config(String productCatalogId,String productCatalogIds,HttpServletRequest request,Model model) {
		List<LovMember> selectedPermissionList = productPermissionService.getPermissionList(productCatalogId);
		model.addAttribute("selectedPermissionList",selectedPermissionList);
		model.addAttribute("selectedPermissions",JSON.toJSONString(selectedPermissionList));
		//这里判断,如果非系统管理员使用roleService.getUserPermissionList(appId, userId)
		List<LovMember> allPermissionList = productPermissionService.getAllPermissionList();
		model.addAttribute("allPermissionList",allPermissionList);
		model.addAttribute("allPermissions",JSON.toJSONString(allPermissionList));
		model.addAttribute("productCatalogId",productCatalogId);
		return forward("config");
	}
	
	@ResponseBody
	@RequestMapping(value="/config" ,method=RequestMethod.POST)
	public String config(String productCatalogIds,String permissions,Model model) {
		String _productCatalogIds = (String)getSessionAttribute(productCatalogIds);
		productPermissionService.configPermission(_productCatalogIds.split("a____z"),permissions.split(","));
		removeAttribute(productCatalogIds);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@NoRight
	@RequestMapping(value = "selectedOptPage" )
	public String selectedOptPage(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		return sendSuccessMessage(productPermissionService.getOptPermissionPage(condition));
	}
	
	@RequestMapping("/configOpt")
	@NoRight
	public String configOpt(String productCatalogId,String productCatalogIds,HttpServletRequest request,Model model) {
		List<LovMember> selectedPermissionList = productPermissionService.getOptPermissionList(productCatalogId);
		model.addAttribute("selectedPermissionList",selectedPermissionList);
		model.addAttribute("selectedPermissions",JSON.toJSONString(selectedPermissionList));
		//这里判断,如果非系统管理员使用roleService.getUserPermissionList(appId, userId)
		List<LovMember> allPermissionList = productPermissionService.getAllPermissionList();
		model.addAttribute("allPermissionList",allPermissionList);
		model.addAttribute("allPermissions",JSON.toJSONString(allPermissionList));
		model.addAttribute("productCatalogId",productCatalogId);
		return forward("config");
	}
	
	@ResponseBody
	@NoRight
	@RequestMapping(value="/configOpt" ,method=RequestMethod.POST)
	public String configOpt(String productCatalogIds,String permissions,Model model) {
		String _productCatalogIds = (String)getSessionAttribute(productCatalogIds);
		productPermissionService.configOptPermission(_productCatalogIds.split("a____z"),permissions.split(","));
		removeAttribute(productCatalogIds);
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/permissionPostData" ,method=RequestMethod.POST)
	public String permissionPostData(@RequestBody String productCatalogIds,Model model) {
		String pid = UUID.randomUUID().toString();
		if(productCatalogIds.endsWith("=")){
			productCatalogIds = productCatalogIds.substring(0, productCatalogIds.length()-1);
		}
		setSessionAttribute(pid,productCatalogIds);
		return sendSuccessMessage(pid);
	}
	
}
