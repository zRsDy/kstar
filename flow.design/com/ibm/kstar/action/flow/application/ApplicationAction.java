package com.ibm.kstar.action.flow.application;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibm.kstar.action.system.lov.member.LovMemberAction;

@Controller
@RequestMapping("/flow/application")
public class ApplicationAction extends LovMemberAction{

	@RequestMapping("/add")
	public String add(String groupId,String parentId,Model model){
		super.add(groupId, null, model);
		return forward("application");
	}
	
	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		super.edit(id, model);
		return forward("application");
	}
	
	@RequestMapping("/list")
	public String list(String id,Model model){
		return super.list("APPLICATION", model);
	}
	
}
