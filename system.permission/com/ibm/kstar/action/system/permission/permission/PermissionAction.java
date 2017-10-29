package com.ibm.kstar.action.system.permission.permission;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibm.kstar.action.system.lov.member.LovMemberAction;

@Controller
@RequestMapping("/permission")
public class PermissionAction extends LovMemberAction{
	
	@RequestMapping("/add")
	public String add(String groupId,String parentId,Model model){
		super.add(groupId, parentId, model);
		return forward("permission");
	}
	
	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		super.edit(id, model);
		return forward("permission");
	}
	
	@RequestMapping("/list")
	public String list(String id,Model model){
		return super.list("PERMISSION", model);
	}
	
	
}
