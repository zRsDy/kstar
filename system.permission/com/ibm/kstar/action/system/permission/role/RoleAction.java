package com.ibm.kstar.action.system.permission.role;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.system.lov.member.LovMemberAction;
import com.ibm.kstar.api.product.permission.IProductPermissionService;
import com.ibm.kstar.api.system.lov.ILovGroupService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.IRoleService;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/role")
public class RoleAction extends LovMemberAction{
	
	@Autowired
	IRoleService roleService; 
	
	@Autowired
	ILovGroupService lovGroupService;
	
	@Autowired
	ICorePermissionService corePermissionService;
	
	@Autowired
	IProductPermissionService productPermissionService; 
	
	@RequestMapping("/add")
	public String add(String groupId,String parentId,Model model){
		super.add(groupId, parentId, model);
		return forward("role");
	}
	
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(LovMember lovMember,HttpServletRequest request){
		roleService.save(lovMember);
		return sendSuccessMessage();
	}
	
	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		super.edit(id, model);
		return forward("role");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/employeePage")
	public String employeePage(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		IPage page = roleService.queryEmployeePage(condition);
		return sendSuccessMessage(page);
	}
	
	@RequestMapping("/config")
	public String config(String id,Model model) {
		List<LovMember> selectedPermissionList = corePermissionService.getRolePermissionList("CRM", id);
		model.addAttribute("selectedPermissionList",selectedPermissionList);
		model.addAttribute("selectedPermissions",JSON.toJSONString(selectedPermissionList));
		//这里判断,如果非系统管理员使用roleService.getUserPermissionList(appId, userId)
		List<LovMember> allPermissionList = corePermissionService.getAllPermissionList("CRM");
		model.addAttribute("allPermissionList",allPermissionList);
		model.addAttribute("allPermissions",JSON.toJSONString(allPermissionList));
		model.addAttribute("roleId",id);
		return forward("config");
	}
	
	@ResponseBody
	@RequestMapping(value="/config" ,method=RequestMethod.POST)
	public String config(String roleId,String permissions,Model model) {
		roleService.configPermission(roleId,permissions.split(","));
		return sendSuccessMessage();
	}
	
	@RequestMapping("/config2")
	public String config2(String id,Model model) {
		model.addAttribute("selectedEmployeeList",JSON.toJSONString(corePermissionService.getRoleEmployeeList(id)));
//		model.addAttribute("allEmployeeList",JSON.toJSONString(corePermissionService.getAllEmployeeList()));
		model.addAttribute("roleId",id);
		return forward("config2");
	}
	
	@ResponseBody
	@RequestMapping(value="/config2" ,method=RequestMethod.POST)
	public String config2(String roleId,String employees,Model model) {
		roleService.configEmployee(roleId,employees.split(","));
		return sendSuccessMessage();
	}
	
	@RequestMapping("/config3")
	public String config3(String id,Model model) {
		model.addAttribute("selectedPositionList",JSON.toJSONString(corePermissionService.getPositionListByRole(id)));
		model.addAttribute("allPositionList",JSON.toJSONString(productPermissionService.getAllPermissionList()));
		model.addAttribute("roleId",id);
		return forward("config3");
	}
	
	@ResponseBody
	@RequestMapping(value="/config3" ,method=RequestMethod.POST)
	public String config3(String roleId,String positions,Model model) {
		roleService.configPosition(roleId,positions.split(","));
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String groupId = condition.getStringCondition("groupId");
		String parentId = condition.getStringCondition("parentId");
		String searchKey = condition.getStringCondition("searchKey");
		String role = condition.getStringCondition("role");
		
		condition.getFilterObject().addCondition("deleteFlag", "eq", "N");
		
		if("Y".equals(role)){
			condition.getFilterObject().addCondition("leafFlag", "eq", "Y");
		}
		
		condition.getFilterObject().addCondition("groupId", "eq", groupId);
		if(parentId !=null){
			condition.getFilterObject().addCondition("path", "like", "%"+parentId+"%");
		}
		if(searchKey !=null){
			condition.getFilterObject().addOrCondition("name", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("code", "like", "%"+searchKey+"%");
		}
		IPage p = lovMemberService.query(condition);
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/list")
	public String list(String id,Model model){
		return super.list("ROLE", model);
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		roleService.delete(id);
		return sendSuccessMessage();
	}
	
}
